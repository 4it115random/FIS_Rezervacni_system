/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import implementation.GlobalLoggedUser;
import implementation.Listok;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import main.main;

/**
 * FXML Controller class
 *
 * @author Marcel
 */
public class EditTicketsController implements Initializable {
        private Connection conn;
        private main mn;
        private ObservableList<Listok> data;
    
    @FXML
    private TableView<Listok> eventsTable;
    @FXML
    private TableColumn<Listok, String> nameColumn;
    @FXML
    private TableColumn<Listok, Date> dateColumn;
    @FXML
    private TableColumn<Listok, Integer> priceColumn;
    @FXML
    private TableColumn<Listok, String> noteColumn;
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
    
    public void loadEventsFromDatabase(  ) throws SQLException, Exception{
            this.conn = db.getConnection();
            data = FXCollections.observableArrayList();
            
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM koupene_listky");
            while (rs.next()){
                PreparedStatement getNazev = conn.prepareStatement("SELECT name,datum FROM udalost WHERE udalost_id LIKE ?");
                getNazev.setInt(1, rs.getInt("udalost_id"));
                ResultSet result = getNazev.executeQuery();
 
                while ( result.next() ) 
                {
                    data.add(new Listok(result.getString("name"),result.getDate("datum"),rs.getInt(4),rs.getString(5)));
                    eventsTable.setItems(data);
                }    
            }
            //Nastavenie hodnot do mojej tabulky
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("nazev"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("cena"));
            noteColumn.setCellValueFactory(new PropertyValueFactory<>("poznamka"));
            noteColumn.setOnEditCommit(
            (CellEditEvent<Listok, String> t) -> {
                ((Listok) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setNote(t.getNewValue());
            }); 
            
            
        }
    
        public void loadEventsFromDatabase( ActionEvent event  ) throws SQLException, Exception{
            this.conn = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM koupene_listky");
            while (rs.next()){
                PreparedStatement getNazev = conn.prepareStatement("SELECT name,datum FROM udalost WHERE udalost_id LIKE ?");
                getNazev.setInt(1, rs.getInt("udalost_id"));
                ResultSet result = getNazev.executeQuery();
 
                while ( result.next() ) 
                {
                    data.add(new Listok(result.getString("name"),result.getDate("datum"),rs.getInt(4),rs.getString(5)));
                }    
                
            }
            //Nastavenie hodnot do mojej tabulky
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("datum"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("ticket_price"));
            noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
            noteColumn.setOnEditCommit(
            (CellEditEvent<Listok, String> t) -> {
                ((Listok) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setNote(t.getNewValue());
            }); 
            
            eventsTable.setItems(null);
            eventsTable.setItems(data);
        }
    
        public void updateEdit(ActionEvent event)
        {                
                //Zobrazeni alertu s uspechem
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("pokus");
                alert.setHeaderText("Po editovani sa ma nahrat hodnota do databaze.");
                alert.showAndWait();
        }
}
