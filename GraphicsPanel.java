import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
public class GraphicsPanel extends JPanel implements Runnable{
    //массив для праметров объектов
    private double [][]arr= new double [2][5];
    //конструктор панели, которая создает объекты
    public GraphicsPanel(){
        arr[0][0]=1;
        arr[1][0]=100;
        arr[0][1]=250;
        arr[1][1]=500;
        arr[0][2]=500;
        arr[1][2]=500;
        arr[0][4]=50;
        arr[1][4]=-0.5;
        //создание секундомера
        (new Thread(this)).start();
    }
    //метод рисовалки
    @Override
    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Paint paint = g2.getPaint();
        //эта штука позволяет их двигать, то есть обновлять экран
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        //рисовалка всех объектов
        for(int i=0; i<arr.length; i++){
            g2.draw(new Ellipse2D.Double(arr[i][1], arr[i][2], 10, 10));
        }
    }
    //функция расстояния между двумя объектами
    public double mod(double x1, double x2, double y1, double y2){
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
    //метод, рассчитывающий силы притяжения объектов
    public double[][] Force(double m1, double m2, double x1, double x2, double y1, double y2 ){
        double [][]test = new double[2][2];
        double g = 9000;
        test[0][0]=g*m2*(x2-x1)/Math.pow(mod(x1, x2, y1, y2), 3);
        test[0][1]=g*m2*(y2-y1)/Math.pow(mod(x1, x2, y1, y2), 3);
        test[1][0]=g*m1*(x1-x2)/Math.pow(mod(x1, x2, y1, y2), 3);
        test[1][1]=g*m1*(y1-y2)/Math.pow(mod(x1, x2, y1, y2), 3);
        return test;
    }
    //метод, двигающий объекты
    public void move( double dt){
        double p1x, q1x, p2x, q2x, p3x, q3x, p4x, q4x;
        double p1y, q1y, p2y, q2y, p3y, q3y, p4y, q4y;
        for(int o=0; o<2; o++) {
            p1x = dt * Force(arr[0][0], arr[1][0], arr[0][1], arr[1][1], arr[0][2], arr[1][2])[o][0];
            q1x = dt * arr[o][3];
            p1y = dt * Force(arr[0][0], arr[1][0], arr[0][1], arr[1][1], arr[0][2], arr[1][2])[o][1];
            q1y = dt * arr[o][4];

            p2x = dt * Force(arr[0][0], arr[1][0], arr[0][1]+q1x/2, arr[1][1], arr[0][2]+q1y/2, arr[1][2])[o][0];
            q2x = dt * (arr[o][3]+p1x/2);
            p2y = dt * Force(arr[0][0], arr[1][0], arr[0][1]+q1x/2, arr[1][1], arr[0][2]+q1y/2, arr[1][2])[o][1];
            q2y = dt * (arr[o][4]+p1y/2);

            p3x = dt * Force(arr[0][0], arr[1][0], arr[0][1]+q2x/2, arr[1][1], arr[0][2]+q2y/2, arr[1][2])[o][0];
            q3x = dt * (arr[o][3]+p2x/2);
            p3y = dt * Force(arr[0][0], arr[1][0], arr[0][1]+q2x/2, arr[1][1], arr[0][2]+q2y/2, arr[1][2])[o][1];
            q3y = dt * (arr[o][4]+p2y/2);

            p4x = dt * Force(arr[0][0], arr[1][0], arr[0][1]+q3x, arr[1][1], arr[0][2]+q3y, arr[1][2])[o][0];
            q4x = dt * (arr[o][3]+p3x);
            p4y = dt * Force(arr[0][0], arr[1][0], arr[0][1]+q3x, arr[1][1], arr[0][2]+q3y, arr[1][2])[o][1];
            q4y = dt * (arr[o][4]+p3y);

            arr[o][3]+=(p1x+2*p2x+2*p3x+p4x)/6;
            arr[o][1]+=(q1x+2*q2x+2*q3x+q4x)/6;
            arr[o][4]+=(p1y+2*p2y+2*p3y+p4y)/6;
            arr[o][2]+=(q1y+2*q2y+2*q3y+q4y)/6;
        }

    }
    //здесь запускается программа
    @Override
    public void run(){
        while(true){
            try {
                move(0.15);
                super.repaint();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //пишем catch как крутые программисты
                e.printStackTrace();
            }
        }
    }
}
