import java.awt.*;
import javax.swing.*;
public class GraphicsPanel extends JPanel implements Runnable{

    private static final long serialVersionUID = 679188895339996746L;

    private Circle circle1;
    private Circle circle2;

    public GraphicsPanel(){
        Circle circle1 = new Circle(100, 100, 10);
        Circle circle2 = new Circle(100, 200, 10);

        (new Thread(this)).start();
    }
    public void drawC(Graphics g, int x, int y, int radius){
        g.drawOval(x-radius, y-radius, 2*radius, 2*radius);
    }
    @Override
    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Paint paint = g2.getPaint();
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

        drawC(g, (int)circle1.getX(),(int)circle1.getY(),(int)circle1.getR());
        drawC(g, (int)circle2.getX(),(int)circle2.getY(),(int)circle2.getR());
    }
    @Override
    public void run(){
        while(true){
            try {
                circle1.move();
                circle2.move();
                super.repaint();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
