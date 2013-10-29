/*

This Class creates a map based on parameters and global settings, it outputs a
2D Array with Tile objects in it

*/

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.*;
import java.util.Hashtable;

public class Map {
      private Integer xsize;
      private Integer ysize;
      private int[] focus;
      private int mountainsticky = 100;
      private double sealevel = 50000.0;
      static int TILE_SIZE = 20;
      private Tile[][] world;
      private Hashtable resources = new Hashtable();

      public static int Tilesize (){
            return TILE_SIZE;
      }

      public int[] Focus (){
            return focus;
      }
      public double Sealevel (){
            return sealevel;
      }

      public Hashtable Resources (){
            return resources;
      }
      
      public Tile[][] World (){
            return world;
      }
      
      public Map Cliff (int stickiness){
            if (stickiness<20){
                  mountainsticky = 20;
            }
            else if (stickiness>500){
                  mountainsticky = 499;
            }
            else {
                  mountainsticky = stickiness;
            }
            return this;
      }

      public int Cliff (){
            return mountainsticky;
      }
      
      public int[] Size (){
            int[] output = {xsize,ysize};
            return output;
      }
      public Map Size (int sizex, int sizey){
            xsize = sizex;
            ysize = sizey;
            world = new Tile[xsize][ysize];
            focus[0]  = 0;
            focus[1]  = 0;
            resources = new Hashtable();
            for (int x = 0;x<xsize;x++){
                  for (int y=0; y<ysize;y++){
                        world[x][y] = new Tile(x,y,"grass", this);
                  }
            }
            return this;
      }

      public Map (){
            focus     = new int[4];
            focus[0]  = 0;
            focus[1]  = 0;
            focus[2]  = DrawGame.hsize;
            focus[3]  = DrawGame.vsize;
            xsize     = 200;
            ysize     = 200;
            world     = new Tile[xsize][ysize];
            resources = new Hashtable();
            for (int x = 0;x<xsize;x++){
                  for (int y=0; y<ysize;y++){
                        world[x][y] = new Tile(x,y,"grass", this);
                  }
            }
      }
      public Map (int sizex, int sizey){
            focus     = new int[4];
            focus[0]  = 0;
            focus[1]  = 0;
            focus[2]  = DrawGame.hsize;
            focus[3]  = DrawGame.vsize;
            xsize     = sizex;
            ysize     = sizey;
            world     = new Tile[xsize][ysize];
            resources = new Hashtable();
            for (int x = 0;x<xsize;x++){
                  for (int y=0; y<ysize;y++){
                        world[x][y] = new Tile(x,y,"grass", this);
                  }
            }
      }

      public void Focus (int newx, int newy){
            focus[0] = (newx+xsize*Map.Tilesize()) % (xsize*Map.Tilesize());
            focus[1] = (newy+ysize*Map.Tilesize()) % (ysize*Map.Tilesize());
            focus[2] = DrawGame.hsize;
            focus[3] = DrawGame.vsize;
      }

      public void CameraTranslate (int dx, int dy){
            focus[0]+=dx;
            focus[1]+=dy;
      }

      public Map setWealth(double newwealth, String kind){
            if (newwealth > 100 || newwealth < 0){
                  return this;
            }
            int wells = (int)(((double)newwealth/100.0)*xsize*ysize);
            ArrayList<Resource> newresource = new ArrayList<Resource>(wells);
            resources.put(kind, newresource);
            int i = 0;
            Boolean[][] resourcemap = new Boolean[xsize][ysize];
            Random rand = new Random();
            while (i<wells){
                  int xpoint = rand.nextInt(xsize);
                  int ypoint = rand.nextInt(ysize);
                  if (resourcemap[xpoint][ypoint] == null && world[xpoint][ypoint].Type().equals("grass")){
                        Resource tempresource = new Resource(xpoint,ypoint,kind,rand.nextDouble()*2000, this);
/* We need to inform the asset which map it belongs too --> yes we have SOOO MANYY maps to deal with... but overall this helps the ability to save and reload maps and saving separate states :)
*/
                        newresource.add(tempresource);
                        i++;
                  }
            }
            return this;
      }

