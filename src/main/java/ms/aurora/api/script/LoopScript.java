package ms.aurora.api.script;

import ms.aurora.api.ClientContext;
import ms.aurora.api.drawing.Drawable;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * @author tobiewarburton
 */
public abstract class LoopScript extends ClientContext implements Runnable, Drawable {
    public final Logger logger = Logger.getLogger(getClass());

    public abstract int loop();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int loopResult = loop();
                if (loopResult != -1) {
                    Thread.sleep(loopResult);
                } else {
                    // Returning -1 means exit.
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics) {

    }

    public void info(String message) {
        logger.info(message);
    }
}
