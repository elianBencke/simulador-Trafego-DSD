package br.udesc.simulador.trafego.model;

import java.awt.Toolkit;

public class GlobalConstants {

    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;
    public static final int CROSSING_UP = 5;
    public static final int CROSSING_RIGHT = 6;
    public static final int CROSSING_DOWN = 7;
    public static final int CROSSING_LEFT = 8;
    public static final int CROSSING_UP_RIGHT = 9;
    public static final int CROSSING_UP_LEFT = 10;
    public static final int CROSSING_RIGHT_DOWN = 11;
    public static final int CROSSING_DOWN_LEFT = 12;
    public static final int GRID_COLUMN_WIDTH = 35;
    public static final int GRID_HEIGHT = 35;
    public static final int MONITOR = 1;
    public static final int SEMAPHOR = 2;
    public static final int SCREEN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.8);
    public static final int SCREEN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.8);
    public static final int SQUARE_SIDE = (int) (SCREEN_HEIGHT*0.053);
    public static final int BUTTON_MARGIN = (int) (SCREEN_WIDTH * 0.00916);
}