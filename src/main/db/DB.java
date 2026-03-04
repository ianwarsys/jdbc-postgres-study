package main.db;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    public static Connection conn = null;

    private DB() {
    }

    // Properties is a hash table structure, accessed through local files(.properties), which contains key-values pairs (Map) representing configurations in format: user=password, thus providing shortcuts when accessing database configuration details

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }
        return conn;

    }

    public static void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            }
        }

    }

    public static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("main/db.properties")) {
            Properties props = new Properties();
            props.load(fs);

            return props;

        } catch (IOException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


}
