import javalib.worldimages.FromFileImage;
import javalib.worldimages.OverlayImagesXY;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

abstract public class Actor {
    double x; 
    double y;
    double theta;
    WorldImage img;

    Actor() {
        this.x = 0;
        this.y = 0;
        this.theta = 0;
        this.img = new FromFileImage(new Posn((int)x,(int)y), Utils.ACTOR_IMG);
    }
    
    void act(){}

    WorldImage drawOn(WorldImage img) {
        return new OverlayImagesXY(img, this.img, (int)(this.x), (int)(this.y));
    }

    void move() {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
} 