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
import com.jfoenix.controls.JFXCheckBox;
import gov.dict.ams.models.AttendeeModel;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.event.ChangeListener;
import org.afterschoolcreatives.polaris.javafx.fxml.PolarisFxController;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableRow;

/**
 *
 * @author Joemar
 */
public class AttendeeRow extends PolarisFxController {

    @FXML
    private JFXCheckBox chkbx_selected;

    @FXML
    private Label lbl_id;

    @FXML
    private ImageView imgvw_gender;

    @FXML
    private Label lbl_full_name;

    @FXML
    private Label lbl_email;

    @FXML
    private JFXButton btn_edit;
    
    private String fullname, email, id;
    private Image gender;
    private AttendeeModel content;
    private boolean isChkbxSelected = false;
    private SimpleTableRow row;
    @Override
    protected void setup() {
        this.lbl_email.setText(email);
        this.lbl_full_name.setText(fullname);
        this.lbl_id.setText(id);
        this.imgvw_gender.setImage(gender);
        this.btn_edit.setOnMouseClicked((MouseEvent value)->{
            NewAttendee edit = new NewAttendee();
            edit.setEditMode(content);
            this.changeRoot(edit.load());
        });
        this.chkbx_selected.selectedProperty().addListener((a)->{
            this.isChkbxSelected = this.chkbx_selected.isSelected();
        });
        this.row.setOnMouseClicked((MouseEvent value)->{
            this.isChkbxSelected = !this.chkbx_selected.isSelected();
            this.chkbx_selected.setSelected(this.isChkbxSelected);
        });
    }

    public void setIsChkbxSelected(boolean isChkbxSelected) {
        this.isChkbxSelected = isChkbxSelected;
        this.chkbx_selected.setSelected(isChkbxSelected);
    }

    public void setRow(SimpleTableRow row) {
        this.row = row;
    }

    public boolean isChkbxSelected() {
        return isChkbxSelected;
    }

    public void setContent(AttendeeModel content) {
        this.content = content;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(Image gender) {
        this.gender = gender;
    }

    public JFXButton getBtn_edit() {
        return btn_edit;
    }
    
}
