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
import gov.dict.ams.Properties;
import gov.dict.ams.models.AttendeeModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.afterschoolcreatives.polaris.java.io.FileTool;
import org.afterschoolcreatives.polaris.java.util.PolarisProperties;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Joemar
 */
public class Settings extends ApplicationForm {

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
    private JFXButton btn_export;

    @FXML
    private JFXButton btn_upload;

    @FXML
    private JFXButton btn_back;
    
    @FXML
    private TextField txt_event_name;

    @FXML
    private TextField txt_venue;

    @FXML
    private TextField txt_date;

    @FXML
    private JFXButton btn_save_changes;
    
    @FXML
    private Label lbl_show_folder;
    
    @Override
    protected void setup() {
        this.lbl_show_folder.setVisible(false);
        this.loadText();
        this.refreshStatus();
        this.eventHandler();
    }
    
    private void eventHandler() {
        this.btn_back.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new SystemHome().load());
        });
        
        this.btn_export.setOnMouseClicked((MouseEvent value) -> {
            this.exportExcel();
        });
        
        this.btn_upload.setOnMouseClicked((MouseEvent value) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");

            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF File", "*.pdf")
            );
            File file = fileChooser.showOpenDialog(this.getStage());
            
            if(file == null) {
                return;
            }
            
            try {
                if (!FileTool.checkFolders("template")) {
                    System.out.println("IM HERE");
                }
                boolean res = FileTool.copyQuietly(file, new File("template" + File.separator + "certification_template.pdf"));
                if(res) {
                    this.showInformationMessage("Upload Successfully", "Your new template is successfully uploaded.");
                } else {
                    this.showErrorMessage("Unable to Upload", "Please try again the upload later.");
                }
            } catch (IOException ex) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.btn_save_changes.setOnMouseClicked((MouseEvent value) -> {
            this.saveText();
        });
        this.lbl_show_folder.setOnMouseClicked((MouseEvent value) -> {
            this.openDirFolder();
        });
    }
    
    private void openDirFolder() {
        boolean suuported = false;
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                try {
                    Desktop.getDesktop().open(selectedDirectory);
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
    
    private void loadText() {
        Properties.instance();
        // set label
        this.lbl_date.setText(Properties.getProperty("lbl_date"));
        this.lbl_event_name.setText(Properties.getProperty("lbl_event_name"));
        this.lbl_venue.setText(Properties.getProperty("lbl_venue"));
        // set textfields
        this.txt_date.setText(Properties.getProperty("lbl_date"));
        this.txt_event_name.setText(Properties.getProperty("lbl_event_name"));
        this.txt_venue.setText(Properties.getProperty("lbl_venue"));
    }
    
    private void saveText() {
        Properties.instance();
        Properties.setProperty("lbl_date", this.txt_date.getText().trim());
        Properties.setProperty("lbl_event_name", this.txt_event_name.getText().trim());
        Properties.setProperty("lbl_venue", this.txt_venue.getText().trim());
        if(Properties.saveProperty()) {
            this.showInformationMessage("Saved Successfully", "Successfully updated the Header Display.");
            this.loadText();
        } else {
            this.showErrorMessage("Cannot Save Changes", "Please try saving again later.");
        }
    }
    
    private File selectedDirectory = null;
    private void exportExcel() {
        //choose location
        DirectoryChooser directoryChooser = new DirectoryChooser();
        selectedDirectory = directoryChooser.showDialog(this.getStage());
        if(selectedDirectory ==  null) {
            return;
        }
        
        //create workbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Attendee");
        HSSFRow rowhead = sheet.createRow((short) 0);
        //create header
        rowhead.createCell((short) 0).setCellValue("ID");
        rowhead.createCell((short) 1).setCellValue("Last Name".toUpperCase());
        rowhead.createCell((short) 2).setCellValue("First Name".toUpperCase());
        rowhead.createCell((short) 3).setCellValue("Middle Initial".toUpperCase());
        rowhead.createCell((short) 4).setCellValue("Gender".toUpperCase());
        rowhead.createCell((short) 5).setCellValue("Email Address".toUpperCase());

        try {
            List<AttendeeModel> content = AttendeeModel.listAllActive(null);
            int ctrRow = 1;
            for(AttendeeModel eachModel: content) {
                // create row
                HSSFRow row = sheet.createRow((short) ctrRow);
                row.createCell((short) 0).setCellValue(eachModel.getId() + "");
                row.createCell((short) 1).setCellValue(eachModel.getLastName());
                row.createCell((short) 2).setCellValue(eachModel.getFirstName());
                row.createCell((short) 3).setCellValue((eachModel.getMiddleInitial().isEmpty()? "N/A" : eachModel.getMiddleInitial()));
                row.createCell((short) 4).setCellValue(eachModel.getGender());
                row.createCell((short) 5).setCellValue((eachModel.getEmail().isEmpty()? "NONE" : eachModel.getEmail()));
                ctrRow++;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String filePath = selectedDirectory.getAbsolutePath() + "\\" + sdf.format(new Date()) + ".xls";
            
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            this.lbl_show_folder.setVisible(true);
            this.showInformationMessage("Exported Successfully", "Your exported excel file is saved. " + filePath);
        } catch (SQLException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void refreshStatus() {
        Integer maleCount = 0, femaleCount = 0;
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
            return;
        } catch (SQLException ex) {
            Logger.getLogger(SystemHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.lbl_female_count.setText("0");
        this.lbl_male_count.setText("0");
    }
}
