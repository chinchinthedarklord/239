import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Circle extends Ellipse2D {
    private Point p;
    private double r;

    public Circle(double x, double y, double r){
        p = new Point(x, y);
        this.r=r;
    }
    @Override
    public double getX() {
        return p.getX();
    }

    @Override
    public double getY() {
        return p.getY();
    }

    @Override
    public double getWidth() {
        return r;
    }

    @Override
    public double getHeight() {
        return r;
    }

    @Override
    public boolean isEmpty() { return false; }
    public double getR() {
        return r;
    }

    @Override
    public void setFrame(double x, double y, double r1, double r2) {
        p.setLocation(x, y);
        this.r = r1;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }
}
