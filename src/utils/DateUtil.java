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
	public final static String[] weekday={"Sun.", "Mon.", "Tue.", "Wed.", "Thu.", "Fri.", "Sat."};
	public static String getWeekDay(int i){
		return weekday[i-1];
	}
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
	public static void setToZero(Calendar calendar){
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	public static int daysBetween(Date date1,Date date2)
	{
		Calendar start = Calendar.getInstance();
		start.setTime(date1);
		DateUtil.setToZero(start);
		Calendar end = Calendar.getInstance();
		end.setTime(date2);
		DateUtil.setToZero(end);
		int day = 0;
		while(!start.after(end)){
			day++;
		}
		return day;
	}

	 public static Date minusDay(Date date, int x){
		 Calendar calendar  = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.DATE, -1 * x);
		 return calendar.getTime();
	 }

	 public static Date minusMonth(Date date, int x){
		 Calendar calendar  = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.MONTH, -1 * x);
		 return calendar.getTime();
	 }
}
