/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi.lab4;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.Key;

import java.io.*;

/**
 *
 * @author mchukDM
 */
public class VerifyMACFileExample {

    public static void main(String[] args) throws Exception {
        System.out.println(
        "Calculating MAC and comparing to MAC from sender...");

      BufferedInputStream in = new BufferedInputStream(new FileInputStream("mac_key.txt"));
        byte[] keyBytes = new byte[in.available()];
        in.read(keyBytes);
        in.close();
        SecretKeySpec skeySpec
                = new SecretKeySpec(keyBytes, "HmacMD5");

        Mac theMac = Mac.getInstance("HmacMD5");

        theMac.init(skeySpec);

        BufferedInputStream inData = new BufferedInputStream(new FileInputStream("plaintext.txt"));
        while (inData.available() > 0) {
            byte[] plaintextBytes = new byte[inData.available()];
            inData.read(plaintextBytes);
            theMac.update(plaintextBytes);
        }    // while
        inData.close();
        byte[] calculatedMacCode = theMac.doFinal();

        in = new BufferedInputStream(new FileInputStream(
                "sender_mac_data.txt"));
        byte[] senderMacCode = new byte[in.available()];
        in.read(senderMacCode);
        in.close();

        boolean macsAgree = true;
        if (calculatedMacCode.length != calculatedMacCode.length) {
            macsAgree = false;
            System.out.println(
                    "Sender MAC and calculated MAC lengthare not the same.");
        } else {

            for (int i = 0; i < senderMacCode.length; i++) {
                if (senderMacCode[i] != calculatedMacCode[i]) {
                    macsAgree = false;
                    System.out.println(
                            "Sender MAC and calculated MAC are different. "
                            + "Message cannotbe authenticated.");
                    break;

                }    // if
            }       // for i
        }          // if

        if (macsAgree) {
            System.out.println("Message authenticated successfully.");
        }    // if

        System.out.println("Done!");
    }    // main

}    // class VerifyMACFile
