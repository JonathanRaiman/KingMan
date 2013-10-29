import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayList;
import java.util.*;
import java.util.Hashtable;

public class SaveMap {
   	public static void main(String[] args) throws IOException{
   	}

   	public SaveMap(Map landmass) throws IOException{
		FileWriter document = new FileWriter(".//save.json");
		PrintWriter fileWriter = new PrintWriter(document);
    	JsonWriter writer;
    	try {
		writer = new JsonWriter(document);
/********************************************************/
	 	writer.beginObject();
/********************************************************/
        writer.name("map").beginObject();
            writer.name("xsize").value(landmass.Size()[0]);
            writer.name("ysize").value(landmass.Size()[1]);
            writer.name("xfocus").value(landmass.Focus()[0]);
            writer.name("yfocus").value(landmass.Focus()[1]);
            writer.name("cliffstickiness").value(landmass.Cliff());
            writer.name("tiles").beginArray(); // [

        	 	for (int x = 0;x<landmass.Size()[0];x++){
        	 		
        	 		writer.beginArray(); // [
                        	
                        	for (int y=0; y<landmass.Size()[1];y++){
                        		writer.beginObject(); // {
                                // x position
                        		writer.name("x").value(landmass.World()[x][y].Position()[0]);
                                // y position
                        		writer.name("y").value(landmass.World()[x][y].Position()[1]);
                                // Type of tile
                        		writer.name("t").value(landmass.World()[x][y].Type());
                            	writer.endObject(); // }
                        	}
                        	
                    writer.endArray(); // ]
                	}

                writer.endArray(); // ]
            writer.endObject();

        System.out.println("Saved Map");
/********************************************************/

        writer.name("resources").beginObject(); // {
        ArrayList<String> keys = Collections.list(landmass.Resources().keys());
        for (String s : keys){
            // for each resources in resources (so if we ever create resources that are not oil or bananas these will have their own arrays.)
            writer.name(s).beginArray(); // [
              for (int i=0; i<((ArrayList<Resource>)landmass.Resources().get(s)).size(); i++){
                    writer.beginObject(); // {
                    // x position
                    writer.name("x").value(((ArrayList<Resource>)landmass.Resources().get(s)).get(i).Position()[0]);
                    // y position
                    writer.name("y").value(((ArrayList<Resource>)landmass.Resources().get(s)).get(i).Position()[1]);
                    // Value of Resource
                    writer.name("v").value(((ArrayList<Resource>)landmass.Resources().get(s)).get(i).Value());
                    writer.endObject(); // }
              }
            writer.endArray(); // ]
        }
        writer.endObject(); // }
        System.out.println("Saved Resources");

/********************************************************/
        writer.name("army").beginArray();
        for (Soldier s : DrawGame.Army()){
            s.Save(writer);
            /*
        Soldier and Kingman have their own Save method that builds an object
        with all their features. Because it relies on redundant names : x and
        y, putting it in its own file allows us to not bog up the namespace
        too soon :)
            */
        }
        writer.endArray();
        System.out.println("Saved Army");
/********************************************************/  
        writer.name("player");
        DrawGame.Player().Save(writer);
        System.out.println("Saved Player");
/********************************************************/
        writer.endObject();
/********************************************************/


		writer.close();
	 
		System.out.println("Done");
 
    	} catch (IOException e) {
		e.printStackTrace();
    	}
   	}
 
}