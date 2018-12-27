package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pers.bocky.finance.bean.LendBean;
import pers.bocky.finance.bean.LendHistoryBean;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.StringUtil;

public class LendDao extends BaseDao {
	
	public static List<LendBean> fetchAllLendRecs(){
		List<LendBean> list = new ArrayList<LendBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.lend_id, d.type_id, t.type_name, d.to_who, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM lend d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + LendBean.CATEGORY_ID
				+ " ORDER BY last_update_ts DESC");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			LendBean bean = null;
			while(rs != null && rs.next()){
				bean = new LendBean();
				bean.setLendId(rs.getInt("lend_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setToWho(rs.getString("to_who"));
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
	
	public static DaoResponse saveNewLend(LendBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		DaoResponse response = DaoResponse.SAVE_ERROR;
		
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into lend(type_id, to_who, amount, description, occur_ts, add_ts) values(?, ?, ?, ?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getToWho());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			if (1 == pstat.executeUpdate()) {
				response = DaoResponse.SAVE_SUCCESS;
			} else {
				response = DaoResponse.SAVE_ERROR;
			}
			pstat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}

		return response;
	}

	public static DaoResponse updateRecord(LendBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		DaoResponse response = DaoResponse.UPDATE_ERROR;
		
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update lend set type_id = ?, to_who = ?, amount = ?, description = ?, occur_ts = ?, last_update_ts = now()"
				+ " where lend_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getToWho());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			pstat.setInt(6, bean.getLendId());
			if (pstat.executeUpdate() != 1) {
				System.out.println("LendDao.updateRecord result != 1");
				response = DaoResponse.UPDATE_ERROR;
			} else {
				response  =DaoResponse.UPDATE_SUCCESS;
			}
			pstat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}

		return response;
	}
	
	public static DaoResponse deleteRecord(LendBean bean) {
		return deleteRecord(bean, false);
	}
	
	public static DaoResponse deleteRecord(LendBean bean, boolean realDelete){
		if (bean.getLendId() == null || bean.getLendId() < 0) return DaoResponse.VALIDATION_ERROR;
		DaoResponse response = DaoResponse.DELETE_ERROR;
		
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				realDelete ? "delete from lend where lend_id = ?" : "update lend set active_flg = 'N' where lend_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getLendId());
			if (pstat.executeUpdate() != 1) {
				System.out.println("LendDao.deleteRecord result != 1");
				response = DaoResponse.DELETE_ERROR;
			} else {
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
	
	public static boolean savePaybackHistory(LendBean bean){
		boolean success = false;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into lend_history(lend_id, amount, description, add_ts, occur_ts) values(?, ?, ?, now(), ?)");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getLendId());
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
	
	public static List<LendHistoryBean> fetchAllLendHistoryRecs(LendHistoryBean param){
		List<LendHistoryBean> list = new ArrayList<LendHistoryBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.lend_history_id, d.occur_ts, d.last_update_ts, d.amount, d.description, d.add_ts"
				+ " FROM lend b, lend_history d"
				+ " WHERE b.active_flg = 'Y' AND b.active_flg = 'Y'"
				+ " AND b.lend_id = d.lend_id"
				+ " AND b.lend_id = ?");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, param.getLendId());
			ResultSet rs = pstat.executeQuery();
			
			LendHistoryBean bean = null;
			while(rs != null && rs.next()){
				bean = new LendHistoryBean();
				bean.setLendHistoryId(rs.getInt("lend_history_id"));
				bean.setAmount(rs.getDouble("amount"));
				bean.setDescription(rs.getString("description"));
				bean.setAddTs(rs.getTimestamp("add_ts"));
				bean.setOccurTs(rs.getTimestamp("occur_ts"));
				bean.setLastUpdateTs(rs.getTimestamp("last_update_ts"));
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
	
	public static double calculateAmountOfLatestMonthOfType(Integer... typeId){
		return calculateAmountOfLatestMonthOfType(LendBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAvgMonthAmountOfType(Integer... typeId){
		return calculateAvgMonthAmountOfType(LendBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAmountFromThisMonthOfType(Integer... typeId) {
		return calculateAmountFromThisMonthOfType(LendBean.CATEGORY_ID, typeId);
	}

	public static double calculateAmountOfLastMonthhOfType(Integer... typeId) {
		return calculateAmountOfLastMonthOfType(LendBean.CATEGORY_ID, typeId);
	}
	
	/**
	 * @param bean
	 */
	private static boolean doValidation(LendBean bean) {
		if (bean == null || StringUtil.isEmpty(bean.getToWho()) || StringUtil.isEmpty(bean.getAmount().toString())) {
			System.out.println("Invalid Input.");
			return false;
		} else {
			return true;
		}
	}

	public static double calculateAmountOfLastWeekOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfLastWeekOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountFromThisWeekOfType(Integer[] selectedTypeIds) {
		return calculateAmountFromThisWeekOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountOfTodayOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfTodayOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAvgWeekAmountOfType(Integer[] selectedTypeIds) {
		return calculateAvgWeekAmountOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAvgDayAmountOfType(Integer[] selectedTypeIds) {
		return calculateAvgDayAmountOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}
	
}
