package de.ur.mi.parse;

import java.util.ArrayList;

import com.parse.ParseObject;

public class AppSingleton {
	private static AppSingleton ourInstance = new AppSingleton();
	
	public ArrayList<ParseObject> objectList = new ArrayList<ParseObject>();
	public ArrayList<ParseObject> delteObjectList = new ArrayList<ParseObject>();
	public ArrayList<Integer> positionList = new ArrayList<Integer>();
	
	public static AppSingleton getInstance() {
		return ourInstance;
	}

	private AppSingleton() {
	}
}