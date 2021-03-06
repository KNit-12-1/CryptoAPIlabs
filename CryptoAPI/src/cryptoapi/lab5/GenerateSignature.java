package cryptoapi.lab5;

/**
 *
 * @author mchukDM
 */
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyPair;/*класс является простым держателем для 
                            пары ключей (открытый ключ и закрытый ключ)*/
import java.security.KeyPairGenerator;/*класс является классом механизма, 
                                        используемым, чтобы генерировать 
                                        пар открытых и закрытых ключей.*/
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;/*класс является классом механизма, разработанным,
                                чтобы обеспечить функциональность криптографического 
                                алгоритма цифровой подписи*/
import java.security.SignatureException;

/**
 * Класс используемый создания электронной цифровой подписи
 */
class GenerateSignature {

    /**
     * сохранение байтового массива в файл
     *
     * @param info данные для записи в файл
     * @param filename название файла
     */
    private static void saveToFile(byte[] info, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(info);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString()
            );
        }
    }// saveToFile ()

    protected static void generate() {
        try {
            KeyPairGenerator keyGen;
            keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey priv = pair.getPrivate();
            PublicKey pub = pair.getPublic();
            /* Создание объекта класса Signature */
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            /* Инициализация частным ключом */
            dsa.initSign(priv);
            /* Чтение данных из файла "data.txt". Вызов метода update() */
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/lab5_files/data.txt");
            try (BufferedInputStream bufin = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[1024];
                int len;
                while (bufin.available() != 0) {
                    len = bufin.read(buffer);
                    dsa.update(buffer, 0, len);
                }
            }
            /* Генерация подписи */
            System.out.println("message--> Генерация подписи");
            byte[] realSig = dsa.sign();
            /* Сохранение подписи в файл "signature.sig" */
            saveToFile(realSig, System.getProperty("user.dir") + "/lab5_files/signature.sig");
            /* Сохранение открытого ключа в файл "pubkey.key" */
            byte[] key = pub.getEncoded();
            saveToFile(key, System.getProperty("user.dir") + "/lab5_files/pubkey.key");
            System.out.println("message--> Подпись сгенерирована");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IOException | SignatureException e) {
            System.err.println(
                    "Caught exception " + e.toString()
            );
        }
    }
}
