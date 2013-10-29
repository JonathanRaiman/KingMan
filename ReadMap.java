import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.util.ArrayList;
 
public class ReadMap {
   	public static void main(String[] args) {
   	}
   	public ReadMap (DrawGame game) {
   	Map readmap = new Map();
   	ArrayList<Soldier> readarmy = new ArrayList<Soldier>();
   	Kingman readplayer = new Kingman(0,0,readmap);
	try {
		JsonReader reader = new JsonReader(new FileReader(".//save.json"));
	 	
		reader.beginObject();
		// We store base values to initialize them and we improve them as we
		// read the file. In the end we build a Map using the collected values
		int xsize          = 0;
		int ysize          = 0;
		int xfocus         = 0;
		int yfocus         = 0;
		int mountainsticky = 100;
		/********************************************************/
		// READ MAP
		/********************************************************/
		while (reader.hasNext()) {
		  	String name = reader.nextName();
			if (name.equalsIgnoreCase("map")) {
				reader.beginObject();
				while (reader.hasNext()) {
					name = reader.nextName();
					if (name.equalsIgnoreCase("xsize")){
						xsize = reader.nextInt();
					}
					else if (name.equalsIgnoreCase("ysize")){
						ysize = reader.nextInt();
						readmap.Size(xsize,ysize);
						// Update the size of the new map to match the read map
						// Hypothetically one could build a tiny map or a huge map,
						// Or even decide to have smaller maps to illustrate rooms
						// inside a building.
					}
					else if (name.equalsIgnoreCase("cliffstickiness")){
						mountainsticky = reader.nextInt();
						readmap.Cliff(mountainsticky);
					}
					else if (name.equalsIgnoreCase("xfocus")){
						xfocus = reader.nextInt();
					}
					else if (name.equalsIgnoreCase("yfocus")){
						yfocus = reader.nextInt();
						readmap.Focus(xfocus,yfocus);
					}
					else if (name.equalsIgnoreCase("tiles")){
						reader.beginArray();
						while (reader.hasNext()) {
							reader.beginArray();
							while (reader.hasNext()) {
								reader.beginObject();
								int x=0;
								int y=0;
								String t="grass";
								while (reader.hasNext()){
									name = reader.nextName();
									if (name.equalsIgnoreCase("x")){
										x = reader.nextInt();
									}
									else if (name.equalsIgnoreCase("y")){
										y = reader.nextInt();
									}
									else if (name.equalsIgnoreCase("t")){
										t = reader.nextString();
										readmap.World()[x][y].Type(t);
										// Make readmap's tiles type follow the read tiles' types
									}
									else {reader.skipValue();}
								}
								reader.endObject();
							}
							reader.endArray();
							}
						reader.endArray();
						}
					else {reader.skipValue();}
				}
				reader.endObject();
				// System.out.println("Loaded Map");
			} else if (name.equalsIgnoreCase("resources")) {
				reader.beginObject();
			 	while (reader.hasNext()) {
					String key = reader.nextName();
					ArrayList<Resource> newresource = new ArrayList<Resource>();
        			readmap.Resources().put(key, newresource);
					reader.beginArray();
					while (reader.hasNext()) {
								reader.beginObject();
								int x=0;
								int y=0;
								double v=0;
								while (reader.hasNext()){
									name = reader.nextName();
									if (name.equalsIgnoreCase("x")){
										x = reader.nextInt();
									}
									else if (name.equalsIgnoreCase("y")){
										y = reader.nextInt();
									}
									else if (name.equalsIgnoreCase("v")){
										v = reader.nextDouble();
										((ArrayList<Resource>)readmap.Resources().get(key)).add(new Resource(false, x,y,key,v, readmap));
										// System.out.println("adding "+key+" at ("+x+","+y+") with value = "+v);
									}
									else {reader.skipValue();}
								}
								reader.endObject();
					}
					reader.endArray();
				}		
				reader.endObject();
				// System.out.println("Loaded Resources");
			} else if (name.equalsIgnoreCase("army")) {
				reader.beginArray();
				while (reader.hasNext()) {
					Soldier readsoldier = Soldier.Load(reader,readmap);
				/* Soldier and Kingman have their own Load method... Tiles and
				Resources might need their own too some day. These methods allow
				us to have an easy .Load() everything eventually feasible that will
				of course make this load Json file more nimble and less reliant on
				any given structure that could easily break with different versions.
				*/
					readarmy.add(readsoldier);
				}
				reader.endArray();
				// System.out.println("Loaded Army");
			} else if (name.equalsIgnoreCase("player")) {
				readplayer = Kingman.Load(reader,readmap);
				// System.out.println("Loaded Player");
			}
			else {reader.skipValue();}
		}
		reader.endObject();
		reader.close();
		DrawGame.Map(readmap);
		DrawGame.Player(readplayer);
		DrawGame.Player().Walk(DrawGame.Player().Destination());
		DrawGame.Army(readarmy);
		game.userinterface.State("Loaded Map",true);
		game.userinterface.Flash();
		// System.out.println("Done");
		 
	} catch (FileNotFoundException e) {
		game.userinterface.State("No saved game found. Saved game should be named 'save.json'",true);
		game.userinterface.Flash();
	} catch (IOException e) {
		game.userinterface.State("Non standard saved game found. Check for typos.",true);
		game.userinterface.Flash();
	}
	 
	}
 
}