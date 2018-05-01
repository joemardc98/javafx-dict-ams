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
package gov.dict.ams.ui.home;

import com.jfoenix.controls.JFXButton;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.Context;
import gov.dict.ams.Properties;
import gov.dict.ams.models.AttendeeModel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.util.PolarisProperties;
import org.afterschoolcreatives.polaris.javafx.fxml.PolarisFxController;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;

/**
 *
 * @author Joemar
 */
public class NewAttendee  extends ApplicationForm  {

    @FXML
    private Label lbl_event_name;

    @FXML
    private Label lbl_venue;

    @FXML
    private Label lbl_date;
    
    @FXML
    private Label lbl_male_count;

    @FXML
    private Label lbl_female_count;

    @FXML
    private TextField txt_first_name;

    @FXML
    private TextField txt_middle_initial;

    @FXML
    private TextField txt_last_name;

    @FXML
    private RadioButton rbtn_male;

    @FXML
    private RadioButton rbtn_female;

    @FXML
    private TextField txt_email_add;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_newly_added;

    @FXML
    private Label lbl_id;

    @FXML
    private Label lbl_first_name;

    @FXML
    private Label lbl_middle_initial;

    @FXML
    private Label lbl_last_name;

    @FXML
    private Label lbl_gender;

    @FXML
    private Label lbl_email_add;

    @FXML
    private Label lbl_title_add;
    
    @FXML
    private JFXButton btn_edit;
    
    @FXML
    private TextField txt_suffix;
    
    @FXML
    private Label lbl_suffix;

    private String mode = "ADD", ADD = "ADD", EDIT = "EDIT";
    @Override
    protected void setup() {
        this.loadText();
        this.vbox_newly_added.setVisible(false);
        
        ToggleGroup grp = new ToggleGroup();
        this.rbtn_female.setToggleGroup(grp);
        this.rbtn_male.setToggleGroup(grp);
        this.rbtn_male.setSelected(true);
        
        try {
            this.reloadStatus();
        } catch (SQLException ex) {
            Logger.getLogger(NewAttendee.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.eventHandler();
        
        if(mode.equalsIgnoreCase(EDIT)) {
            this.setEditModePreview();
        }
    }
    
    private void eventHandler() {
        this.btn_home.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new SystemHome().load());
        });
        
        this.btn_add.setOnMouseClicked((MouseEvent value) -> {
            this.addNew();
        });
        