      public Map setTerrain(double granularity){
            if (granularity > 100 || granularity < 0){
                  return this;
            }
            int seeds = (int)(((double)granularity/100.0)*(double)xsize*(double)ysize);
            Boolean[][] moutainmap = new Boolean[xsize][ysize];
            ArrayList<Tile> recursivecliff = new ArrayList<Tile>();
            int i=0;
            Random rand = new Random();
            while (i<seeds){
                  int xpoint = rand.nextInt(xsize);
                  int ypoint = rand.nextInt(ysize);
                  /* RESOURCES are ONLY added on "grass" -> theoretically each resource could have its own 'favorite' land and we could attribute to each land tile a likelihood of having a resource type <-- OIL could appear more often on sand for instance and BANANAS on jungle tiles... */
                  if (moutainmap[xpoint][ypoint] == null && world[xpoint][ypoint].Type().equals("grass")){
                        world[xpoint][ypoint].Type("cliff");
                        moutainmap[xpoint][ypoint] = true;
                        recursivecliff.add(world[xpoint][ypoint]);
                        i++;
                  }
            }
            Grow(recursivecliff);
            return this;
      }

      public ArrayList<Soldier> addSoldiersCircle(int newx, int newy, int amount){
            ArrayList<Soldier> army = new ArrayList<Soldier>(amount);
            int i=0;
            for (double j=0.0;j<3*Math.PI;j=j+2*Math.PI/amount){
                  if (i == amount){
                        break;
                  }
                  int cos = (int)(Math.cos(j)*(double)amount);
                  int sin = (int)(Math.sin(j)*(double)amount);
                  if (newx+cos<0 || newx + cos > xsize-1 || newy+sin<0 || newy + sin > ysize-1){}
                  else {
                        army.add(new Soldier(newx+cos,newy+sin,this));
                        i++;
                  }

            }
            return army;
      }

      public ArrayList<Soldier> addSoldiers(int newx, int newy, int amount){
            ArrayList<Soldier> army = new ArrayList<Soldier>(amount);
            int i=0;
            while (i<amount){
                  army.add(new Soldier(newx,newy,this));
                  i++;
            }
            return army;
      }

      public Map resetTerrain(){
            for (int x = 0;x<xsize;x++){
                  for (int y=0; y<ysize;y++){
                        world[x][y].Type("grass");
                  }
            }
            return this;
      }

      public Map addWater(int destinationx, int destinationy, int brushsize){
            if (brushsize>100 || brushsize<0){
                  return this;
            }
            int i = 1;
            world[destinationx][destinationy].Type("water");
            while (i<=brushsize){
                  for (double j=0.0;j<2*Math.PI;j=j+0.02){
                        int cos = (int)(Math.cos(j)*(double)i);
                        int sin = (int)(Math.sin(j)*(double)i);
                        if (i == brushsize && !world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Type().equals("water")){
                              world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Type("sand");
                        }
                        else {
                              world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Type("water");
                        }
                  }
                  i++;
            }
            return this;
      }

      public Map addRivers(int startx, int starty, double angle){
            double variedangle = angle;
            int tilex = startx;
            int tiley = starty;
            tilex  = (tilex + xsize) % xsize;
            tiley  = (tiley + ysize) % ysize;
            int advancementx = 1;
            int advancementy = 1;
            int turn = 0;
            Random rand = new Random();
            if (Math.cos(angle)>Math.sin(angle)){
                  if (Math.sin(angle) == 0){
                        advancementy = 0;
                        advancementx = (int)Math.cos(angle);
                  }
                  else {
                        advancementx = (int)(Math.cos(angle)/Math.sin(angle));
                  }
                  if (Math.abs(advancementx)>5){
                        advancementx = 5*(advancementx/Math.abs(advancementx));
                  }
            }
            else if (Math.cos(angle)<Math.sin(angle)){
                  if (Math.cos(angle) == 0){
                        advancementy = (int)Math.sin(angle);
                        advancementx = 0;
                  }
                  else {
                        advancementy = (int)(Math.sin(angle)/Math.cos(angle));
                  }
                  if (Math.abs(advancementy)>5){
                        advancementy = 5*(advancementy/Math.abs(advancementy));
                  }
            }
            else {
                  advancementx = (int)(Math.cos(angle)/Math.abs(Math.cos(angle)));
                  advancementy = (int)(Math.sin(angle)/Math.abs(Math.sin(angle)));
            }
            while (tilex<xsize && tiley<ysize){
                  if (turn>(xsize+ysize)){
                        break;
                  }
                  tilex  = (tilex + xsize) % xsize;
                  tiley  = (tiley + ysize) % ysize;
                  this.addWater(tilex,tiley,4+rand.nextInt(5));
                  tilex += advancementx;
                  tiley += advancementy;
                  tilex  = (tilex + xsize) % xsize;
                  tiley  = (tiley + ysize) % ysize;
                  variedangle = angle + ((rand.nextDouble()-0.5)*2);
                  if (Math.cos(variedangle)>Math.sin(variedangle)){
                  if (Math.sin(variedangle) == 0){
                        advancementy = 0;
                        advancementx = (int)Math.cos(variedangle);
                  }
                  else {
                        advancementx = (int)(Math.cos(variedangle)/Math.sin(variedangle));
                  }
                  if (Math.abs(advancementx)>5){
                              advancementx = 5*(advancementx/Math.abs(advancementx));
                  }
                  }
                  else if (Math.cos(variedangle)<Math.sin(variedangle)){
                        if (Math.cos(variedangle) == 0){
                              advancementy = (int)Math.sin(variedangle);
                              advancementx = 0;
                        }
                        else {
                              advancementy = (int)(Math.sin(variedangle)/Math.cos(variedangle));
                        }
                        if (Math.abs(advancementy)>5){
                              advancementy = 5*(advancementy/Math.abs(advancementy));
                        }
                  }
                  else {
                        advancementx = (int)(Math.cos(variedangle)/Math.abs(Math.cos(variedangle)));
                        advancementy = (int)(Math.sin(variedangle)/Math.abs(Math.sin(variedangle)));
                  }
                  turn++;

            }
            return this;
      }

