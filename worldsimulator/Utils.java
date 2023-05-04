package pl.edu.pg.eti.po.project.worldsimulator;

import java.awt.geom.Point2D;
import java.util.Random;

public class Utils {
    public static boolean randomChoiceProbability(double probability) {
        Random generator = new Random();
        return generator.nextInt(100) <= probability*100;
    }
    public static Point2D randomPosition(int width, int height) {
        Random generator = new Random();
        int x = generator.nextInt(width);
        int y = generator.nextInt(height);
        return new Point2D.Double(x,y);
    }

    public static class Const {
        public Const() {};
        public static final int NEIGHBOUR_SHIFT = 1;
        public static final int CORD_ORIGIN = 0;
        public static final int ERROR = -1;

        public static final double PROBABILITY_SOW = 1.0/2.0;
        public static final int TRY_DANDELION_TO_SOW_NUMBER = 3;
        public static final int GUARANA_INCREASE_STRENGTH_WHO_EAT = 3;

        public static final int PLANT_STRENGTH = 0;
        public static final int NIGHTSHADE_STRENGTH = 99;
        public static final int SOSNOWSKY_STRENGTH = 10;

        public static final int HUMAN_STRENGTH = 5;
        public static final int WOLF_STRENGTH = 9;
        public static final int LAMP_STRENGTH = 4;
        public static final int FOX_STRENGTH = 3;
        public static final int TURTLE_STRENGTH = 2;
        public static final int ANTELOPE_STRENGTH = 4;

        public static final int PLANT_INITIATIVE = 0;

        public static final int HUMAN_INITIATIVE = 4;
        public static final int WOLF_INITIATIVE = 5;
        public static final int LAMP_INITIATIVE = 4;
        public static final int FOX_INITIATIVE = 7;
        public static final int TURTLE_INITIATIVE = 1;
        public static final int ANTELOPE_INITIATIVE = 4;

        public static final int HUMAN_STEP = 1;
        public static final int WOLF_STEP = 1;
        public static final int LAMP_STEP = 1;
        public static final int FOX_STEP = 1;
        public static final int TURTLE_STEP = 1;
        public static final int ANTELOPE_STEP = 2;

        public static final double PROBABILITY_TURTLE_MOVE = 3.0/4.0;
        public static final int STRENGTH_REPEL_ATTACK_TURTLE = 5;
        public static final double PROBABILITY_ANTELOPE_ESCAPE = 1.0/2.0;

        public static final int ALZUR_SHIELD_DURATION = 5;
        public static final int ALZUR_SHIELD_DURATION_SET = ALZUR_SHIELD_DURATION+1;
        public static final int ALZUR_SHIELD_LOCK = 5;
    }
}
