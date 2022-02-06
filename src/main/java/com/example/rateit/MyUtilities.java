package com.example.rateit;

import java.util.Base64;
import java.util.List;

/**
 * created by chethan on 20-12-2021
 **/
public final class MyUtilities {

    private MyUtilities() { }

    public static void print(List<?> list){
        for (Object o : list) {
            System.out.println(o);
        }
    }

    public static void print(String string){
        System.out.println(string);
    }

    public static String minToHours(int runtime){
        int hours = runtime / 60;
        int minutes = runtime % 60;
        if (hours > 0)
            return String.format("%dh %dm", hours,minutes);
        else
            return String.format("%dm", minutes);
    }

    public static String base64Encode(String plainText){
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    public static String base64Decode(String encodedText){
        return new String(Base64.getDecoder().decode(encodedText));
    }
}
