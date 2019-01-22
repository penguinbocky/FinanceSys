package pers.bocky.finance.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * enum实现单例优势
 * - 不用考虑多线程
 * - 防止反序列化问题
 * - （忘记了）
 * @author bocky
 *
 */
public enum DbUtility {
	DBUTIL_MYSQL(false);//, DBUTIL_ORACLE(true);
	
	private final String JDBC_MYSQL = "com.mysql.jdbc.Driver";
	private final String JDBC_ORACLE = "oracle.jdbc.OracleDriver"; //Not recommended, "oracle.jdbc.driver.OracleDriver";
	private String driver;
	private String url;
    private String userName;
    private String password;
    private final String DB_NAME = PropertiesUtil.getValueAsString("mysql.db") != null ? PropertiesUtil.getValueAsString("mysql.db") : "financial_sys";
	private String host = PropertiesUtil.getValueAsString("host.ip") != null ? PropertiesUtil.getValueAsString("host.ip") : "localhost";
    private int connCount;
    
    private DbUtility(boolean useOracle) {
    	connCount = 0;
		this.driver = useOracle ? JDBC_ORACLE : JDBC_MYSQL;
		this.url = useOracle ? "jdbc:oracle:thin:@" + host + ":1521:BOCKYDB" : "jdbc:mysql://" + host + ":3306/" + DB_NAME;
		this.userName = useOracle ? "bocky" : PropertiesUtil.getValueAsString("mysql.user") != null ? PropertiesUtil.getValueAsString("mysql.user") : "root";
		this.password = useOracle ? "123456" : PropertiesUtil.getValueAsString("mysql.password") != null ? PropertiesUtil.getValueAsString("mysql.password") : "123456";
		
		try {
			Class.forName(this.driver);//PropertiesUtil.getValue("dbDriver"));
			System.out.println("数据库驱动程序加载成功 > " + this.driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("数据库驱动程序加载失败 > " + this.driver);
		}
	}


	public Connection getCon() {
		Connection con = null;
		
		try {
			con = (Connection) DriverManager.getConnection(url, userName, password);//DriverManager.getConnection(PropertiesUtil.getValue("dbUrl"), PropertiesUtil.getValue("userName"),
					//PropertiesUtil.getValue("password"));
			System.out.println("数据库连接成功，" + (++connCount));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("数据库连接失败。");
			JOptionPane.showMessageDialog(null, "无法连接数据库， 程序即将强制退出。");
			System.exit(0);
		}

		return con;
	}

	public void close(Connection con) {
		if (con != null) {
			try {
				con.close();
				System.out.println("数据库关闭成功。");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("数据库关闭失败。");
			}
		}
	}
	
	public boolean dumpDB(String dbName, String path) {
		BufferedReader br = null;
		StringBuilder sBuilder = null;
		try {
			String cmd = "cmd /c mysqldump -u" + this.userName 
					+ " -p" + this.password 
					+ " -t " + (dbName == null ? DB_NAME : dbName)//only data, no structure 
					+ " > " + path + "/generated_by_java_" + DateUtil.getCurrentDateString("yy_MM_dd") + ".sql";
			Process process = Runtime.getRuntime().exec(cmd);
			br = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.forName("gbk")));
			sBuilder = new StringBuilder();
			String line;
			while((line = br.readLine()) != null) {
				sBuilder.append(line + "\n");
				System.out.println("CMD result > " + line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return sBuilder == null || "".equals(sBuilder.toString());
	}

}
