
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

public class GameAsset {
      private double health;
      private int x;
      private int y;
      private String type;
      private Color color;
      private boolean alive;
      public Map world;
      private int passable;
      private static Hashtable assetcolors = new Hashtable();
      public static Hashtable images = new Hashtable();
      /*
The static method loads up our Hashtables with content :
      • Colors & Images.
      • Some images are arrays to allow for looping animations like the walking
      animation of our characters... of course we only have _down_# for now,
      but we can easily add 4 more image arrays for _up_, _left_, _right_ and _idle_ 
      •this is quite close to how actual games do this, but of course this is rather
      costly memory-wise and thus we may start to think about not hosting ALL assets
      inside the GameAsset class and instead break down the burden to make sure only
      useful assets are loaded.
      */
      static {
            assetcolors.put("bush", new Color(123, 199, 145));
            assetcolors.put("tree", new Color(58, 134, 7));
            assetcolors.put("player", new Color(255, 255, 255));
            assetcolors.put("oil", new Color(30, 31, 30));
            assetcolors.put("oil_depleted",new Color(211, 211, 211));
            assetcolors.put("soldier",new Color(42, 64, 0));
            
            images.put("oil", Load("barrel_small.png"));
            images.put("banana", Load("palmtree_small.png"));
            images.put("grass", Load("grass_small.png"));
            images.put("sand", Load("sand_small.png"));
            Image[] kingmen_down = {Load("kingman_down_1.png"),Load("kingman_down_2.png"),Load("kingman_down_3.png"),Load("kingman_down_4.png"),Load("kingman_down_5.png"),Load("kingman_down_6.png"),Load("kingman_down_7.png"),Load("kingman_down_8.png")};
            Image[] soldier_down = {Load("soldier_down_1.png"),Load("soldier_down_2.png"),Load("soldier_down_3.png"),Load("soldier_down_4.png"),Load("soldier_down_5.png"),Load("soldier_down_6.png"),Load("soldier_down_7.png"),Load("soldier_down_8.png")};
            images.put("soldier_down", soldier_down);
            images.put("kingman_down", kingmen_down);
            images.put("water_icon", Load("water_icon_small.png"));
            images.put("grass_icon", Load("grass_icon_small.png"));
            images.put("cliff_icon", Load("cliff_icon_small.png"));
            images.put("shovel_icon", Load("shovel_icon_small.png"));
            images.put("sand_icon", Load("sand_icon_small.png"));
            images.put("close", Load("close.png"));
            images.put("minimap_open", Load("minimapopen.png"));
      };

      public static Image Load(String name) {
            return Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(".//assets/"+name));
      }

      public GameAsset (){
            x        = 0;
            y        = 0;
            type     = null;
            color    = new Color(0,0,0);
            passable = 0;
      }
      public Color Color (){
            return color;
      }
      public String Type (){
            return type;
      }
      public GameAsset setColor (Color newcolor){
            color = newcolor;
            return this;
      }

      public void Move (int destinationx, int destinationy){
            x = (destinationx + world.Size()[0]*Map.Tilesize()) % (world.Size()[0]*Map.Tilesize());
            y = (destinationy + world.Size()[1]*Map.Tilesize()) % (world.Size()[1]*Map.Tilesize());
      }
      public void Translate (int dx, int dy){
            x = (x + dx + world.Size()[0]*Map.Tilesize()) % (world.Size()[0]*Map.Tilesize());
            y = (y + dy + world.Size()[1]*Map.Tilesize()) % (world.Size()[1]*Map.Tilesize());
      }
      // Adapt Walk Function to have tiles as destination.
      public void Move (Tile destination){
            if (destination.Passable() <= passable){
                  x = destination.Position()[0];
                  y = destination.Position()[1];
            }
      }
      public int Passable (){
            return passable;
      }

      public GameAsset setPassable (int kind){
            passable = kind;
            return this;
      }
      public GameAsset (Integer newx, Integer newy, String newtype, Map newworld){
            x        = newx*Map.Tilesize();
            y        = newy*Map.Tilesize();
            type     = newtype;
            color    = (Color)assetcolors.get(newtype);
            passable = 0;
            world    = newworld;
      }
      public GameAsset (Boolean usingtileset, Integer newx, Integer newy, String newtype, Map newworld){
            if (usingtileset){
                  x        = newx*Map.Tilesize();
                  y        = newy*Map.Tilesize();
            }
            else {
                  x        = newx;
                  y        = newy;
            }
            type     = newtype;
            color    = (Color)assetcolors.get(newtype);
            passable = 0;
            world    = newworld;
      }
      public static void main (String[] args) {
      }
      public int[] Position (){
            int[] output = {x,y};
            return output;
      }
      public void Draw(Graphics2D graphics) {
            int xdraw = (Position()[0]+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize());
            int ydraw = (Position()[1]+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize());
            if (xdraw > world.Focus()[2] || xdraw < -1*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -1*Map.Tilesize()){}
            else{
                  Color c = Color();
                  graphics.setColor(c);
                  graphics.fillOval(xdraw+Map.Tilesize () / 4,ydraw+Map.Tilesize () / 4,Map.Tilesize () /2 , Map.Tilesize ()/2);
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
                  Color c = Color();
                  graphics.setColor(c);
                  graphics.fillOval(xdraw+Map.Tilesize () / 4,ydraw+Map.Tilesize () / 4,Map.Tilesize () /2 , Map.Tilesize ()/2);
            }

      }
}

      





