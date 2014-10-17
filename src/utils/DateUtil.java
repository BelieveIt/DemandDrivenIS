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
	public final static String[] month={"Jan.", "Feb.", "Mar.", "Api.", "May", "Jun.", "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};
	public static String getWeekDay(int i){
		return weekday[i-1];
	}

	public static String getMonth(int i){
		return month[i];
	}
	public static void main(String[] strings) throws ParseException{
//		System.out.println(Calendar.SEPTEMBER);
//		SalesRecordDao salesRecordDao = new SalesRecordDao();
//		HashMap<String, ArrayList<SalesRecord>> map = salesRecordDao.querySalesRecords();
//		Date date1 = map.get("1").get(0).getCreateTime();
//		Date date2 = map.get("1").get(2).getCreateTime();
//		System.out.println(date1 + " " + date2);
//		System.out.println(daysBetween(date2, date1));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String endInString = "2014-01-01 00:00:00";
		Date start = sdf.parse(endInString);
		System.out.println(start);
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

	 public static int getYearOfDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return year;
	 }

	 public static int getMonthOfDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		return month + 1;
	 }

	 public static int getDayOfDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	 }

	 public static ArrayList<String> getLastYearAndMonthList(Date date, int num){
		 	ArrayList<String> dateList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, -num-1);
			for(int i = 1; i <= num+1; i++){
				calendar.add(Calendar.MONTH, 1);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;

				String monthString = "";
				if(month < 10) {
					monthString = "0" + month;
				}else {
					monthString = Integer.toString(month);
				}

				dateList.add(Integer.toString(year) + monthString);
			}

			return dateList;
	 }

	 public static ArrayList<String> getFutureYearAndMonthList(Date date, int num){
		 	ArrayList<String> dateList = new ArrayList<String>();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			for(int i = 1; i <= num; i++){
				calendar.add(Calendar.MONTH, 1);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH)+1;

				String monthString = "";
				if(month < 10) {
					monthString = "0" + month;
				}else {
					monthString = Integer.toString(month);
				}

				dateList.add(Integer.toString(year) + monthString);
			}
			return dateList;
	 }

	 public static String getYearAndMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		String monthString = "";
		if(month < 10) {
			monthString = "0" + month;
		}else {
			monthString = Integer.toString(month);
		}
		return Integer.toString(year) + monthString;
	 }
}
