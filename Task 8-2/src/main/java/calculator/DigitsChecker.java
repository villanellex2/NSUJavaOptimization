package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigitsChecker {
    public static void main(String[] args) {
       String values[] =  {"13472956", "134729zz"};
       for(String digit: values){
           System.out.println("exception: " + digit + ":  " + exceptionCheck(digit));
           System.out.println("regex: " + digit + ":  " + regexCheck(digit));
           System.out.println("character: " + digit + ":  " + characterCheck(digit));
       }
    }

    public static boolean exceptionCheck(String digit) {
        try {
            Integer.parseInt(digit);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean characterCheck(String digit) {
        char[] digits = digit.toCharArray();
        if (digit.length() == 0) return false;
        for (char c : digits) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean regexCheck(String digit) {
        Pattern regex = Pattern.compile("^\\d+$");
        Matcher matcher = regex.matcher(digit);
        return matcher.find();
    }
}



