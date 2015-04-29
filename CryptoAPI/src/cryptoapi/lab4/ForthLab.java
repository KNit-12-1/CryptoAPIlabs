/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi.lab4;

import java.util.Scanner;


/**
 *
 * @author mchukDM
 */
public class ForthLab {

    public static void startLab4() {
        
            MAC mc = new MAC(System.getProperty("user.dir") + "/lab4_files/plaintext.txt");
            mc.createMAC();
            
            Scanner scan = new Scanner(System.in);
            System.out.print("message--> Нажмите 1 для продолжения: ");
            int input = scan.nextInt();
            
            mc.verifyMAC();

        
    }
}
