package com.example.rateit;

import java.util.List;

/**
 * created by chethan on 20-12-2021
 **/
public class MyUtilities {

    public static void print(List list){
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
            return String.format("%dh %dm",hours,minutes);
        else
            return String.format("%dm",minutes);
    }
}
