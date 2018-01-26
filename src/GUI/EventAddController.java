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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
    private DatePicker terminTF;
    @FXML
    private TextField popisTF;
    @FXML
    private TextField cenaTF;
    @FXML
    private TextField pocetTF;
    @FXML
    private Label invalidData;
    @FXML
    private ComboBox zarizeni;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.conn = db.getConnection();
            initComboBox();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    /**
     * Priradenie názvov predstavení do ComboBoxu
     * 
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    
    public void initComboBox() throws SQLException, IOException {
        PreparedStatement getID = conn.prepareStatement("SELECT name FROM zarizeni");
        ResultSet res = getID.executeQuery();
        ObservableList<String> names = FXCollections.observableArrayList();
        while (res.next()) {
            String name = res.getString("name");
            names.add(name);
        }
        zarizeni.setItems(null);
        zarizeni.setItems(names);
    }
    
    /**
     * 
     * Metoda eventAdd pridáva do databáze údaje z políčok a robí kontroly políčok
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    public void eventAdd ( ActionEvent event ) throws SQLException, IOException {
        boolean spatneUdaje = true;
        invalidData.setVisible(false);
        int i = 0;
        LocalDate localDate = terminTF.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        java.util.Date utilStartDate = Date.from(instant);
        java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());    
        int cena = stringToInt(cenaTF.getText());
        int pocet = stringToInt(pocetTF.getText());
        String popis = popisTF.getText();
        int misto = 0;
        
        //Zkontrolujem, ci nazev daneho mista existuje
        PreparedStatement getID = conn.prepareStatement("SELECT * FROM zarizeni");
        ResultSet res = getID.executeQuery();
        //Vybratie aktualne zvoleneho miesta z ComboBoxu
        String selectedPlace = (String) zarizeni.getSelectionModel().getSelectedItem();
                       
            while ( res.next() ) {
                if ( res.getString("name").equals(selectedPlace) ) {                    
                    misto = res.getInt("zarizeni_id");
                    spatneUdaje = false;
                    break;                    
                }
            }
        
        
        //Zkontrolujem, zda jsou spravne vyplneny vsechny pole
        if ( nazevTF.getText().equals("") ||
             misto<=0 ||
             cenaTF.getText().equals("") || cena<=0 || cena > 999 ||
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
                PreparedStatement insertUser = conn.prepareStatement("INSERT INTO udalost (udalost_id,name,datum,available_seats,zarizeni_id,popis) VALUES (?,?,?,?,?,?)");
                insertUser.setInt(1, i+1);
                insertUser.setString(2, nazevTF.getText());
                insertUser.setDate(3, sqlStartDate);
                insertUser.setInt(4, pocet);
                // misto ma byt cez join
                insertUser.setInt(5, misto); 
                insertUser.setString(6, popis);
                insertUser.executeUpdate();
                
                //pridanie cenovej zony na zaklade ceny
                if (cena <= 199) {
                    PreparedStatement insertMa = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                    insertMa.setInt(1, 3);
                    insertMa.setInt(2, i+1);
                    insertMa.executeUpdate();
                } else if (cena <= 599) {
                    PreparedStatement insertMa = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                    insertMa.setInt(1, 6);
                    insertMa.setInt(2, i+1);
                    insertMa.executeUpdate();
                } else if (cena <= 999) {
                    PreparedStatement insertMa = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                    insertMa.setInt(1, 9);
                    insertMa.setInt(2, i+1);
                    insertMa.executeUpdate();
                } 
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
    
    /**
     * 
     * Metoda Return nastavuje novú scénu do Stage
     * @param event
     * @throws IOException 
     */
    
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
    /**
     * Metóda stringToInt konvertuje String na Integer
     * 
     * @param s
     * @return hodnota Integeru
     */
    private static int stringToInt(String s) {
    try {
        return Integer.valueOf(s);
    } catch (NumberFormatException e) {
        return 0;
    }
}
    
}
