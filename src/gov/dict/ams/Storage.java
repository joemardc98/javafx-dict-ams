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
package gov.dict.ams;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Label;

/**
 *
 * @author Joemar
 */
public class Storage {
    private static JFXButton btn_generate;
    private static JFXButton btn_delete;
    private static JFXButton btn_add;
    private static JFXCheckBox chkbx_select_all;
    private static Label lbl_total_selected;

    public static JFXButton getBtn_generate() {
        return btn_generate;
    }

    public static void setBtn_generate(JFXButton btn_generate_) {
        btn_generate = btn_generate_;
    }

    public static JFXButton getBtn_delete() {
        return btn_delete;
    }

    public static void setBtn_delete(JFXButton btn_delete_) {
        btn_delete = btn_delete_;
    }

    public static JFXButton getBtn_add() {
        return btn_add;
    }

    public static void setBtn_add(JFXButton btn_add_) {
        btn_add = btn_add_;
    }

    public static JFXCheckBox getChkbx_select_all() {
        return chkbx_select_all;
    }

    public static void setChkbx_select_all(JFXCheckBox chkbx_select_all_) {
        chkbx_select_all = chkbx_select_all_;
    }

    public static Label getLbl_total_selected() {
        return lbl_total_selected;
    }

    public static void setLbl_total_selected(Label lbl_total_selected_) {
        lbl_total_selected = lbl_total_selected_;
    }
    
}
