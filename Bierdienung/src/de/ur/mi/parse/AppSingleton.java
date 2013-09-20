package de.ur.mi.parse;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseObject;

public class AppSingleton {
    private static AppSingleton ourInstance = new AppSingleton();


    public String tableNumber = "";


    public static AppSingleton getInstance() {
        return ourInstance;
    }

    private AppSingleton() {
    }
}