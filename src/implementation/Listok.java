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
    private Integer listek_id;
    private StringProperty nazev;
    private Date datum;
    private Integer cena;
    private StringProperty poznamka;
    
    public Listok(Integer id,String nazev, Date datum, int cena, String poznamka){
        this.listek_id = id;
        this.nazev = new SimpleStringProperty(nazev);
        this.datum = datum;
        this.cena = cena;
        this.poznamka = new SimpleStringProperty(poznamka);
    }
    
    public void setNote(String nName){
        this.poznamka = new SimpleStringProperty(nName);
    }
    
    public Integer getID () {
        return listek_id;
    }
    
    public String getNazev () {
        return nazev.get();
    }
    
    public Date getDatum() {
        return datum;
    }
    
    public Integer getCena() {
        return cena;
    }
    
    public String getPoznamka() {
        return poznamka.get();
    }
}
