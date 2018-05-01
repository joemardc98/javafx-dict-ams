
package gov.dict.ams.ui.home;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.Context;
import gov.dict.ams.Properties;
import gov.dict.ams.Storage;
import gov.dict.ams.models.AttendeeModel;
import java.awt.Desktop;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableCell;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableRow;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableView;

public class SystemHome extends ApplicationForm {

    @FXML
    private Label lbl_male_count;

    @FXML
    private Label lbl_female_count;

    @FXML
    private VBox tbl_attendees;

    @FXML
    private JFXCheckBox chkbx_select_all;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXButton btn_delete;
    
    @FXML
    private JFXButton btn_generate;
    
    @FXML
    private JFXButton btn_open_dir;

    @FXML
    private Label lbl_total_selected;
    
    @FXML
    private TextField txt_search;

    @FXML
    private JFXButton btn_search;
    
    @FXML
    private ComboBox<String> cmb_sort;
    
    @FXML
    private JFXButton btn_extras;
    
    @FXML
    private VBox vbox_no_result;
    
    @FXML
    private Label lbl_event_name;

    @FXML
    private Label lbl_venue;

    @FXML
    private Label lbl_date;
    
    @FXML
    private ImageView btn_about;
    
    @FXML
    private ImageView btn_about2;
    
    @Override
    protected void setup() {
        this.loadText();
        
        this.vbox_no_result.setVisible(false);
        this.tbl_attendees.setVisible(true);
        
        this.refreshTable(null);
        this.refreshStatus();
        
        this.cmb_sort.getItems().add("All");
        this.cmb_sort.getItems().add("Male");
        this.cmb_sort.getItems().add("Female");
        this.cmb_sort.getSelectionModel().selectFirst();
        
        this.eventHandler();
    }
    
