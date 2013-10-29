
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;
import java.util.*;
import java.awt.Font;

public class Player extends Character{
      private String direction;
      private int frame               = 0;
      private double speed;
      private boolean walking         = false;
      private int[] destination       = new int[2];
      private boolean rankdestination = false;
      private int[] step              = new int[2];
      private int[][]  ranking        = new int[world.Size()[0]][world.Size()[1]];
      private Tile tiledestination;

      public Player (Integer newx, Integer newy, String newtype, Map newworld){
            super (newx,newy,newtype, newworld);
            direction = "down";
            speed = 1;
            ranking = new int[world.Size()[0]][world.Size()[1]];
                  for (int x = 0;x<world.Size()[0];x++){
                        for (int y=0; y<world.Size()[1];y++){
                              ranking[x][y] = 999999;
                        }
                  }
            destination[0] = 0;
            destination[1] = 0;
            step[0]        = 0;
            step[1]        = 0;
            tiledestination = world.World()[(int)((double)newx/(double)Map.Tilesize())][(int)((double)newy/(double)Map.Tilesize())];
      }
      public Player (Integer newx, Integer newy, String newtype, Map newworld, Tile newdestination){
            super (newx,newy,newtype, newworld);
            direction = "down";
            speed = 1.0;
            ranking = new int[world.Size()[0]][world.Size()[1]];
                  for (int x = 0;x<world.Size()[0];x++){
                        for (int y=0; y<world.Size()[1];y++){
                              ranking[x][y] = 999999;
                        }
                  }
            destination[0] = newx;
            destination[1] = newy;
            tiledestination = newdestination;
      }
      public Player (Boolean usingtileset, Integer newx, Integer newy, String newtype, Map newworld, Tile newdestination){
            super (usingtileset, newx,newy,newtype, newworld);
            direction = "down";
            speed = 1.0;
            // Creates an empty ranking:
            ranking = new int[world.Size()[0]][world.Size()[1]];
                  for (int x = 0;x<world.Size()[0];x++){
                        for (int y=0; y<world.Size()[1];y++){
                              ranking[x][y] = 999999;
                        }
                  }
            destination[0] = newx;
            destination[1] = newy;
            tiledestination = newdestination;
      }
      public Tile Destination() {
            return tiledestination;
      }
      public Player Destination(Tile changeddestination) {
            tiledestination = changeddestination;
            return this;
      }
      public boolean Walking() {
            return walking;
      }
      public int[] Step(){
            return step;
      }
      public int[][] Ranking(){
            return ranking;
      }
      public Player Step(int stepx, int stepy){
            step[0] = stepx;
            step[1] = stepy;
            return this;
      }
      public Player Walking(boolean choice) {
            walking = choice;
            return this;
      }
      public int[] DestinationCoordinates() {
            return destination;
      }
      public Player DestinationCoordinates(int dx, int dy) {
            destination[0] = dx;
            destination[1] = dy;
            return this;
      }
      public Player Frame(int newframe){
            frame = newframe;
            return this;
      }
      public int Frame(){
            return frame;
      }
      public boolean onCourse(){
      // is the character following a path or is he awaiting new orders.
            return rankdestination;
      }
      public Player onCourse(boolean choice){
            rankdestination = choice;
            return this;
      }
      public Player Face (String newdirection){
            direction = newdirection;
            return this;
      }
      public Player Speed (double newspeed){
            if (newspeed< 1){
                  return this;
            }
            speed = newspeed;
            return this;
      }
      public double Speed (){
            return speed;
      }
      public Player Walk (int dx, int dy){
            DestinationCoordinates(dx,dy);
            if (DestinationCoordinates()[0] == 0 && DestinationCoordinates()[1] == 0){
                  Walking(false);
            }
            else {
                  double distance = Math.sqrt(Math.pow(DestinationCoordinates()[0],2)+Math.pow(DestinationCoordinates()[1],2));
                  int steps = (int)((double)distance/Speed());
                  if (steps>0){
                        Walking(true);
                        Step(dx/steps, dy/steps);
                        Translate(Step()[0],Step()[1]);
                        DestinationCoordinates(DestinationCoordinates()[0]-Step()[0], DestinationCoordinates()[1]-Step()[1]);
                  }
                  else {
                        Translate(dx,dy);
                        DestinationCoordinates(0,0);
                        if (onCourse()){
                              Walking(false);
                              WalkRank();
                        }
                  }
            }
            return this;
      }
      public Player Walk (Tile newdestination){
            /* First we check to see if our passability 'rights' are higher
            than or equal to those of the tile (where water tiles might
            require a passability of 2 and we would allow planes
            to fly over but humans without a boat or the ability to swim
            may not)*/
            // destination[0]=Position()[0];
            // destination[1]=Position()[1];
            Tile start = world.World()[(int)((double)Position()[0]/(double)Map.Tilesize())][(int)((double)Position()[1]/(double)Map.Tilesize())];
            if (newdestination.Passable()<= Passable()){
                  ranking = new int[world.Size()[0]][world.Size()[1]];
                  for (int x = 0;x<world.Size()[0];x++){
                        for (int y=0; y<world.Size()[1];y++){
                              ranking[x][y] = 999999;
                        }
                  }
                  // ranking[(int)((double)Position()[0]/(double)Map.Tilesize())][(int)((double)Position()[1]/(double)Map.Tilesize())] = 0;
                  ranking[newdestination.Position()[0]][newdestination.Position()[1]] = 0;
                  ArrayList<Tile> seeds = new ArrayList<Tile>();
                  
                  seeds.add(newdestination);
                  GenerateRank(seeds, 1, start, newdestination);
            }
            else if (start.Passable() <= Passable()){
                  // Walk(start);
            }
            else {
            }
            return this;
      }

