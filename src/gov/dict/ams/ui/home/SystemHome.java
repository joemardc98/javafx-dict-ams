
package gov.dict.ams.ui.home;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.Context;
import gov.dict.ams.models.AttendeeModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.java.io.FileTool;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
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
    
    
    @Override
    protected void setup() {
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
            boolean suuported = false;
            if (Desktop.isDesktopSupported()) {
                if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    try {
                        Desktop.getDesktop().open(new File("certificates"));
                    } catch (Exception e) {
                        this.showErrorMessage("Failed","The folder is not existing or is empty. Try creating certificates first before opening.");
                    }
                    suuported = true;
                }
            }
            if (!suuported) {
                this.showWarningMessage("Failed","Action Not Supported in this Operating System");
            }
            value.consume();
        });
        
        
        this.tbl_attendees.setOnMouseClicked((MouseEvent value) -> {
            this.captureSelectedModel();
        });
    }
    
    private SimpleTable tableAttendee = new SimpleTable();
    private Integer maleCount = 0, femaleCount = 0;
    private void createTable() throws SQLException {
        this.btn_generate.setDisable(true);
        this.btn_delete.setDisable(true);
        this.maleCount = 0;
        this.femaleCount = 0;
        this.tableAttendee.getChildren().clear();
        List<AttendeeModel> content = AttendeeModel.listAllActive();
        for(AttendeeModel each: content) {
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
        simpleTableView.setTable(tableAttendee);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(this.tbl_attendees);
        
        this.lbl_total_selected.setText(this.selectedModel.size() + " out of " + this.tableAttendee.getChildren().size());
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
        itemRow.setBtn_add_new(this.btn_add);
        
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
                this.showInformationMessage("Successfully Deleted", "Selected attendee(s) are deleted. " + selectedModel.size() + "/" + this.lbl_total_no.getText());
                this.createTable();
                this.selectedModel.clear();
             }
        }
    }
    
    private void captureSelectedModel() {
        this.selectedModel.clear();
        ObservableList<Node> rows = this.tableAttendee.getChildren();
        for(Node each: rows) {
            SimpleTableRow row = (SimpleTableRow) each;
            AttendeeRow eachRow= (AttendeeRow) row.getRowMetaData().get(ROW);
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
}
