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
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.sql.RowSetEvent;
import main.main;

/**
 * FXML Controller class
 *
 * @author Jan
 */
public class LoggedWindowController implements Initializable {

    private Connection conn;
    private main mn;
    private ObservableList<Predstavenie> data;
        
    
    @FXML
    private Label ZsLbl;
    @FXML
    private Label WcLbl;
    @FXML
    private MenuButton menuBar;
    @FXML
    private MenuItem menuItem2;  
    @FXML
    private MenuItem menuItem3;
    @FXML
    private TableView<Predstavenie> eventsTable;
    @FXML
    private TableColumn<Predstavenie, String> nazevColumn;
    @FXML
    private TableColumn<Predstavenie, Date> timeColumn;
    
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
    
    public void LogOut (ActionEvent event ) throws IOException
    {       
        MenuItem mItem = (MenuItem) event.getSource();
        
        //vymazani global. prom.
        GlobalLoggedUser.removeUserData();
        
        //zmena na okno pro prihlaseni
        Parent loggedRoot;
        loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/Login.fxml"));
        Scene loggedScene = new Scene(loggedRoot, 800, 480);
        Stage currentStage = (Stage) mItem.getParentPopup().getOwnerWindow();
        currentStage.setScene(loggedScene);
        currentStage.show();
    }
        
        
    public void loadEventsFromDatabase(  ) throws SQLException, Exception{
        
        PreparedStatement getUser = conn.prepareStatement("SELECT osoba_id,username,password,money FROM osoba WHERE osoba_id LIKE ?");
        getUser.setInt(1, GlobalLoggedUser.userID);
        ResultSet result = getUser.executeQuery();
        
        result.next();
        
        //Uprava lablu na username a zustatek penez
        WcLbl.setText("Vítejte: " + GlobalLoggedUser.userUsername);
        ZsLbl.setText(Integer.toString(result.getInt("money")) + " Kč,-");
        
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
    
    public void addMoney (ActionEvent event ) throws IOException
    {   
        //zmena na okno pro pridani penez
        Parent loggedRoot;
        loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/addMoney.fxml"));
        Scene loggedScene = new Scene(loggedRoot, 800, 480);       
        Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        currentStage.setScene(loggedScene);
        currentStage.show();
    }
        
    public void showDetails (ActionEvent event ) throws IOException
    {   
        //TADY MI PROSIM DO TOHO ULOZ ID TE UDALOSTI KTERA JE VYBRANA
        GlobalLoggedUser.eventID = 0;
        
        //zmena na okno pro prihlaseni
        Parent loggedRoot;
        loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/EventDetails.fxml"));
        Scene loggedScene = new Scene(loggedRoot, 800, 480);       
        Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
        currentStage.setScene(loggedScene);
        currentStage.show();
    }
    
    public void eventDetails ( TableColumn.CellEditEvent event )
    {
   //     Predstavenie predst = eventsTable.getSelectionModel().getSelectedItem();
   //     int predstID = predst.getID();
        String nazev = event.getOldValue().toString();


        //Testovaci zobrazeni indexu
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Index");
        alert.setHeaderText("Predstavenie na ktore si klikol ma nazev: " + nazev );                
        alert.showAndWait();
    }
 
    public void editTickets ( ActionEvent event ) throws IOException
    {
        //zmena na okno pro pridani
        Parent loggedRoot;
        loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/EditTickets.fxml"));
        Scene loggedScene = new Scene(loggedRoot, 800, 480);  
        Stage currentStage = (Stage) menuBar.getScene().getWindow();
        currentStage.setScene(loggedScene);
        currentStage.show();
    } 
    
    public void eventAdd ( ActionEvent event ) throws IOException
    {
        //zmena na okno pro pridani
        Parent loggedRoot;
        loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/EventAdd.fxml"));
        Scene loggedScene = new Scene(loggedRoot, 800, 480);  
        Stage currentStage = (Stage) menuBar.getScene().getWindow();
        currentStage.setScene(loggedScene);
        currentStage.show();
    } 
    
    public void eventRemove ( ActionEvent event ) throws IOException
    {
        //zmena na okno pro pridani
        Parent loggedRoot;
        loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/EventRemove.fxml"));
        Scene loggedScene = new Scene(loggedRoot, 800, 480);  
        Stage currentStage = (Stage) menuBar.getScene().getWindow();
        currentStage.setScene(loggedScene);
        currentStage.show();
    }  
    
}
