/*


Jonthan Raiman's CS51 Final Project:

      • This is a game that allows players to navigated a 'semi'
      randomly generated terrain.

      • The game also allows procedural deformations and additions using
      simple painting tools :
            -W for Water
            -G for Grass
            -C for Cliffs

      • The game has characters of two kinds:
            -The King (the player)
            -His Soldiers (within an army)

      • The characters react to terrain by attempting to go places and
      finding paths that avoid obstacles to reach their destinations.
      The path functions using a recursive algorithm :
            -M to reveal King's path ranking.

      • The player can create obstacles by placing terrain that is not
      suited for the characters : water tiles, or cliff tiles.

      • The player can seed resources to his land using :
            —B for Bananas (visible as palmtrees on the map)
            —O for Oil (visible as oil barrels on the map)

      • Resources cannot be placed individually, rather they are added
      according to parameters relating to their frequency: 0-100%.

      • Resource frequency controls the likelihood a tile will hold
      a particular resource.

      • Resources have seeding rules: trees and oil are found on grass
      only. If a map is all water then the seed will fail and the game
      will likely go into an infinite loop.

      • The terrain originally found on the map is built using a seeding
      and recursion method that can be called :
            —SPACEBAR for random cliff additions.
      
      • Unlike Resources that do not have recursive behavior, Cliffs
      built with the SPACEBAR will find seeds randomly to start their
      mountain ranges and will control their outgrowth using a stickiness
      parameter. A seed randomly selects :
            - how many outgrowths it can create
            — where these will go (North, East, South, or West)

      • Stickiness for a moutain defines whether :
            – An outgrowth will join the original moutain range's seeds
            for the next round of recursion.

      • Theoretically but very rarely, all of a moutain's outgrowth could
      join the original moutain and all their childs as well until... the
      entire map is covered using a single seed. The odds of this
      happening are very slim because stickiness controls how large of
      random integer is created. Only if this integer is larger than 10
      is the outgrowth added. So setting this number to be smaller than 10
      will render cliff chains impossible while any number larger than 10
      will increase the likelihood. However this probably is recalculated
      at each recursion, therefore the likelihoods are 'independent' and
      the odds are multiplied, exponentially decaying a cliff's
      growth pattern. Varying the stickiness value will thus affect how
      rapidly a cliff dies off and will thus have geological implications :)

      • Saving and Loading is done using keyboard keys :
            -S to Save game (will store Resources, Terrain, Characters, and their
            destinations);
            -L to Load game (will restore everything to previous state, will erase
            current game though so be sure to save and rename your saves to keep 
            them safe from being overwritten by an over-zealous Save);

      • Inheritance in the game is either simple or complex depending on the usage
      (or more precisely current usage since a lot of routes have been left open for
      increasing complexity later on)
            -Characters are GameAssets => Character => Player => Soldier OR Kingman
            -Tiles are GameStatic => Tile
            -Resources are GameAssets => Resource

      • It will appear that Character and Player are arbitrary distinctions but fear
      not there is a nuance:
            -players can have destinations and implement a walking routine while
            Characters or rather 'GameAssets' with health are Assets that can
            deteriorate / die or revive. Since they require no pathing etc.. they
            should be thought of as parachuted players without true legs. Maybe they
            have Wings.

      • If GameAssets appears bloated, or if it seems that Tiles should just mind their
      own business and not try to poke their images from there (which they used to, to
      display grass and other elements) well this is for a special purpose:
            -let us imagine that grass can be a lively creature that is in fact a
            camouflaged predator, then grass should be available for creatures. However
            in the off case that grass is actually static then rather than have both
            load their own version (this is indeed what Java does, it will load an
            invidual Fresh version for each call) they can call on the same image.
            - to recapitulate GameAssets are a rich class in that they provide services
            to the whole game when it comes to assets, but GameStatic serves a different
            respectable use : its tiles do not move, and this insures game stability and
            safety. Divorcing these features early on is evidenly crucial as can be
            seen from the many methods that depend on the predictable distance and locale
            of Tiles and Static assets.


*/

/*
TODO:

Make most of this not static

*/
import java.awt.Frame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.awt.MouseInfo;

public class DrawGame extends Canvas implements KeyListener, MouseListener,MouseMotionListener {
// Many of these assets are static : this is surely bad design for a server where
// multiple users while co-exist on a single game. But this is a single player game
// and thus because the methods do not need to know which instance of the game they
// must affect, using static will not affect gameplay.

      private static Frame frame = new Frame ();
      static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      static Integer hsize = dim.width;
      static Integer vsize = dim.height;
      private static boolean inDrag = false;
      private static int startX = -1;
      private static int startY = -1;
      private static Map world = new Map();
      private static int dx = -1;
      private static int dy = -1;
      private static int focusX = -1;
      private static int focusY = -1;
      private static Kingman player = new Kingman(0,5,world);
      private static ArrayList<Soldier> army = world.addSoldiers(30,30,75);
      // The following Booleans are not stored and control interface components:
      // These are the 'application states' to decide if the user is doing one of these
      // actions and how big the brush is for painting terrain.
      public static boolean paintranking = false;
      public static boolean fullscreen = true;
      public static String tool = "none";
      public static int brushsize = 5;
      public Interface userinterface;
      // private static Rectangle2D box = new Rectangle2D.Double(0, vsize-10, hsize, 10);
      

