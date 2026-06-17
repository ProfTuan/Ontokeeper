/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package bioportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mac
 */
public class NCBODatabaseController {
    
    private static String dbTableLabels = 
            "CREATE TABLE IF NOT EXISTS employee";
    
    private Connection connection;
    
    private NCBODatabaseController() {
        try {
            String jdbc_string = "jdbc:h2:mem:ncbo";
            
            connection = DriverManager.getConnection(jdbc_string);
            
            
        } catch (SQLException ex) {
            System.getLogger(NCBODatabaseController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
    }
    
    public static NCBODatabaseController getInstance() {
        return NCBODatabaseControllerHolder.INSTANCE;
    }
    
    private void createDatabase(){
        
    }
    
    private static class NCBODatabaseControllerHolder {

        private static final NCBODatabaseController INSTANCE = new NCBODatabaseController();
    }
    
    public void closeDatabase(){
        try {
            connection.close();
        } catch (SQLException ex) {
            System.getLogger(NCBODatabaseController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public static void main(String[] args) throws SQLException {
        
        String jdbcURL = "jdbc:h2:mem:test";
 
        Connection connection = DriverManager.getConnection(jdbcURL);
 
        System.out.println("Connected to H2 in-memory database.");
 
        String sql = "Create table students (ID int primary key, name varchar(50))";
         
        Statement statement = connection.createStatement();
         
        statement.execute(sql);
         
        System.out.println("Created table students.");
         
        sql = "Insert into students (ID, name) values (1, 'Nam Ha Minh')";
         
        int rows = statement.executeUpdate(sql);
         
        if (rows > 0) {
            System.out.println("Inserted a new row.");
        }
 
        connection.close();
        
    }
}
