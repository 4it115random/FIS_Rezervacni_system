/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Jan
 */
public class LoginController implements Initializable {
    
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
        // TODO
    }    
    
    public void Login ( ActionEvent event ) {
        
    }
    
    public void SignUp ( ActionEvent event ) {
        
    }
    
}
