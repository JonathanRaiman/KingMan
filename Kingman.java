
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;
import java.util.*;
import java.awt.Font;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;

public class Kingman extends Player{

      public Kingman (Integer newx, Integer newy, Map newworld){
            super (newx,newy,"kingman", newworld);
      }

      public Kingman (Integer newx, Integer newy, Map newworld, Tile newdestination){
            super (newx,newy,"kingman", newworld, newdestination);
      }
      
      public Kingman (Boolean usingtileset, Integer newx, Integer newy, Map newworld, Tile newdestination){
            super (usingtileset, newx,newy,"kingman", newworld, newdestination);
      }

      public void Save(JsonWriter writer) throws IOException {
            writer.beginObject();
            writer.name("x").value(Position()[0]);
            writer.name("y").value(Position()[1]);
            writer.name("t").value("kingman");          
                  writer.name("destination").beginArray();
                  writer.value(Destination().Position()[0]);
                  writer.value(Destination().Position()[1]); 
                  writer.endArray();
            writer.endObject();
      }
      public static Kingman Load(JsonReader reader, Map readmap) throws IOException {
            reader.beginObject();
            int x=0;
            int y=0;
            Tile readdestination = readmap.World()[0][0];
            String type = "kingman";
            while (reader.hasNext()) {
                  String name = reader.nextName();
                  if (name.equalsIgnoreCase("x")){
                        x = reader.nextInt();
                  }
                  else if (name.equalsIgnoreCase("y")){
                        y = reader.nextInt();
                  }
                  else if (name.equalsIgnoreCase("t")){
                        String t = reader.nextString();
                  }
                  else if (name.equalsIgnoreCase("destination")){
                        reader.beginArray();
                        ArrayList<Integer> dest = new ArrayList<Integer>(2);
                        while (reader.hasNext()){
                              dest.add(reader.nextInt());
                        }
                        reader.endArray();
                        readdestination = readmap.World()[dest.get(0)][dest.get(1)];
                  }
                  else {reader.skipValue();}
            }
            reader.endObject();
            Kingman readkingman = new Kingman (false, x,y,readmap, readdestination);
            return readkingman;
      }

      public void Draw(Graphics2D graphics) {
            /* Calculate next step
            */
            // System.out.println("My destination coordinates are : ("+DestinationCoordinates()[0]+","+DestinationCoordinates()[1]+")");
            if (onCourse() && !Walking()){
                  WalkRank();
                  }
            else if (onCourse() && Walking()){
                  if (DestinationCoordinates()[0] == 0 && DestinationCoordinates()[1] == 0){
                        WalkRank();
                  }
                  else {
                        double distance = Math.sqrt(Math.pow(DestinationCoordinates()[0],2)+Math.pow(DestinationCoordinates()[1],2));
                        int steps = (int)((double)distance/Speed());
                        if (steps>0){
                              Step(DestinationCoordinates()[0]/steps, DestinationCoordinates()[1]/steps);
                              Walking(true);
                              Translate(Step()[0],Step()[1]);
                              DestinationCoordinates(DestinationCoordinates()[0]-Step()[0], DestinationCoordinates()[1]-Step()[1]);
                        }
                        else {
                              Translate(DestinationCoordinates()[0],DestinationCoordinates()[1]);
                              DestinationCoordinates(0,0);
                              Walking(false);
                              if (onCourse()){
                                    WalkRank(); 
                              }
                        }
                  }
            }
            else {}
            // add if step up is greater than step side display up, etc... to illustrate motion. Use Face() to do so.
            int xdraw = (Position()[0]+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize());
            int ydraw = (Position()[1]+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize());
                  if (xdraw > world.Focus()[2] || xdraw < -2*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -2*Map.Tilesize() ||  !Alive()){}
            else{
                  graphics.drawImage((((Image[])GameAsset.images.get("kingman_down"))[Frame()]), xdraw, ydraw, null);
                  Frame(Frame()+1);
                  if (Frame() == ((Image[])GameAsset.images.get("kingman_down")).length){
                        Frame(0);
                  }
            }
            boolean outsideboundaries = false;
            if (xdraw > world.Size()[0]*Map.Tilesize()-Map.Tilesize()){
                  xdraw=xdraw-world.Size()[0]*Map.Tilesize();
                  outsideboundaries = true;
            }
            if (ydraw > world.Size()[1]*Map.Tilesize()-Map.Tilesize()){
                  ydraw=ydraw-world.Size()[1]*Map.Tilesize();
                  outsideboundaries = true;
            }
            if (outsideboundaries){
                  graphics.drawImage((((Image[])GameAsset.images.get("kingman_down"))[Frame()]), xdraw, ydraw, null);
                  Frame(Frame()+1);
                  if (Frame() == ((Image[])GameAsset.images.get("kingman_down")).length){
                        Frame(0);
                  }
            }
      }
}