      private static void Close () {
            frame.setVisible (false);
            frame.dispose();
            System.exit(0);
      }

      public static Kingman Player () {
            return player;
      }
      public static Integer map (int min, int max, int newmin, int newmax, int value) {
            if (value>=max){
                  return newmax;
            }
            else if (value<=min){
                  return newmin;
            }
            else {
                  double output = (double)(value-min)/(double)(max-min);
                  return (int)(output*(newmax-newmin)+newmin);
            }
      }
      public static Integer map (int min, int max, int newmin, int newmax, double value) {
            if (value>=max){
                  return newmax;
            }
            else if (value<=min){
                  return newmin;
            }
            else {
                  double output = (double)(value-min)/(double)(max-min);
                  return (int)(output*(newmax-newmin)+newmin);
            }
      }

      public static ArrayList<Soldier> Army () {
            return army;
      }

      public static void Army (ArrayList<Soldier> newarmy) {
            army = newarmy;
      }

      public static void Player (Kingman newplayer) {
            player = newplayer;
      }

      public static Map Map(){
            return world;
      }

      public static void Map (Map newworld) {
            world = newworld;
      }

      public DrawGame () {
            userinterface = new Interface();
            world.addRivers(0,10,Math.PI/4);
            // world.setTerrain(0.05);
            // world.setTerrain(0.05);
            // world.setTerrain(0.05);
            // world.setTerrain(0.05);
            // world.setTerrain(0.05);
            world.setWealth(0.05, "oil");
            world.setWealth(1, "banana");
            /* We can tell the first soldier to go to these places
            ArrayList<Tile> patrolpoints = new ArrayList<Tile>(4);
            patrolpoints.add(world.World()[5][5]);
            patrolpoints.add(world.World()[55][12]);
            patrolpoints.add(world.World()[13][92]);
            patrolpoints.add(world.World()[0][10]);
            army.get(0).Patrol(patrolpoints);

            OR we can tell all of them to go to random places :
            */
            for (Soldier s : army){
                  s.RandomPatrol();
            }
            addMouseListener(this);
            frame.addKeyListener(this);
            addMouseMotionListener(this);
            this.addKeyListener(this);
      }

      public static void main (String[] args) {
            class WindowClosingListener extends WindowAdapter {
            public void windowClosing (WindowEvent evt) { Close();}
            }

            DrawGame n = new DrawGame();
            frame.setTitle ("Jonathan's Game Window");
            frame.setBackground (new Color(30,31,30));
            frame.setSize (hsize, vsize);
            frame.add(n);
            frame.setUndecorated(true);
            frame.setResizable(false);
            GraphicsDevice device;
            device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            device.setFullScreenWindow(frame);
            frame.addWindowListener (new WindowClosingListener ());
            frame.setVisible(true);
            while (true){
                  n.repaint();
                  try {
                        Thread.sleep (17);
                  } 
                  catch (InterruptedException e) {
                  }
            }

      }

      public void mouseEntered  (MouseEvent e) {
           
      } 
      public void mousePressed  (MouseEvent e) {
            focusX = world.Focus()[0];
            focusY = world.Focus()[1];
            startX = e.getX();
            startY = e.getY();
            inDrag = true;
      } 
      public void mouseExited   (MouseEvent e) {}  
      public void mouseMoved(MouseEvent e) {
             // userinterface.Mouse(e);
      }

      public void mouseClicked  (MouseEvent e) {
            int destinationx = (int)e.getX()+world.Focus()[0];
            int destinationy = (int)e.getY()+world.Focus()[1];
            Point mousecursor  = MouseInfo.getPointerInfo().getLocation();
            if (userinterface.MinimapOpen().contains(mousecursor) && !userinterface.Minimapping()){
                  userinterface.OpenMinimap();
            }
            else if (userinterface.MinimapClose().contains(mousecursor) && userinterface.Minimapping()){
                  userinterface.CloseMinimap();
            }
            else if (userinterface.Minimap().contains(mousecursor) && userinterface.Minimapping())
            {
                  userinterface.MinimapMove(e.getX(),e.getY());
            }
            else if (userinterface.Palette().contains(mousecursor) && userinterface.Paletting())
            {
                  userinterface.PickBrush(mousecursor);
            }
            else {
                  Tile destination = world.World()[((int)((double)destinationx/(double)Map.Tilesize()))%world.Size()[0]][((int)((double)destinationy/(double)Map.Tilesize()))%world.Size()[1]];
                  if (!tool.equals("none")){
                        world.useTool(tool,destination.Position()[0],destination.Position()[1],brushsize);
                        for (Soldier s : army){
                              s.Walk(s.Destination());
                        }
                        player.Walk(player.Destination());
                  }
                  else {
                  player.Destination().Type("grass");
                  destination.Color(new Color(200,10,10));
                  userinterface.State("Destination tile is "+destination, true);
                  userinterface.Flash();
                  player.Walk(destination);
                  }
            }
            
            // player.Walk(destinationx,destinationy);
      }
      public void mouseDragged(MouseEvent e) {
          dx = e.getX()-startX;
          dy = e.getY()-startY;
          if (inDrag) {
            world.Focus(focusX-dx,focusY-dy);
            // repaint();
          }
      }

