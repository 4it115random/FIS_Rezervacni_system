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
    private TextField pocetTF;
    @FXML
    private Label invalidData;
    @FXML
    private ComboBox zarizeni;
    @FXML
    private ComboBox price;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.conn = db.getConnection();
            initDevComboBox();
            initPriceCombobox();
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
    
    public void initDevComboBox() throws SQLException, IOException {
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
     * Priradenie cien predstavení do ComboBoxu
     * 
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    
    public void initPriceCombobox() throws SQLException, IOException {
        PreparedStatement getID = conn.prepareStatement("SELECT price FROM cenova_zona WHERE name='dospely'");
        ResultSet res = getID.executeQuery();
        
        ObservableList<Integer> prices = FXCollections.observableArrayList();
        while (res.next()) {
            int pr = res.getInt("price");
            prices.add(pr);
        }
        price.setItems(null);
        price.setItems(prices);
        
        /*ObservableList<String> prices = FXCollections.observableArrayList();
        while (res.next()) {
            String pr = Integer.toString(res.getInt("price"));
            System.out.println(pr);
            prices.add(pr);
        }
        price.setItems(null);
        price.setItems(prices);*/
        
        //String pr = Integer.toString(res.getInt("price"));
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
        java.sql.Date sqlStartDate = null;
        
        


        //kontrola vyplnenia casoveho udaju
        if (terminTF.getValue() != null)  {
            LocalDate localDate = terminTF.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            java.util.Date utilStartDate = Date.from(instant);
            sqlStartDate = new java.sql.Date(utilStartDate.getTime());
        } else {
            spatneUdaje = true;
        }
        
        
        
        Integer cena = (Integer) price.getSelectionModel().getSelectedItem();
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
             misto<=0 || cena == null ||
             pocetTF.getText().equals("") || pocet<=0 ||
             popisTF.getText().equals("") || terminTF.getValue() == null) 
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
                switch (cena) {
                    case 199:
                        {
                            // vlozenie cenovej zony listku a jeho odvodenych zon do db
                            
                            PreparedStatement insertAdult = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertAdult.setInt(1, 3);
                            insertAdult.setInt(2, i+1);
                            insertAdult.executeUpdate();
                            
                            PreparedStatement insertStudent = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertStudent.setInt(1, 2);
                            insertStudent.setInt(2, i+1);
                            insertStudent.executeUpdate();
                            
                            PreparedStatement insertChild= conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertChild.setInt(1, 1);
                            insertChild.setInt(2, i+1);
                            insertChild.executeUpdate();
                            
                            break;
                        }
                    case 599:
                        {   
                            PreparedStatement insertAdult = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertAdult.setInt(1, 6);
                            insertAdult.setInt(2, i+1);
                            insertAdult.executeUpdate();
                            
                            PreparedStatement insertStudent = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertStudent.setInt(1, 5);
                            insertStudent.setInt(2, i+1);
                            insertStudent.executeUpdate();
                            
                            PreparedStatement insertChild= conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertChild.setInt(1, 4);
                            insertChild.setInt(2, i+1);
                            insertChild.executeUpdate();
                            
                            break; 
                        }
                    case 999:
                        {
                            PreparedStatement insertAdult = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertAdult.setInt(1, 9);
                            insertAdult.setInt(2, i+1);
                            insertAdult.executeUpdate();
                            
                            PreparedStatement insertStudent = conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertStudent.setInt(1, 8);
                            insertStudent.setInt(2, i+1);
                            insertStudent.executeUpdate();
                            
                            PreparedStatement insertChild= conn.prepareStatement("INSERT INTO ma (cenova_zona_id,udalost_id) VALUES (?,?)");
                            insertChild.setInt(1, 7);
                            insertChild.setInt(2, i+1);
                            insertChild.executeUpdate();
                            
                            break;
                        }
                    default:
                        break;
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
