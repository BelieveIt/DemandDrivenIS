package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import model.SalesRecord;

import dao.SalesRecordDao;

public class DateUtil {
	public static void main(String[] strings){
		SalesRecordDao salesRecordDao = new SalesRecordDao();
		HashMap<String, ArrayList<SalesRecord>> map = salesRecordDao.querySalesRecords();
		Date date1 = map.get("1").get(0).getCreateTime();
		Date date2 = map.get("1").get(2).getCreateTime();
		System.out.println(date1 + " " + date2);
		System.out.println(daysBetween(date2, date1));
	}
	public static String formatDuring(Date date1, Date date2) {
		long mss = date1.getTime() - date2.getTime();
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		if(days != 0){
			return days + " days " + hours + " hours";
		}else {
			return hours + " hours";
		}
	}

	 public static int daysBetween(Date smdate,Date bdate)
	    {
		 try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			smdate=sdf.parse(sdf.format(smdate));
	        bdate=sdf.parse(sdf.format(bdate));
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(smdate);
	        long time1 = cal.getTimeInMillis();
	        cal.setTime(bdate);
	        long time2 = cal.getTimeInMillis();
	        long between_days=(time2-time1)/(1000*3600*24);
	        return Integer.parseInt(String.valueOf(between_days)) + 1;
	        } catch (ParseException e) {
				e.printStackTrace();
			}
		 return -1;
	    }
}
