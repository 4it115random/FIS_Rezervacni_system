/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Database.db;
import implementation.GlobalLoggedUser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javafx.stage.Stage;
import main.main;

/**
 * FXML Controller class
 *
 * @author Jan
 */
public class LoginController implements Initializable {
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.conn = db.getConnection();
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    
    public void Login ( ActionEvent event ) throws SQLException, IOException, NoSuchAlgorithmException  {
        
        invalidLoginLbl.setVisible(false);
        boolean loginSuccess = false;
        
        PreparedStatement getUser = conn.prepareStatement("SELECT osoba_id,username,password FROM osoba");
        ResultSet result = getUser.executeQuery();
            
        //Zkontrolujem, zda jsou prihlasovaci udaje v db
        while ( result.next() ) {
            if ( result.getString("username").equals(usernameLogin.getText()) && result.getString("password").equals(hashPasswd(passwordLogin)) ) {
                //Zobrazeni alertu s uspesnym prihlasenim
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Úspěšné přihlášení");
                alert.setHeaderText("Přihlášení proběhlo v pořádku");
                alert.setContentText("Jste přihlášen jako: " + usernameLogin.getText());
                alert.showAndWait();
                
                //ulozeni do globalnich promennych
                GlobalLoggedUser.userID = result.getInt("osoba_id");
                GlobalLoggedUser.userUsername = result.getString("username");
                
                //zmena na okno kde uz je uzivatel prihlasen
                Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/LoggedWindow.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
                
                loginSuccess = true;
                break;
            }
        }
        
        //Pokud byly zadany chybne udaje
        if ( loginSuccess == false) {
            invalidLoginLbl.setVisible(true);
        }
              
    }
    
    /**
     * Funkce zahešuje heslo, které získá z PasswordFieldu 
     * 
     * @param passwordLogin
     * @return passHash
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.io.UnsupportedEncodingException
     */
    public String hashPasswd(TextField passwordLogin) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String passwd = passwordLogin.getText();
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] passBytes = passwd.getBytes();
        byte[] passHash = sha256.digest(passBytes);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < passHash.length; i++) {
            sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hashString = sb.toString();
        return hashString;
    }
    
    public void SignUp ( ActionEvent event ) throws SQLException, IOException {
                 Parent loggedRoot;
                loggedRoot = FXMLLoader.load(getClass().getResource("/GUI/signup.fxml"));
                Scene loggedScene = new Scene(loggedRoot, 800, 480);
                Stage currentStage = (Stage) ( (Node) event.getSource() ).getScene().getWindow();
                currentStage.setScene(loggedScene);
                currentStage.show();
    }
}