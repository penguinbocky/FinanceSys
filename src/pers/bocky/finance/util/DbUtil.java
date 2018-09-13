package pers.bocky.finance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DbUtil {
	private static DbUtil instance;
	private final String JDBC_MYSQL = "com.mysql.jdbc.Driver";
	private final String JDBC_ORACLE = "oracle.jdbc.OracleDriver"; //Not recommended, "oracle.jdbc.driver.OracleDriver";
	private String driver;
	private String url;
    private String userName;
    private String password;
	
    public static DbUtil getInstanceForMySql() {
		if (instance == null) {
			synchronized (DbUtil.class) {
				if (instance == null) {
					instance = new DbUtil(false);
				}
			}
		}
		return instance;
	}
    
    public static DbUtil getInstanceForOracle() {
		if (instance == null) {
			synchronized (DbUtil.class) {
				if (instance == null) {
					instance = new DbUtil(true);
				}
			}
		}
		return instance;
	}
    
    private DbUtil(boolean useOracle) {
		this.driver = useOracle ? JDBC_ORACLE : JDBC_MYSQL;
		this.url = useOracle ? "jdbc:oracle:thin:@localhost:1521:BOCKYDB" : "jdbc:mysql://localhost:3306/financial_sys";
		this.userName = useOracle ? "bocky" : "root";
		this.password = useOracle ? "123456" : "123456";
		
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
			System.out.println("数据库连接成功。");
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

}
