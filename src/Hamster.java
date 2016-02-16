import java.awt.Color;
import java.util.ArrayList;

import javalib.worldimages.DiskImage;
import javalib.worldimages.OverlayImagesXY;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

public class Hamster extends Actor {
    private String name;
    private String description;
    private Posn target;
    private House targetHouse;
    private boolean housed;
    private boolean goForward;
    private Color bodyColor;
    private Color earColor;
    private ArrayList<House> houses;
    private int step;
    private double scale;
    private boolean canMove;
    private boolean manthony;

    Hamster(double x, double y, double theta, ArrayList<House> houses) {
        super();
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.housed = false;
        this.goForward = true;
        this.bodyColor = Color.GRAY;
        this.earColor = Color.WHITE;
        this.houses = houses;
        this.step = 1;
        this.scale = .5;
        this.img = this.drawHampster(this.theta);
        this.targetHouse = this.houses.get(this.randomRange(0,
                this.houses.size() - 1));
        this.target = this.targetHouse.getEntrance();
        this.canMove = true;
        this.manthony = false;
        this.generateEboard();
    }

    Hamster(double x, double y, double theta, ArrayList<House> houses,
            boolean manthony) {
        this(x, y, theta, houses);
        this.manthony = manthony;
        this.name = "MANthony";
        this.description = "manly";
        this.bodyColor = Color.RED;
        this.earColor = Color.PINK;
        this.scale = 1;
    }

    void act() {
        this.turn();
        if (this.distanceToTarget() > Utils.HAMPSTER_SPD * 1.5 && this.canMove)
        {
            this.move();
        }
        else {
            this.chooseTargetHouse();
        }
        this.img = this.drawHampster(this.theta);
    }

    void chooseTargetHouse() {
        this.goForward = true;
        if (this.step <= 4) {
            if (this.step % 2 == 0) {
                this.target = this.randomLoc();
            }
            else {
                this.targetHouse = this.houses.get(this.randomRange(0,
                        this.houses.size() - 1));
                this.target = this.targetHouse.getEntrance();
            }
        }
        else if (this.step <= 10) {
            this.targetHouse = this.houses.get(this.randomRange(0,
                    this.houses.size() - 1));
            this.target = this.targetHouse.getEntrance();
            if (Math.random() < step / 10.0) {
                this.step = 10;
            }
        }
        else if (this.step == 11) {
            double x = this.targetHouse.x - Math.sin(this.targetHouse.theta)
                    * Utils.STOP_DIST;
            double y = this.targetHouse.y + Math.cos(this.targetHouse.theta)
                    * Utils.STOP_DIST;
            this.target = new Posn((int) x, (int) y);
        }
        else if (this.step < 30) {
            if (!this.canMove && step == 29) {
                this.canMove = true;
                this.goForward = false;
                this.target = this.targetHouse.getEntrance();
                this.step = 9;
            }
            else if (Math.random() < .5) {
                this.canMove = false;
            }
            else if (this.canMove) {
                this.step = 29;
            }
        }
        else if (this.step == 30) {
            this.target = new Posn((int) (this.targetHouse.x),
                    (int) (this.targetHouse.y));
        }
        else {
            this.housed = true;
            this.targetHouse.entered(this);
        }
        this.step++;
    }

    void turn() {
        this.theta = this.smartMod(this.theta, 2 * Math.PI);
        double dx = this.target.x - this.x;
        double dy = this.y - this.target.y;
        double thetaToTarget = this.smartMod(
                (dy > 0 ? 0 : Math.PI) + Math.atan(dx / dy), 2 * Math.PI);
        if (!this.goForward) {
            thetaToTarget = this.smartMod(thetaToTarget + Math.PI, 2 * Math.PI);
        }
        double actualDtheta = this.smartMod(thetaToTarget - this.theta,
                2 * Math.PI);
        double dtheta = actualDtheta
                - ((actualDtheta > Math.PI) ? 2 * Math.PI : 0);
        if (Math.abs(dtheta) > Utils.TURN_SPD) {
            this.theta = this.smartMod(this.theta + (dtheta < 0 ? -1 : 1)
                    * Utils.TURN_SPD, 2 * Math.PI);
        }
    }

    void move() {
        if (this.goForward) {
            this.x += Math.sin(this.theta) * Utils.HAMPSTER_SPD;
            this.y -= Math.cos(this.theta) * Utils.HAMPSTER_SPD;
        }
        else {
            this.x -= Math.sin(this.theta) * Utils.HAMPSTER_SPD;
            this.y += Math.cos(this.theta) * Utils.HAMPSTER_SPD;
        }
    }

    private WorldImage drawHampster(double theta) {
        WorldImage img = new DiskImage(new Posn((int) x, (int) y),
                (int) (Utils.BODY_RADIUS * this.scale), this.bodyColor);
        return this.drawNameOn(this.drawHeadOn(theta,
                this.drawTailOn(theta, img)));
    }

    private WorldImage drawHeadOn(double theta, WorldImage img) {
        double x = this.x + (Math.sin(theta) * Utils.BODY_RADIUS * this.scale);
        double y = this.y - (Math.cos(theta) * Utils.BODY_RADIUS * this.scale);
        WorldImage head = new OverlayImagesXY(img, new DiskImage(new Posn(
                (int) x, (int) y), (int) (Utils.HEAD_RADIUS * this.scale),
                this.bodyColor), 0, 0);
        return this.drawEarsOn(theta, x, y, this.drawEyesOn(theta, x, y,
                this.drawNoseOn(theta, x, y, head)));
    }

