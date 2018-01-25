/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import GUI.LoginController;
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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jan
 */
public class AddMoneyController implements Initializable {

    private Connection conn;
    
    @FXML
    private Label errorLbl;
    @FXML
    private TextField moneyFld;
    
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
    
    public void addMoney( ActionEvent event  ) throws SQLException, Exception{
        String money = moneyFld.getText();
        if (isNumeric(money)) {
            
            PreparedStatement getUser = conn.prepareStatement("SELECT osoba_id,money FROM osoba WHERE osoba_id LIKE ?");
            getUser.setInt(1, GlobalLoggedUser.userID);
            ResultSet result = getUser.executeQuery();
            result.next();
            
            int newValue = result.getInt("money") + Integer.parseInt(money);
            PreparedStatement updateUser = conn.prepareStatement("UPDATE osoba SET money = ? WHERE osoba_id = ?");
            updateUser.setInt(1, newValue);
            updateUser.setInt(2, GlobalLoggedUser.userID);
            updateUser.executeUpdate();

            Parent loggedRoot;
            loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/LoggedWindow.fxml"));
            Scene loggedScene = new Scene(loggedRoot, 800, 480);
            Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
            currentStage.setScene(loggedScene);
            currentStage.show();
            
            
        } else {
            errorLbl.setVisible(true);
            moneyFld.setText("");
        }
    }
    
    
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
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
}
