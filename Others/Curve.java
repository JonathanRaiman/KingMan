// Curve.java -- demonstrate 2D curve drawing possibilities

import java.awt.Frame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Curve extends Canvas {
   private static Color bckgrnd = new Color(145, 156, 150);
   private static Color yellow = new Color(255, 255, 196);
   private static Frame f = new Frame ();

   public static void main (String[] args) {
      f.setTitle ("Curves");
      f.setBackground (bckgrnd);
      f.setSize (600, 600);
      class WindowClosingListener extends WindowAdapter {
   public void windowClosing (WindowEvent evt) { close(); }
      }
      f.addWindowListener (new WindowClosingListener ());
      f.add (new Curve());
      f.setVisible(true);
   }
   
   private static void close () {
      f.setVisible (false);
      f.dispose();
      System.exit(0);
   }

   private static final Point2D.Double[] cc = {
      new Point2D.Double (10+30, 17+400),
      new Point2D.Double (80+30, 11+400),
      new Point2D.Double (262+30, 33+400),
      new Point2D.Double (400+30, 15+400)
   };

   private static final Point2D.Double[] longo = {
      new Point2D.Double (50, 50),
      new Point2D.Double (350, 50),
      new Point2D.Double (500, 200),
      new Point2D.Double (500, 500)
   };

   private static final Point2D.Double[] qc = {
      new Point2D.Double (10+50, 247),
      new Point2D.Double (123+50, 279),
      new Point2D.Double (279+50, 245)
   };

   private static RoundRectangle2D circle (Point2D p,double w,double h) {
      final double x=p.getX()-w/2,y=p.getY()-h/2;
      return new RoundRectangle2D.Double(x,y,w,h,w,h);
   }

  private static Rectangle2D square (Point2D p,double w,double h) {
      final double x=p.getX()-w/2,y=p.getY()-h/2;
      return new Rectangle2D.Double(x,y,w,h);
   }

   public void paint (Graphics g) {
      final Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      final BasicStroke s = new BasicStroke (2);
      final BasicStroke longos = new BasicStroke (2);
      // Graphics g = new Graphics();
      // g.drawRoundRect(200, 200, 80, 80, 80, 80);
      // g.setColor(new Color(255, 255, 196));
      // // Paint four control points
      // System.out.println(cc[0].getX()+"  "+cc[0].getY());
      // g2.drawRoundRect(cc[0].getX(),cc[0].getY(),5,5,5,5);


      g2.setPaint (yellow);
      g2.fill (circle(cc[0],10,10));
      g2.fill (circle(cc[3],10,10));


      g2.setPaint (Color.red);
      g2.fill (square(cc[1],2,2));
      g2.fill (square(cc[2],2,2));
      g2.fill (square(longo[1],2,2));
      g2.fill (square(longo[2],2,2));
      

      g2.setPaint (yellow);
      g2.setStroke (s);

      
      final CubicCurve2D c = new CubicCurve2D.Double ();
      c.setCurve (cc,0);
      g2.draw (c); 

      g2.fill(circle(longo[0],10,10));
      g2.fill(circle(longo[3],10,10));


      final CubicCurve2D f = new CubicCurve2D.Double (); // create a curve based on longo points.
      f.setCurve (longo,0);
      g2.draw (f);


      



      // Connect three control points with lines
      g2.setPaint (Color.gray);
      g2.setStroke (new BasicStroke (1));
      g2.draw (new Line2D.Double (qc[0], qc[1]));
      g2.draw (new Line2D.Double (qc[1], qc[2]));
      g2.setPaint (new Color(255, 255, 196));
      g2.fill (circle(qc[0],10,10));
      g2.fill (circle(qc[2],10,10));
      g2.setPaint (Color.red);
      g2.fill (circle(qc[1],2,2));

      g2.setPaint (yellow);
      g2.setStroke (s);
      final QuadCurve2D q = new QuadCurve2D.Double ();
      q.setCurve (qc,0);
      g2.draw (q);

   }

}