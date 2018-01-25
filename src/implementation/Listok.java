/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import java.sql.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Marcel
 */
public class Listok {
    private final StringProperty nazev;
    private final Date datum;
    private final Integer cena;
    private final StringProperty poznamka;
    
    public Listok(String nazev, Date datum, int cena, String poznamka){
        this.nazev = new SimpleStringProperty(nazev);
        this.datum = datum;
        this.cena = cena;
        this.poznamka = new SimpleStringProperty(poznamka);
    }
    
    public void setNote(String nName){
        poznamka.set(nName);
    }
    
}
