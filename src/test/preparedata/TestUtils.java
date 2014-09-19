package test.preparedata;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

public class TestUtils {

	public static void main(String[] args){
		for(int i = 0; i < 10; i++){
			String S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getRandomDate("2013-9-10 00:00:00", "2014-09-1 00:00:00"));
			System.out.println(S);
		}
		System.out.println(getRandom(0, 2));

	}

	/**
	 * Get random from range: [min, max);
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min, int max){
		 Random random = new Random();
	     int s = random.nextInt(max)%(max-min+1) + min;
	     return s;
	}

	public static boolean isHappen(int num){
		int random = getRandom(0, 100);
		if(random < num){
			return true;
		}else {
			return false;
		}
	}

	public static Timestamp getRandomDate(String startTime, String endTime){
		long offset = Timestamp.valueOf(startTime).getTime();
		long end = Timestamp.valueOf(endTime).getTime();
		long diff = end - offset + 1;
		Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));
		return rand;
	}



}
