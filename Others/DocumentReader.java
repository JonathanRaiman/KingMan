import java.io.*;
import java.util.*;
import java.util.ArrayList;

/*
┌─────────┐
│ VISUALS │
└─────────┘ 
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/*
┌───────────────┐
│ MAIN FUNCTION │
└───────────────┘ 
*/
public class DocumentReader extends Frame {

    private static BufferedReader input;
    private static String line;
    private static Color bckgrnd = new Color(145, 156, 150);
    private static Color yellow = new Color(255, 255, 196);

    Stroke drawingStroke = new BasicStroke(2);
    QuadCurve2D curve = new QuadCurve2D.Double(30,50,20,200,100,100);

    public void paint(Graphics g) {
    Graphics2D ga = (Graphics2D)g;
    ga.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    ga.setStroke(drawingStroke);
    ga.setPaint(Color.green);
    ga.draw(curve);
      
    }
    

    // Constructor
    public static void displaydocument(String fileName, int displaysize) throws IOException {
        input = new BufferedReader(new FileReader(fileName));
        String text = "";
        line = readALine();
	    while (hasNext()){
            text += "\n"+line;
            line = readALine();
        }
        String[] words = text.split("\\W++");
        ArrayList<Word> smartwords = new ArrayList<Word>();
        int position = 0;
        for (String i : words){
            smartwords.add(new Word(i,position));
            position++;
        }
        for (int j=0;j<(Math.min(displaysize+1,smartwords.size())); j++){
            System.out.print(smartwords.get(j).Word()+" ");
            if (smartwords.get(j).Position()%20==0){
                System.out.println();
            }
        }
    }
    

    public static String readALine () {
        String ln = "";
        try {
            ln = input.readLine();
        }
        catch (IOException e) {
            ;
        }
        return ln;
    }


    public static boolean hasNext() {
        return line != null;
    }

/*
┌─────────────────────────────┐
│ NAME THE DOCUMENT TO TAGIFY │
└─────────────────────────────┘ 
*/    public static void main (String[] args) throws IOException  {
        displaydocument(args[0], Integer.parseInt(args[1]));

        
        Frame frame = new DocumentReader();

        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                System.exit(0);
            }
        });
        frame.setSize(600, 600);
        frame.setVisible(true);
        
        frame.setTitle (args[0]+" - Document Analysis");

    }

}
