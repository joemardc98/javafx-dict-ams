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
import gov.dict.ams.ApplicationForm;
import gov.dict.ams.Context;
import gov.dict.ams.models.AttendeeModel;
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
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.java.io.FileTool;
import org.afterschoolcreatives.polaris.javafx.scene.control.PolarisDialog;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableCell;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableRow;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTableView;

/**
 *
 * @author Joemar
 */
public class GenerateCertificate extends ApplicationForm {

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
    private JFXButton btn_proceed;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    private VBox tbl_attendees;
    
    @FXML
    private Label lbl_total_selected;
    
    @FXML
    private TextField txt_spacing;
    
    private ArrayList<AttendeeModel> selectedModels;
    private SimpleTable tableAttendee = new SimpleTable();
    @Override
    protected void setup() {
        this.txt_spacing.setText("10");
        
        for (int i = 5; i <= 100; i++) {
            this.cmb_font_size.getItems().add(i);
        }
        this.cmb_font_size.getSelectionModel().select(Integer.valueOf(22));
        
        this.cmb_font_style.getItems().clear();
        this.cmb_font_style.getItems().add("Courier");
        this.cmb_font_style.getItems().add("Helvetica");
        this.cmb_font_style.getItems().add("Times New Roman");
        this.cmb_font_style.getItems().add("Undefined");
        this.cmb_font_style.getSelectionModel().select(1);
        
        try {
            this.createTable();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateCertificate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.eventHandler();
    }
    
    private void eventHandler() {
        this.btn_cancel.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new SystemHome().load());
        });
        
        this.btn_proceed.setOnMouseClicked((MouseEvent value) -> {
            try {
                spaceBefore = Integer.valueOf(this.txt_spacing.getText()); 
                this.generateCert(value);
            } catch (NumberFormatException e) {
                this.showWarningMessage("Invalid Spacing", "Please provide an integer value for spacing before.");
            }
        });
        
    }
    
    private Integer spaceBefore = 10; 
    private void generateCert(MouseEvent value) {
        Thread generateCert = new Thread(()->{
            int ctrCount = 0;
            for(AttendeeModel eachModel: this.selectedModels) {
                try {
                    if (this.generateCertificate(eachModel)) {
                        ctrCount++;
                        if(this.selectedModels.size() == ctrCount) {
                            Platform.runLater(()->{
                                this.showInformationMessage("Successfully created.", "Certificate successfully created.");
                                changeRoot(new SystemHome().load());
                            });
                        }
                    } else {
                        Platform.runLater(()->{
                            this.showWarningMessage("Failed to Generate.","Failed to create certificate.");
                        });
                        break;
                    }
                } catch (DocumentException | IOException e) {
                    Platform.runLater(()->{
                        this.showWarningMessage("No Template Found","Please proceed to Extras then upload a template to proceed.");
                    });
                    break;
                }
                value.consume();
            }
        });
        generateCert.setDaemon(true);
        generateCert.start();
    }

    private static final com.itextpdf.text.Font font7Bold = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, 20, com.itextpdf.text.Font.BOLD);
    //---------------------------------------
    // CERT MAKER
    //---------------------------------------
    /**
     * Creates a certificate based on the template.
     *
     * @param name The name of the certificate holder.
     * @param corporation The name of the corporation.
     * @param content The content of the certificate.
     * @param control The name of the control number.
     * @return true or false if successfully created.
     * @throws com.itextpdf.text.DocumentException
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException if some of the required directories
     * or files is not existing.
     */
    private  boolean generateCertificate(AttendeeModel eachModel)
            throws DocumentException, FileNotFoundException, IOException {
        /**
         * Attempt to check the template directory.
         */
        if (!FileTool.checkFoldersQuietly("template")) {
            throw new FileNotFoundException("Template directory cannot be created.");
        }
        /**
         * Attempt to check the certificates directory.
         */
        if (!FileTool.checkFoldersQuietly("certificates")) {
            throw new FileNotFoundException("Certificates directory cannot be created.");
        }
        /**
         * Template File Path.
         */
        String templatePath = "template" + File.separator + "certification_template.pdf";
        File templateFile = new File(templatePath);

        /**
         * Check if the template file is not existing.
         */
        if (!templateFile.exists()) {
            throw new FileNotFoundException("Certificate Template not existing.");
        }
        
        PdfReader reader = null;
        try {
            reader = new PdfReader(templateFile.getAbsolutePath());
            Document document = new Document();
            document.setPageSize(reader.getPageSize(1));
            OutputStream out = new FileOutputStream("certificates" + File.separator  + eachModel.getId() + "_" + eachModel.getFullName(AttendeeModel.NameFormat.SURNAME_FIRST)  + ".pdf");
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            
            PdfImportedPage page = writer.getImportedPage(reader, 1); 
            
            cb.addTemplate(page, 0, 0);
            for (int i = 0; i < spaceBefore; i++) {
                cb.getPdfDocument().add(new Paragraph(" "));
            }
            
            Paragraph text = new Paragraph(50);
            text.setAlignment(Element.ALIGN_CENTER);
            Chunk chunk = new Chunk(eachModel.getFullName(AttendeeModel.NameFormat.NORMAL), FontCreator.createFont(this.cmb_font_style.getSelectionModel().getSelectedIndex(), this.cmb_font_size.getSelectionModel().getSelectedItem()));
            //chunk.setUnderline(0.1f, -2f);
            text.add(chunk);
            
            document.add(text); 

            document.close();
            writer.close();
            out.close();
            reader.close();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            }
        }
        return true;
    }
    
    private Integer maleCount = 0, femaleCount = 0;
    private void createTable() throws SQLException {
        this.maleCount = 0;
        this.femaleCount = 0;
        this.tableAttendee.getChildren().clear();
        List<AttendeeModel> content = AttendeeModel.listAllActive(null);
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
        this.lbl_total_no.setText(content.size() + "");
        
        this.lbl_total_selected.setText(this.selectedModels.size() + "");
        for(AttendeeModel eachModel : this.selectedModels) {
            this.createRow(eachModel);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tableAttendee);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(this.tbl_attendees);
    }
    
    private Image maleIcon = new Image(Context.getResourceStream("drawable/male.png"));
    private Image femaleIcon = new Image(Context.getResourceStream("drawable/female.png"));
    private String ROW = "ROW", CONTENT = "CONTENT";
    
    private void createRow(AttendeeModel content) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);
        
        AttendeeRowPreview itemRow = new AttendeeRowPreview();
        itemRow.setContent(content);
        
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

    public void setSelectedModels(ArrayList<AttendeeModel> selectedModels) {
        this.selectedModels = selectedModels;
    }
    
}
