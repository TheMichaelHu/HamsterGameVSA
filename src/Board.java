import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.World;
import javalib.worldimages.OverlayImagesXY;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

public class Board extends World {
    ArrayList<House> houses;
    ArrayList<Hamster> hamsters;
    boolean started;

    Board() {
        super();
        this.houses = new ArrayList<House>();
        this.populateHouseList();
        this.hamsters = new ArrayList<Hamster>();
        this.started = false;
    }

    private void populateHouseList() {
        for (int i = 0; i < Utils.NUM_HOUSES; i++) {
            double theta = (2*Math.PI / Utils.NUM_HOUSES) * i;
            double x = Utils.BOARD_RADIUS
                    + (Math.sin(theta) * (Utils.BOARD_RADIUS - 50));
            double y = Utils.BOARD_RADIUS
                    - (Math.cos(theta) * (Utils.BOARD_RADIUS - 50));
            this.houses.add(new House(x, y, theta, i+1));
        }
    }
    
    private void populateHouseList(int num) {
        if(num < 1) {
            return;
        }
        this.houses.clear();
        for (int i = 0; i < num; i++) {
            double theta = (2*Math.PI / num) * i;
            double x = Utils.BOARD_RADIUS
                    + (Math.sin(theta) * (Utils.BOARD_RADIUS - 50));
            double y = Utils.BOARD_RADIUS
                    - (Math.cos(theta) * (Utils.BOARD_RADIUS - 50));
            this.houses.add(new House(x, y, theta, i+1));
        }
    }

    public void onTick() {
        for (int i = 0; i < this.hamsters.size(); i++) {
            Hamster hamster = this.hamsters.get(i);
            if(hamster.isHoused()) {
                this.hamsters.remove(i);
            }
            hamster.act();
        }
        for (House house : this.houses) {
            house.act();
        }
    }

    public WorldImage makeImage() {
        return this.drawHousesOn(this.drawHamstersOn(this.drawTextOn(Utils.SCENE)));

    }
    
    private WorldImage drawTextOn(WorldImage img) {
        return new OverlayImagesXY(new OverlayImagesXY(new OverlayImagesXY(img, new TextImage(new Posn(Utils.BOARD_RADIUS, Utils.BOARD_RADIUS-100),
                "NU VSA Presents:", 30, Color.DARK_GRAY), 0, 0), new TextImage(new Posn(Utils.BOARD_RADIUS, Utils.BOARD_RADIUS),
                        "Saigon", 150, Color.DARK_GRAY), 0, 0), new TextImage(new Posn(Utils.BOARD_RADIUS, Utils.BOARD_RADIUS+120),
                                "by Night", 70, Color.DARK_GRAY), 0 ,0 );
    }

    private WorldImage drawHamstersOn(WorldImage img) {
        for (Hamster hamster : this.hamsters) {
            img = hamster.drawOn(img);
        }
        return img;
    }
    
    private WorldImage drawHousesOn(WorldImage img) {
        for (House house : this.houses) {
            img = house.drawOn(img);
        }
        return img;
    }

    public void onKeyEvent(String key) {
        if (key.equals(" ")) {
            this.hamsters.add(new Hamster(500, 500, 0, houses));
        } else if (key.equals("r")) {
            this.resetHouses();
        } else if (key.equals("k")) {
            this.hamsters.clear();
        } else if (key.equals("m")) {
            this.hamsters.add(new Hamster(500, 500, 0, houses, true));
        } else if (key.equals("[")) {
            this.populateHouseList(this.houses.size()-1);
        } else if (key.equals("]")) {
            this.populateHouseList(this.houses.size()+1);
        }
    }
    
    private void resetHouses() {
        for(House house: this.houses) {
            house.reset();
        }
    }
    
    public static void main(String[] args) {
        new Board().bigBang(Utils.BOARD_RADIUS*2, Utils.BOARD_RADIUS*2, .03);
    }
}
