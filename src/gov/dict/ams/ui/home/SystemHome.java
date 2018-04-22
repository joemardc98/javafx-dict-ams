
package gov.dict.ams.ui.home;

import com.jfoenix.controls.JFXButton;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.Context;
import gov.dict.ams.models.AttendeeModel;
import java.awt.GraphicsEnvironment;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableCell;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableRow;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableView;

public class SystemHome extends ApplicationForm {

    @FXML
    private Label lbl_total_no;

    @FXML
    private Label lbl_male_count;

    @FXML
    private Label lbl_female_count;

    @FXML
    private ComboBox<String> cmb_font_style;

    @FXML
    private ComboBox<Integer> cmb_font_size;

    @FXML
    private VBox tbl_attendees;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXButton btn_delete;

    @Override
    protected void setup() {
        this.setComboBoxes();
        try {
            this.createTable();
        } catch (SQLException ex) {
            Logger.getLogger(SystemHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.eventHandler();
    }
    
    private void eventHandler() {
        this.btn_add.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new NewAttendee().load());
        });
        
        this.btn_delete.setOnMouseClicked((MouseEvent value) -> {
            this.onDeleteConfirmation();
        });
    }

    private void setComboBoxes() {
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for ( int i = 0; i < fonts.length; i++ ) {
          this.cmb_font_style.getItems().add(fonts[i]);
        }
        this.cmb_font_style.getSelectionModel().select("Calibri");
        for (int i =5 ; i <= 100; i++) {
            this.cmb_font_size.getItems().add(i);
        }
        this.cmb_font_size.getSelectionModel().select(10);
    }
    
    private SimpleTable tableStudent = new SimpleTable();
    private Integer maleCount = 0, femaleCount = 0;
    private void createTable() throws SQLException {
        List<AttendeeModel> content = AttendeeModel.listAllActive();
        for(AttendeeModel each: content) {
            System.out.println(each.getFullName());
            this.createRow(each);
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
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tableStudent);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(this.tbl_attendees);
    }
    
    private Image maleIcon = new Image(Context.getResourceStream("drawable/male.png"));
    private Image femaleIcon = new Image(Context.getResourceStream("drawable/female.png"));
    private void createRow(AttendeeModel content) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);
        
        AttendeeRow itemRow = new AttendeeRow();
        itemRow.setEmail(content.getEmail()==null? "No Email Address Found" : content.getEmail());
        itemRow.setFullname(content.getFullName());
        itemRow.setId(content.getId() + "");
        
        if(content.getGender() != null) {
            if(content.getGender().equalsIgnoreCase("F")) {
                itemRow.setGender(femaleIcon);
            } else {
                itemRow.setGender(maleIcon);
            }
        }
        
        HBox hboxRow = itemRow.load();
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(hboxRow);
        
        row.addCell(cellParent);
        this.tableStudent.addRow(row);
    }
    
    private void onDeleteConfirmation() {
        Optional<ButtonType> res = PolarisDialog.create(PolarisDialog.Type.CONFIRMATION)
                .setTitle("Delete")
                .setOwner(this.getStage())
                .setHeaderText("Delete Attendee")
                .setContentText("Are you sure you want to delete the selected attendee(s)?")
                .showAndWait();
        if (res.get().getText().equals("OK")) {
            PolarisDialog.create(PolarisDialog.Type.INFORMATION)
                    .setTitle("Delete")
                    .setHeaderText("Successfully Deleted")
                    .setContentText("Selected attendee(s) are deleted.")
                    .setOwner(this.getStage())
                    .show();
        }
    }
}
