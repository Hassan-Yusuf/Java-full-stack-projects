package UserIO;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
@Component
public class UserIOImpl implements UserIO{

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);
        Scanner read = new Scanner(System.in);
        return read.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        System.out.println(prompt);
        Scanner read = new Scanner(System.in);
        return Integer.parseInt(read.nextLine());
    }

    @Override
    public int readInt(String prompt,int min,int max) {
        System.out.println(prompt);
        int number;
        while(true){
            Scanner read = new Scanner(System.in);
            number = Integer.parseInt(read.nextLine());
            if(number>=min&&number<=max) {
                break;
            }
        }
        return number;
    }

    @Override
    public BigDecimal readCurrencyValue(String prompt) {
        System.out.println(prompt);
        Scanner read = new Scanner(System.in);
        BigDecimal number = new BigDecimal(read.nextLine());
        return number.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal readCurrencyValue(String prompt, BigDecimal min, BigDecimal max) {
        System.out.println(prompt);
        BigDecimal number;
        while(true){
            Scanner read = new Scanner(System.in);
            number = new BigDecimal(read.nextLine());
            if(number.compareTo(min)>=0&&number.compareTo(max)<=0) {
                break;
            }
        }
        return number.setScale(2, RoundingMode.HALF_UP);
    }
}
