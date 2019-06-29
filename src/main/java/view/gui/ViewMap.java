package view.gui;

import java.util.Arrays;

public abstract class ViewMap {
    private static final String[] ids = {"0,0", "0,1", "0,2", "0,3", "1,0", "1,1", "1,2", "1,3", "2,0", "2,1", "2,2", "2,3"};

    private static final int[][] xCoord = {{490, 905, 1335, 1770},{490, 905, 1335, 1770}, {490, 905, 1335, 1770}};
    private static final int[][] yCoord = {{470, 470, 470, 470} ,{900 ,900 ,900 ,900},  {1350, 1350, 1350, 1350}};

    private static final int[][] xWeapon = {{1350, 1625, 1905},{10, 10, 10}, {2198, 2198, 2198, 2198}};
    private static final int[][] yWeapon = {{1,1,1},{702, 981, 1258}, {1096, 1373, 1652}};
    private static final int[] xWeaponIncrement = {240, 350, 350};
    private static final int[] yWeaponIncrement = {350, 240, 240};

    public static int getXCoordinates(String id){
        return xCoord[Character.getNumericValue(id.charAt(0))][Character.getNumericValue(id.charAt(2))];
    }

    public static int getYCoordinates(String id){
        return yCoord[Character.getNumericValue(id.charAt(0))][Character.getNumericValue(id.charAt(2))];
    }

    public static String[] getIds() {
        return ids;
    }

    public static int getSquareNumber(String id){
        return Arrays.asList(ids).indexOf(id);
    }

    public static int getxWeapon(String id, int i){
        return xWeapon[Character.getNumericValue(id.charAt(0))][i];
    }

    public static int getyWeapon(String id, int i){
        return yWeapon[Character.getNumericValue(id.charAt(0))][i];
    }

    public static int getxWeaponIncrement(String id){
        return xWeaponIncrement[Character.getNumericValue(id.charAt(0))];
    }

    public static int getyWeaponIncrement(String id){
        return yWeaponIncrement[Character.getNumericValue(id.charAt(0))];
    }
}
