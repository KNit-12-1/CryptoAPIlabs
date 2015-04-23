package cryptoapi.lab3;

import java.io.File;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author mchukDM
 */
public class ThirdLab {

    public static void startLab3() {

        // create the input.txt file in the current directory before continuing
        File input = new File("input.txt");
        File eoutput = new File("encrypted.aes");
        File doutput = new File("decrypted.txt");
        String iv;
        String salt;

        System.out.println("ФАЗА_1");
        SymmetricEncryptor en = new SymmetricEncryptor("test_password");

        /*
         * setup encryption cipher using password. print out iv and salt
         */
        en.setupEncrypt();
        iv = Hex.encodeHexString(en.getInitVec()).toUpperCase();
        salt = Hex.encodeHexString(en.getSalt()).toUpperCase();

        /*
         * write out encrypted file
         */
        en.writeEncryptedFile(input, eoutput);
        System.out.println("message--> Зашифрованные данные записаны в файл: " + eoutput.getName());

        System.out.println("\nФАЗА_2");
        /*
         * decrypt file
         */
        SymmetricEncryptor dc = new SymmetricEncryptor("test_password");
        dc.setupDecrypt(iv, salt);

        /*
         * write out decrypted file
         */
        dc.readEncryptedFile(eoutput, doutput);
        System.out.println("message--> Расшифрованные данные записаны в файл:" + doutput.getName());
    }
}
