/**
 *
 * Copyright 2018 Afterschool Creatives "Captivating Creativity"
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package gov.dict.ams;

import gov.dict.ams.ui.Login;
import gov.dict.ams.ui.SplashScreen;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public class AMS extends Application {

    private static final Logger logger = LoggerFactory.getLogger(AMS.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.trace("Assembling Stage . . .");
        SplashScreen ss = new SplashScreen();
        Pane root = ss.load();

        // prepare settings for splash screen
        Scene splashScene = new Scene(root, 600, 400);
        Stage splashStage = new Stage();
        splashStage.initModality(Modality.APPLICATION_MODAL);
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setResizable(false);
        splashStage.getIcons().add(new Image(Context.getResourceStream("drawable/afterschoolcreatives/afterschool-creatives-logo.png")));
        splashStage.setScene(splashScene);
        splashStage.centerOnScreen();
        splashStage.show();

        Thread splashWaitingThread = new Thread(() -> {
            try {
                Thread.sleep(3000); // verify splash screen for 3 seconds
                Platform.runLater(() -> {
                    splashStage.close(); // close the splash screen
                    // display main menu
                    this.showMain(primaryStage);
                });
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        });
        splashWaitingThread.setDaemon(true);
        splashWaitingThread.start();
    }
    
    private void showMain(Stage primaryStage) {
        primaryStage.setTitle(Context.APPLICATION_NAME);
        primaryStage.getIcons().add(new Image(Context.getResourceStream("drawable/afterschoolcreatives/afterschool-creatives-logo.png")));
        primaryStage.setScene(new Scene(new Login().load()));
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(value -> {
            AMS.onCloseConfirmation(primaryStage);
        });
        primaryStage.show();
        logger.trace("Stage was displayed . . .");
    }

    public static void onCloseConfirmation(Stage owner) {
        Optional<ButtonType> res = PolarisDialog.create(PolarisDialog.Type.CONFIRMATION)
                .setTitle("Exit")
                .setOwner(owner)
                .setHeaderText("Close Application")
                .setContentText("Are you sure you want to close the application?")
                .showAndWait();
        if (res.get().getText().equals("OK")) {
            logger.trace("Application Exit !");
            Platform.exit(); // exit java fx
        }
    }

    public static void main(String... args) {
        Application.launch(args);
        logger.trace("Application Launched");
    }

}
