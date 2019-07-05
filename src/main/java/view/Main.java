package view;

import view.gui.MainGuiView;

import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        if (args[0].equals("gui")) {
            MainGuiView.setUIManager();
            new MainGuiView(args[1]).main();
        }else if (args[0].equals("cli")) {
            //TODO start cli view
        }
    }
}
