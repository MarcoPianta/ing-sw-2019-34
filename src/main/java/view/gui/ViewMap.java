package view.gui;

public abstract class ViewMap {
    private static final String[] ids = {"0,0", "0,1", "0,2", "1,0", "1,1", "1,2", "1,3", "2,1", "2,2", "2,3"};

    private static final int[][] xCoord = {{490, 905, 1335, 1770},{490, 905, 1335, 1770}, {490, 905, 1335, 1770}};
    private static final int[][] yCoord = {{470, 470, 470, 470} ,{900 ,900 ,900 ,900},  {1350, 1350, 1350, 1350}};

    public static int getXCoordinates(String id){
        return xCoord[Character.getNumericValue(id.charAt(0))][Character.getNumericValue(id.charAt(2))];
    }

    public static int getYCoordinates(String id){
        return yCoord[Character.getNumericValue(id.charAt(0))][Character.getNumericValue(id.charAt(2))];
    }

    public static String[] getIds() {
        return ids;
    }
}
