package com.example.rateit;

import java.util.List;

/**
 * created by chethan on 20-12-2021
 **/
public class MyUtilities {
    public static void print(List list){
        for (int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
    }
    public static void print(String string){
        System.out.println(string);
    }
}
