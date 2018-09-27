package pers.bocky.finance.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static Date string2Date(String dateStr, String format) {
		if(StringUtil.isEmpty(dateStr)){
			return null;
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static String getCurrentDateString(String format){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format == null ? "yyyy-MM-dd HH:mm:ss" : format);
		return sdf.format(date);
	}
	
	@SuppressWarnings("deprecation")
	public static String timestamp2DateStr(Timestamp ts) {
		int year = 1900 + ts.getYear();
		int month = 1 + ts.getMonth();
		int day = ts.getDate();
		
		return "" + year 
				+ (month < 10 ? "-0" : "-") + month
				+ (day < 10 ? "-0" : "-") + day;
	}
	
}
