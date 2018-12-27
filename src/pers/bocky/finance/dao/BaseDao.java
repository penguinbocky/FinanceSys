package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pers.bocky.finance.bean.CategoryBean;
import pers.bocky.finance.util.DbUtility;
import pers.bocky.finance.util.LoggerUtil;

public class BaseDao {
	protected static DbUtility dbUtil = DbUtility.DBUTIL_MYSQL;
	protected static LoggerUtil logger = LoggerUtil.SQLogger;
	
	public static boolean dumpAllData(String path) {
		return dbUtil.dumpDB(null, path);
	}
	
	protected static double calculateAmountOfType(int categoryId, Integer... typeId){
		System.out.println("calculating amount of type >>> for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}
	
	protected static double calculateAmountOfLatestMonthOfType(int categoryId, Integer... typeId){
		System.out.println("calculating latest 30 days for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		
		sql.append(" AND occur_ts >= date_sub(CURRENT_TIMESTAMP, interval 30 day)");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}
	
	protected static double calculateAvgMonthAmountOfType(int categoryId, Integer... typeId){
		System.out.println("calculating average month amount for categoryId: " + categoryId + ", and typeId: " + typeId);
		double avg = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer("select avg(sum) avg from (" + 
				"SELECT date_format(occur_ts, '%Y-%m'), sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		if (categoryId == CategoryBean.CONSUME) {//consume within 2018.5/6 has dirty data
			sql.append(" AND date_format(occur_ts, '%Y-%m') != '2018-05' ");
			sql.append(" AND date_format(occur_ts, '%Y-%m') != '2018-06' ");
		}
		sql.append(" AND date_format(occur_ts, '%Y-%m') != date_format(now(), '%Y-%m') ");
		sql.append(" group by date_format(occur_ts, '%Y-%m')) temp");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				avg = rs.getDouble("avg");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return avg;
	}
	
	protected static double calculateAmountFromThisMonthOfType(int categoryId, Integer... typeId) {
		System.out.println("calculating current month for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		sql.append(" AND date_format(occur_ts, '%Y-%m') = date_format(now(), '%Y-%m')");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}

	protected static double calculateAmountOfLastMonthOfType(int categoryId, Integer... typeId) {
		System.out.println("calculating last month for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM"
				+ " " + tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		sql.append(" AND date_format(occur_ts, '%Y-%m') = date_format(date_sub(CURRENT_TIMESTAMP, interval 1 month), '%Y-%m')");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}
	
	protected static double calculateAvgWeekAmountOfType(int categoryId, Integer... typeId){
		System.out.println("calculating average week amount for categoryId: " + categoryId + ", and typeId: " + typeId);
		double avg = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer("select avg(sum) avg from (" + 
				"SELECT date_format(occur_ts, '%x-%v'), sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		if (categoryId == CategoryBean.CONSUME) {//consume within 2018.5/6 has dirty data
			sql.append(" AND date_format(occur_ts, '%Y-%m') != '2018-05' ");
			sql.append(" AND date_format(occur_ts, '%Y-%m') != '2018-06' ");
		}
		sql.append(" AND date_format(occur_ts, '%x-%v') != date_format(now(), '%x-%v') ");
		sql.append(" group by date_format(occur_ts, '%x-%v')) temp");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				avg = rs.getDouble("avg");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return avg;
	}
	
	protected static double calculateAmountFromThisWeekOfType(int categoryId, Integer... typeId) {
		System.out.println("calculating current week for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		sql.append(" AND date_format(occur_ts, '%x-%v') = date_format(now(), '%x-%v')");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}

	protected static double calculateAmountOfLastWeekOfType(int categoryId, Integer... typeId) {
		System.out.println("calculating last week for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM"
				+ " " + tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		sql.append(" AND date_format(occur_ts, '%x-%v') = date_format(date_sub(CURRENT_TIMESTAMP, interval 1 week), '%x-%v')");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}
	
	protected static double calculateAmountOfTodayOfType(int categoryId, Integer... typeId) {
		System.out.println("calculating today for categoryId: " + categoryId + ", and typeId: " + typeId);
		double sum = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) as sum"
				+ " FROM"
				+ " " + tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		sql.append(" AND date_format(occur_ts, '%Y-%m-%d') = date_format(now(), '%Y-%m-%d')");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				sum = rs.getDouble("sum");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return sum;
	}
	
	protected static double calculateAvgDayAmountOfType(int categoryId, Integer... typeId){
		System.out.println("calculating average day amount for categoryId: " + categoryId + ", and typeId: " + typeId);
		double avg = 0;
		Connection con = dbUtil.getCon();
		String tableName = null;
		switch (categoryId) {
		case CategoryBean.DEPOSIT:
			tableName = "deposit";
			break;
		case CategoryBean.CONSUME:
			tableName = "consume";
			break;
		case CategoryBean.BORROW:
			tableName = "borrow";
			break;
		case CategoryBean.LEND:
			tableName = "lend";
			break;

		default:
			tableName = "consume";
			break;
		}
		StringBuffer sql = new StringBuffer("select avg(sum) avg from (" + 
				"SELECT date_format(occur_ts, '%Y-%m-%d'), sum(d.amount) as sum"
				+ " FROM "
				+ tableName
				+ " d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + categoryId);

		if (typeId != null && typeId.length > 0) {
			sql.append(" AND ( d.type_id = " + typeId[0]);
			for (int i = 1; i < typeId.length; i++) {
				Integer type = typeId[i];
				sql.append(" or d.type_id = " + type);
			}
			sql.append(" ) ");
		}
		if (categoryId == CategoryBean.CONSUME) {//consume within 2018.5/6 has dirty data
			sql.append(" AND date_format(occur_ts, '%Y-%m') != '2018-05' ");
			sql.append(" AND date_format(occur_ts, '%Y-%m') != '2018-06' ");
		}
		sql.append(" AND date_format(occur_ts, '%Y-%m-%d') != date_format(now(), '%Y-%m-%d') ");
		sql.append(" group by date_format(occur_ts, '%Y-%m-%d')) temp");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			if(rs != null && rs.next()){
				avg = rs.getDouble("avg");
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return avg;
	}
	
}
