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
import gov.dict.ams.models.AttendeeModel;
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
import org.afterschoolcreatives.polaris.javafx.fxml.PolarisFxController;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;

/**
 *
 * @author Joemar
 */
public class NewAttendee  extends ApplicationForm  {

    @FXML
    private Label lbl_total_no;

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
    
    private String mode = "ADD", ADD = "ADD", EDIT = "EDIT";
    @Override
    protected void setup() {
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
        
        this.btn_home.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new SystemHome().load());
        });
        
        this.btn_add.setOnMouseClicked((MouseEvent value) -> {
            this.addNew();
        });
        
        this.btn_edit.setOnMouseClicked((MouseEvent value) -> {
            this.setEditModePreview();
        });
        
        if(mode.equalsIgnoreCase(EDIT)) {
            this.setEditModePreview();
        }
    }
    
    private void setEditModePreview() {
        mode = EDIT;
        
        this.lbl_title_add.setText("Edit the selected attendee's information here.");
        
        this.lbl_email_add.setText(this.model.getEmail());
        this.lbl_first_name.setText(this.model.getFirstName());
        this.lbl_gender.setText(this.model.getGender().equalsIgnoreCase("F")? "Female" : "Male");
        this.lbl_id.setText(model.getId() + "");
        this.lbl_last_name.setText(this.model.getLastName());
        this.lbl_middle_initial.setText(this.model.getMiddleInitial());
        
        this.btn_add.setText("Save Changes");
        this.txt_email_add.setText(this.lbl_email_add.getText());
        this.txt_first_name.setText(this.lbl_first_name.getText());
        this.txt_last_name.setText(this.lbl_last_name.getText());
        this.txt_middle_initial.setText(this.lbl_middle_initial.getText());
        this.rbtn_male.setSelected(!this.lbl_gender.getText().equalsIgnoreCase("Female"));
        this.rbtn_female.setSelected(!this.lbl_gender.getText().equalsIgnoreCase("Male"));
    }
    
    private AttendeeModel model = new AttendeeModel();
    private void addNew() {
        if(this.txt_first_name.getText().equalsIgnoreCase("")) {
            this.showWarningMessage("Empty Field", "Please fill up the fields with asterisk (*) to continue.");
            return;
        } else if(this.txt_last_name.getText().equalsIgnoreCase("")) {
            this.showWarningMessage("Empty Field", "Please fill up the fields with asterisk (*) to continue.");
            return;
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
    
    private void setPreviewResult(boolean res) {
        if(res) {
            if(mode.equalsIgnoreCase(ADD)) {
                try {
                    this.reloadStatus();
                } catch (SQLException ex) {
                    Logger.getLogger(NewAttendee.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            this.lbl_email_add.setText(this.txt_email_add.getText());
            this.lbl_first_name.setText(this.txt_first_name.getText());
            this.lbl_gender.setText(this.rbtn_female.isSelected()? "Female" : "Male");
            this.lbl_id.setText(model.getId() + "");
            this.lbl_last_name.setText(this.txt_last_name.getText());
            this.lbl_middle_initial.setText(this.txt_middle_initial.getText());

            this.txt_email_add.setText("");
            this.txt_first_name.setText("");
            this.txt_last_name.setText("");
            this.txt_middle_initial.setText("");
            this.rbtn_male.setSelected(true);

            this.vbox_newly_added.setVisible(true);
        } else {
            this.showErrorMessage("Failed", "Failed adding new attendee.");
        }
    }
    
    private void setModelValues() {
        model.setEmail(this.txt_email_add.getText());
        model.setFirstName(this.txt_first_name.getText());
        model.setGender(this.rbtn_female.isSelected()? "F" : "M");
        model.setLastName(this.txt_last_name.getText());
        model.setMiddleInitial(this.txt_middle_initial.getText());
    }
    
    private void reloadStatus() throws SQLException {
        Integer maleCount = 0, femaleCount = 0;
        List<AttendeeModel> content = AttendeeModel.listAllActive();
        for(AttendeeModel each: content) {
            System.out.println(each.getFullName());
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
        this.lbl_total_no.setText(content.size() + "");
    }
    
    public void setEditMode(AttendeeModel model) {
        this.mode = EDIT;
        this.model = model;
    }
}
