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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.main;

/**
 * FXML Controller class
 *
 * @author Marcel
 */





public class EventAddController implements Initializable {
    private Connection conn;
    private main mn;
    
    @FXML
    private TextField nazevTF;
    @FXML
    private TextField adresaTF;
    @FXML
    private DatePicker terminTF;
    @FXML
    private TextField popisTF;
    @FXML
    private TextField cenaTF;
    @FXML
    private TextField pocetTF;
    @FXML
    private Label invalidData;
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
    
    public void eventAdd ( ActionEvent event ) throws SQLException, IOException {
        boolean spatneUdaje = false;
        invalidData.setVisible(false);
        int i = 0;
        LocalDate localDate = terminTF.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date utilStartDate = Date.from(instant);
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());    
        int cena = stringToInt(cenaTF.getText());
        int pocet = stringToInt(pocetTF.getText());
        int misto = stringToInt(adresaTF.getText());
        
        //Zkontrolujem, zda jsou spravne vyplneny vsechny pole
        if ( nazevTF.getText().equals("") ||
             adresaTF.getText().equals("") || misto<=0 ||
             cenaTF.getText().equals("") || cena<=0 ||
             pocetTF.getText().equals("") || pocet<=0 ||
             popisTF.getText().equals("")) 
        {            
            invalidData.setVisible(true);
        } else {
            PreparedStatement getUsername = conn.prepareStatement("SELECT name FROM udalost");
            ResultSet result = getUsername.executeQuery();
            
            //Zkontrolujem, zda uz v db neni stejny predstaveni
            while ( result.next() ) {
                if ( result.getString("name").equals(nazevTF.getText()) ) {
                    invalidData.setVisible(true);
                    spatneUdaje = true;
                    break;
                }
                i++;
            }
            
            //Vse je v poradku a vlozime nove predstaveni do databaze
            if ( spatneUdaje == false ) {
                
                //insertnem do db
                PreparedStatement insertUser = conn.prepareStatement("INSERT INTO udalost (udalost_id,name,datum,available_seats,ticket_price,zarizeni_zarizeni_id,restriction) VALUES (?,?,?,?,?,?,0)");
                insertUser.setInt(1, i+1);
                insertUser.setString(2, nazevTF.getText());
                insertUser.setDate(3, sqlStartDate);
                insertUser.setInt(4, pocet);
                insertUser.setInt(5, cena);
                insertUser.setInt(6, misto);                
                insertUser.executeUpdate();
                
                
                //Zobrazeni alertu s uspechem
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Úspešné přidání události");
                alert.setHeaderText("Přidání události proběhlo v pořádku.");
                alert.showAndWait();
                
                
                //zmena na okno udalosti
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/LoggedWindow.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
               
            }
            
        }
    }
    
    public void Return (ActionEvent event ) throws IOException
    {
                //zmena na okno pro prihlaseni
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/LoggedWindow.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
    
    private static int stringToInt(String s) {
    try {
        return Integer.valueOf(s);
    } catch (NumberFormatException e) {
        return 0;
    }
}
    
}
