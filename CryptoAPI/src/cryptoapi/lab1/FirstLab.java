package cryptoapi.lab1;

import java.security.Provider;
import java.security.Security;

/**
 *
 * @author mchukDM
 */
public class FirstLab {

    public static void startLab1() {
        showProviders();
    }

    private static void showProviders() {
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider name:" + provider.getName()
                    + "\nProvider version: " + provider.getVersion()
                    + "\nProvider info: " + provider.getInfo() + "\n");

            int i = 0;
            System.out.println("Algorithms:");
            for (Provider.Service service : provider.getServices()) {
                i++;

                System.out.println(i + " Name: " + service.getAlgorithm() + "\tType " + service.getType());
            }
            System.out.println("\n\n");
        }
    }
}
