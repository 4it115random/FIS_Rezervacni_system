/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author Jan
 */
public class db {
    
    public static Connection getConnection() throws Exception {
        Connection conn = null;
        
        //getting db connection
        try {
            String driver = "com.mysql.jdbc.Driver";
            String dbUrl = "jdbc:mysql://127.0.0.1:3306/";
            String databaseName = "ticket_portal";
            String dbUsername = "root";
            String dbPassword = "";
            
            Class.forName(driver);
            conn = DriverManager.getConnection(dbUrl + databaseName, dbUsername, dbPassword);
            
            return conn;
            
        } catch( ClassNotFoundException | NullPointerException | SQLException ex ) {
            Platform.runLater(() -> {
                System.out.println(ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("DB");
                alert.setHeaderText("Chyba pripojeni");
                
                alert.showAndWait();
                System.out.println(ex.getMessage());
            });
        }
        
        return null;
    }
    
}
