import java.awt.Color;

import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Utils {
    
    // World
    final static int BOARD_RADIUS = 500;
    final static WorldImage SCENE = new RectangleImage(new Posn(BOARD_RADIUS, BOARD_RADIUS), BOARD_RADIUS*2, BOARD_RADIUS*2, Color.BLACK);
    
    // Actor
    final static String ACTOR_IMG = "actor.png";
    
    // Hamster
    final static int NUM_HAMPSTERS = 1;
    final static int HAMPSTER_SPD = 10;
    final static double TURN_SPD = .4;
    final static int BODY_RADIUS = 50;
    final static int HEAD_RADIUS = 40;
    final static int EAR_RADIUS = 20;
    final static double EAR_THETA = Math.PI/2;
    final static int EYE_RADIUS = 10;
    final static double EYE_THETA = Math.PI/4;
    final static int EYE_DIST = 2;
    final static int NOSE_RADIUS = 5;
    final static int TAIL_RADIUS = 10;
    
    // House
    final static int NUM_HOUSES = 10;
    final static int HOUSE_RADIUS = 50;
    final static int ENTRANCE_DIST = 130;
    final static int STOP_DIST = 40;
}
