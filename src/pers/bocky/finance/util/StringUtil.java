package pers.bocky.finance.util;

public class StringUtil {
	
	/**
	 * 判断字符串为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(null == str || "".equals(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断字符串不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		if(null != str && !"".equals(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 数值型字符串，去掉小数点后无效的数字0
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s){  
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }
	
	/**
	 * 去除Double类型无效的小数点和数值0，并以字符串输出。
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(Double s){  
        return subZeroAndDot(s.toString());  
    }
	
}
