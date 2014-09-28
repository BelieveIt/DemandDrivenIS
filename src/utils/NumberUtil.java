package utils;

import java.math.BigDecimal;

public class NumberUtil {
	public static double divide(Integer num1, Integer num2){
		BigDecimal numBigDecimal1 = new BigDecimal(num1);
		BigDecimal numBigDecimal2 = new BigDecimal(num2);
		BigDecimal result = numBigDecimal1.divide(numBigDecimal2);
        return result.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
