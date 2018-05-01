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
import java.awt.Desktop;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.afterschoolcreatives.polaris.javafx.scene.control.simpletable.SimpleTable;

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
        this.btn_view_pdf.setOnMouseClicked((MouseEvent value) -> {
            this.openDirFolder("src/storage/AMS - FAQs.pdf");
        });
        this.btn_back.setOnMouseClicked((MouseEvent value) -> {
            this.changeRoot(new SystemHome().load());
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
    
}
