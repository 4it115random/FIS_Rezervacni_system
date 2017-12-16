/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author john
 */
public class main extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        getConnection();
        
        
        BorderPane Login = new BorderPane();
        
        Scene loginScene = new Scene(Login, 1200, 900);
        
        primaryStage.setTitle("Adventura");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Connection getConnection() throws Exception {
        Connection conn = null;
        
        try {
            String driver = "com.mysql.jdbc.Driver";
            String dbUrl = "jdbc:mysql://127.0.0.1:3306/";
            String databaseName = "ticket_portal";
            String dbUsername = "root";
            String dbPassword = "";
            
            Class.forName(driver);
            conn = DriverManager.getConnection(dbUrl + databaseName, dbUsername, dbPassword);
            
            System.out.println("Connected");
            
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
    
    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
}
