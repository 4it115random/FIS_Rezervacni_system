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
public class Predstavenie {
    
    private Integer udalost_id;
    private StringProperty name;
    private Date datum;
    private Integer available_seats;
    private Integer ticket_price;
    private Integer zarizeni_zarizeni_id;
    private Integer restriction_id;
    
    public Predstavenie(String name, Date datum)
    {
    //    this.udalost_id = udalost_id;
        this.name = new SimpleStringProperty(name);
        this.datum = datum;
    }
    
    public String getName(){
        return name.get();
    }
    public Integer getID(){
        return udalost_id;
    }
    /*
    public void setName(String name){
        name.set(name);
    }
*/
    
    public Date getDatum(){
        return datum;
    }
}

