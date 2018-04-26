
package gov.dict.ams.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import gov.dict.ams.AMS;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.ui.home.SystemHome;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login extends ApplicationForm {

    /**
     * Create Logger for this class always change the CLASS.class.
     */
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    @FXML
    private JFXButton btn_sample;

    @FXML
    private JFXButton btn_login;
    
    @FXML
    private JFXProgressBar prgbr_loading;

    @FXML
    private Label lbl_loading;


    public Login() {
        logger.trace("Login Object Created . . .");
        // Dialog Message Title for this form.
        this.setDialogMessageTitle("Login");
    }

    @Override
    protected void setup() {
        this.prgbr_loading.setVisible(false);
        this.lbl_loading.setVisible(false);
        logger.trace("Login View Initialized . . .");
        this.btn_sample.setOnMouseClicked(value -> {
            //this.showInformationMessage("Some Message", "Hello World");
            AMS.onCloseConfirmation(this.getStage());
        });

        this.btn_login.setOnMouseClicked(value -> {
            this.prgbr_loading.setVisible(true);
            this.lbl_loading.setVisible(true);
            Thread threadLoad = new Thread(()->{
                try {
                    Thread.sleep(3000);
                    Platform.runLater(()->{
                        this.changeRoot(new SystemHome().load());
                    });
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            threadLoad.setDaemon(true);
            threadLoad.start();
        });
    }

}
