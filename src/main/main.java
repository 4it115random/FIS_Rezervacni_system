/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Application;
import javafx.scene.Scene;
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
        
        BorderPane Login = new BorderPane();
        
        Scene loginScene = new Scene(Login, 1200, 900);
        
        primaryStage.setTitle("Adventura");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
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
