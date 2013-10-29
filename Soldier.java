
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;
import java.util.*;
import java.awt.Font;
import java.io.IOException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;


public class Soldier extends Player{

      private ArrayList<Tile> patrolpoints = new ArrayList<Tile>();
      private int patrolschedule = 0;
      private boolean patroling = false;

      public Soldier (Integer newx, Integer newy, Map newworld){
            super (newx,newy,"soldier", newworld);
            // Speed(4);
      }
      public Soldier (Integer newx, Integer newy, Map newworld, Tile newdestination){
            super (newx,newy,"soldier", newworld, newdestination);
            // Speed(4);
      }
      public Soldier (Boolean usingtileset, Integer newx, Integer newy, Map newworld, Tile newdestination){
            super (usingtileset, newx,newy,"soldier", newworld, newdestination);
            // Speed(4);
      }
      public void GenerateRank (ArrayList<Tile> seeds, int currentrank, Tile start, Tile newdestination){
            if (Ranking()[start.Position()[0]][start.Position()[1]] != 999999){
                  // System.out.println("Found path in "+Ranking()[start.Position()[0]][start.Position()[1]]+" steps");
                  onCourse(true);
                  Destination(newdestination);
                  // WalkRank();
                  return; // the destination has a rank.
            }
            else if (currentrank > world.Size()[0]*world.Size()[1]/2){
                  // System.out.println("Not reachable...");
                  Destination(start);
                  onCourse(false);
                  if (patrolpoints.indexOf(newdestination) != -1){
                        patrolpoints.remove(patrolpoints.indexOf(newdestination));
                        patrolschedule++;
                        if (patrolschedule >= patrolpoints.size()){
                              patrolschedule = 0;
                        }
                        if (patrolpoints.size() == 0){
                              patroling = false;
                        }
                        else {
                              Walk(patrolpoints.get(patrolschedule));
                        }
                  }
                  return; // we've taken way too many steps... destination is not reachable
            }
            else if (seeds.size() == 0){ // we've eliminated all ways of getting there. We're landlocked.
                  // System.out.println("Landlocked...");
                  Destination(start);
                  onCourse(false);
                  if (patrolpoints.indexOf(newdestination) != -1){
                        patrolpoints.remove(patrolpoints.indexOf(newdestination));
                        patrolschedule++;
                        if (patrolschedule >= patrolpoints.size()){
                              patrolschedule = 0;
                        }
                        if (patrolpoints.size() == 0){
                              patroling = false;
                        }
                        else {
                              Walk(patrolpoints.get(patrolschedule));
                        }
                  }
                  return;
            }
            else {
            Object[] origseeds = seeds.toArray();
            
            for (int i=0; i<origseeds.length; i++) {
                  int[] pos = ((Tile)origseeds[i]).Position();
                  if (world.World()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]].Passable() <= Passable() && Ranking()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]] == 999999){
                        Ranking()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]] = currentrank;
                        seeds.add(world.World()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]]);
                  } 
                  if (world.World()[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]].Passable() <= Passable() && Ranking()[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]] == 999999){
                        Ranking()[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]] = currentrank;
                        seeds.add(world.World()[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]]);
                  } 
                  if (world.World()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]].Passable() <= Passable() && Ranking()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]] == 999999){
                        Ranking()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]] = currentrank;
                        seeds.add(world.World()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]]);
                  } 
                  if (world.World()[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]].Passable() <= Passable() && Ranking()[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]] == 999999){
                        Ranking()[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]] = currentrank;
                        seeds.add(world.World()[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]]);
                  } 
                  seeds.remove(seeds.indexOf(origseeds[i]));

            }
            GenerateRank(seeds, currentrank+1, start, newdestination);
            }
      }

      public void WalkRank(){
            Tile start = world.World()[(int)((double)Position()[0]/(double)Map.Tilesize())][(int)((double)Position()[1]/(double)Map.Tilesize())];
            // System.out.println("Player is on : "+start);
            if (!Walking() && onCourse()){
                  // System.out.println("Next tile is : "+NextTile(start));
                  int[] DNT = DeltaNextTile(start);
                  Walk(DNT[0],DNT[1]);
            }
            if (start == Destination()){
                  Walking(false);
                  onCourse(false);
                  if (patrolpoints.size()>0 && patroling){
                        patrolschedule++;
                        if (patrolschedule >= patrolpoints.size()){
                              patrolschedule = 0;
                        }
                        Walk(patrolpoints.get(patrolschedule));
                  }
                  // System.out.println("arrived at destination : "+tiledestination);
            }
            if (Walking() && onCourse()){
                  Walk(DestinationCoordinates()[0],DestinationCoordinates()[1]);
            }
      }

      public boolean Patroling(){
            return patroling;
      }

      public Soldier Schedule(int state){
            patrolschedule = state;
            return this;
      }

      public ArrayList<Tile> PatrolPoints(){
            return patrolpoints;
      }

      public Soldier Patrol(ArrayList<Tile> points){
            patroling = false;
            patrolpoints = points;
            if (patrolpoints.size()>1){
                  patroling = true;
                  Walk(patrolpoints.get(0));
            }
            return this;
      }

      public Soldier RandomPatrol(){
            ArrayList<Tile> newpoints = new ArrayList<Tile>(10);
            Random rand = new Random();
            int i = 0;
            while (i<11){
                  int xpoint = rand.nextInt(world.Size()[0]);
                  int ypoint = rand.nextInt(world.Size()[1]);
                  if (world.World()[xpoint][ypoint].Passable() <= Passable()){
                        newpoints.add(world.World()[xpoint][ypoint]);
                        i++;
                  }
            }
            Patrol(newpoints);
            return this;
      }

      public void Save(JsonWriter writer) throws IOException {
            writer.beginObject();
            writer.name("x").value(Position()[0]);
            writer.name("y").value(Position()[1]);
            writer.name("t").value("soldier");
                  writer.name("destination").beginArray();
                  writer.value(Destination().Position()[0]);
                  writer.value(Destination().Position()[1]); 
                  writer.endArray();           
            writer.name("schedule").value(patrolschedule);
            writer.name("patrolpoints").beginArray();
            for (Tile t : patrolpoints){
                  writer.beginObject();
                  writer.name("x").value(t.Position()[0]);
                  writer.name("y").value(t.Position()[1]);
                  writer.endObject();
            }
            writer.endArray();
            writer.endObject();
      }

      public static Soldier Load(JsonReader reader, Map readmap) throws IOException {
            reader.beginObject();
            int x=0;
            int y=0;
            ArrayList<Tile> readpatrolpoints = new ArrayList<Tile>();
            Tile readdestination = readmap.World()[0][0];
            int readschedule = 0;
            String type = "soldier";
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
                        // System.out.println("Added a destination of ("+dest.get(0)+","+dest.get(1)+")");
                  }
                  else if (name.equalsIgnoreCase("schedule")){
                        readschedule = reader.nextInt();
                  }
                  else if (name.equalsIgnoreCase("patrolpoints")){
                        reader.beginArray();
                        while (reader.hasNext()){
                              reader.beginObject();
                              int xpatrolpoint=0;
                              int ypatrolpoint=0;
                              while (reader.hasNext()){
                                    name = reader.nextName();
                                    if (name.equalsIgnoreCase("x")){
                                          xpatrolpoint = reader.nextInt();
                                    }
                                    else if (name.equalsIgnoreCase("y")){
                                          ypatrolpoint = reader.nextInt();

                                    }
                                    else {reader.skipValue();}
                              }
                              readpatrolpoints.add(readmap.World()[xpatrolpoint][ypatrolpoint]);
                              reader.endObject();
                        }
                        reader.endArray();
                  }
                  else {reader.skipValue();}
            }
            reader.endObject();
            Soldier readsoldier = new Soldier (false, x,y,readmap, readdestination);
            readsoldier.Patrol(readpatrolpoints).Schedule(readschedule);
            return readsoldier;
      }

      public void Draw(Graphics2D graphics) {
            /* Calculate next step
            */
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
            if (xdraw > world.Focus()[2] || xdraw < -1*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -1*Map.Tilesize() ||  !Alive()){}
            else{
                  graphics.drawImage((((Image[])GameAsset.images.get("soldier_down"))[Frame()]), xdraw, ydraw, null);
                  Frame(Frame()+1);
                  if (Frame() == ((Image[])GameAsset.images.get("soldier_down")).length){
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
                  graphics.drawImage((((Image[])GameAsset.images.get("soldier_down"))[Frame()]), xdraw, ydraw, null);
                  Frame(Frame()+1);
                  if (Frame() == ((Image[])GameAsset.images.get("soldier_down")).length){
                        Frame(0);
                  }
            }

      }
}

