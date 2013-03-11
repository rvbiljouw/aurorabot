package ms.aurora;

import ms.aurora.gui.ApplicationController;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceGraphiteAquaLookAndFeel());
                    new ApplicationController();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
