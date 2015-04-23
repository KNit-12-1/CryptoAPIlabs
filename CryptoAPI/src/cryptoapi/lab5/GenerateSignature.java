/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi.lab5;

/**
 *
 * @author mchukDM
 */
import java.io.*;
import java.security.*;

/**
 *
 * Класс используемый создания электронной цифровой подписи
 */
class GenerateSignature {

    //сохранение байтового массива в файл
    /**
     *
     * @param info данные для записи в файл
     * @param filename название файла
     */
    private static void saveToFile(byte[] info,
            String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(info);
            fos.close();
        } catch (Exception e) {
            System.err.println(
                    "Caught exception "
                    + e.toString()
            );
        }
    }// saveToFile ()

    protected static void generate() {
        try {
            /* Генерация ключей */
            KeyPairGenerator keyGen
                    = KeyPairGenerator.getInstance("DSA", "SUN");
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
            FileInputStream fis = new FileInputStream("data.txt");
            try (BufferedInputStream bufin = new BufferedInputStream(fis)) {
                byte[] buffer = new byte[1024];
                int len;
                while (bufin.available() != 0) {
                    len = bufin.read(buffer);
                    dsa.update(buffer, 0, len);
                }
            }
            /* Генерация подписи */
            byte[] realSig = dsa.sign();
            /* Сохранение подписи в файл "signature" */
            saveToFile(realSig, "signature.sig");
            /* Сохранение открытого ключа в файл "pubkey" */
            byte[] key = pub.getEncoded();
            saveToFile(key, "pubkey.key");
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IOException | SignatureException e) {
            System.err.println(
                    "Caught exception " + e.toString()
            );
        }
    }// main()
}// class GenerateSignature 

