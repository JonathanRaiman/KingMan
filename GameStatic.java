// Jonathan Raiman 010207881 jonathanraiman@gmail.com

import java.awt.Graphics2D;

public interface GameStatic {

    public abstract void Draw (Graphics2D graphics);
    public abstract int[] Position();
    public abstract int Passable();

}