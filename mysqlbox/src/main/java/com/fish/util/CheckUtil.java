package com.fish.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class CheckUtil {
    private static final String NUMBER = "^\\d+$";
    private static final String CHARACTER = "^[a-zA-Z]+$";

    public static boolean checkNumber(String value){
        return Pattern.matches(NUMBER, value);
    }

    public static boolean checkDateByPattern(String date, String pattern){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean checkDateDefault(String date){
        return checkDateByPattern(date, "yyyy-MM-dd");
    }

    public static boolean checkCharacter(String str){
        return Pattern.matches(CHARACTER, str);
    }
}
