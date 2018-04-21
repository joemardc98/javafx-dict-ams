
package gov.dict.ams.ui.home;

import gov.dict.ams.ApplicationForm;
import java.awt.GraphicsEnvironment;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

    @Override
    protected void setup() {
        this.setComboBoxes();
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
    
}
