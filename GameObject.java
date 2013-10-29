// Jonathan Raiman 010207881 jonathanraiman@gmail.com

import java.awt.Graphics2D;

public interface GameObject {

    public abstract void Translate (int dx, int dy);
    public abstract void Move (int dx, int dy);
    public abstract void Move (Tile destination);
    public abstract void Draw (Graphics2D graphics);
    public abstract int[] Position();

}