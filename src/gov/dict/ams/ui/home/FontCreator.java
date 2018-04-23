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

import com.itextpdf.text.Font;

/**
 *
 * @author Joemar
 */
public class FontCreator {
    private com.itextpdf.text.Font createdFont;
    
    public static Font createFont(int fontStyle, Integer fontSize) {
        FontCreator fc = new FontCreator(fontStyle, fontSize);
        return fc.getCreatedFont();
    }
    
    public FontCreator(int fontStyle, Integer fontSize) {
        switch(fontStyle) {
            case 0:
                this.createdFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.COURIER, fontSize, com.itextpdf.text.Font.BOLD);
                break;
            case 1:
                this.createdFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, fontSize, com.itextpdf.text.Font.BOLD);
                break;
            case 2:
                this.createdFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, fontSize, com.itextpdf.text.Font.BOLD);
                break;
            case 3:
                this.createdFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.UNDEFINED, fontSize, com.itextpdf.text.Font.BOLD);
                break;
        }
    }

    public Font getCreatedFont() {
        return createdFont;
    }
    
}