    private void eventHandler() {
        this.btn_add.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new NewAttendee().load());
        });
        
        this.btn_delete.setOnMouseClicked((MouseEvent value) -> {
            try {
                this.onDeleteConfirmation();
            } catch (SQLException ex) {
                Logger.getLogger(SystemHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.chkbx_select_all.setOnMouseClicked((MouseEvent a)->{
            this.selectAllRow(this.chkbx_select_all.isSelected());
            this.captureSelectedModel();
            this.btn_generate.setDisable(this.selectedModel.isEmpty());
            this.btn_delete.setDisable(this.selectedModel.isEmpty());
        });
        
        this.btn_generate.setOnMouseClicked((MouseEvent value) -> {
            this.captureSelectedModel();
            GenerateCertificate genCert = new GenerateCertificate();
            genCert.setSelectedModels(this.selectedModel);
            this.changeRoot(genCert.load());
        });
        
        this.btn_open_dir.setOnMouseClicked(value -> {
            this.openDirFolder("certificates");
            value.consume();
        });
        
        
        this.tbl_attendees.setOnMouseClicked((MouseEvent value) -> {
            this.captureSelectedModel();
        });
        
        this.btn_search.setOnMouseClicked((MouseEvent value) -> {
            this.searchAttendee();
        });
        
        this.cmb_sort.setOnAction((a)->{
            String selected = this.cmb_sort.getSelectionModel().getSelectedItem();
            System.out.println(selected);
            if(selected.equalsIgnoreCase("All")) {
                this.refreshTable(null);
            } else if(selected.equalsIgnoreCase("Male")) {
                this.refreshTable("M");
            } else if(selected.equalsIgnoreCase("Female")) {
                this.refreshTable("F");
            }
        });
        
        this.btn_extras.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new Settings().load());
        });
        
        this.btn_about.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new About().load());
        });
        
        this.btn_about2.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new About().load());
        });
    }
    
    private void openDirFolder(String dir) {
        boolean suuported = false;
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                try {
                    Desktop.getDesktop().open(new File(/*"certificates"*/dir));
                } catch (Exception e) {
                    this.showErrorMessage("Failed","The folder is not existing or is empty. Try creating certificates first before opening.");
                }
                suuported = true;
            }
        }
        if (!suuported) {
            this.showWarningMessage("Failed","Action Not Supported in this Operating System");
        }
    }
    
    private void searchAttendee() {
        String keyTag = this.txt_search.getText();
        if(keyTag.isEmpty()) {
            this.refreshTable(null);
            return;
        }
        
        try {
            List<AttendeeModel> content = AttendeeModel.searchAttendee(keyTag);
            this.createTable(content);
        } catch (SQLException ex) {
            Logger.getLogger(SystemHome.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void refreshStatus() {
        this.maleCount = 0;
        this.femaleCount = 0;
        List<AttendeeModel> content;
        try {
            content = AttendeeModel.listAllActive(null);
            for(AttendeeModel each: content) {
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
            this.totalNumber = content.size();
            return;
        } catch (SQLException ex) {
            Logger.getLogger(SystemHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.lbl_female_count.setText("0");
        this.lbl_male_count.setText("0");
    }
    
    private SimpleTable tableAttendee = new SimpleTable();
    private Integer maleCount = 0, femaleCount = 0, totalNumber = 0;
    private void createTable(List<AttendeeModel> content) throws SQLException {
        this.btn_generate.setDisable(true);
        this.btn_delete.setDisable(true);
        this.tableAttendee.getChildren().clear();
        if(content == null || content.isEmpty()) {
            this.vbox_no_result.setVisible(true);
            this.tbl_attendees.setVisible(false);
        } else {
            this.vbox_no_result.setVisible(false);
            this.tbl_attendees.setVisible(true);
        }
        
        Storage.setBtn_add(btn_add);
        Storage.setBtn_delete(btn_delete);
        Storage.setBtn_generate(btn_generate);
        Storage.setChkbx_select_all(chkbx_select_all);
        Storage.setLbl_total_selected(lbl_total_selected);
        
        for(AttendeeModel each: content) {
            this.createRow(each);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tableAttendee);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(this.tbl_attendees);
        
        this.lbl_total_selected.setText(this.selectedModel.size() + " out of " + this.tableAttendee.getChildren().size());
        this.captureSelectedModel();
    }
    
    private Image maleIcon = new Image(Context.getResourceStream("drawable/male.png"));
    private Image femaleIcon = new Image(Context.getResourceStream("drawable/female.png"));
    private ArrayList<AttendeeModel> selectedModel = new ArrayList<>();
    private String ROW = "ROW", CONTENT = "CONTENT";
    
    private void createRow(AttendeeModel content) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);
        
        AttendeeRow itemRow = new AttendeeRow();
        itemRow.setContent(content);
        itemRow.setRow(row);
        itemRow.setSelectedModel(selectedModel);
        itemRow.setTableAttendee(tableAttendee);
        
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
        row.getRowMetaData().put(ROW, itemRow);
        row.getRowMetaData().put(CONTENT, content);
        row.addCell(cellParent);
        this.tableAttendee.addRow(row);
    }
    
    private void onDeleteConfirmation() throws SQLException {
        int res = this.showConfirmationMessage("Delete Attendee", "Are you sure you want to delete the selected attendee(s)?");
        if (res == 1) {
            this.captureSelectedModel();
            int notDeleted = 0;
            for(AttendeeModel eachModel: this.selectedModel) {
                eachModel.setActive(0);
                boolean res_ = false;
                try(ConnectionManager con = Context.app().db().createConnectionManager()) {
                    res_ = eachModel.update(con);
                }
                if(!res_) {
                    notDeleted++;
                } 
            }
            if(notDeleted != 0) {
                this.showErrorMessage("Unable To Delete", "We cannot delete at this moment. Please try again later.");
            } else {
                this.showInformationMessage("Successfully Deleted", "Selected attendee(s) are deleted. " + selectedModel.size() + "/" + this.totalNumber);
                this.refreshTable(null);
                this.selectedModel.clear();
             }
        }
    }
    
    private void refreshTable(String genderSelected) {
        try {
            List<AttendeeModel> content = AttendeeModel.listAllActive(genderSelected);
            this.createTable(content);
        } catch (SQLException ex) {
            Logger.getLogger(SystemHome.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void captureSelectedModel() {
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
        this.btn_add.setDisable(!this.selectedModel.isEmpty());
        this.btn_generate.setDisable(this.selectedModel.isEmpty());
        this.btn_delete.setDisable(this.selectedModel.isEmpty());
        this.lbl_total_selected.setText(this.selectedModel.size() + " out of " + this.tableAttendee.getChildren().size());
        this.chkbx_select_all.setSelected(this.selectedModel.size() == this.tableAttendee.getChildren().size());
    }
    
    private void selectAllRow(boolean selectAll) {
        ObservableList<Node> rows = this.tableAttendee.getChildren();
        for(Node each: rows) {
            SimpleTableRow row = (SimpleTableRow) each;
            AttendeeRow eachRow = (AttendeeRow) row.getRowMetaData().get(ROW);
            eachRow.setIsChkbxSelected(selectAll);
        }
        this.lbl_total_selected.setText((selectAll? rows.size() : "0") + " out of " + rows.size());
    }
    
    private void loadText() {
        Properties.instantiate();
        this.lbl_date.setText(Properties.getProperty("lbl_date"));
        this.lbl_event_name.setText(Properties.getProperty("lbl_event_name"));
        this.lbl_venue.setText(Properties.getProperty("lbl_venue"));
    }
}
