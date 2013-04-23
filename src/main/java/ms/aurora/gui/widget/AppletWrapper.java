package ms.aurora.gui.widget;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import ms.aurora.core.Session;
import ms.aurora.event.PaintManager;
import ms.aurora.event.listeners.SwapBufferListener;
import ms.aurora.input.ClientCanvas;
import ms.aurora.rt3.Client;

import java.applet.Applet;
import java.awt.image.BufferedImage;

import static javafx.embed.swing.SwingFXUtils.toFXImage;
import static ms.aurora.core.SessionRepository.get;

/**
 * A widget for wrapping the RuneScape applet
 *
 * @author rvbiljouw
 */
public class AppletWrapper extends Region implements SwapBufferListener {
    private final ImageView imageView;
    private final WritableImage canvas = new WritableImage(765, 503);
    private final Applet applet;

    public AppletWrapper(Applet applet) {
        imageView = new ImageView(canvas);
        this.applet = applet;
        this.init();
    }

    private void init() {
        Session session = get(applet.hashCode());
        if (session != null) {
            PaintManager pm = session.getPaintManager();
            pm.setSwapBufferListener(this);
        }

        addEventFilter(KeyEvent.ANY, keyEventHandler);
        addEventFilter(MouseEvent.ANY, mouseEventHandler);
        imageView.addEventFilter(KeyEvent.ANY, keyEventHandler);
        imageView.addEventFilter(MouseEvent.ANY, mouseEventHandler);
        setWidth(765);
        setHeight(503);
        getChildren().add(imageView);
        setVisible(true);

        focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> a, Boolean b, Boolean newValue) {
                imageView.requestFocus();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void onSwapBuffer(BufferedImage image) {
        toFXImage(image, canvas);
    }

    /**
     * Obtains the ClientCanvas in the case the applet was injected
     * If it wasn't injected, the canvas was never swapped and we can't do this
     * so we return null
     *
     * @return client canvas or null
     */
    private ClientCanvas getClientCanvas() {
        return (applet instanceof Client) ?
                (ClientCanvas) ((Client) applet).getCanvas() : null;
    }

    /**
     * Converts JavaFX key events into AWT key events
     *
     * @param event Event to be converted
     * @return corresponding AWT event
     */
    private java.awt.event.KeyEvent transform(KeyEvent event) {
        int eventCode = java.awt.event.KeyEvent.KEY_PRESSED;
        if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            eventCode = java.awt.event.KeyEvent.KEY_RELEASED;
        } else if (event.getEventType() == KeyEvent.KEY_TYPED) {
            eventCode = java.awt.event.KeyEvent.KEY_TYPED;
        }

        char keyChar = event.getCharacter().toCharArray()[0];
        int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(keyChar);
        int modifiers = 0;
        if (event.isAltDown()) {
            modifiers = java.awt.event.KeyEvent.ALT_DOWN_MASK;
        }

        if (event.isControlDown()) {
            modifiers = java.awt.event.KeyEvent.CTRL_DOWN_MASK;
        }

        if (event.isShiftDown()) {
            modifiers = java.awt.event.KeyEvent.SHIFT_DOWN_MASK;
        }

        return new java.awt.event.KeyEvent(
                getClientCanvas(), eventCode, System.currentTimeMillis(),
                event.getEventType() == KeyEvent.KEY_PRESSED ? modifiers : 0,
                event.getEventType() == KeyEvent.KEY_TYPED ? 0 : keyCode, keyChar);
    }

    private final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (getClientCanvas() == null) return;
            System.out.println(keyEvent.getText());
            imageView.requestFocus();
            getClientCanvas().dispatchEvent(transform(keyEvent));
        }
    };

    /**
     * Converts JavaFX mouse events into AWT mouse events
     *
     * @param event Event to be converted
     * @return corresponding AWT event
     */
    private java.awt.event.MouseEvent transform(MouseEvent event) {
        int action = java.awt.event.MouseEvent.MOUSE_MOVED;
        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            action = java.awt.event.MouseEvent.MOUSE_MOVED;
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            action = java.awt.event.MouseEvent.MOUSE_CLICKED;
        } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            action = java.awt.event.MouseEvent.MOUSE_PRESSED;
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            action = java.awt.event.MouseEvent.MOUSE_RELEASED;
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            action = java.awt.event.MouseEvent.MOUSE_DRAGGED;
        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            action = java.awt.event.MouseEvent.MOUSE_EXITED;
        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            action = java.awt.event.MouseEvent.MOUSE_ENTERED;
        }
        int button = java.awt.event.MouseEvent.BUTTON1;
        if (event.getButton() == MouseButton.SECONDARY) {
            button = java.awt.event.MouseEvent.BUTTON2;
        }

        return new java.awt.event.MouseEvent(getClientCanvas(), action,
                System.currentTimeMillis(), 0, (int) event.getX(),
                (int) event.getY(), 1, false, button);
    }

    private final EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (getClientCanvas() == null) return;
            imageView.requestFocus();

            getClientCanvas().dispatchEvent(transform(mouseEvent));
        }
    };
}
