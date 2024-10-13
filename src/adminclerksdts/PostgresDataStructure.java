/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class PostgresDataStructure {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Soetsang@144156"; // Replace with your actual password

    public static void main(String[] args) {
        try {
            // Connect to the database
            try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

                // Get metadata about the database
                DatabaseMetaData meta = conn.getMetaData();

                // Create a mapping for SQL data types to PostgreSQL types
                Map<Integer, String> typeMapping = new HashMap<>();
                typeMapping.put(Types.INTEGER, "INTEGER");
                typeMapping.put(Types.VARCHAR, "VARCHAR(500)");  // Change VARCHAR to VARCHAR(500)
                typeMapping.put(Types.DATE, "DATE");
                typeMapping.put(Types.TIMESTAMP, "TIMESTAMP");
                typeMapping.put(Types.BINARY, "BYTEA");
                typeMapping.put(Types.BOOLEAN, "BOOLEAN");
                typeMapping.put(Types.TIME, "TIME");

                // Get a list of all the tables in the database
                try (ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                    while (tables.next()) {
                        // Get the name of the table
                        String tableName = tables.getString("TABLE_NAME");

                        // Get the list of fields in the table
                        try (ResultSet fields = meta.getColumns(null, null, tableName, "%")) {

                            // Build the "CREATE TABLE" statement for the table
                            StringBuilder createTableSQL = new StringBuilder("CREATE TABLE " + tableName + " (");
                            while (fields.next()) {
                                // Get the name and data type of the field
                                String fieldName = fields.getString("COLUMN_NAME");
                                int fieldType = fields.getInt("DATA_TYPE");

                                // Map the SQL type to a PostgreSQL type
                                String postgresType = typeMapping.get(fieldType);
                                if (postgresType == null) {
                                    postgresType = "OTHER";  // Default to OTHER if the type isn't mapped
                                }

                                // Special handling for certain fields:
                                if (fieldName.equals("profile_photo")) {
                                    postgresType = "BYTEA";  // Binary data
                                } else if (fieldName.equals("has_image") || 
                                           fieldName.equals("package_completed") || 
                                           fieldName.equals("completion_status") || 
                                           fieldName.equals("previous_experience")) {
                                    postgresType = "BOOLEAN";  // Boolean fields
                                } else if (fieldName.equals("session_start_time") || 
                                           fieldName.equals("session_end_time")) {
                                    postgresType = "TIME";  // Time fields
                                }

                                // Append the field to the "CREATE TABLE" statement
                                createTableSQL.append(fieldName).append(" ").append(postgresType).append(", ");
                            }

                            // Remove the final comma and close the parentheses
                            createTableSQL.setLength(createTableSQL.length() - 2);
                            createTableSQL.append(");");

                            // Print the "CREATE TABLE" statement
                            System.out.println(createTableSQL.toString());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the error message if there's a SQL exception
        }
    }
}
