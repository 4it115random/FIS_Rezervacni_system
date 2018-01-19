/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import main.main;

/**
 * FXML Controller class
 *
 * @author Narek
 */
public class BuyTicketsController implements Initializable {

    private Connection conn;
    private main mn;
    
    @FXML
    private Label detailLbl;
    @FXML
    private Label nazevLbl;
    @FXML
    private Label adresaLbl;
    @FXML
    private Label terminLbl;
    @FXML
    private Label popisLbl;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
