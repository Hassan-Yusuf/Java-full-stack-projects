package UserIO;

import java.math.BigDecimal;

public interface UserIO {
    void print(String msg);
    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
    BigDecimal readCurrencyValue(String prompt);
    BigDecimal readCurrencyValue(String prompt, BigDecimal min, BigDecimal max);


}
