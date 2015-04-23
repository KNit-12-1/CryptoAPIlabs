/*
 * Получить основные навыки работы с функциями хеширования, 
 *создания ключей и генерации случайных чисел 
 */
package cryptoapi.lab2;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
/**
 *
 * @author mchukDM
 */
public class SecondLab {

    public static void createPassword() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("RC2");
            //javax.crypto.KeyGenerator генерирует ключ для симметричного шифра
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            //создается вероятностный источник — объект класса SecureRandom
            kg.init(random);
            SecretKey key;
            key = kg.generateKey();//получение ключа для симметричного шифрования
            System.out.println("Alg-> " + key.getAlgorithm());
            System.out.println("Key-> " + key);
            //AlgorithmParameterSpec aps = new AlgorithmParameterSpec();
        } catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
            Logger.getLogger(SecondLab.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void createPassword2() {
        try {
            String pass = "sdfdsalaksdfhlksdfh";
            DESKeySpec ks = new DESKeySpec(pass.getBytes());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey key = skf.generateSecret(ks);
            System.out.println("Alg-> "+key.getAlgorithm());
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException ex) {
            Logger.getLogger(SecondLab.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
