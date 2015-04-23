package cryptoapi.lab2;

import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author mchukDM
 */
public class SecretKeyGenerator {

    private String mPassword = null;
    private final static int SALT_LEN = 8;
    private byte[] mInitVec = null;
    private byte[] mSalt = null;
    private final int KEYLEN_BITS = 128; // see notes below where this is used.
    private final int ITERATIONS = 65536;
    private final int MAX_FILE_BUF = 1024;
    private Cipher mEcipher = null;

    public SecretKeyGenerator(String pass) {
        mPassword = pass;
        generateKey();
    }

    /**
     * создает соль соли в байтах и генерирует байт соль, используя
     * SecureRandom(). Секретный ключ шифрования создается вместе с
     * инициализацией vectory
     */
    private final void generateKey() {
        try {
            SecretKeyFactory factory = null;
            SecretKey secretKey = null;
            mSalt = new byte[SALT_LEN];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(mSalt);
            logMessage("Сгенерированая соль сгенерирована:" + Hex.encodeHexString(mSalt));
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            /* Формирование ключа, из входного пароля и соли.
             *
             */
            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);
            secretKey = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            /* Создание объекта шифрования
             */
            mEcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mEcipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = mEcipher.getParameters();

            // получение вектора инициализации
            mInitVec = params.getParameterSpec(IvParameterSpec.class).getIV();

            logMessage("Вектор инициализации :" + Hex.encodeHexString(mInitVec));
            logMessage("Был создан ключ шифрования на основе пароля.");
            logMessage("Генерация ключа из пароля завершена.");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                InvalidKeyException | InvalidParameterSpecException ex) {
            Logger.getLogger(SecretKeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void logMessage(String msg) {
        System.out.println("message--> " + msg);
    }
}
