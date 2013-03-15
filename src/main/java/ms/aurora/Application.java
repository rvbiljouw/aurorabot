package ms.aurora;

import ms.aurora.gui.ApplicationController;
import org.apache.log4j.Logger;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;

import javax.swing.*;

public final class Application {
    private static Logger logger = Logger.getLogger(Application.class);

    private Application() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceGraphiteAquaLookAndFeel());
                    new ApplicationController();
                } catch (Exception e) {
                    logger.error("Couldn't initialize substance L&F", e);
                }
            }
        });
    }
}
