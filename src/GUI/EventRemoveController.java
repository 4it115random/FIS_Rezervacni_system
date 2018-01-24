/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import implementation.Predstavenie;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.main;

/**
 * FXML Controller class
 *
 * @author Marcel
 */
public class EventRemoveController implements Initializable {
    private Connection conn;
    private main mn;
    private ObservableList<Predstavenie> data;
    
    @FXML
    private TableView<Predstavenie> eventsTable;
    @FXML
    private TableColumn<Predstavenie, String> nazevColumn;
    @FXML
    private TableColumn<Predstavenie, Date> timeColumn;
    @FXML
    private TextField nameTF;
    @FXML
    private Label invalidName;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.conn = db.getConnection();
            loadEventsFromDatabase();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
        public void loadEventsFromDatabase(  ) throws SQLException, Exception{
            this.conn = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM udalost");
            while (rs.next()){
                data.add(new Predstavenie(rs.getString(2),rs.getDate(3)));
            }
            
            //Nastavenie hodnot do mojej tabulky
            nazevColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
            
            eventsTable.setItems(null);
            eventsTable.setItems(data);
        }
    
        public void loadEventsFromDatabase( ActionEvent event  ) throws SQLException, Exception{
            this.conn = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM udalost");
            while (rs.next()){
                data.add(new Predstavenie(rs.getString(2),rs.getDate(3)));
            }
            
            //Nastavenie hodnot do mojej tabulky
            nazevColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
            
            eventsTable.setItems(null);
            eventsTable.setItems(data);
        }
      
        public void eventRemove ( ActionEvent event ) throws SQLException 
        {
        boolean spatneUdaje = false;
        invalidName.setVisible(false);
        
        //Zkontrolujem, zda je vyplneny nazev
        if ( nameTF.getText().equals("")) 
        {            
            invalidName.setVisible(true);
        } else {
            PreparedStatement getUdalost = conn.prepareStatement("SELECT name FROM udalost");
            ResultSet result = getUdalost.executeQuery();
            
            //Zkontrolujem, zda v db je dane predstavenie
            while ( result.next() ) {
                if ( result.getString("name").equals(nameTF.getText()) ) {                    
                    spatneUdaje = false;
                    break;
                }
                else
                {
                    spatneUdaje = true;
                    invalidName.setVisible(true);
                }
            }
            
            //Vse je v poradku a odebereme predstaveni z databaze
            if ( spatneUdaje == false ) {
                
                //insertnem do db
                PreparedStatement removeUser = conn.prepareStatement("DELETE FROM udalost WHERE name = ?");
                removeUser.setString(1, nameTF.getText());
                
                //Zobrazeni alertu o potvrzeni zmazani                
                ButtonType anoButt = new ButtonType("Smazat");
                ButtonType nieButt = new ButtonType("Storno");
                Alert alert = new Alert(AlertType.NONE, "Opravdu chcete představení zmazat?", anoButt, nieButt);
                alert.setTitle("Zmazání představení");
                alert.showAndWait().ifPresent(response -> {
                    if (response == anoButt) {
                        try {
                            removeUser.executeUpdate();
                            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                            a.setTitle("Úspěch");
                            a.setHeaderText("Zmazání proběhlo v pořádku");
                            a.setContentText("Pro aktualizaci představení klikněte na Aktualizovat.");
                            a.showAndWait();
                        } catch (SQLException ex) {
                            Logger.getLogger(EventRemoveController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });               
            }
            
        }
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
