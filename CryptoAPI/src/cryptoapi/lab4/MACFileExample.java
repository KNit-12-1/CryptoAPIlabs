/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi.lab4;
import javax.crypto.*;
import java.io.*;
/**
 *
 * @author mchukDM
 */
public class MACFileExample {
   public static void main(String[] args) throws Exception {
      System.out.println("Generating MAC key and code files..");

      KeyGenerator keygen = KeyGenerator.getInstance("HmacMD5");
      SecretKey macKey = keygen.generateKey();

      byte[] keyBytes = macKey.getEncoded();

      BufferedOutputStream out = new
         BufferedOutputStream(new FileOutputStream("mac_key.txt"));
      out.write(keyBytes);
      out.flush();
      out.close();

      Mac theMac = Mac.getInstance("HmacMD5");
      theMac.init(macKey);

      BufferedInputStream in = new
         BufferedInputStream(new FileInputStream("plaintext.txt"));
      while (in.available() > 0) {
         byte[] plaintextBytes = new byte[in.available()];
         in.read(plaintextBytes);
         theMac.update(plaintextBytes);
      }    // while
      in.close();

      BufferedOutputStream macData = new
         BufferedOutputStream(new FileOutputStream(
            "sender_mac_data.txt"));
      macData.write(theMac.doFinal());
      macData.flush();
      macData.close();

      theMac.reset();

      System.out.println("Done!");
   }    // main


}       // class MACFileExample
