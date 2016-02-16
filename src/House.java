import java.awt.Color;

import javalib.worldimages.DiskImage;
import javalib.worldimages.OverlayImagesXY;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

public class House extends Actor {
    private int id;
    private Posn entrance;
    private Color color;
    private boolean filled;

    House(double x, double y, double theta, int id) {
        super();
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.id = id;
        this.color = Color.YELLOW;
        this.generateEntrance();
        this.img = this.drawHouse();
        this.filled = false;
    }

    void act() {
        this.img = this.drawHouse();
    }

    WorldImage drawHouse() {
        // double x0 = this.x - Math.sin(theta) * Utils.STOP_DIST;
        // double y0 = this.y + Math.cos(theta) * Utils.STOP_DIST;
        // this.img = new OverlayImagesXY(new OverlayImagesXY(new DiskImage(
        // new Posn((int) x, (int) y), Utils.HOUSE_RADIUS, Color.CYAN),
        // new DiskImage(this.entrance, 20, Color.RED), 0, 0),
        // new DiskImage(new Posn((int) x0, (int) y0), 20, Color.RED), 0,
        // 0);
        return this.drawTextOn(new DiskImage(new Posn((int) x, (int) y),
                Utils.HOUSE_RADIUS, this.color));
    }

    WorldImage drawTextOn(WorldImage img) {
        Color color = Color.DARK_GRAY;
        if(this.filled) {
            color = Color.WHITE;
        }
        return new OverlayImagesXY(img, new TextImage(new Posn((int) x
                - Utils.HEAD_RADIUS / 8, (int) y + Utils.HEAD_RADIUS / 4),
                this.id + "", 50, color), 0, 0);
    }

    void entered(Hamster hamster) {
        this.filled = true;
        this.color = Color.BLACK;
    }

    void generateEntrance() {
        double x = this.x - Math.sin(this.theta) * Utils.ENTRANCE_DIST;
        double y = this.y + Math.cos(this.theta) * Utils.ENTRANCE_DIST;
        this.entrance = new Posn((int) x, (int) y);
    }

    Posn getEntrance() {
        return this.entrance;
    }

    void reset() {
        this.color = Color.YELLOW;
        this.filled = false;
    }
}
