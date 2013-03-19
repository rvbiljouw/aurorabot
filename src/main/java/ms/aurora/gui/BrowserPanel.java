package ms.aurora.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebViewBuilder;

import javax.swing.*;

public final class BrowserPanel extends JPanel {
    private static final long serialVersionUID = -8751526200873458952L;
    private static final String DEFAULT_URL = "http://www.aurora.ms";
    private String url = DEFAULT_URL;

    private JFXPanel jfxPanel;
    private WebView webView;

    /**
     * Create the panel.
     *
     * @param url URL to navigate to after creation.
     */
    public BrowserPanel(String url) {
        this.url = url;
        setLayout(null);
        initializeJFX();
    }

    /**
     * Create the panel.
     */
    public BrowserPanel() {
        setLayout(null);
        initializeJFX();
    }

    private void initializeJFX() {
        jfxPanel = new JFXPanel();
        jfxPanel.setSize(765, 503);

        Platform.runLater(new Runnable() {
            public void run() {
                Group group = new Group();
                Scene scene = new Scene(group);
                jfxPanel.setScene(scene);
                webView = WebViewBuilder.create().minWidth(765).
                        prefWidth(765).prefHeight(503).build();
                webView.getEngine().load(url);
                group.getChildren().add(webView);
                jfxPanel.setScene(scene);
            }
        });
        add(jfxPanel);
    }

    public void navigateTo(String url) {
        webView.getEngine().load(url);
    }
}
