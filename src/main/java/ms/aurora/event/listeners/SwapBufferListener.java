package ms.aurora.event.listeners;

import java.awt.image.BufferedImage;

/**
 * @author rvbiljouw
 */
public interface SwapBufferListener {

    /**
     * Called by a double buffered source, on the moment it swaps it's buffer.
     * @param image The discarded buffer.
     */
    public void onSwapBuffer(BufferedImage image);

}
