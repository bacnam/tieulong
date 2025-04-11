package bsh.util;

import bsh.Interpreter;

import java.awt.*;

public class Util {
    static Window splashScreen;

    public static void startSplashScreen() {
        int width = 275, height = 148;
        Window win = new Window(new Frame());
        win.pack();
        BshCanvas can = new BshCanvas();
        can.setSize(width, height);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        win.setBounds(dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height);

        win.add("Center", can);
        Image img = tk.getImage(Interpreter.class.getResource("/bsh/util/lib/splash.gif"));

        MediaTracker mt = new MediaTracker(can);
        mt.addImage(img, 0);
        try {
            mt.waitForAll();
        } catch (Exception e) {
        }
        Graphics gr = can.getBufferedGraphics();
        gr.drawImage(img, 0, 0, can);
        win.setVisible(true);
        win.toFront();
        splashScreen = win;
    }

    public static void endSplashScreen() {
        if (splashScreen != null)
            splashScreen.dispose();
    }
}

