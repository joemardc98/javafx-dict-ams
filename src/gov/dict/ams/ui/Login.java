
package gov.dict.ams.ui;

import com.jfoenix.controls.JFXButton;
import gov.dict.ams.AMS;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.ui.home.SystemHome;
import javafx.fxml.FXML;
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

    public Login() {
        logger.trace("Login Object Created . . .");
        // Dialog Message Title for this form.
        this.setDialogMessageTitle("Login");
    }

    @Override
    protected void setup() {
        logger.trace("Login View Initialized . . .");
        this.btn_sample.setOnMouseClicked(value -> {
            //this.showInformationMessage("Some Message", "Hello World");
            AMS.onCloseConfirmation(this.getStage());
        });

        this.btn_login.setOnMouseClicked(value -> {
            this.changeRoot(new SystemHome().load());
        });
    }

}
