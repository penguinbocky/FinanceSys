package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pers.bocky.finance.bean.BorrowBean;
import pers.bocky.finance.bean.BorrowHistoryBean;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.StringUtil;

public class BorrowDao extends BaseDao {
	
	public static List<BorrowBean> fetchAllBorrowRecs(){
		List<BorrowBean> list = new ArrayList<BorrowBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT occur_ts, borrow_id, type_id, type_name, from_who, amount, description, add_ts, last_update_ts, PAYBACKEDAMT, (AMOUNT - PAYBACKEDAMT) LEFTAMT"
				+ " FROM ("
				+ " SELECT d.occur_ts, d.borrow_id, d.type_id, t.type_name, d.from_who, d.amount, d.description, d.add_ts, d.last_update_ts, SUM(H.AMOUNT) PAYBACKEDAMT"
				+ " FROM borrow d LEFT JOIN BORROW_HISTORY H ON D.BORROW_ID = H.BORROW_ID, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + BorrowBean.CATEGORY_ID
				+ " GROUP BY D.BORROW_ID"
				+ " ) TEMP"
				+ " ORDER BY last_update_ts DESC");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			BorrowBean bean = null;
			while(rs != null && rs.next()){
				bean = new BorrowBean();
				bean.setBorrowId(rs.getInt("borrow_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setFromWho(rs.getString("from_who"));
				bean.setAmount(rs.getDouble("amount"));
				bean.setDescription(rs.getString("description"));
				bean.setAddTs(rs.getTimestamp("add_ts"));
				bean.setLastUpdateTs(rs.getTimestamp("last_update_ts"));
				bean.setOccurTs(rs.getTimestamp("occur_ts"));
				bean.setPaybackedAmt(rs.getDouble("paybackedAmt"));
				bean.setLeftAmt(rs.getDouble("leftAmt"));
				list.add(bean);
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return list;
	}
	
	public static DaoResponse saveNewBorrow(BorrowBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into borrow(type_id, from_who, amount, description, occur_ts, add_ts) values(?, ?, ?, ?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getFromWho());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			if (pstat.executeUpdate() == 1) {
				response = DaoResponse.SAVE_SUCCESS;
			}
			pstat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}

		return response;
	}

	public static DaoResponse updateRecord(BorrowBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.UPDATE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update borrow set type_id = ?, from_who = ?, amount = ?, description = ?, occur_ts = ?, last_update_ts = now()"
				+ " where borrow_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getFromWho());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			pstat.setInt(6, bean.getBorrowId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("BorrowDao.updateRecord result == 1");
				response = DaoResponse.UPDATE_SUCCESS;
			}
			pstat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}

		return response;
	}
	
	public static DaoResponse deleteRecord(BorrowBean bean) {
		return deleteRecord(bean, false);
	}
	
	public static DaoResponse deleteRecord(BorrowBean bean, boolean realDelete){
		if (bean.getBorrowId() == null || bean.getBorrowId() < 0) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.DELETE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				realDelete ? "delete from borrow where borrow_id = ?" : "update borrow set active_flg = 'N' where borrow_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getBorrowId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("BorrowDao.deleteRecord result == 1");
				response = DaoResponse.DELETE_SUCCESS;
			}
			pstat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}

		return response;
	}
	
	public static boolean savePaybackHistory(BorrowBean bean){
		boolean success = false;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into borrow_history(borrow_id, amount, description, add_ts, occur_ts) values(?, ?, ?, now(), ?)");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getBorrowId());
			pstat.setDouble(2, bean.getAmount());
			pstat.setString(3, bean.getDescription());
			pstat.setTimestamp(4, bean.getOccurTs());
			pstat.executeUpdate();
			pstat.close();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
			success = false;
		} finally {
			dbUtil.close(con);
		}

		return success;
	}
	
	public static List<BorrowHistoryBean> fetchAllBorrowHistoryRecs(BorrowHistoryBean param){
		List<BorrowHistoryBean> list = new ArrayList<BorrowHistoryBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.borrow_history_id, d.occur_ts, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM borrow b, borrow_history d"
				+ " WHERE b.active_flg = 'Y' AND b.active_flg = 'Y'"
				+ " AND b.borrow_id = d.borrow_id"
				+ " AND b.borrow_id = ?");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, param.getBorrowId());
			ResultSet rs = pstat.executeQuery();
			
			BorrowHistoryBean bean = null;
			while(rs != null && rs.next()){
				bean = new BorrowHistoryBean();
				bean.setBorrowHistoryId(rs.getInt("borrow_history_id"));
				bean.setAmount(rs.getDouble("amount"));
				bean.setDescription(rs.getString("description"));
				bean.setAddTs(rs.getTimestamp("add_ts"));
				bean.setLastUpdateTs(rs.getTimestamp("last_update_ts"));
				bean.setOccurTs(rs.getTimestamp("occur_ts"));
				list.add(bean);
			}
			pstat.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}
		return list;
	}
	
	public static double calAllBorrowHistoryAmountOfType(Integer typeId){
		System.out.println("In calAllBorrowHistoryAmountOfType >>> ");
		double sum = 0;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT sum(d.amount) sum"
				+ " FROM borrow b, borrow_history d"
				+ " WHERE b.active_flg = 'Y' AND b.active_flg = 'Y'"
				+ " AND b.borrow_id = d.borrow_id"
				+ " AND b.type_id = ?");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, typeId);
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
	
	public static double calAllBorrowAmountOfType(Integer typeId){
		return calculateAmountOfType(BorrowBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAmountOfLatestMonthOfType(Integer... typeId){
		return calculateAmountOfLatestMonthOfType(BorrowBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAvgMonthAmountOfType(Integer... typeId){
		return calculateAvgMonthAmountOfType(BorrowBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAmountFromThisMonthOfType(Integer... typeId) {
		return calculateAmountFromThisMonthOfType(BorrowBean.CATEGORY_ID, typeId);
	}

	public static double calculateAmountOfLastMonthhOfType(Integer... typeId) {
		return calculateAmountOfLastMonthOfType(BorrowBean.CATEGORY_ID, typeId);
	}
	
	/**
	 * @param bean
	 */
	private static boolean doValidation(BorrowBean bean) {
		if (bean == null || StringUtil.isEmpty(bean.getFromWho()) || StringUtil.isEmpty(bean.getAmount().toString())) {
			System.out.println("Invalid Input.");
			return false;
		} else {
			return true;
		}
	}

	public static double calculateAmountOfLastWeekOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfLastWeekOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountFromThisWeekOfType(Integer[] selectedTypeIds) {
		return calculateAmountFromThisWeekOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountOfTodayOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfTodayOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAvgWeekAmountOfType(Integer[] selectedTypeIds) {
		return calculateAvgWeekAmountOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAvgDayAmountOfType(Integer[] selectedTypeIds) {
		return calculateAvgDayAmountOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}
	
}
