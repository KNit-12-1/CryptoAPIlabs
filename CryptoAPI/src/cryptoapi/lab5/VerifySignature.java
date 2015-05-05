package cryptoapi.lab5;

/**
 *
 * @author mchukDM
 */
/* Верификация DSA-подписи */
import java.io.*;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;/*класс является классом механизма, разработанным,
                                чтобы обеспечить функциональность криптографического 
                                алгоритма цифровой подписи*/
import java.security.SignatureException;

class VerifySignature {

    /**
     * чтение из файла в байтовый массив
     *
     * @param fileName имя файла из которого производится чтение
     * @return масив байтов
     */
    private static byte[] readFromFile(String fileName) {
        byte[] info;
        try (FileInputStream fis = new FileInputStream(fileName)) {
            info = new byte[fis.available()];
            fis.read(info);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
            info = new byte[0];
        }
        return (info);
    }

    /**
     * проверка подлинности подписи и данных
     */
    protected static void verify() {
        try {
            /* Получение encoded public key из файла "pubkey" */
            byte[] encKey = readFromFile(System.getProperty("user.dir") + "/lab5_files/pubkey.key");

            /* Создание спецификации ключа */
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);

            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            /* Чтение подписи из файла "signature" */
            byte[] sigToVerify = readFromFile(System.getProperty("user.dir") + "/lab5_files/signature.sig");
            /* Создание объекта класса Signature и инициализация с помощью открытого ключа    */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);
            /* Чтение данных из файла "data" и вызов метода update() */
            FileInputStream datafis = new FileInputStream(System.getProperty("user.dir") + "/lab5_files/data.txt");
            try (BufferedInputStream bufin = new BufferedInputStream(datafis)) {
                byte[] buffer = new byte[1024];
                int len;
                while (bufin.available() != 0) {
                    len = bufin.read(buffer);
                    sig.update(buffer, 0, len);
                }
            }
            /* Верификация */
            System.out.println("message--> Проверка подписи");
            boolean verifies = sig.verify(sigToVerify);
            System.out.println("message--> Подпись подтверждена: " + verifies);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | InvalidKeyException | IOException | SignatureException e) {
            System.err.println("Caught exception " + e.toString()
            );
        }
    }
}
