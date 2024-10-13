/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
  private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Soetsang@144156"; // Replace with your actual password
*/
package adminclerksdts;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresDataContents {

    // It's better to load credentials from environment variables or an external config file.
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Soetsang@144156"; // Consider loading password from env variable

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Get database metadata
            DatabaseMetaData meta = conn.getMetaData();

            // Get a list of all tables in the database
            try (ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    // Get the table name
                    String tableName = tables.getString("TABLE_NAME");

                    // Query to retrieve the table's contents
                    try (Statement statement = conn.createStatement();
                         ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {

                        // Get metadata for the result set
                        ResultSetMetaData metaData = resultSet.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        // Process each row in the result set
                        while (resultSet.next()) {
                            StringBuilder insertStatement = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

                            for (int i = 1; i <= columnCount; i++) {
                                String value = resultSet.getString(i);

                                if (resultSet.wasNull()) {
                                    insertStatement.append("NULL");
                                } else {
                                    // Escape single quotes in the value
                                    value = value.replace("'", "''");
                                    insertStatement.append("'").append(value).append("'");
                                }

                                if (i < columnCount) {
                                    insertStatement.append(", ");
                                }
                            }

                            insertStatement.append(");");
                            System.out.println(insertStatement.toString());
                        }

                        System.out.println(); // Separate INSERT blocks for different tables
                    }
                }
            }
        } catch (SQLException e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }
}
