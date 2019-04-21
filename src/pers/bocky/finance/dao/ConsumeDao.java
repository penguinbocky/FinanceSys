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
	
	public static List<ConsumeBean> fetchAllConsumeRecs(){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID
				+ " ORDER BY last_update_ts DESC");
	
		logger.log(new LogBean(sql.toString(), new Date()));
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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

	public static List<ConsumeBean> fetchConsumeRecsByType(Comparator selectedComparator, Integer typeId){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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
	
	public static List<ConsumeBean> fetchConsumeRecsByOccurDate(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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
	
	public static List<ConsumeBean> fetchConsumeRecsByDest(Comparator selectedComparator, String dest){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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
	
	public static List<ConsumeBean> fetchConsumeRecsByDesc(Comparator selectedComparator, String desc){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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
	
	public static List<ConsumeBean> fetchConsumeRecsByAmount(Comparator selectedComparator, Double amount){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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
	
	public static List<ConsumeBean> fetchConsumeRecsByLastUpdTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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
	
	public static List<ConsumeBean> fetchConsumeRecsByCreatedTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		List<ConsumeBean> list = new ArrayList<ConsumeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.consume_id, d.type_id, t.type_name, d.dest, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM consume d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + ConsumeBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			ConsumeBean bean = null;
			while(rs != null && rs.next()){
				bean = new ConsumeBean();
				bean.setConsumeId(rs.getInt("consume_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDest(rs.getString("dest"));
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

	public static DaoResponse updateRecord(ConsumeBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.UPDATE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update consume set type_id = ?, dest = ?, amount = ?, description = ?, occur_ts = ?, last_update_ts = now()"
				+ " where consume_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getDest());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			pstat.setInt(6, bean.getConsumeId());
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
			list = fetchConsumeRecsByType(selectedComparator, new Integer(_filterValue));
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
	
}