      public Map varyDepth(int destinationx, int destinationy, int brushsize, boolean positive){
            if (brushsize>100 || brushsize<0){
                  return this;
            }
            int i = 1;
            int modifier = 1;
            if (!positive){
                  modifier = -1;
            }
            world[destinationx][destinationy].Depth(world[destinationx][destinationy].Depth()+modifier*20);
            while (i<=brushsize){
                  for (double j=0.0;j<2*Math.PI;j=j+0.02){
                        int cos = (int)(Math.cos(j)*(double)i);
                        int sin = (int)(Math.sin(j)*(double)i);
                        double change = modifier*1/(Math.pow(0.25*i/brushsize,0.5));
                        double newdepth = world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Depth()+change*10;
                        if (newdepth<0){
                              world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Depth(0.0);
                        }
                        else {
                              world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Depth(newdepth);
                        }
                  }
                  i++;
            }
            return this;
      }
      public Map useTool(String tool, int destinationx, int destinationy, int brushsize){
            if (tool.equals("water")){
                  addWater(destinationx,destinationy,brushsize);
            }
            else if (tool.equals("shovel")){
                  varyDepth(destinationx,destinationy,brushsize,true);
            }
            else if (tool.equals("grass") || tool.equals("cliff") || tool.equals("sand")){
                  addSubstance(tool, destinationx,destinationy,brushsize);
            }
            else {}
            return this;
      }

      public Map addGrass(int destinationx, int destinationy, int brushsize){
            if (brushsize>100 || brushsize<0){
                  return this;
            }
            int i = 1;
            world[destinationx][destinationy].Type("grass");
            while (i<=brushsize){
                  for (double j=0.0;j<2*Math.PI;j=j+0.02){
                        int cos = (int)(Math.cos(j)*(double)i);
                        int sin = (int)(Math.sin(j)*(double)i);
                        world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Type("grass");
                  }
                  i++;
            }
            return this;
      }

