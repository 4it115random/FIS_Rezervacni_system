/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.main;

/**
 * FXML Controller class
 *
 * @author Marcel
 */
public class SignupController implements Initializable {
    private Connection conn;
    private main mn;
    
    @FXML
    private Label loginLbl;
    @FXML
    private Label signUpLbl;
    @FXML
    private Label invalidLoginLbl;
    @FXML
    private Label invalidUsernameLbl;
    @FXML
    private Label invalidEmailLbl;
    @FXML
    private Label invalidFillingLbl;
    @FXML
    private TextField usernameLogin;
    @FXML
    private TextField passwordLogin;
    @FXML
    private TextField usernameSignUp;
    @FXML
    private TextField nameSignUp;
    @FXML
    private TextField surnameSignUp;
    @FXML
    private TextField passwordSignUp;
    @FXML
    private TextField emailSignUp;
    @FXML
    private ImageView backgroundImg;

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
    
    public void SignUp ( ActionEvent event ) throws SQLException, IOException {
        boolean invalidSignUp = false;
        invalidUsernameLbl.setVisible(false);
        invalidEmailLbl.setVisible(false);
        invalidFillingLbl.setVisible(false);
        
        //Zkontrolujem, zda jsou vyplneny vsechny pole
        if ( usernameSignUp.getText().equals("") || 
             passwordSignUp.getText().equals("") ||
             nameSignUp.getText().equals("") ||
             surnameSignUp.getText().equals("") ||
             emailSignUp.getText().equals("") ) {
            
            invalidFillingLbl.setVisible(true);
        } else {
            PreparedStatement getUsername = conn.prepareStatement("SELECT username,email FROM osoba");
            ResultSet result = getUsername.executeQuery();
            
            //Zkontrolujem, zda uz v db neni stejny username nebo email
            while ( result.next() ) {
                if ( result.getString("username").equals(usernameSignUp.getText()) ) {
                    invalidUsernameLbl.setVisible(true);
                    invalidSignUp = true;
                    break;
                }
                if ( result.getString("email").equals(emailSignUp.getText()) ) {
                    invalidEmailLbl.setVisible(true);
                    invalidSignUp = true;
                    break;
                }
            }
            
            //Vse je v poradku a vlozime noveho uzivatele do databaze
            if ( invalidSignUp == false ) {
                
                //insertnem do db
                PreparedStatement insertUser = conn.prepareStatement("INSERT INTO osoba (name,surname,username,password,email,rights) VALUES (?,?,?,?,?,0)");
                insertUser.setString(1, nameSignUp.getText());
                insertUser.setString(2, surnameSignUp.getText());
                insertUser.setString(3, usernameSignUp.getText());
                insertUser.setString(4, passwordSignUp.getText());    //!!ukladani hesla v Plaintextu je sice nejhorsi, ale hashovani dodelam pozdeji
                insertUser.setString(5, emailSignUp.getText());
                insertUser.executeUpdate();
                
                //skryjeme alerty
                invalidUsernameLbl.setVisible(false);
                invalidEmailLbl.setVisible(false);
                invalidFillingLbl.setVisible(false);
                
                //Zobrazeni alertu s uspechem
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Úspešná registrace");
                alert.setHeaderText("Registrace proběhla v pořádku, nyní se můžete přihlásit.");
                alert.showAndWait();
                
                
                //smazem veci z fieldu
                usernameSignUp.clear();
                nameSignUp.clear();
                surnameSignUp.clear();
                passwordSignUp.clear();
                emailSignUp.clear();
                
                //zmena na okno pro prihlaseni
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/Login.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 700, 400);
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
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/Login.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 700, 400);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
}
