// import java.awt.Dimension;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Image;

// import javax.swing.ImageIcon;
// import javax.swing.JFrame;
// import javax.swing.JPanel;

// public class DisplayImage extends JPanel {

//    Image castle;
//    Dimension size;

//     public DisplayImage() {
//         size = new Dimension();
//         castle = new ImageIcon(this.getClass().getResource("redrock.png")).getImage();
//         size.width = castle.getWidth(null);
//         size.height = castle.getHeight(null);
//         setPreferredSize(size);
//     }

//     public void paint(Graphics g) {

//         Graphics2D g2d = (Graphics2D) g;

//         g2d.drawImage(castle, 0, 0, null);
//     }

//     public static void main(String[] args) {

//         JFrame frame = new JFrame("Red Rock");
//         frame.add(new DisplayImage());
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.pack();
//         frame.setLocationRelativeTo(null);
//         frame.setVisible(true);
//     }
// }
