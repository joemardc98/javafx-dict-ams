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
package gov.dict.ams.models;

import gov.dict.ams.Context;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.afterschoolcreatives.polaris.java.sql.ConnectionManager;
import org.afterschoolcreatives.polaris.java.sql.builder.SimpleQuery;
import org.afterschoolcreatives.polaris.java.sql.orm.PolarisRecord;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Column;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.FetchOnly;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.PrimaryKey;
import org.afterschoolcreatives.polaris.java.sql.orm.annotations.Table;

/**
 *
 * @author Jhon Melvin
 */
@Table(AttendeeModel.TABLE)
public class AttendeeModel extends PolarisRecord {

    //==========================================================================
    // Afterschool Creatives Polaris Record Content Standardization
    //==========================================================================
    // Sections
    // 01. Table Columns
    // 02. Model Fields
    // 03. Constructor (Initialize Default Values)
    // 04-A. Static Inner Classes
    // 04-B. Static Methods (Mostly Database Queries)
    // 04-C. Custom Methods (Non-Static Methods)
    // 05-A. Getters
    // 05-B. Setters
    //
    // To standardized the creation of PolarisRecord classes. a complete child
    // class must contain all of the following sections mentioned above.
    //
    // All Child classes must contain this standardization notice for reference.
    //
    // Standardization Code: 001 - 03/31/2018
    //==========================================================================
    // 01. Table Columns
    //==========================================================================
    public final static String TABLE = "attendee";
    public final static String ID = "id";
    public final static String FIRST_NAME = "first_name";
    public final static String MIDDLE_INITIAL = "middle_initial";
    public final static String LAST_NAME = "last_name";
    public final static String GENDER = "gender";
    public final static String MAIL = "email";
    public final static String ACTIVE = "active";
    //==========================================================================
    // 02. Model Fields
    //==========================================================================
    @PrimaryKey
    @FetchOnly
    @Column(ID)
    private Integer Id;

    @Column(FIRST_NAME)
    private String firstName;

    @Column(MIDDLE_INITIAL)
    private String middleInitial;

    @Column(LAST_NAME)
    private String lastName;

    @Column(GENDER)
    private String gender;

    @Column(MAIL)
    private String mail;

    @Column(ACTIVE)
    private Integer active;

    //==========================================================================
    // 03. Constructor (Initialize Default Values)
    //==========================================================================
    public AttendeeModel() {
//        this.Id = null
        this.firstName = "";
        this.middleInitial = "";
        this.lastName = "";
        this.gender = Gender.UNKNOWN;
        this.mail = "";
        this.active = 1;
    }

    //==========================================================================
    // 04-A. Static Inner Classes
    //==========================================================================
    public final static class Gender {

        public final static String MALE = "MALE";
        public final static String FEMALE = "FEMALE";
        public final static String UNKNOWN = "UNKNOWN";
        // (you can use this in combo box choices)
        public final static String[] LIST = new String[]{MALE, FEMALE};
    }

    //==========================================================================
    // 04-B. Static Class Methods
    //==========================================================================
    public static <T> List<T> listAllActive() throws SQLException {
        // Build Query
        SimpleQuery querySample = new SimpleQuery();
        querySample.addStatement("SELECT")
                .addStatement("*")
                .addStatement("FROM")
                .addStatement(TABLE)
                .addStatement("WHERE")
                .addStatement(ACTIVE)
                .addStatement("= 1");
        // Execute Query
        try (ConnectionManager con = Context.app().db().createConnectionManager()) {
            return new AttendeeModel().findMany(con, querySample);
        }
    }

    public static boolean insert(AttendeeModel model) throws SQLException {
        try (ConnectionManager con = Context.app().db().createConnectionManager()) {
            return model.insert(con);
        }
    }

    public static boolean update(AttendeeModel model) throws SQLException {
        try (ConnectionManager con = Context.app().db().createConnectionManager()) {
            return model.updateFull(con);
        }
    }

    public static boolean remove(AttendeeModel model) throws SQLException {
        ConnectionManager con = null;
        try {
            //------------------------------------------------------------------
            // open connection
            con = Context.app().db().createConnectionManager();
            //------------------------------------------------------------------
            model.setActive(0); // set deleted at with date now
            // execute query.
            return model.updateFull(con);
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    //==========================================================================
    // 04-C. Custom Methods
    //==========================================================================
    public String getFullName() {
        return this.getLastName() + ", "
                + this.getFirstName() + " "
                + (this.getMiddleInitial() ==null? "" : this.getMiddleInitial());
    }

    //==========================================================================
    // 05-A. Getters
    //==========================================================================
    public Integer getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getMail() {
        return mail;
    }

    public Integer getActive() {
        return active;
    }

    //==========================================================================
    // 05-B. Setters
    //==========================================================================
    public void setId(Integer Id) {
        this.Id = Id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

}
