package utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IdentityUtil {
	public static void main(String[] str){
		System.out.println(randomString(10));
	}
	public static String randomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	public static String randomUUID(){
		return randomString(10);
	}
	public static String randomVersionId(){
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date())+"_"+randomString(4);
	}

}
