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

import java.io.InputStream;
import org.afterschoolcreatives.polaris.java.sql.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jhon Melvin
 */
public class Context {

    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    //--------------------------------------------------------------------------
    // STATIC Context contents. (STATELESS)
    //--------------------------------------------------------------------------
    // Application Specific Constant (These values are visible across)
    public final static String APPLICATION_NAME = "Attendee Management System";
    public final static int VERSION_CODE = 0;
    public final static String VERSION_NAME = "1.0.2-prototype-system";

    /**
     * Get Resources from the storage directory.
     *
     * @param name
     * @return
     */
    public static InputStream getResourceStream(String name) {
        return Context.class.getClass().getResourceAsStream("/storage/" + name);
    }

    //--------------------------------------------------------------------------
    // (STATEFUL) Controls the instance of the application
    //--------------------------------------------------------------------------
    // Instance Holder (THE ONLY STATIC VAR IN STATEFUL)
    private static volatile Context instance;

    /**
     * Override Clone Method.
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        throw new CloneNotSupportedException("Not Allowed!");
    }

    /**
     * Application Instance Gateway.
     *
     * @return project context manager.
     */
    public static Context app() {
        Context localInstance = Context.instance;
        if (localInstance == null) {
            synchronized (Context.class) {
                localInstance = Context.instance;
                if (localInstance == null) {
                    Context.instance = localInstance = new Context();
                }
            }
        }
        return localInstance;
    }

    /**
     * Only Called once in the entire application life time.
     */
    private Context() {
        logger.trace("Application Instance Created");
        // set values
        this.host = "127.0.0.1";
        this.databaseName = "ams";
        this.databasePort = "3306";
        this.databaseUser = "root";
        this.databasePass = "root";
        // initialization
        this.createConnectionFactory();
        logger.trace("Application Initialization Completed");
    }

    //--------------------------------------------------------------------------
    private String host;
    private String databaseName;
    private String databasePort;
    private String databaseUser;
    private String databasePass;
    //--------------------------------------------------------------------------
    private ConnectionFactory connectionFactory;
    //--------------------------------------------------------------------------

    /**
     * Creates The Connection Factory.
     */
    private void createConnectionFactory() {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setConnectionDriver(ConnectionFactory.Driver.MariaDB);
        this.connectionFactory.setDatabaseName(this.databaseName);
        this.connectionFactory.setHost(this.host);
        this.connectionFactory.setPort(this.databasePort);
        this.connectionFactory.setUsername(this.databaseUser);
        this.connectionFactory.setPassword(this.databasePass);
        logger.trace("Connection Factory Created");
    }

    /**
     * Get this application's connection factory.
     *
     * @return
     */
    public ConnectionFactory db() {
        logger.trace("Database Gateway Called");
        return this.connectionFactory;
    }

}
