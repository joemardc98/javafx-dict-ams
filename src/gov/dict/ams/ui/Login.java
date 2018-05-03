
package gov.dict.ams.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import gov.dict.ams.AMS;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.Properties;
import gov.dict.ams.ui.home.SystemHome;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login extends ApplicationForm {

    /**
     * Create Logger for this class always change the CLASS.class.
     */
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    @FXML
    private VBox vbox_home;

    @FXML
    private JFXButton btn_continue;

    @FXML
    private JFXButton btn_change_ip;

    @FXML
    private JFXButton btn_close;

    @FXML
    private VBox vbox_change_server_ip;

    @FXML
    private JFXTextField txt_server_ip;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    private JFXProgressBar prgbr_loading;

    @FXML
    private Label lbl_loading;

    public Login() {
        logger.trace("Login Object Created . . .");
        // Dialog Message Title for this form.
        this.setDialogMessageTitle("AMS");
    }

    @Override
    protected void setup() {
        this.setTextField();
        
        this.vbox_change_server_ip.setVisible(false);
        this.vbox_home.setVisible(true);
        
        this.prgbr_loading.setVisible(false);
        this.lbl_loading.setVisible(false);
        
        logger.trace("Login View Initialized . . .");
        this.btn_close.setOnMouseClicked(value -> {
            //this.showInformationMessage("Some Message", "Hello World");
            AMS.onCloseConfirmation(this.getStage());
        });

        this.btn_continue.setOnMouseClicked(value -> {
            this.loadSystemHome();
        });
        this.btn_cancel.setOnMouseClicked(value -> {
            this.vbox_change_server_ip.setVisible(false);
            this.vbox_home.setVisible(true);
        });
        this.btn_change_ip.setOnMouseClicked(value -> {
            this.vbox_change_server_ip.setVisible(true);
            this.vbox_home.setVisible(false);
        });
        this.btn_save_changes.setOnMouseClicked(value -> {
            if(txt_server_ip.getText().trim().isEmpty()) {
                int res = this.showConfirmationMessage("Use Default IP Address", "Saving the server's ip with an empty field will automatically set the host ip address into default 127.0.0.1. Do you want to continue?");
                if(res==1) {
                    this.txt_server_ip.setText("127.0.0.1");
                    this.saveChanges();
                }
            } else {
                this.saveChanges();
            }
        });
    }
    
    private void saveChanges() {
        Properties.instantiate();
        Properties.setProperty("host", txt_server_ip.getText().trim());
        if(Properties.saveProperty()) {
            this.loadSystemHome();
        } else {
            this.showErrorMessage("Saving Failed", "The system can't save the server's ip at this moment. Please try restarting the application then change the server's ip again.");
        }
    }
    
    private void setTextField() {
        Properties.instantiate();
        this.txt_server_ip.setText(Properties.getProperty("host"));
    }
    
    private void loadSystemHome() {
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
    }

}
