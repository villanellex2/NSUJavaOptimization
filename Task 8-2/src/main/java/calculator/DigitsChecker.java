package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    public static boolean isDigit1(String digit) {
        try {
            Integer.parseInt(digit);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDigit2(String digit) {
        char[] digits = digit.toCharArray();
        if (digit.length() == 0) return false;
        for (char c : digits) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDigit3(String digit) {
        Pattern regex = Pattern.compile("^\\d+$");
        Matcher matcher = regex.matcher(digit);
        return matcher.find();
    }
}



