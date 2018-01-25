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
    @FXML
    private Label noMoneyLb1;
    
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
            
        PreparedStatement r = conn.prepareStatement("SELECT cenova_zona_id FROM ma WHERE udalost_id LIKE ?");
        r.setInt(1, GlobalLoggedUser.eventID);        
        ResultSet result1 = r.executeQuery();  
        // Vsetko skryt
        dospeliLbl.setVisible(false);
        detiLbl.setVisible(false);
        studentiLbl.setVisible(false);
        dospeliLbl1.setVisible(false);
        detiLbl1.setVisible(false);
        studentiLbl1.setVisible(false);
        dospeliPoc.setVisible(false);
        detiPoc.setVisible(false);
        studentiPoc.setVisible(false);
        
        while ( result1.next() ) {        

                PreparedStatement r2 = conn.prepareStatement("SELECT price,name FROM cenova_zona WHERE cenova_zona_id LIKE ?");
                r2.setInt(1, result1.getInt("cenova_zona_id"));        
                ResultSet result2 = r2.executeQuery();
                
                while ( result2.next() ) {
                if ( result2.getString("name").equals(dospeliLbl1.getText()) )
                {
                    dospeliLbl.setVisible(true);
                    dospeliLbl1.setVisible(true);
                    dospeliPoc.setVisible(true);
                    dospeliLbl.setText(result2.getString("price"));
                }
                if ( result2.getString("name").equals(detiLbl1.getText()) )
                {
                    detiLbl.setVisible(true);
                    detiLbl1.setVisible(true);
                    detiPoc.setVisible(true);
                    detiLbl.setText(result2.getString("price"));
                }
                if ( result2.getString("name").equals(studentiLbl1.getText()) )
                {
                    studentiLbl.setVisible(true);
                    studentiLbl1.setVisible(true);
                    studentiPoc.setVisible(true);
                    studentiLbl.setText(result2.getString("price"));
                }
                }
            }
            
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
        noMoneyLb1.setVisible(false);      

        PreparedStatement getCapacity = conn.prepareStatement("SELECT available_seats FROM udalost WHERE udalost_id LIKE ?");
        getCapacity.setInt(1, GlobalLoggedUser.eventID);
        ResultSet result = getCapacity.executeQuery();
 
        while ( result.next() ) {
            int kapacitaPredstav = result.getInt("available_seats");
            int i = 0, zostatok = 0;
            if ( kapacitaPredstav > 0 ) {
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
                        //zistenie zostatku       
                        PreparedStatement getMoney = conn.prepareStatement("SELECT money FROM osoba WHERE osoba_id LIKE ?");
                        getMoney.setInt(1, GlobalLoggedUser.userID);
                        ResultSet r2 = getMoney.executeQuery();
 
                        while ( r2.next() ) {
                        zostatok = r2.getInt("money");    
                        }
                        int suma = stringToInt(dospeliLbl.getText())*dospeliListky + stringToInt(detiLbl.getText())*detiListky + stringToInt(studentiLbl.getText())*studentListky;
                        if (zostatok < suma )
                        {
                            noMoneyLb1.setVisible(true);
                            break;
                        }
                        
        // Priradime osobu k predstaveniu podla poctu nakupenych listkov        
        PreparedStatement listekID = conn.prepareStatement("SELECT * FROM koupene_listky WHERE osoba_id LIKE ?");
        listekID.setInt(1, GlobalLoggedUser.userID);
        ResultSet r1 = listekID.executeQuery();
        // nastavime listek_id na spravnu hodnotu
        while (r1.next()) { i++; }
        // Dospele listky
        for (int j= 0; j<dospeliListky;j++)
        {
            PreparedStatement insertTicket = conn.prepareStatement("INSERT INTO koupene_listky (listek_id,osoba_id,udalost_id,price,poznamka) VALUES (?,?,?,?,?)");
            insertTicket.setInt(1, ++i);
            insertTicket.setInt(2, GlobalLoggedUser.userID);
            insertTicket.setInt(3, GlobalLoggedUser.eventID);
            insertTicket.setInt(4, stringToInt(dospeliLbl.getText()) );
            insertTicket.setString(5, "");
            insertTicket.executeUpdate();
        }
        // Deti listky
        for (int k= 0; k<detiListky;k++)
        {
            PreparedStatement insertTicket = conn.prepareStatement("INSERT INTO koupene_listky (listek_id,osoba_id,udalost_id,price,poznamka) VALUES (?,?,?,?,?)");
            insertTicket.setInt(1, ++i);
            insertTicket.setInt(2, GlobalLoggedUser.userID);
            insertTicket.setInt(3, GlobalLoggedUser.eventID);
            insertTicket.setInt(4, stringToInt(detiLbl.getText()) );
            insertTicket.setString(5, "");
            insertTicket.executeUpdate();
        }
        // Studenti listky
        for (int l= 0; l<studentListky;l++)
        {
            PreparedStatement insertTicket = conn.prepareStatement("INSERT INTO koupene_listky (listek_id,osoba_id,udalost_id,price,poznamka) VALUES (?,?,?,?,?)");
            insertTicket.setInt(1, ++i);
            insertTicket.setInt(2, GlobalLoggedUser.userID);
            insertTicket.setInt(3, GlobalLoggedUser.eventID);
            insertTicket.setInt(4, stringToInt(studentiLbl.getText()) );
            insertTicket.setString(5, "");
            insertTicket.executeUpdate();
        }
                    
        // Odoberieme pocet volnych listkov na predstavenie 
        PreparedStatement znizenieCapacity = conn.prepareStatement("UPDATE udalost SET available_seats = ? WHERE udalost_id = ?");                    
        znizenieCapacity.setInt(1, kapacitaPredstav - listkyCelkovo);
        znizenieCapacity.setInt(2, GlobalLoggedUser.eventID);
        znizenieCapacity.executeUpdate();
                    
        //Zobrazeni alertu s uspesnym zakoupenim listku
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Úspěšné objednání");
        alert.setHeaderText("Lístky jste si úspěšně objednal.");
        alert.setContentText("Celková suma na zaplacení je: " + suma + "Kč.");
       
        // odpocet sumy
        PreparedStatement poplatok = conn.prepareStatement("UPDATE osoba SET money = ? WHERE osoba_id = ?");                    
        poplatok.setInt(1, zostatok - suma);
        poplatok.setInt(2, GlobalLoggedUser.userID);
        poplatok.executeUpdate();
        
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