      public Map addSubstance(String substance, int destinationx, int destinationy, int brushsize){
            if (brushsize>100 || brushsize<0){
                  return this;
            }
            int i = 1;
            world[destinationx][destinationy].Type(substance);
            while (i<=brushsize){
                  for (double j=0.0;j<2*Math.PI;j=j+0.02){
                        int cos = (int)(Math.cos(j)*(double)i);
                        int sin = (int)(Math.sin(j)*(double)i);
                        world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Type(substance);
                  }
                  i++;
            }
            return this;
      }
      public Map addCliff(int destinationx, int destinationy, int brushsize){
            if (brushsize>100 || brushsize<0){
                  return this;
            }
            int i = 1;
            world[destinationx][destinationy].Type("cliff");
            while (i<=brushsize){
                  for (double j=0.0;j<2*Math.PI;j=j+0.02){
                        int cos = (int)(Math.cos(j)*(double)i);
                        int sin = (int)(Math.sin(j)*(double)i);
                        world[(destinationx+cos+Size()[0])%Size()[0]][(destinationy+sin+Size()[1])%Size()[1]].Type("cliff");
                  }
                  i++;
            }
            return this;
      }
      /*
A NOTE ABOUT RECURSION:

While this method is described in the DrawGame class, it is important to note a few
points: The method stops executing when seed size = 0;

      */
      public void Grow (ArrayList<Tile> seeds){
            if (seeds.size() == 0){
                  return;
            }
            Random rand = new Random();
            Object[] origseeds = seeds.toArray();
            
            for (int i=0; i<origseeds.length; i++) {
                  int growthamount = rand.nextInt(3)+1;
                  int[] pos = ((Tile)origseeds[i]).Position();
                  /* Growth amount is random  */
                  while (growthamount>0){
                        int direction = rand.nextInt(4);
                        /* Direction is a random value N,E,S, or W.  */
                        switch (direction) {
                              case 0: {
                                          /* Cliffs only appear on grass tiles
                                          And to avoid array out of bound errors the tiles
                                          used for seeding only examine tiles within the map and tests for objects outside the map size
                                          or below 0 to avoid changing the type of a non-existent tile  */
                                    if (world[pos[0]][(pos[1]-1+Size()[1])%Size()[1]].Type().equals("grass")){
                                          world[pos[0]][(pos[1]-1+Size()[1])%Size()[1]].Type("cliff");
                                          /* Based on cliff "stickiness" add new members to the cliff for future formations... odds are 1 in 2 */
                                          int stickiness = rand.nextInt(mountainsticky);
                                          if (stickiness > 10){
                                                seeds.add(world[pos[0]][(pos[1]-1+Size()[1])%Size()[1]]);
                                          }
                                    }
                                    break; // NORTH
                              }
                              case 1:{
                                    if (world[(pos[0]+1+Size()[0])%Size()[0]][pos[1]].Type().equals("grass")){
                                          world[(pos[0]+1+Size()[0])%Size()[0]][pos[1]].Type("cliff");
                                          int stickiness = rand.nextInt(mountainsticky);
                                          if (stickiness > 10){
                                                seeds.add(world[(pos[0]+1+Size()[0])%Size()[0]][pos[1]]);
                                          }
                                    }
                                    break; // EAST
                              }
                              case 2:{
                                    if (world[pos[0]][(pos[1]+1+Size()[1])%Size()[1]].Type().equals("grass")){
                                          world[pos[0]][(pos[1]+1+Size()[1])%Size()[1]].Type("cliff");
                                          int stickiness = rand.nextInt(mountainsticky);
                                          if (stickiness > 10){
                                                seeds.add(world[pos[0]][(pos[1]+1+Size()[1])%Size()[1]]);
                                          }
                                    }
                                    break; // SOUTH
                              }
                              case 3:{
                                    if (world[(pos[0]-1+Size()[0])%Size()[0]][pos[1]].Type().equals("grass")){
                                          world[(pos[0]-1+Size()[0])%Size()[0]][pos[1]].Type("cliff");
                                          int stickiness = rand.nextInt(mountainsticky);
                                          if (stickiness > 10){
                                                seeds.add(world[(pos[0]-1+Size()[0])%Size()[0]][pos[1]]);
                                          }
                                    }
                                    break; // WEST
                              }
                        }
                        growthamount--;
                  }
                  seeds.remove(seeds.indexOf(origseeds[i]));
                  /* (DEPRECATED) REMOVES LANDLOCKED SEEDS
                  if (pos[1] == 0 || world[pos[0]][pos[1]-1].Type() != "grass"){
                        if (pos[0] == 0 || world[pos[0]-1][pos[1]].Type() != "grass"){
                              if (pos[0] == xsize || world[pos[0]+1][pos[1]].Type() != "grass"){
                                    if (pos[1] == ysize  || world[pos[0]][pos[1]+1].Type() != "grass"){
                                          seeds.remove(seeds.indexOf(t));
                                    }
                              }
                        }
                  }
                  */
            }
            Grow(seeds);
      }
      /*
A NOTE ABOUT DRAWING METHODS AND FOCUS():

Focus() is a method that controls where the camera is pointing; this has 2 main features
      a) Where to render the elements once the map is moved.

      b) What elements to show to avoid rendering elements that will be outside the scope
      of the window. 
            • as is visible in each .Draw() method each object queries Focus() to know
            if it is within the visible range of tiles.
            • An element within these bounds is drawn, others vanish on repaint().

      */
      public void Draw (Graphics2D graphics){
            for (int x = 0;x<xsize;x++){
                  for (int y=0; y<ysize;y++){
                        world[x][y].Draw(graphics);
                  }
            }
/* DRAWING RESOURCES OF ANY KIND:

Each resource is a key inside a hashtable, and the value of each key in this hash
of resources is an arraylist of resources. We extract keys into their own arraylist
of string-keys and then iterate through those. (there might be a better way to do 
this but the for each method tends to break down with hashtables here I've noticed...)

*/
            ArrayList<String> keys = Collections.list(resources.keys());
            for (String s : keys){
                  for (int i=0; i<((ArrayList<Resource>)resources.get(s)).size(); i++){
                        ((ArrayList<Resource>)resources.get(s)).get(i).Draw(graphics);
                  }
            }

      }
}