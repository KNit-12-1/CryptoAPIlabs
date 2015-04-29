package cryptoapi;

import cryptoapi.lab1.FirstLab;
import cryptoapi.lab2.SecondLab;
import cryptoapi.lab3.ThirdLab;
import cryptoapi.lab4.ForthLab;
import cryptoapi.lab5.FifthLab;
import java.util.Scanner;

/**
 *
 * @author mchukDM
 */
public class CryptoAPI {

    /**
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        System.out.print("Введите номер лабораторной, или 0 для выхода:");
        Scanner scan = new Scanner(System.in);
        int input=0;
        do {
            
            input = scan.nextInt();
            switch (input) {
                case 1:
                    System.out.println("Лабораторная №1\nРабота с криптопровайдерами");
                    FirstLab.startLab1();
                    break;
                case 2:
                    System.out.println("Лабораторная №2\nГенерация ключа симметричного шифрования на основе пароля");
                    SecondLab.startLab2();
                    break;
                case 3:
                    System.out.println("Лабораторная №3\nСимметричная криптосистема. Шифрование");
                    ThirdLab.startLab3();
                    break;
                case 4:
                    System.out.println("Лабораторная №4\nСимметричная криптосистема. Генерация имитовставки");
                    ForthLab.startLab4();
                    break;
                case 5:
                    System.out.println("Лабораторная №5\nВыработка и проверка электронной цифровой подписи");
                    FifthLab.startLab5();
                    break;
                case 0:
                    break;
            }
            System.out.print("\nВведите номер лабораторной, или 0 для выхода:");
        } while (input!=0);
        //FifthLab.generateAndVerifySignature();
    }

}
