/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi.lab4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mchukDM
 */
public class MAC {

    private String file;

    public MAC(String file) {
        this.file = file;
    }

    /**
     * генерация имитовставки
     */
    public void createMAC() {
        try {
            System.out.println("message--> Generating MAC key and code files..");

            KeyGenerator keygen = KeyGenerator.getInstance("HmacMD5");
            SecretKey macKey = keygen.generateKey();

            byte[] keyBytes = macKey.getEncoded();

            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(System.getProperty("user.dir") + "/lab4_files/mac_key.txt"))) {
                out.write(keyBytes);
                out.flush();
            }

            Mac theMac = Mac.getInstance("HmacMD5");
            theMac.init(macKey);

            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
                while (in.available() > 0) {
                    byte[] plaintextBytes = new byte[in.available()];
                    in.read(plaintextBytes);
                    theMac.update(plaintextBytes);
                }    // while
            }

            try (BufferedOutputStream macData = new BufferedOutputStream(new FileOutputStream(
                    System.getProperty("user.dir") + "/lab4_files/sender_mac_data.txt"))) {
                macData.write(theMac.doFinal());
                macData.flush();
            }

            theMac.reset();

            System.out.println("message--> Done!");
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(MAC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MAC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MAC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * проверка имитовставки
     */
    public void verifyMAC() {
        BufferedInputStream in = null;
        try {
            System.out.println("message--> Calculating MAC and comparing to MAC from sender...");
            in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/lab4_files/mac_key.txt"));
            byte[] keyBytes = new byte[in.available()];
            in.read(keyBytes);
            in.close();
            SecretKeySpec skeySpec
                    = new SecretKeySpec(keyBytes, "HmacMD5");
            Mac theMac = Mac.getInstance("HmacMD5");
            theMac.init(skeySpec);
            try (BufferedInputStream inData = new BufferedInputStream(new FileInputStream(file))) {
                while (inData.available() > 0) {
                    byte[] plaintextBytes = new byte[inData.available()];
                    inData.read(plaintextBytes);
                    theMac.update(plaintextBytes);
                }
            }
            byte[] calculatedMacCode = theMac.doFinal();

            in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + "/lab4_files/sender_mac_data.txt"));
            byte[] senderMacCode = new byte[in.available()];
            in.read(senderMacCode);
            in.close();
            boolean macsAgree = true;
            if (calculatedMacCode.length != calculatedMacCode.length) {
                macsAgree = false;
                System.out.println("message--> Sender MAC and calculated MAC are not the same.");
            } else {

                for (int i = 0; i < senderMacCode.length; i++) {
                    if (senderMacCode[i] != calculatedMacCode[i]) {
                        macsAgree = false;
                        System.out.println("message--> Sender MAC and calculated MAC are different. "
                                + "Message cannotbe authenticated.");
                        break;

                    }
                }
            }
            if (macsAgree) {
                System.out.println("message--> Message authenticated successfully.");
            }    // if
            System.out.println("message--> Done!");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MAC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(MAC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MAC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
