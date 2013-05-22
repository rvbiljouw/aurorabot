package ms.aurora.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author rvbiljouw
 */
public class Icons {
    public static final ImageView INPUT_ENABLED = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/enabled.png")));
    public static final ImageView INPUT_DISABLED = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/disabled.png")));

    public static final ImageView ADD = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/add.png")));
    public static final ImageView PAUSE = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/pause.png")));
    public static final ImageView STOP = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/stop.png")));
    public static final ImageView PLAY = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/play.png")));
    public static final ImageView RESUME = new ImageView(new Image(
            Main.class.getResourceAsStream("icons/play.png")));
}
