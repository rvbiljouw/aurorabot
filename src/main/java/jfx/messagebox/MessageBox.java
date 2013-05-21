/*******************************************************************************
 * JfxMessageBox
 * Copyright (C) 2012 Toshiki IGA
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
/*******************************************************************************
 * Copyright (c) 2012 Toshiki IGA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Toshiki IGA - initial implementation
 *******************************************************************************/
/*******************************************************************************
 * Copyright 2012 Toshiki IGA and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package jfx.messagebox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ms.aurora.gui.Messages;

/**
 * JfxMessageBox is free MessageBox API for JavaFX 2.
 * <p/>
 * Using JfxMessageBox, you can use MessageBox in JavaFX 2 easily.
 * <p/>
 * <h3>
 * Usage 1</h3>
 * <p/>
 * <code>
 * <pre>
 * import jfx.messagebox.MessageBox;
 *
 *     MessageBox.show(primaryStage,
 *         "Sample of error dialog.\n\nDialog option is below.\n[MessageBox.ICON_ERROR]",
 *         "Error dialog",
 *         MessageBox.ICON_ERROR);
 * </pre>
 * </code>
 * <p/>
 * <h3>
 * Usage 2</h3>
 * <p/>
 * <code>
 * <pre>
 * import jfx.messagebox.MessageBox;
 *
 *     MessageBox.show(primaryStage,
 *         "Sample of information dialog.\n\nDialog option is below.\n[MessageBox.ICON_INFORMATION | MessageBox.OK | MessageBox.CANCEL]",
 *         "Information dialog",
 *         MessageBox.ICON_INFORMATION | MessageBox.OK | MessageBox.CANCEL);
 * </pre>
 * </code>
 * <p/>
 * <h3>
 * System Require</h3>
 * <ul>
 * <li>JfxMessageBox require JavaFX 2.2 or later.</li>
 * </ul>
 *
 * @author Toshiki Iga
 * @see <a href="http://sourceforge.jp/projects/jfxmessagebox/wiki/">Project
 *      Home</a>
 * @see <a
 *      href="http://sourceforge.jp/projects/jfxmessagebox/wiki/GettingStarted">Getting
 *      Started</a>
 */
public class MessageBox {
    // ///////////////////////////////////////////////////
    // Types of icon.

    /**
     * Error icon.
     */
    public static final int ICON_ERROR = 0x01000000;
    /**
     * Warning icon.
     */
    public static final int ICON_WARNING = 0x02000000;
    /**
     * Information icon.
     */
    public static final int ICON_INFORMATION = 0x04000000;
    /**
     * Question icon.
     */
    public static final int ICON_QUESTION = 0x08000000;

    // ///////////////////////////////////////////////////
    // Types of button.
    /**
     * OK button.
     */
    public static final int OK = 0x00010000;
    /**
     * Cancel button.
     */
    public static final int CANCEL = 0x00020000;
    /**
     * Yes button.
     */
    public static final int YES = 0x00040000;
    /**
     * No button.
     */
    public static final int NO = 0x00080000;
    /**
     * Abort button.
     */
    public static final int ABORT = 0x00100000;
    /**
     * Retry button.
     */
    public static final int RETRY = 0x00200000;
    /**
     * Ignore button.
     */
    public static final int IGNORE = 0x00400000;

    // ///////////////////////////////////////////////////
    // Control for default button.
    /**
     * Set first button as default button.
     */
    public static final int DEFAULT_BUTTON1 = 0x00000100;
    /**
     * Set second button as default button.
     */
    public static final int DEFAULT_BUTTON2 = 0x00000200;
    /**
     * Set third button as default button.
     */
    public static final int DEFAULT_BUTTON3 = 0x00000400;
    /**
     * Set fourth button as default button.
     */
    public static final int DEFAULT_BUTTON4 = 0x00000800;

    /**
     * Hide constructor.
     */
    protected MessageBox() {
        // Do nothing.
    }

