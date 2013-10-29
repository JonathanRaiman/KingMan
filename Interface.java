import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Hashtable;
import java.awt.Image;
import java.util.*;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import java.awt.MouseInfo;
/*
Displays text at the bottom of the page based on queries to boolean states
Is its own class to eventually have more advanced interface take over :

	• images or icons to display tools :
	• Rather than using the W key for water we could have a water drop :)

*/
public class Interface {
	private int framestate;
	private Rectangle2D topbar;
	private Rectangle2D box;
	private Rectangle2D palette;
	private Rectangle2D palette_border;
	private Rectangle2D minimap_border;
	private Rectangle2D minimap;
	private String state;
	private boolean junk;
	private boolean paletting;
	private boolean minimapping;
	private String brush_selected = "grass";
	private ArrayList<String> brush_selections = Collections.list(Tile.Tilecolors().keys());
	private ArrayList<Rectangle2D> brush_bbox = new ArrayList<Rectangle2D>();
	private static int SIZE = 25;
	private static int SPEED = 2;
	private static int PALETTESIZE = 40;
	private static int MINIMAPXSIZE = 200;
	private static int MINIMAPXOFFSET = 60;
	private static int MINIMAPYSIZE = (200*DrawGame.Map().Size()[1])/(DrawGame.Map().Size()[0]);
	private static Color palette_color = new Color(30,31,30);
	private static Color border_color = new Color(200,210,200);
	private Rectangle2D minimapclose = new Rectangle2D.Double(MINIMAPXOFFSET+MINIMAPXSIZE-13, DrawGame.vsize-MINIMAPXOFFSET-MINIMAPYSIZE-15, 30,30);
	private Rectangle2D minimapopen = new Rectangle2D.Double(0, DrawGame.vsize-30, 30,30);
	private int timer = 0;
	private static String schpeel = "W : Water, C : Cliff, G : Grass, F: Flatten, L: Load, S: Save, SPACE : Randomize, UP/DOWN : +/- Hero Speed OR +/-Brushsize ";

	public Interface State(String newstate){
		state = newstate;
		return this;
	}
	public Interface State(String newstate, boolean isjunk){
		state = newstate;
		junk = isjunk;
		return this;
	}
	public Interface TogglePalette(){
		if (paletting){
			paletting = false;
		}
		else {
			paletting = true;
		}
		return this;
	}
	public boolean Paletting(){
		return paletting;
	}
	public Rectangle2D Palette(){
		return palette;
	}
	public Interface State(int newstate){
		if (newstate == 0){
			state = schpeel;
			junk = false;
		}
		return this;
	}
	public Rectangle2D Minimap(){
		return minimap;
	}
	public Rectangle2D MinimapClose(){
		return minimapclose;
	}
	public Rectangle2D MinimapOpen(){
		return minimapopen;
	}
	public void OpenMinimap(){
		minimapping = true;
	}
	public void CloseMinimap(){
		minimapping = false;
	}

	public boolean Minimapping(){
		return minimapping;
	}

	public void MinimapMove(int newx, int newy) {
		double minimaptilesize = ((double) MINIMAPXSIZE)/((double) DrawGame.Map().Size()[0]);
		int destinationx = (int)((double)(newx-MINIMAPXOFFSET)/minimaptilesize);
		int destinationy = (int)((double)(newy+MINIMAPXOFFSET-DrawGame.vsize)/minimaptilesize);
		DrawGame.Map().Focus(destinationx*Map.Tilesize()-(int)((double)DrawGame.hsize/2),destinationy*Map.Tilesize()-(int)((double)DrawGame.vsize/2));
	}

	public void PickBrush(Point selection){
		for (int i=0;i<brush_bbox.size();i++){
			if (brush_bbox.get(i).contains(selection)){
				if (DrawGame.tool.equals("none")){
					State("Add "+brush_selections.get(i)).Flash();
					DrawGame.tool = brush_selections.get(i);
				}
				else if (DrawGame.tool.equals(brush_selections.get(i))){
					DrawGame.tool = "none";
					State(0).Flash();
				}
				else {
					State("Add "+brush_selections.get(i)).Flash();
					DrawGame.tool = brush_selections.get(i);
				}
				break;
			}
		}
	}

	public Interface Flash(){
		framestate = SIZE;
		timer = 50;
		return this;
	}

	public Interface(){
		framestate     = 0;
		state          = schpeel;
		paletting      = false;
		minimapping    = true;
		topbar         = new Rectangle2D.Double(0,DrawGame.vsize,DrawGame.hsize,0);
		box            = new Rectangle2D.Double(0,DrawGame.vsize-SIZE,DrawGame.hsize,SIZE);
		palette        = new Rectangle2D.Double(0,0,PALETTESIZE,DrawGame.vsize);
		palette_border = new Rectangle2D.Double(PALETTESIZE,0,1,DrawGame.vsize);
		minimap_border = new Rectangle2D.Double(MINIMAPXOFFSET,DrawGame.vsize-MINIMAPXOFFSET-2-MINIMAPYSIZE,MINIMAPXSIZE+4,MINIMAPYSIZE+4);
		minimap        = new Rectangle2D.Double(MINIMAPXOFFSET+2,DrawGame.vsize-MINIMAPXOFFSET-MINIMAPYSIZE,MINIMAPXSIZE,MINIMAPYSIZE);
	}

