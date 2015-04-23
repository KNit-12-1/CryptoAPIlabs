/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptoapi;

import cryptoapi.lab2.Crypto;
import java.util.Scanner;

/**
 *
 * @author mchukDM
 */
public class CryptoAPI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print("Введите номер лабораторной:");
        Scanner scan = new Scanner(System.in);
        switch (scan.nextInt()){
            case 1:
                System.out.println("Лабораторная №1\nРабота с криптопровайдерами");
                break;
            case 2:
                System.out.println("Лабораторная №2\nГенерация ключа симметричного шифрования на основе пароля");
                Crypto.startLab2();
                break;
            case 3:
                System.out.println("Лабораторная №3\nСимметричная криптосистема. Шифрование");
                break;
            case 4:
                System.out.println("Лабораторная №4\nСимметричная криптосистема. Генерация имитовставки");
                break;
            case 5:
                System.out.println("Лабораторная №5\nВыработка и проверка электронной цифровой подписи");
                break;
        }
        
       
        //FifthLab.generateAndVerifySignature();
    }
    
}
