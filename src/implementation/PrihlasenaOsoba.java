/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

/**
 *
 * @author Jan
 */
public class PrihlasenaOsoba {
    private String username;
    private String name;
    private String surname;
    private String email;
    private int rights;
    
    public PrihlasenaOsoba(){};
            
    public PrihlasenaOsoba(String username, String name, String surname, String email, int rights) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.rights = rights;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setRights(int rights) {
        this.rights = rights;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSurname() {
        return this.surname;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public int getRights() {
        return this.rights;
    }
    
}
