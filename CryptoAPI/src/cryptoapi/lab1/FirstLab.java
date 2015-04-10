/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi.lab1;

import java.security.Provider;
import java.security.Security;

/**
 *
 * @author mchukDM
 */
public class FirstLab {

    public static void showProviders1() {
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider + "\ninfo: " + provider.getInfo());
            int i = 0;

//            for (String key : provider.stringPropertyNames()) {
//                System.out.println("\tKey:  " + key + "\t" + provider.getProperty(key));
//            }
            for (Provider.Service service : provider.getServices()) {
                i++;

                System.out.println(i + "  Algorithm: " +service.getAlgorithm()+"\tType "+service.getType());
            }
            System.out.println("\n\n");
        }
    }
}
