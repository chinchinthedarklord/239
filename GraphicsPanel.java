import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
public class GraphicsPanel extends JPanel implements Runnable{
    //массив для праметров объектов
    private double [][]arr= new double [2][7];
    //конструктор панели, которая создает объекты
    public GraphicsPanel(){
        arr[0][0]=1;
        arr[1][0]=15;
        arr[0][1]=150;
        arr[1][1]=300;
        arr[0][2]=150;
        arr[1][2]=150;
        arr[0][4]=-30;
        arr[1][4]=2;
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
    public double mod(int i1, int i2){
        return Math.sqrt((arr[i1][1]-arr[i2][1])*(arr[i1][1]-arr[i2][1])+(arr[i1][2]-arr[i2][2])*(arr[i1][2]-arr[i2][2]));
    }
    //метод, рассчитывающий силы притяжения объектов
    public double[][] Force(int i1, int i2){
        double [][]test = new double[2][2];
        double g = 9000;
        arr[i1][5]=g*arr[i2][0]*(arr[i2][1]-arr[i1][1])/Math.pow(mod(i1, i2), 3);
        arr[i1][6]=g*arr[i2][0]*(arr[i2][2]-arr[i1][2])/Math.pow(mod(i1, i2), 3);
        arr[i2][5]=g*arr[i1][0]*(arr[i1][1]-arr[i2][1])/Math.pow(mod(i1, i2), 3);
        arr[i2][6]=g*arr[i1][0]*(arr[i1][2]-arr[i2][2])/Math.pow(mod(i1, i2), 3);
        test[0][0]=arr[i1][5];
        test[0][1]=arr[i1][6];
        test[1][0]=arr[i2][5];
        test[1][1]=arr[i2][6];
        return test;
    }
    //метод, двигающий объекты
    public void move( double dt){
        double p1, q1, p2, q2, p3, q3, p4, q4;
        for(int o=0; o<2; o++) {
            for (int i = 0; i < 2; i++) {
                p1 = dt * Force(0, 1)[o][i];
                q1 = dt * arr[o][3 + i];
                arr[o][1+i]+=q1;
                arr[o][3+i]+=p1;


            }
        }
        System.out.println(arr[0][1]);
        System.out.println(arr[0][3]);
        System.out.println(arr[0][5]);

    }
    //здесь запускается программа
    @Override
    public void run(){
        while(true){
            try {
                move(0.15);
                super.repaint();
                Thread.sleep(100);
                //System.out.println(arr[0][5]);
            } catch (InterruptedException e) {
                //пишем catch как крутые программисты
                e.printStackTrace();
            }
        }
    }
}
