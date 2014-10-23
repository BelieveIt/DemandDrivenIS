package utils;

import java.util.ArrayList;
import java.util.Collections;

public class StringUtil {
	public static void main(String[] str){
		String dataString = "####name##y";
		System.out.println(getStringsBySplit(dataString));
	}

	public static ArrayList<String> getStringsBySplit(String splitStr){
		ArrayList<String> values =  new ArrayList<String>();
		if(splitStr != null){
			String[] strs = splitStr.split("##");
			Collections.addAll(values, strs);
		}
		if(values.size() != 0) values.remove(0);
		return values;
	}

	public static String getStringByList(ArrayList<String> listData){
		String stringData = "";
		if(listData != null){
			for(String item : listData){
				stringData = stringData + "##" + item;
			}
		}

		return stringData;
	}

}
