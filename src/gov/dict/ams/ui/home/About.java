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
import java.awt.Desktop;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableCell;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableRow;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableView;

/**
 *
 * @author Joemar
 */
public class About extends ApplicationForm {

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
    private VBox tbl_view;

    @FXML
    private JFXButton btn_view_pdf;

    @FXML
    private JFXButton btn_back;
    
    private SimpleTable tableAttendee = new SimpleTable();
    @Override
    protected void setup() {
        this.loadText();
        this.createTable();
        this.refreshStatus();
        
        this.btn_view_pdf.setOnMouseClicked((MouseEvent value) -> {
            this.openDirFolder("AMS - FAQs.pdf");
        });
        this.btn_back.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new SystemHome().load());
        });
    }
    
    private void createTable() {
        for (int i = 1; i <= 17; i++) {
            System.out.println("image" + i + ".PNG");
            double height = 0.0;
            switch(i) {
                case 1:
                    height = 194.0;
                    break;
                case 2:
                    height = 475.0;
                    break;
                case 3:
                    height = 349.0;
                    break;
                case 4:
                    height = 237.0;
                    break;
                case 5:
                    height = 525.0;
                    break;
                case 6:
                    height = 294.0;
                    break;
                case 7:
                    height = 209.0;
                    break;
                case 8:
                    height = 617.0;
                    break;
                case 9:
                    height = 511.0;
                    break;
                case 10:
                    height = 402.0;
                    break;
                case 11:
                    height = 165.0;
                    break;
                case 12:
                    height = 166.0;
                    break;
                case 13:
                    height = 439.0;
                    break;
                case 14:
                    height = 206.0;
                    break;
                case 15:
                    height = 230.0;
                    break;
                case 16:
                    height = 338.0;
                    break;
                case 17:
                    height = 459.0;
                    break;
            }
            this.createRow("image" + i + ".PNG", (height));
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tableAttendee);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(this.tbl_view);
    }
    
    private void createRow(String imageFileName, double height) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(height);
        
        FAQRow faqRow = new FAQRow();
        faqRow.setDisplayImage(new Image(Context.getResourceStream("drawable/faqs/" + imageFileName)));
        
        VBox itemRow = faqRow.load();
        itemRow.setPrefHeight(height);
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(itemRow);
        row.addCell(cellParent);
        this.tableAttendee.addRow(row);
    }
    
    private void openDirFolder(String dir) {
        boolean suuported = false;
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                try {
                    Desktop.getDesktop().open(new File(/*"certificates"*/dir));
                } catch (Exception e) {
                    this.showErrorMessage("Failed","The cannot find the AMS - FAQs file.");
                }
                suuported = true;
            }
        }
        if (!suuported) {
            this.showWarningMessage("Failed","Action Not Supported in this Operating System");
        }
    }
    
    private void loadText() {
        Properties.instantiate();
        this.lbl_date.setText(Properties.getProperty("lbl_date"));
        this.lbl_event_name.setText(Properties.getProperty("lbl_event_name"));
        this.lbl_venue.setText(Properties.getProperty("lbl_venue"));
    }
    
    private void refreshStatus() {
        Integer maleCount = 0,
                femaleCount = 0;
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
