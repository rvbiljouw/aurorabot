package ms.aurora.api.plugin.internal;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alowaniak
 * Author: alowaniak
 * Date: 2-7-13
 * Time: 21:13
 * <p/>
 * This code is for educational purposes only.
 * I can not be held responsible for any event triggered by the use
 * - in the broadest sense of the word;
 * this includes but is not limited to seeing, distributing, executing and eating -
 * of this piece of code.
 */

/**
 * Plugin for viewing the Settings.
 */
@PluginManifest(name = "Settings Viewer", version = 0.1, author = "Alowaniak" )
public class SettingsViewer extends Plugin {
    private Menu menu;
    private CheckMenuItem toggleSettingsViewer;
    private ViewerStage viewerStage;

    static class ViewerStage extends Stage {
        VBox settingsBox;
        VBox changedBox;

        int[] oldSettings;
        int[] curSettings;

        public ViewerStage(int[] settings) {
            oldSettings = settings.clone();
            curSettings = settings;
            setTitle("Settings Viewer by Alowaniak");

            HBox pane = new HBox(10);
            pane.setMaxWidth(Double.MAX_VALUE);
            //Settings
            settingsBox = new VBox(1);
            settingsBox.getChildren().addAll(buildCurSettingsLbls());
            settingsBox.setMaxWidth(Double.MAX_VALUE);

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(settingsBox);
            scrollPane.setPrefViewportHeight(600);
            scrollPane.setMaxWidth(Double.MAX_VALUE);
            pane.getChildren().add(scrollPane);

            changedBox = new VBox(1);
            changedBox.getChildren().addAll(updateChangedSettingsLbls());
            changedBox.setMaxWidth(Double.MAX_VALUE);
            pane.getChildren().add(changedBox);

            Button btn = new Button("Update settings");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changedBox.getChildren().setAll(updateChangedSettingsLbls());
                    settingsBox.getChildren().setAll(buildCurSettingsLbls());
                }
            });
            btn.setMaxWidth(Double.MAX_VALUE);
            pane.getChildren().add(btn);

            setScene(new Scene(pane));
        }

        private List<Label> updateChangedSettingsLbls() {
            LinkedList<Label> labels = new LinkedList<Label>();
            labels.add(new Label("Changed settings"));
            for(int i = 0; i < curSettings.length; i++) {
                if(oldSettings[i] != curSettings[i]) {
                    labels.add(new Label(i + ": " + oldSettings[i] + "->" + curSettings[i]));
                }
            }
            oldSettings = curSettings.clone();
            return labels;
        }

        private Label[] buildCurSettingsLbls() {
            Label[] labels = new Label[curSettings.length+1];
            labels[0] = new Label("New settings");
            for(int i = 0; i < curSettings.length; i++) {
                labels[i+1] = new Label(i + ": " + curSettings[i]);
            }
            return labels;
        }
    }

    @Override
    public void startup() {
        menu = new Menu("Settings Viewer");
        toggleSettingsViewer = new CheckMenuItem("Toggle settings viewer");
        final int[] settings = getClient().getWidgetSettings();
        toggleSettingsViewer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(viewerStage != null && viewerStage.isShowing()) {
                    viewerStage.hide();
                } else {
                    if(viewerStage == null) {
                        viewerStage = new ViewerStage(settings);
                        viewerStage.setOnHidden(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent windowEvent) {
                                toggleSettingsViewer.setSelected(false);
                            }
                        });
                    }
                    viewerStage.show();
                }
            }
        });
        menu.getItems().add(toggleSettingsViewer);
        registerMenu(menu);
    }

    @Override
    public void execute() {
    }

    @Override
    public void cleanup() {
        deregisterMenu(menu);
    }
}