      public void GenerateRank (ArrayList<Tile> seeds, int currentrank, Tile start, Tile newdestination){
            if (ranking[start.Position()[0]][start.Position()[1]] != 999999){
                  // System.out.println("Found path in "+ranking[start.Position()[0]][start.Position()[1]]+" steps");
                  rankdestination = true;
                  tiledestination = newdestination;
                  // WalkRank();
                  return; // the destination has a rank.
            }
            else if (currentrank > world.Size()[0]*world.Size()[1]/2){
                  // System.out.println("Not reachable...");
                  tiledestination = start;
                  rankdestination = false;
                  return; // we've taken way too many steps... destination is not reachable
            }
            else if (seeds.size() == 0){ // we've eliminated all ways of getting there. We're landlocked.
                  // System.out.println("Landlocked...");
                  tiledestination = start;
                  rankdestination = false;
                  return;
            }
            else {
            Object[] origseeds = seeds.toArray();
            
            for (int i=0; i<origseeds.length; i++) {
                  int[] pos = ((Tile)origseeds[i]).Position();
                  
                  if (world.World()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]].Passable() <= Passable() && ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]] == 999999){
                        ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]] = currentrank;
                        seeds.add(world.World()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]]);
                  } 
            
                  if (world.World()[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]].Passable() <= Passable() && ranking[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]] == 999999){
                        ranking[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]] = currentrank;
                        seeds.add(world.World()[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]]);
                  } 
           
                  if (world.World()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]].Passable() <= Passable() && ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]] == 999999){
                        ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]] = currentrank;
                        seeds.add(world.World()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]]);
                  } 
           
                  if (world.World()[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]].Passable() <= Passable() && ranking[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]] == 999999){
                        ranking[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]] = currentrank;
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
            if (!walking && onCourse()){
                  // System.out.println("Next tile is : "+NextTile(start));
                  int[] DNT = DeltaNextTile(start);
                  Walk(DNT[0],DNT[1]);
            }
            if (start == tiledestination){
                  Walking(false);
                  onCourse(false);
                  // System.out.println("arrived at destination : "+tiledestination);
            }
            if (walking && onCourse()){
                  Walk(DestinationCoordinates()[0],DestinationCoordinates()[1]);
            }
      }