    /**
     * Display MessageBox.
     * <p/>
     * <h3>Requirement</h3>
     * <ul>
     * <li>Program should run as JavaFX Application.</li>
     * </ul>
     *
     * @param parent  Parent window object. If null was set, dialog may be modeless.
     * @param message Message string for dialog.
     * @param title   Title string for dialog.
     * @param option  Display option. The option value is either one of the option
     *                constants OR'ing together (using the int "|" operator) two or
     *                more of those MessageBox option constants. ex.
     *                MessageBox.ICON_INFORMATION | MessageBox.OK |
     *                MessageBox.CANCEL
     * @return Selected button value. Default value is MessageBox.CANCEL.
     *         Selected button value is one of MessageBox.OK, MessageBox.CANCEL,
     *         MessageBox.YES, MessageBox.NO, MessageBox.ABORT,
     *         MessageBox.RETRY, MessageBox.IGNORE.
     */
    public static int show(final Window parent, final String message, final String title, final int option) {
        // Default return value is CANCEL.
        final int[] result = new int[]{CANCEL};

        // Create stage without iconized button.
        final Stage dialog = new Stage(StageStyle.UTILITY);
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.initModality(Modality.WINDOW_MODAL);
        if (parent != null) {
            // Only set in case of not null.
            dialog.initOwner(parent);
        }

        final VBox totalPane = new VBox();
        dialog.setScene(new Scene(totalPane));
        totalPane.setAlignment(Pos.CENTER);
        totalPane.setSpacing(2);

        final HBox pane = new HBox();
        totalPane.getChildren().add(pane);

        pane.setSpacing(10);

        // Pad left space.
        pane.getChildren().add(new Label("")); //$NON-NLS-1$
        pane.getChildren().add(new Label("")); //$NON-NLS-1$

        {
            final VBox vbox = new VBox();
            pane.getChildren().add(vbox);
            vbox.setAlignment(Pos.CENTER);

            if ((option & ICON_ERROR) == ICON_ERROR) {
                final Group group = MessageIconBuilder.drawErrorIcon(3);
                vbox.getChildren().add(group);
            } else if ((option & ICON_WARNING) == ICON_WARNING) {
                final Group group = MessageIconBuilder.drawWarningIcon(3);
                vbox.getChildren().add(group);
            } else if ((option & ICON_INFORMATION) == ICON_INFORMATION) {
                final Group group = MessageIconBuilder.drawInformationIcon(3);
                vbox.getChildren().add(group);
            } else if ((option & ICON_QUESTION) == ICON_QUESTION) {
                final Group group = MessageIconBuilder.drawQuestionIcon(3);
                vbox.getChildren().add(group);
            }
        }

        {
            final VBox vbox = new VBox();
            pane.getChildren().add(vbox);

            vbox.setAlignment(Pos.CENTER);

            vbox.getChildren().add(new Label(""));//$NON-NLS-1$
            vbox.getChildren().add(new Label(message));

            // Pad right space.
            pane.getChildren().add(new Label("")); //$NON-NLS-1$
            pane.getChildren().add(new Label("")); //$NON-NLS-1$

            // Pad message and buttons.
            vbox.getChildren().add(new Label("")); //$NON-NLS-1$
            vbox.getChildren().add(new Label("")); //$NON-NLS-1$

            boolean isButtonExists = false;

            final int[] BUTTON_LIST = new int[]{OK, YES, NO, ABORT, RETRY, IGNORE, CANCEL};
            final String[] BUTTON_STRING_LIST = new String[]{
                    Messages.getString("MessageBox.OK"), Messages.getString("MessageBox.YES"), Messages.getString("MessageBox.NO"), Messages.getString("MessageBox.ABORT"), Messages.getString("MessageBox.RETRY"), Messages.getString("MessageBox.IGNORE"), Messages.getString("MessageBox.CANCEL")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$

            final HBox hboxButtons = new HBox();
            totalPane.getChildren().add(hboxButtons);
            hboxButtons.setSpacing(10);
            hboxButtons.setAlignment(Pos.CENTER);
            hboxButtons.getChildren().add(new Label("")); //$NON-NLS-1$

            int buttonCounter = 0;
            for (int index = 0; index < BUTTON_LIST.length; index++) {
                if ((option & BUTTON_LIST[index]) == BUTTON_LIST[index]) {
                    final Button btnAdd = new Button();
                    btnAdd.setText(BUTTON_STRING_LIST[index]);
                    isButtonExists = true;
                    final int resultValue = BUTTON_LIST[index];
                    btnAdd.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            dialog.close();
                            result[0] = resultValue;
                        }
                    });
                    hboxButtons.getChildren().add(btnAdd);
                    buttonCounter++;

                    if ((option & CANCEL) == CANCEL) {
                        btnAdd.setCancelButton(true);
                    }

                    setupDefaultButton(option, buttonCounter, btnAdd);
                }
            }

            // In case of no button found.
            if (isButtonExists == false) {
                final Button btnAdd = new Button();
                hboxButtons.getChildren().add(btnAdd);
                btnAdd.setText(BUTTON_STRING_LIST[0]);
                btnAdd.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        dialog.close();
                        result[0] = OK;
                    }
                });
                btnAdd.setCancelButton(true);
            }

            hboxButtons.getChildren().add(new Label("")); //$NON-NLS-1$

            totalPane.getChildren().add(new Label(""));//$NON-NLS-1$
        }

        // Below method is supported JavaFX 2.2 or lator.
        dialog.showAndWait();

        return result[0];
    }

    /**
     * Set up default button.
     *
     * @param option
     * @param buttonCounter
     * @param btnAdd
     */
    private static void setupDefaultButton(final int option, final int buttonCounter, final Button btnAdd) {
        switch (buttonCounter) {
            case 1:
                if ((option & DEFAULT_BUTTON1) == DEFAULT_BUTTON1) {
                    btnAdd.setDefaultButton(true);
                }
                break;
            case 2:
                if ((option & DEFAULT_BUTTON2) == DEFAULT_BUTTON2) {
                    btnAdd.setDefaultButton(true);
                }
                break;
            case 3:
                if ((option & DEFAULT_BUTTON3) == DEFAULT_BUTTON3) {
                    btnAdd.setDefaultButton(true);
                }
                break;
            case 4:
                if ((option & DEFAULT_BUTTON4) == DEFAULT_BUTTON4) {
                    btnAdd.setDefaultButton(true);
                }
                break;
        }
    }
}