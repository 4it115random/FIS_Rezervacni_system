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
public class GlobalLoggedUser {
    public static int userID = -1;
    public static String userName = "";
    public static String userSurname = "";
    public static String userUsername = "";
    
 
    public static void removeData () {
        GlobalLoggedUser.userID = -1;
        GlobalLoggedUser.userName = "";
        GlobalLoggedUser.userSurname = "";
        GlobalLoggedUser.userUsername = "";
    }
}
