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
	
	public static String date2Str(Date ts, String format) {
		return Objects.isNull(ts) ? null : new SimpleDateFormat(format == null ? "yyyy-MM-dd" : format).format(ts);
	}
	
	public static String timestamp2Str(Timestamp ts, String format) {
		return date2Str(ts, format) ;
	}
	
	public static String date2Str(Date ts) {
		return date2Str(ts, null);
	}
	
	public static String timestamp2Str(Timestamp ts) {
		return date2Str(ts, "yyyy-MM-dd HH:mm:ss") ;
	}
	
	/**
	 * truncate given timestamp
	 * by default 'day'
	 * @param ts
	 * @return
	 */
	public static String truncTimestamp(Timestamp ts) {
		return truncTimestamp(ts, "day");
	}
	
	/**
	 * truncate given timestamp
	 * @param ts
	 * @param precision
	 * 	given trunc precision
	 * @return
	 */
	public static String truncTimestamp(Timestamp ts, String precision) {
		String trunc = null;
		switch (precision) {
		case "day":
			trunc = timestamp2Str(ts);
			break;
		case "month":
			trunc = timestamp2Str(ts, "yyyy-MM");
			break;
		case "year":
			trunc = timestamp2Str(ts, "yyyy");
			break;
		case "hour":
			trunc = timestamp2Str(ts, "yyyy-MM-dd HH");
			break;
		case "minute":
			trunc = timestamp2Str(ts, "yyyy-MM-dd HH:mm");
			break;
		case "second":
		default:
			trunc = timestamp2Str(ts, "yyyy-MM-dd HH:mm:ss");
			break;
		}
		
		return trunc;
	}
	
}
