// Curve.java -- demonstrate 2D curve drawing possibilities

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;

public class Tile implements GameStatic{
      private int x;
      private int y;
      private String type;
      private Color color;
      private double z;
      Map world;
      private int passable;
      private static Hashtable tilepassage = new Hashtable();
      private static Hashtable tilecolors = new Hashtable();
      static {
            tilepassage.put("grass","0");
            tilecolors.put("grass", new Color(133, 209, 155));
            tilepassage.put("cliff","1");
            tilecolors.put("cliff", new Color(72, 89, 14));
            tilepassage.put("sand","0");
            tilecolors.put("sand", new Color(245, 236, 204));
            tilepassage.put("water","1");
            tilecolors.put("water", new Color(93, 182, 204));
            tilecolors.put("shovel", new Color(100, 101, 100));
      };

      public Tile (){
            x        = 0;
            y        = 0;
            z        = world.Sealevel()+50;
            type     = null;
            color    = new Color(0,0,0);
            passable = 0;
            world    = DrawGame.Map();
      }
      public static Hashtable Tilecolors (){
            return tilecolors;
      }
      public Color Color (){
            return color;
      }

      public String Type (){
            return type;
      }

      public Tile Color (Color newcolor){
            color = newcolor;
            return this;
      }
      public Color Color (int newr, int newg, int newb){
            if (newr>255 || newg>255 || newb>255 || newr<0 || newg<0 || newb<0){
                  return color;
            }
            else {
                  Color newcolor = new Color(newr,newg,newb);
                  return newcolor;
            }
      }
      public Tile Depth (double newdepth ){
            if (newdepth<=0){
                  z = 0;
            }
            else {
                  z     = newdepth;
            }
            if (z<=world.Sealevel()){
                  Type("water");
            }
            else if (z>world.Sealevel() && type.equals("water")){
                  Type("sand");
            }
            else {
                  color = BuildColor();
            }
            return this;
      }
      public double Depth (){
            return z;
      }

      public Tile Type (String newtype){
            if (type.equals("water") && !newtype.equals("water")){
                  z = world.Sealevel()+1;
            }
            if (newtype.equals("water") && !type.equals("water")){
                  z = world.Sealevel()-1;
            }
            type     = newtype;
            passable = Integer.parseInt((String)tilepassage.get(newtype));
            color    = BuildColor();
            return this;
      }

      public int[] Position (){
            int[] output = {x,y};
            return output;
      }

      public int Passable (){
            return passable;
      }

      public Tile (int newx, int newy, String newtype, Map newworld){
            world    = newworld;
            x        = newx;
            y        = newy;
            z        = world.Sealevel()+50;
            type     = newtype;
            color    = BuildColor();
            passable = Integer.parseInt((String)tilepassage.get(newtype));
      }
      public Color BuildColor(){
            if (type.equals("grass")){
                  return new Color(DrawGame.map((int)world.Sealevel()+5000,100000,133,72,z),DrawGame.map((int)world.Sealevel(),100000,209,89,z),DrawGame.map((int)world.Sealevel(),100000,155,14,z));
            }
            else if (type.equals("water")){
                  return new Color(DrawGame.map(0,(int)world.Sealevel(),10,117,z),DrawGame.map(0,(int)world.Sealevel(),44,204,z),DrawGame.map(0,(int)world.Sealevel(),111,188,z));
            }
            else if (type.equals("sand")){
                  if (z>world.Sealevel()+10000){
                        return new Color(DrawGame.map((int)world.Sealevel()+10000,100000,245,191,z),DrawGame.map(0,(int)world.Sealevel(),236,201,z),DrawGame.map(0,(int)world.Sealevel(),204,150,z));
                  }
                  else {
                        return new Color(DrawGame.map((int)world.Sealevel(),(int)world.Sealevel()+5000,227,245,z),DrawGame.map(0,(int)world.Sealevel(),206,236,z),DrawGame.map(0,(int)world.Sealevel(),116,204,z));
                  }
            }
            else {
                  Color newcolor    = (Color)tilecolors.get(type);
                  newcolor    = Color((int)(newcolor.getRed()+z*0.01),(int)(newcolor.getGreen()+z*0.01),(int)(newcolor.getBlue()+z*0.01));
                  return newcolor;
            }
      }

      public static void main (String[] args) {
      }

      public String toString () {
            return type+", ("+x+", "+y+")";
      }
      
      public void Draw(Graphics2D graphics) {
            int xdraw = (Position()[0]*Map.Tilesize()+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize());
            int ydraw = (Position()[1]*Map.Tilesize()+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize());
            if (xdraw > world.Focus()[2] || xdraw < -2*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -2*Map.Tilesize()){}
            else{
                  if (type == "grassy"){
                        graphics.drawImage((Image)GameAsset.images.get("grass"), xdraw, ydraw, null);
                  }
                  else {
                  Color c = this.color;
                  graphics.setColor(c);
                  graphics.fillRect(xdraw, ydraw,Map.Tilesize(), Map.Tilesize());
                  /*
                  g.fillOval(
                              x * TILE_SIZE + TILE_SIZE / 4,
                              y * TILE_SIZE + TILE_SIZE / 4,
                              TILE_SIZE / 2,
                              TILE_SIZE / 2);
                  */
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
                  Color c = this.color;
                  graphics.setColor(c);
                  graphics.fillRect(xdraw, ydraw,Map.Tilesize(), Map.Tilesize());
            }
      }
}


      





