package collectivedatabase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Andrei-Madalin on 11/02/14.
 * Logger levels: INFO    - informative messages (ex: Server has been started, Incoming messages, outgoing messages etc...)
 *                WARNING - any message that might warn us of potential problems (ex: when a user tried to log in with wrong credentials)
 *                SEVERE  - exceptions
 */
public class CollectiveDatabase {
    public static Logger DBLogger;
    public static Connection CONNECTION;
    public static Statement STATEMENT;

    public static void setLogger(){
        try {
            DBLogger = Logger.getLogger("DBLog");
            String filename = "database/database-" + System.currentTimeMillis() + ".log";
            FileHandler DBLoggerFileHandler = new FileHandler("logs/" + filename);
            DBLogger.addHandler(DBLoggerFileHandler);
            DBLoggerFileHandler.setFormatter(new SimpleFormatter());
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void setConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");

            BufferedReader br = new BufferedReader(new FileReader("resources/database.txt"));

            String url = br.readLine();
            String username = br.readLine();
            String password = br.readLine();

            br.close();
            CONNECTION = DriverManager.getConnection(url, username, password);
            STATEMENT = CONNECTION.createStatement();
        } catch (ClassNotFoundException e) {
            DBLogger.log(Level.INFO, "Where is your MySQL JDBC Driver?");
            DBLogger.log(Level.SEVERE, e.getMessage(), e);
        } catch (SQLException e) {
            DBLogger.log(Level.INFO, "Connection failed!");
            DBLogger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            DBLogger.log(Level.SEVERE, e.getMessage(), e);
        }

        if(checkConnection())
            DBLogger.log(Level.INFO, "Successfully connected");
        else
            DBLogger.log(Level.INFO, "Filed to connect");
    }

    static boolean checkConnection(){
        return CONNECTION != null;
    }
}
