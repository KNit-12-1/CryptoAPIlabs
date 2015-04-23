package cryptoapi.lab3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author mchukDM
 */
public class SymmetricEncryptor {

    String mPassword = null;
    public final static int SALT_LEN = 8;
    byte[] mInitVec = null;
    byte[] mSalt = null;
    Cipher mEcipher = null;
    Cipher mDecipher = null;
    private final int KEYLEN_BITS = 128; //длина ключа
    private final int ITERATIONS = 65536;// количество итераций
    private final int MAX_FILE_BUF = 1024;

    /**
     * Создается объект с парольной фразы от пользователя.
     *
     * @param password
     */
    public SymmetricEncryptor(String password) {
        mPassword = password;
    }

    /**
     * Возвращает соль для данного объекта
     *
     * @return сгенерированая соль
     */
    public byte[] getSalt() {
        return (mSalt);
    }

    /**
     * Возвращает вектор инициализации
     *
     * @return вектор инициализации
     */
    public byte[] getInitVec() {
        return mInitVec;
    }

    /**
     * Отображение собщений
     *
     * @param msg текст сообщения
     */
    private static void logMessage(String msg) {
        System.out.println("message--> " + msg);
    }

    /**
     * функция вызывается после создания первоначального объекта
     * SymmetricEncryptor. в функции создеатся соль, используя SecureRandom ().
     * Секретный ключ шифрования создается вместе с инициализации vectory.
     * Переменная mEcipher элемент создан, чтобы быть использованы по классу
     * позже, когда либо создание CipherOutputStream или шифрование буфера
     * должны быть записаны на диск.
     *
     */
    public void setupEncrypt() {
        try {
            SecretKeyFactory factory = null;
            SecretKey tmp = null;

            //создание соли
            mSalt = new byte[SALT_LEN];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(mSalt);
            logMessage("сгенерированая соль: " + Hex.encodeHexString(mSalt));

            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // Формирования ключа, с помощью пароля пароль и соли.
            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);
            tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // Создание объекта шифрования
            mEcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mEcipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = mEcipher.getParameters();

            // получение вектора инициализации
            mInitVec = params.getParameterSpec(IvParameterSpec.class).getIV();

            logMessage("вектор инициализации: " + Hex.encodeHexString(mInitVec));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException |
                NoSuchPaddingException | InvalidKeyException |
                InvalidParameterSpecException ex) {
            Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Функция дешифрования
     *
     * @param initvec вектор инициализации
     * @param salt соль
     */
    public void setupDecrypt(String initvec, String salt) {
        try {
            SecretKeyFactory factory = null;
            SecretKey tmp = null;
            SecretKey secret = null;

            mSalt = Hex.decodeHex(salt.toCharArray());
            logMessage("полученая соль: " + Hex.encodeHexString(mSalt));

            // получение вектора инициализации из переданной строки
            mInitVec = Hex.decodeHex(initvec.toCharArray());
            logMessage("полученый вектор инициализации" + Hex.encodeHexString(mInitVec));

            //Формирования ключа, с помощью пароля пароль и соли.
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(mPassword.toCharArray(), mSalt, ITERATIONS, KEYLEN_BITS);

            tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            /* Расшифровка сообщение, с использованием полученного ключа и вектора инициализации. */
            mDecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mDecipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(mInitVec));
        } catch (DecoderException | NoSuchAlgorithmException |
                InvalidKeySpecException | NoSuchPaddingException |
                InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Функция используемая для записи зашифрованных данных в файл на диск
     *
     * @param input - файл, из которого будут считыватся исходние данные
     * @param output - файл, куда будут записаны зашифрованные данные
     */
    public void writeEncryptedFile(File input, File output) {
        try {
            FileInputStream fin;
            FileOutputStream fout;
            long totalread = 0;
            int nread = 0;
            byte[] inbuf = new byte[MAX_FILE_BUF];

            fout = new FileOutputStream(output);
            fin = new FileInputStream(input);

            while ((nread = fin.read(inbuf)) > 0) {
                logMessage("read " + nread + " bytes");
                totalread += nread;

                byte[] trimbuf = new byte[nread];
                System.arraycopy(inbuf, 0, trimbuf, 0, nread);

                // шифрования буфера, используя шифр, полученный ранее
                byte[] tmp = mEcipher.update(trimbuf);

                if (tmp != null) {
                    fout.write(tmp);
                }
            }

            // завершение шифрования
            byte[] finalbuf = mEcipher.doFinal();
            if (finalbuf != null) {
                fout.write(finalbuf);
            }

            fout.flush();
            fin.close();
            fout.close();
            fout.close();

            logMessage("записано " + totalread + " зашифрованных байт");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Чтение из зашифрованного файла (input) и преобразование шифротекста в
     * открытый вид.
     *
     * @param input Объект File, представляющий зашифрованные данные на диске
     * @param output Объект File, куда будет записан расшифрованный текст
     *
     */
    public void readEncryptedFile(File input, File output) {
        try {
            FileInputStream fin;
            FileOutputStream fout;
            CipherInputStream cin;
            long totalread = 0;
            int nread = 0;
            byte[] inbuf = new byte[MAX_FILE_BUF];

            fout = new FileOutputStream(output);
            fin = new FileInputStream(input);

            // создание декодированого потока из FileInputStream, с использованием шифра
            cin = new CipherInputStream(fin, mDecipher);

            try {
                while ((nread = cin.read(inbuf)) > 0) {
                    logMessage("read " + nread + " bytes");
                    totalread += nread;

                    // создается буфер для записи с точным числом считанных байт 
                    byte[] trimbuf = new byte[nread];
                    System.arraycopy(inbuf, 0, trimbuf, 0, nread);

                    fout.write(trimbuf);
                }
            } catch (IOException ex) {
                Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                fout.flush();
                cin.close();
                fin.close();
                fout.close();
            }

            logMessage("записано " + totalread + " зашифрованных байт");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SymmetricEncryptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
