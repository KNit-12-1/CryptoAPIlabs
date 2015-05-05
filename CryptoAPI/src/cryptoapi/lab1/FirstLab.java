package cryptoapi.lab1;

import java.security.Provider;//Представляет собой "провайдер" для API 
                              //безопасности Java, где провайдер реализует 
                              //некоторые или все части Java Security.
import java.security.Security;//Обеспечивает классы и интерфейсы для платформы безопасности.

/**
 *
 * @author mchukDM
 */
public class FirstLab {

    public static void startLab1() {
        showProviders();
    }
    
    private static void showProviders() {
        //получение информации о провайдерах
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider name:" + provider.getName()
                    + "\nProvider version: " + provider.getVersion()
                    + "\nProvider info: " + provider.getInfo() + "\n");

            int i = 0;
            System.out.println("Algorithms:");
            for (Provider.Service service : provider.getServices()) {
                i++;
                //получение списка алгоритмов конкретного провайдера
                System.out.println(i + " Name: " + service.getAlgorithm() + "\tType " + service.getType());
            }
            System.out.println("\n\n");
        }
    }
}
