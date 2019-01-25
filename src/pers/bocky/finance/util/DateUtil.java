package pers.bocky.finance.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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
	
	public static String getCurrentDateAsString(String format){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format == null ? "yyyy-MM-dd HH:mm:ss" : format);
		return sdf.format(date);
	}
	
	public static String date2Str(Date ts) {
		return Objects.isNull(ts) ? null : new SimpleDateFormat("yyyy-MM-dd").format(ts);
	}
	
	public static String timestamp2Str(Timestamp ts) {
		return date2Str(ts) ;
	}
}
