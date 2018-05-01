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

import java.io.File;
import java.io.IOException;
import org.afterschoolcreatives.polaris.java.util.PolarisProperties;

/**
 *
 * @author Joemar
 */
public class Properties {
    
    private static PolarisProperties prop = new PolarisProperties();
    public Properties() {
        try {
            prop.read(new File("session.prop"));
        } catch (IOException e) {
            // ignore
        }
    }
    
    public static Properties instantiate() {
        return new Properties();
    }
    
    public static String getProperty(String propName) {
        return prop.getProperty(propName, "");
    }
    
    public static void setProperty(String propName, String value) {
        prop.setProperty(propName, value.trim());
    }
    
    public static boolean saveProperty() {
        try {
            prop.write(new File("session.prop"));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
