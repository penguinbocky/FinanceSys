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

import pers.bocky.finance.bean.ConsumeBean;
import pers.bocky.finance.bean.LogBean;
import pers.bocky.finance.component.Comparator;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.DateUtil;
import pers.bocky.finance.util.StringUtil;

public class ConsumeDao extends BaseDao {
	private final static String preSql = "SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
			+ ", (SELECT COUNT(1) FROM history h WHERE h.category_id = t.category_id AND h.id = d.consume_id) has_history"
			+ " FROM consume d, type_dfntn t, category_dfntn c"
			+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
			+ " AND d.type_id = t.type_id"
			+ " AND t.category_id = c.category_id"
			+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID;
	
	private static StringBuffer getPreSql() {
		return new StringBuffer(preSql);
	}
	
	private static ConsumeBean buildFrom(ResultSet rs) throws SQLException {
		return new ConsumeBean().buildFrom(rs);
	}
	
	public static List<ConsumeBean> fetchAllConsumeRecs(){
		StringBuffer sql = getPreSql().append(" ORDER BY last_update_ts DESC");
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}

	private static List<ConsumeBean> getRecListBySql(String sql) {
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
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
	
	private static List<ConsumeBean> fetchRecsByType(Comparator selectedComparator, Integer typeId){
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
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<ConsumeBean> fetchConsumeRecsByOccurDate(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
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
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<ConsumeBean> fetchConsumeRecsByDest(Comparator selectedComparator, String dest){
		StringBuffer sql = getPreSql();

		if (dest != null && !"".equals(dest)) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.dest = '" + dest + "'");
				break;
			case 不等于:
				sql.append(" AND d.dest <> '" + dest + "'");
				break;
			case 包含:
				sql.append(" AND d.dest like '%" + dest + "%'");
				break;
			default:
				break;
			}
		}
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<ConsumeBean> fetchConsumeRecsByDesc(Comparator selectedComparator, String desc){
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
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<ConsumeBean> fetchConsumeRecsByAmount(Comparator selectedComparator, Double amount){
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
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<ConsumeBean> fetchConsumeRecsByLastUpdTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		StringBuffer sql = getPreSql();

		if (ts != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" and date_format(d.last_update_ts, '%Y-%m-%d') = '" + DateUtil.truncTimestamp(ts) + "'");
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
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	
	private static List<ConsumeBean> fetchConsumeRecsByCreatedTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
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
		sql.append(" ORDER BY last_update_ts DESC");
		
		logger.log(new LogBean(sql.toString(), new Date()));
		
		return getRecListBySql(sql.toString());
	}
	public static DaoResponse saveNewConsume(ConsumeBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into consume(type_id, dest, amount, description, occur_ts, add_ts) values(?, ?, ?, ?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getDest());
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

	private static DaoResponse saveAsHistory(ConsumeBean bean){
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into history(category_id, id, amount, description, add_ts, occur_ts) values(?, ?, ?, ?, ?, ?)");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, ConsumeBean.CATEGORY_ID);
			pstat.setInt(2, bean.getConsumeId());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			//last record's upd ts would be history record's creation/added ts to display
			//history's own creation ts would be upd_ts generated by DB self
			pstat.setTimestamp(5, bean.getLastUpdateTs());
			pstat.setTimestamp(6, bean.getOccurTs());
			if (pstat.executeUpdate() == 1) {
				System.out.println("ConsumeDao.saveAsHistory result == 1");
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
	
	public static DaoResponse saveHistoryAndUpdateRecord(ConsumeBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.UPDATE_ERROR;
		ConsumeBean curBean = (ConsumeBean) fetchAllConsumeRecs().stream().filter(rec -> rec.getConsumeId().intValue() == bean.getConsumeId().intValue()).toArray()[0];
		if (DaoResponse.SAVE_SUCCESS != saveAsHistory(curBean)) return response;
		
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update consume set type_id = ?, dest = ?, amount = ?, description = ?, last_update_ts = now()"
				+ " where consume_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getDest());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
//			pstat.setTimestamp(5, bean.getOccurTs());
			pstat.setInt(5, bean.getConsumeId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("ConsumeDao.updateRecord result == 1");
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
	
	public static DaoResponse deleteRecord(ConsumeBean bean) {
		return deleteRecord(bean, false);
	}
	public static DaoResponse deleteRecord(ConsumeBean bean, boolean realDelete){
		if (bean.getConsumeId() == null || bean.getConsumeId() < 0) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.DELETE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				realDelete ? "delete from consume where consume_id = ?" : "update consume set active_flg = 'N' where consume_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getConsumeId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("ConsumeDao.deleteRecord result == 1");
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
	
	public static double calculateAmountOfType(Integer... typeId){
		return calculateAmountOfType(ConsumeBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAmountOfLatestMonthOfType(Integer... typeId){
		return calculateAmountOfLatestMonthOfType(ConsumeBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAvgMonthAmountOfType(Integer... typeId){
		return calculateAvgMonthAmountOfType(ConsumeBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAmountFromThisMonthOfType(Integer... typeId) {
		return calculateAmountFromThisMonthOfType(ConsumeBean.CATEGORY_ID, typeId);
	}

	public static double calculateAmountOfLastMonthhOfType(Integer... typeId) {
		return calculateAmountOfLastMonthOfType(ConsumeBean.CATEGORY_ID, typeId);
	}

	/**
	 * @param bean
	 */
	private static boolean doValidation(ConsumeBean bean) {
		if (bean == null || StringUtil.isEmpty(bean.getDest()) || StringUtil.isEmpty(bean.getAmount().toString())) {
			System.out.println("Invalid Input.");
			return false;
		} else {
			return true;
		}
	}

	public static List<ConsumeBean> getRecByFilter(String filterName, Comparator selectedComparator, String _filterValue, String _filterValue2) {
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		
		_filterValue2 = _filterValue2 == null ? "0" : _filterValue2;
		switch (filterName) {
		case "类型":
			list = fetchRecsByType(selectedComparator, new Integer(_filterValue));
			break;
		case "发生时间":
			list = fetchConsumeRecsByOccurDate(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "去向":
			list = fetchConsumeRecsByDest(selectedComparator, _filterValue);
			break;
		case "备注":
			list = fetchConsumeRecsByDesc(selectedComparator, _filterValue);
			break;
		case "数量":
			list = fetchConsumeRecsByAmount(selectedComparator, Double.parseDouble(_filterValue));
			break;
		case "最后更新于":
			list = fetchConsumeRecsByLastUpdTs(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "创建时间":
			list = fetchConsumeRecsByCreatedTs(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		default:
			break;
		}
		
		return list;
	}

	public static double calculateAmountOfLastWeekOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfLastWeekOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountFromThisWeekOfType(Integer[] selectedTypeIds) {
		return calculateAmountFromThisWeekOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAmountOfTodayOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfTodayOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAvgWeekAmountOfType(Integer[] selectedTypeIds) {
		return calculateAvgWeekAmountOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateAvgDayAmountOfType(Integer[] selectedTypeIds) {
		return calculateAvgDayAmountOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}

	public static double calculateYesterdayAmountOfType(Integer[] selectedTypeIds) {
		return calculateAmountOfYesterdayOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}
	
	public static Map<String, Double> getAmountGroupByMonth(Integer[] selectedTypeIds) {
		return getAmountGroupByMonth(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}
	
	public static double calculateForCustomizedAmountOfType(Integer[] selectedTypeIds) {
		return calculateForCustomizedPeriodAmountOfType(ConsumeBean.CATEGORY_ID, selectedTypeIds);
	}
	
}
