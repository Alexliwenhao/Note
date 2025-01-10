package org.elasticsearch.xpack.jdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class ApiTest {

    public static void main(String[] args) throws Exception {
        String address = "jdbc:es://http://10.0.3.5:32092/";
        Properties connectionProperties = new Properties();
        try {
            Connection connection = DriverManager.getConnection(address, connectionProperties);
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT status,project_id FROM sprint");
            while (results.next()) {
                System.out.println(results.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