      public void mouseReleased(MouseEvent e) {
          inDrag = false;
          // System.out.println("dx : " + dx + ", dy : " + dy);
          world.Focus(focusX-dx,focusY-dy);
          dx = 0;
          dy = 0;
          // repaint();
      }

      public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                        // KEYBOARD CONTROLS (WITHOUT REPEATS)
                        case KeyEvent.VK_ESCAPE: {
                              Close();
                              break;
                        }
                        case KeyEvent.VK_M: {
                              userinterface.State(0);
                              paintranking = false;
                              break;
                        }
                        case KeyEvent.VK_W: {
                              userinterface.Flash();
                              if (tool.equals("water")){
                                   userinterface.State(0);
                                   tool = "none";
                              }
                              else {
                                    userinterface.State("Add water");
                                    tool = "water";
                              }
                              break;
                        }
                        case KeyEvent.VK_G: {
                              userinterface.Flash();
                              if (tool.equals("grass")){
                                   userinterface.State(0);
                                   tool = "none";
                              }
                              else {
                                    userinterface.State("Add grass");
                                    tool = "grass";
                              }
                              break;
                        }
                        case KeyEvent.VK_C: {
                              userinterface.Flash();
                              if (tool.equals("cliff")){
                                   userinterface.State(0);
                                   tool = "none";
                              }
                              else {
                                    userinterface.State("Add cliff");
                                    tool = "cliff";
                              }
                              break;
                        }
                        case KeyEvent.VK_F: {
                              world.resetTerrain();
                              for (Soldier s : army){
                                    s.Walk(s.Destination());
                              }
                              player.Walk(player.Destination());
                              userinterface.State("Flattened Landmass with great power and determination.", true);
                              userinterface.Flash();
                              break;
                        }
                        case KeyEvent.VK_L: {
                              ReadMap read = new ReadMap(this);
                              break;
                        }
                        case KeyEvent.VK_S: {
                              try {
                                    SaveMap save = new SaveMap(world);
                                    userinterface.State("Map Saved", true);
                                    userinterface.Flash();
                              }
                              catch (IOException f){
                                    userinterface.State("Error saving :"+f, true);
                                    userinterface.Flash();
                              }
                              break;
                        }
            }
      }
      public void keyTyped(KeyEvent e) {}
      public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
                  // KEYBOARD CONTROLS (WITH REPEATS)
                  case KeyEvent.VK_UP: {
                        if (!tool.equals("none")){
                              brushsize++;
                              if (brushsize>100){
                                    brushsize = 100;
                              }
                              userinterface.State("Brushsize = "+brushsize, true);
                              userinterface.Flash();
                        }
                        else {
                              player.Speed(player.Speed()+0.05);
                              userinterface.State("Speed++ ", true);
                              userinterface.Flash();
                        }
                        break;
                  }
                  case KeyEvent.VK_DOWN: {
                        if (!tool.equals("none")){
                              brushsize--;
                              if (brushsize<1){
                                    brushsize = 1;
                              }
                              userinterface.State("Brushsize = "+brushsize, true);
                              userinterface.Flash();
                        }
                        else {
                              player.Speed(player.Speed()-0.05);
                              userinterface.State("Speed-- ", true);
                              userinterface.Flash();
                        }
                        break;
                  }
                  case KeyEvent.VK_SPACE: {
                        world.setTerrain(0.05);
                        for (Soldier s : army){
                              s.Walk(s.Destination());
                        }
                        player.Walk(player.Destination());
                        userinterface.State("Added Cliffs", true);
                        userinterface.Flash();
                        break;
                  }
                  case KeyEvent.VK_B: {
                        world.setWealth(1, "banana");
                        userinterface.State("Changed Bananas", true);
                        userinterface.Flash();
                        break;
                  }
                  case KeyEvent.VK_O: {
                        world.setWealth(0.05, "oil");
                        userinterface.State("Planted Oil Fields", true);
                        userinterface.Flash();
                        break;
                  }
                  case KeyEvent.VK_M: {
                        userinterface.State("King's path");
                        userinterface.Flash();
                        paintranking = true;
                        break;
                  }
            }
      }

      public void paint(Graphics g1) {
            Graphics2D g = (Graphics2D) g1;
            g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            world.Draw(g);
            for (Soldier s : army){
                  s.Draw(g);
            }
            player.DrawRank(g);
            player.Draw(g);
            userinterface.Draw(g);
      }
}


      





