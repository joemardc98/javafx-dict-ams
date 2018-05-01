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
import gov.dict.ams.Storage;
import gov.dict.ams.models.AttendeeModel;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.afterschoolcreatives.polaris.javafx.fxml.PolarisFxController;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;
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
    
    private Image gender;
    private AttendeeModel content;
    private boolean isChkbxSelected = false;
    private SimpleTableRow row;
    @Override
    protected void setup() {
        this.lbl_email.setText(content.getEmail()==null? "No Email Address Found" : content.getEmail());
        this.lbl_full_name.setText(content.getFullName(AttendeeModel.NameFormat.SURNAME_FIRST));
        this.lbl_id.setText(content.getId() + "");
        this.imgvw_gender.setImage(gender);
        this.btn_edit.setOnMouseClicked((MouseEvent value)->{
            this.editInfo();
        });
//        this.chkbx_selected.selectedProperty().addListener((a)->{
//            System.out.println("selectedProperty");
//            this.isChkbxSelected = this.chkbx_selected.isSelected();
//            if(btn_add_new != null) {
//                this.btn_add_new.setDisable(this.isChkbxSelected);
//            }
//        });
        this.chkbx_selected.setOnMouseClicked((a)->{
            this.isChkbxSelected = chkbx_selected.isSelected();
            System.out.println("chkbx_selected.setOnMouseClicked setSelected " + isChkbxSelected);
            this.chkbx_selected.setSelected(isChkbxSelected);
            this.captureSelectedModel();
        });
        this.row.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("row.setOnMouseClicked");
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        System.out.println("Double clicked");
                        editInfo();
                    } else {
                        isChkbxSelected = !chkbx_selected.isSelected();
                        chkbx_selected.setSelected(isChkbxSelected);
                    }
                }
            }
        });
    }
    
    private void editInfo() {
        NewAttendee edit = new NewAttendee();
        edit.setEditMode(content);
        this.changeRoot(edit.load());
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
    
    public void setGender(Image gender) {
        this.gender = gender;
    }

    public JFXButton getBtn_edit() {
        return btn_edit;
    }
    
    private ArrayList<AttendeeModel> selectedModel;
    private SimpleTable tableAttendee = new SimpleTable();
    private String ROW = "ROW", CONTENT = "CONTENT";
    private void captureSelectedModel() {
        System.out.println("CALLED");
        this.selectedModel.clear();
        ObservableList<Node> rows = this.tableAttendee.getChildren();
        for(Node each: rows) {
            SimpleTableRow row = (SimpleTableRow) each;
            AttendeeRow eachRow = (AttendeeRow) row.getRowMetaData().get(ROW);
            AttendeeModel eachModel = (AttendeeModel) row.getRowMetaData().get(CONTENT);
            if(eachRow.isChkbxSelected()) {
                this.selectedModel.add(eachModel);
            }
        }
        
        System.out.println(this.selectedModel.size());
        
        Storage.getBtn_add().setDisable(!this.selectedModel.isEmpty());
        Storage.getBtn_generate().setDisable(this.selectedModel.isEmpty());
        Storage.getBtn_delete().setDisable(this.selectedModel.isEmpty());
        Storage.getLbl_total_selected().setText(this.selectedModel.size() + " out of " + this.tableAttendee.getChildren().size());
        Storage.getChkbx_select_all().setSelected(this.selectedModel.size() == this.tableAttendee.getChildren().size());
    }

    public void setSelectedModel(ArrayList<AttendeeModel> selectedModel) {
        this.selectedModel = selectedModel;
    }

    public void setTableAttendee(SimpleTable tableAttendee) {
        this.tableAttendee = tableAttendee;
    }
}
