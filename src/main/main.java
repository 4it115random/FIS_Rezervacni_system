/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Database.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author john
 */
public class main extends Application {

    private Stage stage;
    private Connection conn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        conn = db.getConnection();
        
        Parent loginRoot;
        loginRoot = FXMLLoader.load(getClass().getResource("/GUI/Login.fxml"));
        Scene loginScene = new Scene(loginRoot, 800, 480);
        
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Systém pro rezervaci a nákup lístků");
        primaryStage.setResizable(false);
        primaryStage.show();
        
        
    }
    
    public Connection getConnection () {
        return this.conn;
    }
    
    public static void main(String[] args) {
        launch(args);
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