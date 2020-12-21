package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pers.bocky.finance.bean.LendBean;
import pers.bocky.finance.component.Comparator;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.DateUtil;
import pers.bocky.finance.util.StringUtil;

public class LendDao extends BaseDao {
	private final static String preSql = "SELECT occur_ts, lend_id, type_id, type_name, to_who, amount, description, add_ts, last_update_ts"
			+ ", paybackedAmt, CASE WHEN PAYBACKEDAMT IS NULL THEN amount else (amount - paybackedAmt) end leftAmt"
			+ " FROM ("
			+ " SELECT d.occur_ts, d.lend_id, d.type_id, t.type_name, d.to_who, d.amount, d.description, d.add_ts, d.last_update_ts, SUM(h.amount) paybackedAmt"
			+ " FROM lend d LEFT JOIN (select * from history where history_type = 'PAY_BACK' and category_id = " + LendBean.CATEGORY_ID
			+ " ) H ON D.LEND_ID = H.ID, type_dfntn t, category_dfntn c"
			+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
			+ " AND d.type_id = t.type_id"
			+ " AND t.category_id = c.category_id"
			+ " AND c.category_id = " + LendBean.CATEGORY_ID;
	
	private static StringBuffer getPreSql() {
		return new StringBuffer(preSql);
	}
	
	private static LendBean buildFrom(ResultSet rs) throws SQLException {
		return new LendBean().buildFrom(rs);
	}
	