      public void Draw(Graphics2D graphics) {
      	if (paletting){
      		graphics.setColor(palette_color);
			graphics.fill(palette);
			graphics.setColor(border_color);
			graphics.fill(palette_border);
			int ydraw = 10;
			brush_bbox = new ArrayList<Rectangle2D>();
			for (String c : brush_selections){
				brush_bbox.add(new Rectangle2D.Double(10,ydraw,Map.Tilesize(),Map.Tilesize()));
				if (c.equals("water") || c.equals("grass") || c.equals("cliff") || c.equals("sand") || c.equals("shovel")){
					graphics.drawImage((Image)GameAsset.images.get(c+"_icon"), 10, ydraw, null);
				}
				else {
					graphics.setColor((Color)Tile.Tilecolors().get(c));
	                graphics.fillOval(10,ydraw,Map.Tilesize() , Map.Tilesize());
	            }
                ydraw += Map.Tilesize()+10;
			}			

      	}
		Point mousecursor  = MouseInfo.getPointerInfo().getLocation();
		if ((palette.contains(mousecursor) || box.contains(mousecursor)) && framestate<SIZE)
		{
        framestate=framestate+4*SPEED;
        paletting = true;
        if (framestate>SIZE){
    		framestate = SIZE;
    		timer = 24;
    	}
        topbar = new Rectangle2D.Double(0,DrawGame.vsize-framestate,DrawGame.hsize,framestate);
        }
            else if (framestate>0 && !(palette.contains(mousecursor) || box.contains(mousecursor))){
            	timer--;
            	if (timer<0){
            		framestate=framestate-SPEED;
            		timer = 0;
            	}
            	if (framestate<0){
            		paletting = false;
            		framestate = 0;
            		if (junk){
            			state = schpeel; // reverts to standard message for junk message
            			junk = false;
            		}
            	}
            	topbar = new Rectangle2D.Double(0,DrawGame.vsize-framestate,DrawGame.hsize,framestate);
            }
            else {}
      	graphics.setColor(Color.BLACK);
      	graphics.fill(topbar);
      	if (framestate>5){
      		graphics.setColor(Color.WHITE);
	      	graphics.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	      	graphics.drawString(state, 25,DrawGame.vsize+20-framestate);
      	}
      	if (minimapping){
      		MINIMAPYSIZE = (200*DrawGame.Map().Size()[1])/(DrawGame.Map().Size()[0]);
			double topx            = 62.0;
			double topy            = DrawGame.vsize-60-MINIMAPYSIZE;
			double minimaptilesize = ((double) MINIMAPXSIZE)/((double) DrawGame.Map().Size()[0]);
			if (minimaptilesize<1){
				minimaptilesize = 1;
			}
      		graphics.setColor(border_color);
			graphics.fill(minimap_border);
			graphics.setColor(Color.black);
			graphics.fill(minimap);
			for (int x = 0;x<DrawGame.Map().Size()[0];x++){
                  for (int y=0; y<DrawGame.Map().Size()[1];y++){
                        graphics.setColor(DrawGame.Map().World()[x][y].Color());
                        graphics.fill(new Rectangle2D.Double(topx+x*minimaptilesize,topy+y*minimaptilesize,minimaptilesize,minimaptilesize));
                  }
            }
            graphics.setColor(palette_color);
            graphics.drawRect((int)(topx+(double)DrawGame.Map().Focus()[0]/(double)Map.Tilesize()), (int)(topy+(double)DrawGame.Map().Focus()[1]/(double)Map.Tilesize()), (int)(minimaptilesize*(double)DrawGame.Map().Focus()[2]/(double)Map.Tilesize()),(int)(minimaptilesize*(double)DrawGame.Map().Focus()[3]/(double)Map.Tilesize()));
            graphics.drawImage((Image)GameAsset.images.get("close"), MINIMAPXOFFSET+MINIMAPXSIZE-13, DrawGame.vsize-MINIMAPXOFFSET-MINIMAPYSIZE-15, null);
      	}
      	else{
      		graphics.setColor(Color.white);
            graphics.fillOval(0,DrawGame.vsize-34,34 , 34);
      		graphics.setColor(Color.black);
            graphics.fillOval(2,DrawGame.vsize-34+2,30 , 30);
      		graphics.drawImage((Image)GameAsset.images.get("minimap_open"), 2, DrawGame.vsize-32, null);
      	}
      }

}




