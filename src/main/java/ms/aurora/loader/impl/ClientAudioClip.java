package ms.aurora.loader.impl;

import java.applet.AudioClip;
import java.net.URL;

/**
 * Client audio clip implementation.
 * This is only here for completeness sake,
 * I have no idea if it's actually used but I put
 * some debugs in here to make sure if its.
 *
 * @author rvbiljouw
 */
public class ClientAudioClip implements AudioClip {
    private final javafx.scene.media.AudioClip clip;

    public ClientAudioClip(URL url) {
        this.clip = new javafx.scene.media.AudioClip(url.toString());
    }

    public void play() {
        clip.play();
    }

    public void loop() {
        clip.stop();
        clip.setCycleCount(javafx.scene.media.AudioClip.INDEFINITE);
        clip.play();
    }

    public void stop() {
        clip.stop();
    }

}
