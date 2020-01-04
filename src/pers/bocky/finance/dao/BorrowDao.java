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

import pers.bocky.finance.bean.BorrowBean;
import pers.bocky.finance.bean.HistoryBean;
import pers.bocky.finance.component.Comparator;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.DateUtil;
import pers.bocky.finance.util.StringUtil;

public class BorrowDao extends BaseDao {
	private final static String preSql = "SELECT occur_ts, borrow_id, type_id, type_name, from_who, amount, description, add_ts, last_update_ts"
			+ ", PAYBACKEDAMT, CASE WHEN PAYBACKEDAMT IS NULL THEN amount else (AMOUNT - PAYBACKEDAMT) end LEFTAMT"
			+ " FROM ("
			+ " SELECT d.occur_ts, d.borrow_id, d.type_id, t.type_name, d.from_who, d.amount, d.description, d.add_ts, d.last_update_ts, SUM(H.AMOUNT) PAYBACKEDAMT"
			+ " FROM borrow d LEFT JOIN ("
			+ " SELECT * FROM HISTORY WHERE category_id = " + BorrowBean.CATEGORY_ID
			+ " ) H ON D.BORROW_ID = H.ID, type_dfntn t, category_dfntn c"
			+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
			+ " AND d.type_id = t.type_id"
			+ " AND t.category_id = c.category_id"
			+ " AND c.category_id = " + BorrowBean.CATEGORY_ID;
	
	private static StringBuffer getPreSql() {
		return new StringBuffer(preSql);
	}
	
	private static BorrowBean buildFrom(ResultSet rs) throws SQLException {
		return new BorrowBean().buildFrom(rs);
	}
	
	public static List<BorrowBean> fetchAllBorrowRecs(){
		StringBuffer sql = getPreSql().append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");

		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> getRecListBySql(String sql) {
		List<BorrowBean> list = new ArrayList<BorrowBean>();
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
	
	private static List<BorrowBean> fetchRecsByType(Comparator selectedComparator, Integer typeId){
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
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> fetchRecsBySource(Comparator selectedComparator, String dest){
		StringBuffer sql = getPreSql();

		if (dest != null && !"".equals(dest)) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.from_who = '" + dest + "'");
				break;
			case 不等于:
				sql.append(" AND d.from_who <> '" + dest + "'");
				break;
			case 包含:
				sql.append(" AND d.from_who like '%" + dest + "%'");
				break;
			default:
				break;
			}
		}
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> fetchRecsByAmount(Comparator selectedComparator, Double amount){
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
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> fetchRecsByDesc(Comparator selectedComparator, String desc){
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
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> fetchRecsByOccurDate(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
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
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> fetchRecsByCreatedTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
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
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		return getRecListBySql(sql.toString());
	}
	
	private static List<BorrowBean> fetchRecsByLastUpdTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
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
		sql.append(" GROUP BY d.borrow_id ) TEMP ORDER BY last_update_ts DESC");
		return getRecListBySql(sql.toString());
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
				"insert into history(category_id, id, amount, description, add_ts, occur_ts) values(?, ?, ?, ?, now(), ?)");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, BorrowBean.CATEGORY_ID);
			pstat.setInt(2, bean.getBorrowId());
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
	
	public static List<HistoryBean> fetchAllBorrowHistoryRecs(HistoryBean param){
		List<HistoryBean> list = new ArrayList<HistoryBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.history_id, d.occur_ts, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM borrow b, history d"
				+ " WHERE b.active_flg = 'Y' AND b.active_flg = 'Y'"
				+ " AND b.borrow_id = d.id"
				+ " AND D.CATEGORY_ID = " + BorrowBean.CATEGORY_ID
				+ " AND b.borrow_id = ?");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, param.getId());
			ResultSet rs = pstat.executeQuery();
			
			HistoryBean bean = null;
			while(rs != null && rs.next()){
				bean = new HistoryBean();
				bean.setHistoryId(rs.getInt("history_id"));
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
				+ " FROM borrow b, history d"
				+ " WHERE b.active_flg = 'Y' AND b.active_flg = 'Y'"
				+ " AND b.borrow_id = d.id"
				+ " AND D.CATEGORY_ID = " + BorrowBean.CATEGORY_ID
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
	
	public static double calAllBorrowAmountOfType(Integer... typeId){
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

	public static List<BorrowBean> getRecByFilter(String filterName, Comparator selectedComparator, String _filterValue, String _filterValue2) {
		List<BorrowBean> list = new ArrayList<BorrowBean>();
		
		_filterValue2 = _filterValue2 == null ? "0" : _filterValue2;
		switch (filterName) {
		case "类型":
			list = fetchRecsByType(selectedComparator, new Integer(_filterValue));
			break;
		case "发生时间":
			list = fetchRecsByOccurDate(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "来源":
			list = fetchRecsBySource(selectedComparator, _filterValue);
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

	public static double calculateYesterdayAmountOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfYesterdayOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}
	
	public static double calculateForCustomizedAmountOfType(Integer[] selectedTypeIds) {
		return calculateForCustomizedPeriodAmountOfType(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}

	public static Map<String, Double> getAmountGroupByMonth(Integer[] selectedTypeIds) {
		return getAmountGroupByMonth(BorrowBean.CATEGORY_ID, selectedTypeIds);
	}
	
}
