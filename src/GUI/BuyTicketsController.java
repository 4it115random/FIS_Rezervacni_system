/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import implementation.GlobalLoggedUser;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    private int kupujuciID = 1;
    private int udalostID = 1;
    private String nazovPredstavenia = "Predstavenie 1";
    
    @FXML
    private TextField dospeliPoc;
    @FXML
    private TextField detiPoc;
    @FXML
    private TextField studentiPoc;
    @FXML
    private Label dospeliLbl;
    @FXML
    private Label detiLbl;
    @FXML
    private Label studentiLbl; 
    @FXML
    private Text dospeliLbl1;
    @FXML
    private Text detiLbl1;
    @FXML
    private Text studentiLbl1;
    @FXML
    private Label notFilledLbl;
    @FXML
    private Label invalidAmountLbl;

    
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
            /*
        PreparedStatement r = conn.prepareStatement("SELECT cenova_zona_id FROM ma WHERE udalost_id LIKE ?");
        r.setInt(1, GlobalLoggedUser.eventID);        
        ResultSet result1 = r.executeQuery();       
        int czID = result1.getInt("cenova_zona_id");
        
        
        
        while ( result1.next() ) {        

                PreparedStatement r2 = conn.prepareStatement("SELECT price,name FROM cenova_zona WHERE cenova_zona_id LIKE ?");
                r2.setInt(1, czID);        
                ResultSet result2 = r2.executeQuery();
                
                while ( result2.next() ) {
                if ( result2.getString("name").equals(dospeliLbl1.getText()) )
                {
                    dospeliLbl.setText(result2.getString("price"));
                }
                if ( result2.getString("name").equals(detiLbl1.getText()) )
                {
                    detiLbl.setText(result2.getString("price"));
                }
                if ( result2.getString("name").equals(studentiLbl1.getText()) )
                {
                    studentiLbl.setText(result2.getString("price"));
                }
                }
            }
            */
    }
    
    
    
    public void Return (ActionEvent event ) throws IOException
    {
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/EventDetails.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
    
    public void Pay ( ActionEvent event ) throws SQLException, IOException
    {
            notFilledLbl.setVisible(false);
            invalidAmountLbl.setVisible(false);            
            

                 PreparedStatement getCapacity = conn.prepareStatement("SELECT * FROM udalost");

                 ResultSet result = getCapacity.executeQuery();
 
                while ( result.next() ) {
                    int kapacitaPredstav = result.getInt("available_seats");
                if ( kapacitaPredstav > 0 ) {
                    PreparedStatement znizenieCapacity = conn.prepareStatement("UPDATE udalost SET available_seats = ? WHERE name = ?");
                    
                    // Overime ze mnozstvi je vyplneno a pocet chcenych listkov je mensi ako pocet predavanych
                    int dospeliListky = stringToInt(dospeliPoc.getText());
                    int detiListky = stringToInt(detiPoc.getText());
                    int studentListky = stringToInt(studentiPoc.getText());
                    int listkyCelkovo = dospeliListky + detiListky + studentListky;
                    if ( (dospeliListky <= 0 || dospeliListky > kapacitaPredstav) &&
                         (detiListky <= 0 || detiListky > kapacitaPredstav) &&
                         (studentListky <= 0 || studentListky > kapacitaPredstav)
                            )
                    {
                    //Zobrazeni alertu o chybnom mnozstve kupujucich listkov
                    invalidAmountLbl.setVisible(true);
                    break;
                    }
                    else {
                        int suma = stringToInt(dospeliLbl.getText())*dospeliListky + stringToInt(detiLbl.getText())*detiListky + stringToInt(studentiLbl.getText())*studentListky;
                    // Priradime osobu k predstaveniu podla poctu nakupenych listkov
 //                   for (int i= 0; i<listkyCelkovo;i++)
 //                   {
                    PreparedStatement insertTicket = conn.prepareStatement("INSERT INTO rezervace (udalost_id,osoba_id) VALUES (?,?)");
                    insertTicket.setInt(1, udalostID);
                    insertTicket.setInt(2, kupujuciID);
                    insertTicket.executeUpdate();
 //                   }
                        
                    // Odoberieme pocet volnych listkov na predstavenie              
                    znizenieCapacity.setInt(1, kapacitaPredstav - listkyCelkovo);
                    znizenieCapacity.setString(2, nazovPredstavenia);
                    znizenieCapacity.executeUpdate();
                    
                    //Zobrazeni alertu s uspesnym zakoupenim listku
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Úspěšné objednání");
                    alert.setHeaderText("Lístky jste si úspěšně objednal.");
                    alert.setContentText("Celková suma na zaplacení je: " + suma + "Kč. Uhraďte ji prosím u pokladny.");
                    alert.showAndWait();
                    
                    // Zmena na okno s predstaveniami
                    Parent loggedRoot;
                    loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/LoggedWindow.fxml"));
                    Scene loggedScene = new Scene(loggedRoot, 800, 480);
                    Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                    currentStage.setScene(loggedScene);
                    currentStage.show();                        
                    }
           }    
        }
             
    }
    
    
    private static int stringToInt(String s) {
    try {
        return Integer.valueOf(s);
    } catch (NumberFormatException e) {
        return 0;
    }
}
}
