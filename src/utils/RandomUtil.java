package utils;

import java.util.Random;

public class RandomUtil {
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
}