        this.btn_edit.setOnMouseClicked((MouseEvent value) -> {
            // save the inputted before clearing the textfields & rioud buttons
            this.fnPrevious = this.txt_first_name.getText();
            this.lnPrevious = this.txt_last_name.getText();
            this.miPrevious = this.txt_middle_initial.getText();
            this.emailPrevious = this.txt_email_add.getText();
            this.suffixPrevious = this.txt_suffix.getText();
            this.genderPrevious = (this.rbtn_female.isSelected()? "F" : "M");
            
            this.setEditModePreview();
        });
    }
    
    private String fnPrevious = "", miPrevious = "", 
            lnPrevious = "", genderPrevious = "", emailPrevious = "",
            suffixPrevious = "";
    private void setEditModePreview() {
        mode = EDIT;
        this.lbl_title_add.setText("Edit the selected attendee's information here.");
        this.lbl_email_add.setText(this.model.getEmail().isEmpty()? DEFAULT_EMAIL_ADD : this.model.getEmail());
        this.lbl_first_name.setText(this.model.getFirstName());
        this.lbl_gender.setText(this.model.getGender().equalsIgnoreCase("F")? "Female" : "Male");
        this.lbl_id.setText(model.getId() + "");
        this.lbl_last_name.setText(this.model.getLastName());
        this.lbl_middle_initial.setText(this.model.getMiddleInitial().isEmpty()? DEFAULT_MI : this.model.getMiddleInitial());
        this.lbl_suffix.setText(this.model.getSuffix().isEmpty()? DEFAULT_SUFFIX : this.model.getSuffix());
        
        this.btn_add.setText("Save Changes");
        this.btn_edit.setDisable(true);
        this.txt_email_add.setText(this.model.getEmail());
        this.txt_first_name.setText(this.model.getFirstName());
        this.txt_last_name.setText(this.model.getLastName());
        this.txt_middle_initial.setText(this.model.getMiddleInitial());
        this.txt_suffix.setText(this.model.getSuffix());
        this.rbtn_male.setSelected(!this.lbl_gender.getText().equalsIgnoreCase("Female"));
        this.rbtn_female.setSelected(!this.lbl_gender.getText().equalsIgnoreCase("Male"));
    }
    
    private AttendeeModel model = new AttendeeModel();
    private void addNew() {
        if(this.txt_first_name.getText().trim().equalsIgnoreCase("")) {
            this.showWarningMessage("Empty Field", "Please fill up the fields with asterisk (*) to continue.");
            return;
        } else if(this.txt_last_name.getText().trim().equalsIgnoreCase("")) {
            this.showWarningMessage("Empty Field", "Please fill up the fields with asterisk (*) to continue.");
            return;
        }
        String temp_email = this.txt_email_add.getText().trim();
        if(!temp_email.isEmpty()) {
            boolean invalidEmail = false;
            if(!temp_email.contains("@") || !temp_email.contains(".") || temp_email.length()==2) {
                invalidEmail = true;
                System.out.println("1");
            } else {
                String[] explodedEmail = temp_email.split("@");
                if(explodedEmail[0].isEmpty()) {
                    invalidEmail = true;
                    System.out.println("2");
                } else {
                    String strCheck = explodedEmail[1];
                    if(strCheck.length() < 3) {
                        System.out.println("6");
                        invalidEmail = true;
                    } else {
                        int indexOfDot = strCheck.indexOf(".");
                        String phase1 = strCheck.substring(0, indexOfDot);
                        String phase2 = strCheck.substring(indexOfDot, strCheck.length());
                        if(phase1.isEmpty() || phase2.isEmpty()) {
                            System.out.println("7");
                            invalidEmail = true;
                        }
                    }
                }
            }
            if(invalidEmail) {
                this.showWarningMessage("Invalid Email", "Please provide a valid email address or just keep this field empty to continue.");
                return;
            }
        }
        if(mode.equalsIgnoreCase(EDIT)) {
            this.setModelValues();
            boolean res = false;
            try (ConnectionManager con = Context.app().db().createConnectionManager()) {
                res = model.update(con);
            } catch (SQLException ex) {
                Logger.getLogger(NewAttendee.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.setPreviewResult(res);
            this.btn_add.setText("Add");
            this.btn_edit.setDisable(false);
            mode = ADD;
            this.lbl_title_add.setText("Please fill up the following then click Add.");
            return;
        }
        model = new AttendeeModel();
        this.setModelValues();
        boolean res = false;
        try (ConnectionManager con = Context.app().db().createConnectionManager()) {
            res = model.insert(con);
        } catch (SQLException ex) {
            Logger.getLogger(NewAttendee.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setPreviewResult(res);
    }
    
    private String DEFAULT_EMAIL_ADD = "--", DEFAULT_SUFFIX = "--",
            DEFAULT_MI = "--";
    private void setPreviewResult(boolean res) {
        if(res) {
            if(mode.equalsIgnoreCase(ADD)) {
                try {
                    this.reloadStatus();
                } catch (SQLException ex) {
                    Logger.getLogger(NewAttendee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.lbl_email_add.setText(this.txt_email_add.getText().trim().isEmpty()? DEFAULT_EMAIL_ADD : this.txt_email_add.getText().trim().toLowerCase());
            this.lbl_first_name.setText(this.txt_first_name.getText().trim().toUpperCase());
            this.lbl_gender.setText(this.rbtn_female.isSelected()? "Female" : "Male");
            this.lbl_id.setText(model.getId() + "");
            this.lbl_last_name.setText(this.txt_last_name.getText().trim().toUpperCase());
            this.lbl_middle_initial.setText(this.txt_middle_initial.getText().trim().isEmpty()? DEFAULT_MI : this.txt_middle_initial.getText().trim().toUpperCase());
            this.lbl_suffix.setText(this.txt_suffix.getText().trim().isEmpty()? DEFAULT_SUFFIX : this.txt_suffix.getText().trim().toUpperCase());

            // set previous inputted
            this.txt_email_add.setText(this.emailPrevious);
            this.txt_first_name.setText(this.fnPrevious);
            this.txt_last_name.setText(this.lnPrevious);
            this.txt_middle_initial.setText(this.miPrevious);
            this.txt_suffix.setText(this.suffixPrevious);
            if(this.genderPrevious.isEmpty()) {
                this.rbtn_male.setSelected(true);
            } else {
                this.rbtn_female.setSelected(this.genderPrevious.equals("F"));
                this.rbtn_male.setSelected(this.genderPrevious.equals("M"));
            }
            
            this.vbox_newly_added.setVisible(true);
        } else {
            this.showErrorMessage("Failed", "Failed adding new attendee.");
        }
    }
    
    private void setModelValues() {
        model.setEmail(this.txt_email_add.getText().trim().toLowerCase());
        model.setFirstName(this.txt_first_name.getText().trim().toUpperCase());
        model.setGender(this.rbtn_female.isSelected()? "F" : "M");
        model.setLastName(this.txt_last_name.getText().trim().toUpperCase());
        model.setMiddleInitial(this.txt_middle_initial.getText().trim().toUpperCase());
        model.setSuffix(this.txt_suffix.getText().trim().toUpperCase());
    }
    
    private void reloadStatus() throws SQLException {
        Integer maleCount = 0, femaleCount = 0;
        List<AttendeeModel> content = AttendeeModel.listAllActive(null);
        for(AttendeeModel each: content) {
            System.out.println(each.getFullName(AttendeeModel.NameFormat.SURNAME_FIRST));
            if(each.getGender() != null) {
                if(each.getGender().equalsIgnoreCase("M")) {
                    maleCount++;
                } else {
                    femaleCount++;
                }
            }
        }
        this.lbl_female_count.setText(femaleCount + "");
        this.lbl_male_count.setText(maleCount + "");
    }
    
    public void setEditMode(AttendeeModel model) {
        this.mode = EDIT;
        this.model = model;
    }
    
    private void loadText() {
        Properties.instantiate();
        this.lbl_date.setText(Properties.getProperty("lbl_date"));
        this.lbl_event_name.setText(Properties.getProperty("lbl_event_name"));
        this.lbl_venue.setText(Properties.getProperty("lbl_venue"));
    }
}
