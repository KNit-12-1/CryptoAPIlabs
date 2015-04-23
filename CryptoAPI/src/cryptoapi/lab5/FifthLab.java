package cryptoapi.lab5;

import java.util.Scanner;

/**
 *
 * @author mchukDM
 */
public class FifthLab {

    public static void startLab5() {
        GenerateSignature.generate();
        
        
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        VerifySignature.verify();
    }
}
