
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;
import com.google.gson.stream.JsonWriter;

public class Resource extends GameAsset{
      private double value;

      public Resource (){
            super (); // tell above item to do its thing to build empty gameasset
            value    = 0.0;
      }
      public Resource Restore (Double amount){
            value = amount;
            return this;
      }
      public Resource Drain (){
            value = 0.0;
            return this;
      }
      public Resource Withdraw (Double amount){
            value += -1*amount;
            if (value <= 0.0){ // if value drops below zero, return it to zero.
                  value = 0.0;
            }
            return this;
      }

      public Resource Add (Double amount){
            value += amount;
            return this;
      }

      public double Value (){
            return value;
      }
      public Resource setValue (Double newvalue){
            value = newvalue;
            return this;
      }
      public Resource (Integer newx, Integer newy, String newtype, double newquantity, Map newworld){
            super (newx,newy,newtype, newworld);
            value   = newquantity;
      }
      public Resource (Boolean usingtileset, Integer newx, Integer newy, String newtype, double newquantity, Map newworld){
            super (usingtileset, newx,newy,newtype, newworld);
            value   = newquantity;
      }
      public static void main (String[] args) {
      }

      public void Draw(Graphics2D graphics) {
            int xdraw = (Position()[0]+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize());
            int ydraw = (Position()[1]+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize());
            if (xdraw > world.Focus()[2] || xdraw < -1*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -1*Map.Tilesize()){}
            else{
                  if (Type().equals("oil")){
                        graphics.drawImage((Image)GameAsset.images.get("oil"), xdraw+4, ydraw, null);
                  }
                  else if (Type().equals("banana")){
                        graphics.drawImage((Image)GameAsset.images.get("banana"), xdraw+5, ydraw, null);
                  }
                  else {
                        Color c = Color();
                        graphics.setColor(c);
                        graphics.fillOval(xdraw+Map.Tilesize () / 4,ydraw+Map.Tilesize () / 4,Map.Tilesize () /2 , Map.Tilesize ()/2);
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
                  if (Type().equals("oil")){
                        graphics.drawImage((Image)GameAsset.images.get("oil"), xdraw+4, ydraw, null);
                  }
                  else if (Type().equals("banana")){
                        graphics.drawImage((Image)GameAsset.images.get("banana"), xdraw+5, ydraw, null);
                  }
                  else {
                        Color c = Color();
                        graphics.setColor(c);
                        graphics.fillOval(xdraw+Map.Tilesize () / 4,ydraw+Map.Tilesize () / 4,Map.Tilesize () /2 , Map.Tilesize ()/2);
                  }
            }
      }
      public String toString() {
            return "("+this.Type()+", "+this.value+", ("+this.Position()[0]+","+this.Position()[1]+")";
      }
}