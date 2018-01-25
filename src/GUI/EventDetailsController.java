/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import implementation.GlobalLoggedUser;
import implementation.Predstavenie;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
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
 * @author Marcel
 */
public class EventDetailsController implements Initializable {

    private Connection conn;
    private main mn;
    private ObservableList<Predstavenie> data;
    
    @FXML
    private Label nazevLbl2;
    @FXML
    private Label adresaLbl2;
    @FXML
    private Label terminLbl2;
    @FXML
    private Label popisLbl2;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.conn = db.getConnection();
            changeLabels();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } 
    
    public void changeLabels() throws SQLException, IOException {
        PreparedStatement r = conn.prepareStatement("SELECT name,datum,popis,zarizeni_id FROM udalost WHERE udalost_id LIKE ?");
        r.setInt(1, GlobalLoggedUser.eventID);
        ResultSet result1 = r.executeQuery();       

        while ( result1.next() ) {
        nazevLbl2.setText(result1.getString("name"));
        terminLbl2.setText(result1.getDate("datum").toString());
        popisLbl2.setText(result1.getString("popis"));
        
        PreparedStatement r2 = conn.prepareStatement("SELECT name FROM zarizeni WHERE zarizeni_id LIKE ?");
        r2.setInt(1, result1.getInt("zarizeni_id"));
        ResultSet result2 = r2.executeQuery();
                while ( result2.next() ) {
                    adresaLbl2.setText(result2.getString("name"));
                }
        }

    }
    
    public void BuyTickets ( ActionEvent event ) throws SQLException, IOException {
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/BuyTickets.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
    
    public void Return (ActionEvent event ) throws IOException
    {
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/LoggedWindow.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
}
