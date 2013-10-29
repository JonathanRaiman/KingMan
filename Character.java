
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;

public class Character extends GameAsset{
      private Double health;
      private Boolean alive;
      private double value;

      public Character (){
            super (); // tell above item to do its thing to build empty gameasset
            health   = 100.0;
            value    = 0.0;
            alive    = true;
      }

      public boolean Alive (){
            return alive;
      }
      public Character Revive (){
            alive  = true;
            health = 100.0;
            return this;
      }
      public Character Kill (){
            alive = false;
            health = 0.0;
            return this;
      }
      public Character Damage (Double damage){
            health += -1*damage;
            if (health <= 0.0){ // if health drops below zero character is no
                                // longer alive.
                  alive = false;
                  health = 0.0;
            }
            return this;
      }
      public Character Heal (Double healing){
            if (!alive){ // Only heal alive beings
                  health += healing;
                  if (health >= 100.0){
                        health = 0.0;
                  }
            }
            return this;
      }
      public double Value (){
            return value;
      }
      public Character setValue (Double newvalue){
            value = newvalue;
            return this;
      }
      public Character (Integer newx, Integer newy, String newtype, Map newworld){
            super (newx,newy,newtype, newworld);
            alive    = true;
            health   = 100.0;
            value    = 0.0;
      }
      public Character (Boolean usingtileset, Integer newx, Integer newy, String newtype, Map newworld){
            super (usingtileset, newx,newy,newtype, newworld);
            alive    = true;
            health   = 100.0;
            value    = 0.0;
      }
      public static void main (String[] args) {
      }
      public void Draw(Graphics2D graphics) {
            int xdraw = (Position()[0]+world.Size()[0]*Map.Tilesize()-world.Focus()[0]) % (world.Size()[0]*Map.Tilesize());
            int ydraw = (Position()[1]+world.Size()[1]*Map.Tilesize()-world.Focus()[1]) % (world.Size()[1]*Map.Tilesize());
            if (xdraw > world.Focus()[2] || xdraw < -1*Map.Tilesize() || ydraw > world.Focus()[3] || ydraw < -1*Map.Tilesize() ||  !alive){}
            else{
                  ((GameAsset)this).Draw(graphics);
            }
      }
}




