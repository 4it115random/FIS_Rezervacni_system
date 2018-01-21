/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.main;

/**
 * FXML Controller class
 *
 * @author Narek
 */
public class BuyTicketsController implements Initializable {

    private Connection conn;
    private main mn;
    
    @FXML
    private Label detailLbl;
    @FXML
    private Label nazevLbl;
    @FXML
    private Label adresaLbl;
    @FXML
    private Label terminLbl;
    @FXML
    private Label popisLbl;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.conn = db.getConnection();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
      
    
    public void Return (ActionEvent event ) throws IOException
    {
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/EventDetails.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 700, 400);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
}