    private WorldImage drawEarsOn(double theta, double x0, double y0,
            WorldImage img) {
        double theta1 = (theta + Utils.EAR_THETA) % (2 * Math.PI);
        double x1 = x0 + (Math.sin(theta1) * Utils.HEAD_RADIUS * this.scale);
        double y1 = y0 - (Math.cos(theta1) * Utils.HEAD_RADIUS * this.scale);
        double theta2 = (theta - Utils.EAR_THETA) % (2 * Math.PI);
        double x2 = x0 + (Math.sin(theta2) * Utils.HEAD_RADIUS * this.scale);
        double y2 = y0 - (Math.cos(theta2) * Utils.HEAD_RADIUS * this.scale);
        return new OverlayImagesXY(img, new OverlayImagesXY(new DiskImage(
                new Posn((int) x1, (int) y1),
                (int) (Utils.EAR_RADIUS * this.scale), this.earColor),
                new DiskImage(new Posn((int) x2, (int) y2),
                        (int) (Utils.EAR_RADIUS * this.scale), this.earColor),
                0, 0), 0, 0);
    }

    private WorldImage drawEyesOn(double theta, double x0, double y0,
            WorldImage img) {
        double theta1 = (theta + Utils.EYE_THETA) % (2 * Math.PI);
        double x1 = x0
                + ((Math.sin(theta1) * Utils.HEAD_RADIUS * this.scale) / Utils.EYE_DIST);
        double y1 = y0
                - ((Math.cos(theta1) * Utils.HEAD_RADIUS * this.scale) / Utils.EYE_DIST);
        double theta2 = (theta - Utils.EYE_THETA) % (2 * Math.PI);
        double x2 = x0
                + ((Math.sin(theta2) * Utils.HEAD_RADIUS * this.scale) / Utils.EYE_DIST);
        double y2 = y0
                - ((Math.cos(theta2) * Utils.HEAD_RADIUS * this.scale) / Utils.EYE_DIST);
        return new OverlayImagesXY(img, new OverlayImagesXY(new DiskImage(
                new Posn((int) x1, (int) y1),
                (int) (Utils.EYE_RADIUS * this.scale), Color.BLACK),
                new DiskImage(new Posn((int) x2, (int) y2),
                        (int) (Utils.EYE_RADIUS * this.scale), Color.BLACK), 0,
                0), 0, 0);
    }

    private WorldImage drawNoseOn(double theta, double x0, double y0,
            WorldImage img) {
        double x = x0 + (Math.sin(theta) * Utils.HEAD_RADIUS * this.scale);
        double y = y0 - (Math.cos(theta) * Utils.HEAD_RADIUS * this.scale);
        return new OverlayImagesXY(img, new DiskImage(
                new Posn((int) x, (int) y),
                (int) (Utils.NOSE_RADIUS * this.scale), Color.PINK), 0, 0);
    }

    private WorldImage drawTailOn(double theta, WorldImage img) {
        double x = this.x - (Math.sin(theta) * Utils.BODY_RADIUS * this.scale);
        double y = this.y + (Math.cos(theta) * Utils.BODY_RADIUS * this.scale);
        return new OverlayImagesXY(img, new DiskImage(
                new Posn((int) x, (int) y),
                (int) (Utils.TAIL_RADIUS * this.scale), this.bodyColor), 0, 0);
    }

    private WorldImage drawNameOn(WorldImage img) {
        if (manthony) {
            return new OverlayImagesXY(new OverlayImagesXY(img,
                    new TextImage(new Posn((int) x, (int) y - 140), this.name,
                            40, Color.WHITE), 0, 0), new TextImage(new Posn(
                    (int) x, (int) y - 110), "the " + this.description
                    + " hamster king", 25, Color.WHITE), 0, 0);
        }
        return new OverlayImagesXY(new OverlayImagesXY(img, new TextImage(
                new Posn((int) x, (int) y - 80), this.name, 30, Color.WHITE),
                0, 0), new TextImage(new Posn((int) x, (int) y - 55), "the "
                + this.description + " hamster", 20, Color.WHITE), 0, 0);
    }

    private double distanceToTarget() {
        return Math.sqrt((this.x - this.target.x) * (this.x - this.target.x)
                + (this.y - this.target.y) * (this.y - this.target.y));
    }

    private int randomRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private Posn randomLoc() {
        return new Posn(this.randomRange(250, 750), this.randomRange(250, 750));
    }

    private double smartMod(double num1, double num2) {
        double result = num1 % num2;
        return num1 < 0 ? result + num2 : result;
    }

    private void generateEboard() {
        int rand = this.randomRange(1, 11);
        switch (rand) {
        case 1:
            this.name = "Anthony";
            this.description = "your mom";
            break;
        case 2:
            this.name = "Dinh";
            this.description = "cute";
            break;
        case 3:
        	this.name = "Michael";
            this.description = "blonde";
            this.bodyColor = Color.ORANGE;
            this.earColor = Color.YELLOW;
            break;
        case 4:
        	this.name = "Kimberly";
            this.description = "tiniest";
            this.scale = .3;
            break;
        case 5:
            this.name = "Matt";
            this.description = "Mr. VSA";
            break;
        case 6:
            this.name = "Ari";
            this.description = "caw caw motherfucking";
            break;
        case 7:
            this.name = "Kristine";
            this.description = "___";
            break;
        case 8:
            this.name = "Jackie";
            this.description = "___";
            break;
        case 9:
            this.name = "Annvie";
            this.description = "___";
            this.scale = .4;
            break;
        case 10:
            this.name = "Crystal";
            this.description = "Rheely freshy";
            break;
        default:
            this.name = "Hilary";
            this.description = "___";
            break;
        }
    }

    public boolean isHoused() {
        return this.housed;
    }
}