/*
The following method evaluates the ranking of neighboring tiles for lowest rank
and chooses the best as the new tile for the player to walk to -> lowest rank means
closest to 0, the source of rankings, the destination... so this tile must be closer
than the one we are currently on.
*/
      public Tile NextTile(Tile start){
            Tile nexttile = start;
            int bestranking = ranking[start.Position()[0]][start.Position()[1]];
            int[] pos = start.Position();
                  if (ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]] != 999999 && ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]]<bestranking){
                        nexttile = world.World()[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]];
                        bestranking = ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]];
                  }
                  if (ranking[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]] != 999999 && ranking[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]]<bestranking){
                        nexttile = world.World()[(pos[0]+1+world.Size()[0])%world.Size()[0]][pos[1]];
                        bestranking = ranking[(pos[0]+1+world.Size()[0])%world.Size()[0]][pos[1]];
                  }
                  if (ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]] != 999999 && ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]]<bestranking){
                        nexttile = world.World()[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]];
                        bestranking = ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]];
                  } 
                  if (ranking[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]] != 999999 && ranking[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]]<bestranking){
                        nexttile = world.World()[(pos[0]-1+world.Size()[0])%world.Size()[0]][pos[1]];
                        bestranking = ranking[(pos[0]-1+world.Size()[0])%world.Size()[0]][pos[1]];
                  }
            return nexttile;
      }

      public int[] DeltaNextTile(Tile start){
            int[] delta = {0,0};
            int bestranking = ranking[start.Position()[0]][start.Position()[1]];
            int[] pos = start.Position();
            if (ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]] != 999999 && ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]]<bestranking){
                  bestranking = ranking[pos[0]][(pos[1]-1+world.Size()[1])%world.Size()[1]];
                  delta[0] = 0;
                  delta[1] = -1*Map.Tilesize();
            }
            if (ranking[(pos[0]+1+world.Size()[0])%world.Size()[0]][pos[1]] != 999999 && ranking[(pos[0]+1+world.Size()[0])%world.Size()[1]][pos[1]]<bestranking){
                  bestranking = ranking[(pos[0]+1+world.Size()[0])%world.Size()[0]][pos[1]];
                  delta[0] = Map.Tilesize();
                  delta[1] = 0;
            }
            if (ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]] != 999999 && ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]]<bestranking){
                  bestranking = ranking[pos[0]][(pos[1]+1+world.Size()[1])%world.Size()[1]];
                  delta[0] = 0;
                  delta[1] = Map.Tilesize();
            } 
            if (ranking[(pos[0]-1+world.Size()[0])%world.Size()[0]][pos[1]] != 999999 && ranking[(pos[0]-1+world.Size()[0])%world.Size()[1]][pos[1]]<bestranking){
                  bestranking = ranking[(pos[0]-1+world.Size()[0])%world.Size()[0]][pos[1]];
                  delta[0] = -1*Map.Tilesize();
                  delta[1] = 0;
            }
            return delta;
      }


      public static void main (String[] args) {
      }
      public void DrawRank(Graphics2D graphics){
            if (DrawGame.paintranking){
                  graphics.setColor(Color.BLACK);
                  graphics.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
                  for (int x = 0;x<world.Size()[0];x++){
                        for (int y=0; y<world.Size()[1];y++){
                              int xdraw = ((x*Map.Tilesize()+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize()))+Map.Tilesize()/2;
                              int ydraw = (((y+1)*Map.Tilesize()+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize()))-Map.Tilesize()/2;
                              if (xdraw > world.Focus()[2] || xdraw < -1*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -1*Map.Tilesize()){}
                              else {
                                    if (ranking[x][y] == 999999){
                                          graphics.setColor(new Color(100,100,100));
                                          graphics.drawString("X", xdraw,ydraw);
                                          graphics.setColor(Color.BLACK);
                                    }
                                    else{
                                    graphics.drawString(""+ranking[x][y], xdraw,ydraw);
                                    }
                              }
                        }
                  }
            }
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
                              // System.out.println("Taking a Step : ("+Step()[0]+","+Step()[1]+")");
                        }
                        else {
                              Translate(DestinationCoordinates()[0],DestinationCoordinates()[1]);
                              DestinationCoordinates(0,0);
                              Walking(false);
                              // System.out.println("We've arrived");
                              if (onCourse()){
                                    WalkRank(); 
                              }
                        }
                  }
            }
            else {}
            /*Need to add if step up is greater than step side display up, etc...
             to illustrate motion. Use Face() to do so.*/
            int xdraw = (Position()[0]+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize());
            int ydraw = (Position()[1]+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize());
            if (xdraw > world.Focus()[2] || xdraw < -1*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -1*Map.Tilesize() ||  !Alive()){}
            else{
                  graphics.drawImage((((Image[])GameAsset.images.get("soldier_down"))[Frame()]), xdraw, ydraw, null);
                  Frame(Frame()+1);
                  if (frame == ((Image[])GameAsset.images.get("soldier_down")).length){
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