	public static List<LendBean> fetchAllLendRecs(){
		StringBuffer sql = getPreSql().append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> getRecListBySql(String sql) {
		List<LendBean> list = new ArrayList<LendBean>();
		Connection con = dbUtil.getCon();
	
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			pstat = con.prepareStatement(sql);
			rs = pstat.executeQuery();
			
			while(rs != null && rs.next()){
				list.add(buildFrom(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(pstat, rs, con);
		}
		return list;
	}
	
	private static List<LendBean> fetchRecsByType(Comparator selectedComparator, Integer typeId){
		StringBuffer sql = getPreSql();

		if (typeId != null && typeId != 0) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.type_id = " + typeId);
				break;
			case 不等于:
				sql.append(" AND d.type_id <> " + typeId);
				break;
			default:
				break;
			}
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> fetchRecsByDest(Comparator selectedComparator, String dest){
		StringBuffer sql = getPreSql();

		if (dest != null && !"".equals(dest)) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.to_who = '" + dest + "'");
				break;
			case 不等于:
				sql.append(" AND d.to_who <> '" + dest + "'");
				break;
			case 包含:
				sql.append(" AND d.to_who like '%" + dest + "%'");
				break;
			default:
				break;
			}
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> fetchRecsByAmount(Comparator selectedComparator, Double amount){
		StringBuffer sql = getPreSql();

		if (amount != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.amount = " + amount);
				break;
			case 不等于:
				sql.append(" AND d.amount <> " + amount);
				break;
			case 大于:
				sql.append(" AND d.amount > " + amount);
				break;
			case 小于:
				sql.append(" AND d.amount < " + amount);
				break;
			default:
				break;
			}
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> fetchRecsByDesc(Comparator selectedComparator, String desc){
		StringBuffer sql = getPreSql();

		if (desc != null && !"".equals(desc)) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.description = '" + desc + "'");
				break;
			case 不等于:
				sql.append(" AND d.description <> '" + desc + "'");
				break;
			case 包含:
				sql.append(" AND d.description like '%" + desc + "%'");
				break;
			default:
				break;
			}
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> fetchRecsByOccurDate(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		StringBuffer sql = getPreSql();

		if (ts != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" and d.occur_ts = '" + ts + "'");
				break;
			case 不等于:
				sql.append(" AND d.occur_ts <> '" + ts + "'");
				break;
			case 介于:
				if (ts2.compareTo(new Date(0)) > 0) {
					sql.append(" AND d.occur_ts <= '" + ts2 + "'");
				}
			case 大于:
				sql.append(" AND d.occur_ts > '" + ts + "'");
				break;
			case 小于:
				sql.append(" AND d.occur_ts < '" + ts + "'");
				break;
			default:
				break;
			}
			
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> fetchRecsByCreatedTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		StringBuffer sql = getPreSql();

		if (ts != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" and date_format(d.add_ts, '%Y-%m-%d') = '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			case 不等于:
				sql.append(" AND date_format(d.add_ts, '%Y-%m-%d') <> '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			case 介于:
				if (ts2.compareTo(new Date(0)) > 0) {
					sql.append(" AND date_format(d.add_ts, '%Y-%m-%d') <= '" + DateUtil.truncTimestamp(ts2) + "'");
				}
			case 大于:
				sql.append(" AND date_format(d.add_ts, '%Y-%m-%d') > '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			case 小于:
				sql.append(" AND date_format(d.add_ts, '%Y-%m-%d') < '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			default:
				break;
			}
			
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		return getRecListBySql(sql.toString());
	}
	
	private static List<LendBean> fetchRecsByLastUpdTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		StringBuffer sql = getPreSql();

		if (ts != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" and date_format(d.last_update_ts, '%Y-%m-%d') = date_format('" + ts + "', '%Y-%m-%d')");
				break;
			case 不等于:
				sql.append(" AND date_format(d.last_update_ts, '%Y-%m-%d') <> '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			case 介于:
				if (ts2.compareTo(new Date(0)) > 0) {
					sql.append(" AND date_format(d.last_update_ts, '%Y-%m-%d') <= '" + DateUtil.truncTimestamp(ts2) + "'");
				}
			case 大于:
				sql.append(" AND date_format(d.last_update_ts, '%Y-%m-%d') > '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			case 小于:
				sql.append(" AND date_format(d.last_update_ts, '%Y-%m-%d') < '" + DateUtil.truncTimestamp(ts) + "'");
				break;
			default:
				break;
			}
			
		}
		sql.append(" GROUP BY d.lend_id ) TEMP ORDER BY last_update_ts DESC");
		return getRecListBySql(sql.toString());
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
	
	private static DaoResponse saveAsHistory(LendBean bean){
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into history(category_id, id, amount, description, add_ts, occur_ts) values(?, ?, ?, ?, ?, ?)");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, LendBean.CATEGORY_ID);
			pstat.setInt(2, bean.getLendId());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			//last record's upd ts would be history record's creation/added ts to display
			//history's own creation ts would be upd_ts generated by DB self
			pstat.setTimestamp(5, bean.getLastUpdateTs());
			pstat.setTimestamp(6, bean.getOccurTs());
			if (pstat.executeUpdate() == 1) {
				System.out.println("LendDao.saveAsHistory result == 1");
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
	
	public static DaoResponse saveHistoryAndUpdateRecord(LendBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.UPDATE_ERROR;
		LendBean curBean = (LendBean) fetchAllLendRecs().stream().filter(rec -> rec.getLendId().intValue() == bean.getLendId().intValue()).toArray()[0];
		if (DaoResponse.SAVE_SUCCESS != saveAsHistory(curBean)) return response;
		
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update lend set type_id = ?, to_who = ?, amount = amount + ?, description = ?, last_update_ts = now()"
				+ " where lend_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getToWho());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
//			pstat.setTimestamp(5, bean.getOccurTs());
			pstat.setInt(5, bean.getLendId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("LendDao.updateRecord result == 1");
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
				"insert into history(category_id, id, amount, description, add_ts, occur_ts, history_type) values(?, ?, ?, ?, now(), ?, 'PAY_BACK')");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, LendBean.CATEGORY_ID);
			pstat.setInt(2, bean.getLendId());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
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

	public static List<LendBean> getRecByFilter(String filterName, Comparator selectedComparator, String _filterValue, String _filterValue2) {
		List<LendBean> list = new ArrayList<LendBean>();
		
		_filterValue2 = _filterValue2 == null ? "0" : _filterValue2;
		switch (filterName) {
		case "类型":
			list = fetchRecsByType(selectedComparator, new Integer(_filterValue));
			break;
		case "发生时间":
			list = fetchRecsByOccurDate(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "去向":
			list = fetchRecsByDest(selectedComparator, _filterValue);
			break;
		case "备注":
			list = fetchRecsByDesc(selectedComparator, _filterValue);
			break;
		case "数量":
			list = fetchRecsByAmount(selectedComparator, Double.parseDouble(_filterValue));
			break;
		case "最后更新于":
			list = fetchRecsByLastUpdTs(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "创建时间":
			list = fetchRecsByCreatedTs(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		default:
			break;
		}
		
		return list;
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

	public static double calculateYesterdayAmountOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfYesterdayOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}
	
	public static double calculateForCustomizedAmountOfType(Integer[] selectedTypeIds) {
		return calculateForCustomizedPeriodAmountOfType(LendBean.CATEGORY_ID, selectedTypeIds);
	}

	public static Map<String, Double> getAmountGroupByMonth(Integer[] selectedTypeIds) {
		return getAmountGroupByMonth(LendBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountOfType(Integer[] typeId) {
		return calculateAmountOfType(LendBean.CATEGORY_ID, typeId);
	}
	
}
