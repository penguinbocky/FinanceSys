package pers.bocky.finance.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
*1.在jar包代码中要使用绝对路径，路径以 / 开始。
*2.在jar包代码中使用相对路径的起点是和jar所在目录。比如说test.jar所处位置为/opt/test/test.jar,那么你在代码中写相对路径：“pathTest/haha.txt”,那么实际指向的位置**为/opt/test/pathTest/haha.txt
*3.在jar包代码中使用：InputStream inputStream = this.getClass().getResourceAsStream(path) 的写法，不管路径以是否以 / 开始，都是从jar包内部的根目录为起点。
*
*/
public class PropertiesUtil {

	private static String resourcePath = "/app.properties";
	
	static {
//		String userDir = System.getProperty("user.dir");
//		resourcePath = userDir + resourcePath;
		System.out.println("resourcePath > " + resourcePath);
	}
	
	public static String getValue(String key) {
		Properties p = new Properties();
		InputStream in = PropertiesUtil.class.getResourceAsStream(resourcePath);
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return (String) p.get(key);
	}
	
//	public static void setValue(String key, String value) {
//		Properties p = new Properties();
//		InputStream in = PropertiesUtil.class.getResourceAsStream("/client.properties");
//		try {
//			p.load(in);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		p.setProperty(key, value);//不能成功写入
//	}
//	
//	public static String getValue() {
//		File f = new File("client.txt");
//		if(!f.exists()){
//			return "";
//		}
//		FileReader fr = null;;
//		char[] cbuf = new char[20];
//		
//		try {
//			fr = new FileReader(f);
//			fr.read(cbuf);
//			fr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return new String(cbuf).trim();
//	}
//	
//	public static void setValue(String ip) {
//		File f = new File("client.txt");
//		FileWriter fw = null;
//
//		try {
//			if (!f.exists()) {
//				f.createNewFile();
//			}
//			fw = new FileWriter(f);
//			fw.write(ip, 0, ip.length());
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
}
