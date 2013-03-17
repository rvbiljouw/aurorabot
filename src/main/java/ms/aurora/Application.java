package ms.aurora;

import ms.aurora.gui.ApplicationController;
import org.apache.log4j.Logger;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;

import javax.swing.*;
import java.io.File;

public final class Application {
    private static Logger logger = Logger.getLogger(Application.class);
    private static File props = new File(System.getProperty("user.home") + "/.aurora");

    private Application() {

    }

    public static void main(String[] args) {
        if(!props.exists()) {
            props.mkdir();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
                    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
                    ApplicationController.startApplication();
                } catch (Exception e) {
                    logger.error("Couldn't initialize substance L&F", e);
                }
            }
        });
    }
}
