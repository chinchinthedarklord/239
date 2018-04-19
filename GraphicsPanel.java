import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;
public class GraphicsPanel extends JPanel implements Runnable{
    //массив для праметров объектов
    private double [][]arr = new double [3][5];
    //массив, куда запихиваются координаты тела для отрисовки кривой
    private ArrayList <Double> curve1 = new ArrayList<>();
    private ArrayList <Double> curve2 = new ArrayList<>();
    private ArrayList <Double> curve3 = new ArrayList<>();
    private double g = arr.length-1;
    //конструктор панели
    public GraphicsPanel(){
        //Вот тут вводятся параметры. Можно написать ввод через консоль, но пока он не нужен.
        arr[0][0]=1;
        arr[1][0]=1;
        arr[2][0]=1;
        arr[0][1]=500+300*0.97000436;
        arr[0][2]=500-300*0.24308753;
        arr[1][1]=500+300*(-0.97000436);
        arr[1][2]=500+300*0.24308753;
        arr[2][1]=500;
        arr[2][2]=500;
        arr[0][3]=0.5*300*(0.93240737);
        arr[0][4]=0.5*300*(0.86473146);
        arr[1][3]=0.5*300*(0.93240737);
        arr[1][4]=0.5*300*(0.86473146);
        arr[2][3]=-300*(0.93240737);
        arr[2][4]=-300*(0.86473146);

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
            g2.draw(new Ellipse2D.Double(arr[i][1]-5, arr[i][2]-5, 10, 10));
        }
        //отрисовка траектории 1 тела, для примера
        for(int i=0; i<(curve1.size()-2)/2; i++){
            g2.draw(new Line2D.Double(curve1.get(2*i), curve1.get(2*i+1), curve1.get(2*(i+1)), curve1.get(2*(i+1)+1)));
        }
        for(int i=0; i<(curve2.size()-2)/2; i++){
            g2.draw(new Line2D.Double(curve2.get(2*i), curve2.get(2*i+1), curve2.get(2*(i+1)), curve2.get(2*(i+1)+1)));
        }
        for(int i=0; i<(curve3.size()-2)/2; i++){
            g2.draw(new Line2D.Double(curve3.get(2*i), curve3.get(2*i+1), curve3.get(2*(i+1)), curve3.get(2*(i+1)+1)));
        }
        //отрисовка вектора скорости 1 тела, для примера
        //g2.draw(new Line2D.Double(arr[0][1], arr[0][2], arr[0][1]+arr[0][3], arr[0][2]+arr[0][4]));
    }
    //функция расстояния между двумя объектами
    public double mod(double x1, double x2, double y1, double y2){
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
    //метод, рассчитывающий силы притяжения объектов
    public double[] Force( double m2, double x1, double x2, double y1, double y2 ){
        double []test = new double[2];
        //double g =9000;
        test[0]=g*m2*(x2-x1)/Math.pow(mod(x1, x2, y1, y2)/300, 3);
        test[1]=g*m2*(y2-y1)/Math.pow(mod(x1, x2, y1, y2)/300, 3);
        return test;
    }
    //метод, двигающий объекты
    public void move( double dt){
        double [][] px = new double[arr.length][5];
        double [][] py = new double[arr.length][5];
        double [][] qx = new double[arr.length][5];
        double [][] qy = new double[arr.length][5];
        // вот тут Рунге-Кутта
        for(int o=0; o<arr.length; o++) {
            for (int w=0; w<arr.length; w++) {
                if (w!=o){
                    px[o][1] += dt * Force( arr[w][0], arr[o][1], arr[w][1], arr[o][2], arr[w][2])[0];
                    qx[o][1] += dt * arr[o][3];
                    py[o][1] += dt * Force( arr[w][0], arr[o][1], arr[w][1], arr[o][2], arr[w][2])[1];
                    qy[o][1] += dt * arr[o][4];
                }
            }
        }
        for(int o=0; o<arr.length; o++) {
            for (int w=0; w<arr.length; w++) {
                if (w!=o) {
                    px[o][2] += dt * Force( arr[w][0], arr[o][1] + qx[o][1] / 2, arr[w][1] + qx[w][1] / 2, arr[o][2] + qy[o][1] / 2, arr[w][2] + qx[w][1] / 2)[0];
                    qx[o][2] += dt * (arr[o][3] + px[o][1] / 2);
                    py[o][2] += dt * Force( arr[w][0], arr[o][1] + qx[o][1] / 2, arr[w][1] + qx[w][1] / 2, arr[o][2] + qy[o][1] / 2, arr[w][2] + qx[w][1] / 2)[1];
                    qy[o][2] += dt * (arr[o][4] + py[o][1] / 2);
                }
            }
        }
        for(int o=0; o<arr.length; o++) {
            for (int w = 0; w < arr.length; w++) {
                if (w != o) {
                    px[o][3] += dt * Force( arr[w][0], arr[o][1] + qx[o][2] / 2, arr[w][1] + qx[w][2] / 2, arr[o][2] + qy[o][2] / 2, arr[w][2] + qx[w][2] / 2)[0];
                    qx[o][3] += dt * (arr[o][3] + px[o][2] / 2);
                    py[o][3] += dt * Force( arr[w][0], arr[o][1] + qx[o][2] / 2, arr[w][1] + qx[w][2] / 2, arr[o][2] + qy[o][2] / 2, arr[w][2] + qx[w][2] / 2)[1];
                    qy[o][3] += dt * (arr[o][4] + py[o][2] / 2);
                }
            }
        }
        for(int o=0; o<arr.length; o++) {
            for (int w=0; w<arr.length; w++) {
                if (w != o) {
                    px[o][4] += dt * Force( arr[w][0], arr[o][1] + qx[o][3], arr[w][1] + qx[w][3], arr[o][2] + qy[o][3], arr[w][2] + qy[w][3])[0];
                    qx[o][4] += dt * (arr[o][3] + px[o][3]);
                    py[o][4] += dt * Force( arr[w][0], arr[o][1] + qx[o][3], arr[w][1] + qx[w][3], arr[o][2] + qy[o][3], arr[w][2] + qy[w][3])[1];
                    qy[o][4] += dt * (arr[o][4] + py[o][3]);
                }
            }
        }
        for(int o=0; o<arr.length; o++) {
            arr[o][3] += (px[o][1] + 2 * px[o][2] + 2 * px[o][3] + px[o][4]) / 6;
            arr[o][1] += (qx[o][1] + 2 * qx[o][2] + 2 * qx[o][3] + qx[o][4]) / 6;
            arr[o][4] += (py[o][1] + 2 * py[o][2] + 2 * py[o][3] + py[o][4]) / 6;
            arr[o][2] += (qy[o][1] + 2 * qy[o][2] + 2 * qy[o][3] + qy[o][4]) / 6;
        }
        //новая точка кривой кладется в массив
        curve1.add(arr[0][1]);
        curve1.add(arr[0][2]);
        curve2.add(arr[1][1]);
        curve2.add(arr[1][2]);
        curve3.add(arr[2][1]);
        curve3.add(arr[2][2]);

        //Вот с этой штукой можно поиграться
        //С некоторого момента она может изменять скорость объекта
        /*if(curve4.size() == 500){
            arr[0][3]+=0;
            arr[0][4]+=0;
        }*/

    }
    //здесь запускается программа
    @Override
    public void run(){
        while(true){
            try {
                move(0.0003);
                super.repaint();
                Thread.sleep(1);
            } catch (InterruptedException e) {
                //пишем catch как крутые программисты
                e.printStackTrace();
            }
        }
    }
}
