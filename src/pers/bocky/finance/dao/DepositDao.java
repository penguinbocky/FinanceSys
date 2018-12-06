package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pers.bocky.finance.bean.DepositBean;
import pers.bocky.finance.component.Comparator;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.StringUtil;

public class DepositDao extends BaseDao {
	
	public static List<DepositBean> fetchAllDepositRecs(){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID
				+ " ORDER BY last_update_ts DESC");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsByType(Comparator selectedComparator, Integer typeId){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsBySource(Comparator selectedComparator, String source){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

		if (source != null && !"".equals(source)) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" AND d.source = '" + source + "'");
				break;
			case 不等于:
				sql.append(" AND d.source <> '" + source + "'");
				break;
			case 包含:
				sql.append(" AND d.source like '%" + source + "%'");
				break;
			default:
				break;
			}
		}
		sql.append(" ORDER BY last_update_ts DESC");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsByAmount(Comparator selectedComparator, Integer amount){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsByDesc(Comparator selectedComparator, String desc){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

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
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsByOccurDate(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

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
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsByCreatedTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

		if (ts != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" and d.add_ts = '" + ts + "'");
				break;
			case 不等于:
				sql.append(" AND d.add_ts <> '" + ts + "'");
				break;
			case 介于:
				if (ts2.compareTo(new Date(0)) > 0) {
					sql.append(" AND d.add_ts < '" + ts2 + "'");
				}
			case 大于:
				sql.append(" AND d.add_ts > '" + ts + "'");
				break;
			case 小于:
				sql.append(" AND d.add_ts < '" + ts + "'");
				break;
			default:
				break;
			}
			
		}
		sql.append(" ORDER BY last_update_ts DESC");
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static List<DepositBean> fetchDepositRecsByLastUpdTs(Comparator selectedComparator, Timestamp ts, Timestamp ts2){
		List<DepositBean> list = new ArrayList<DepositBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.occur_ts, d.deposit_id, d.type_id, t.type_name, d.source, d.amount, d.description, d.add_ts, d.last_update_ts"
				+ " FROM deposit d, type_dfntn t, category_dfntn c"
				+ " WHERE d.active_flg = 'Y' AND t.active_flg = 'Y' AND c.active_flg = 'Y'"
				+ " AND d.type_id = t.type_id"
				+ " AND t.category_id = c.category_id"
				+ " AND c.category_id = " + DepositBean.CATEGORY_ID);

		if (ts != null) {
			switch (selectedComparator) {
			case 等于:
				sql.append(" and d.last_update_ts = '" + ts + "'");
				break;
			case 不等于:
				sql.append(" AND d.last_update_ts <> '" + ts + "'");
				break;
			case 介于:
				if (ts2.compareTo(new Date(0)) > 0) {
					sql.append(" AND d.last_update_ts < '" + ts2 + "'");
				}
			case 大于:
				sql.append(" AND d.last_update_ts > '" + ts + "'");
				break;
			case 小于:
				sql.append(" AND d.last_update_ts < '" + ts + "'");
				break;
			default:
				break;
			}
			
		}
		sql.append(" ORDER BY last_update_ts DESC");
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			DepositBean bean = null;
			while(rs != null && rs.next()){
				bean = new DepositBean();
				bean.setDepositId(rs.getInt("deposit_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setSource(rs.getString("source"));
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
	
	public static DaoResponse saveNewDeposit(DepositBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into deposit(type_id, source, amount, description, occur_ts, add_ts) values(?, ?, ?, ?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getSource());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			if (pstat.executeUpdate() == 1) {
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

	public static DaoResponse updateRecord(DepositBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse responseCode = DaoResponse.UPDATE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update deposit set type_id = ?, source = ?, amount = ?, description = ?, occur_ts = ?, last_update_ts = now()"
				+ " where deposit_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			pstat.setString(2, bean.getSource());
			pstat.setDouble(3, bean.getAmount());
			pstat.setString(4, bean.getDescription());
			pstat.setTimestamp(5, bean.getOccurTs());
			pstat.setInt(6, bean.getDepositId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("DepositDao.updateRecord result == 1");
				responseCode = DaoResponse.UPDATE_SUCCESS;
			}
			pstat.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbUtil.close(con);
		}

		return responseCode;
	}
	
	public static DaoResponse deleteRecord(DepositBean bean) {
		return deleteRecord(bean, false);
	}
	public static DaoResponse deleteRecord(DepositBean bean, boolean realDelete){
		if (bean.getDepositId() == null || bean.getDepositId() < 0) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.DELETE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				realDelete ? "delete from deposit where deposit_id = ?" : "update deposit set active_flg = 'N' where deposit_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getDepositId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("DepositDao.deleteRecord result == 1");
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
	
	public static double calculateAllDepositRecsAmount(){
		System.out.println("In calculateAllDepositRecsAmount >>> ");
		return calculateAmountOfType(DepositBean.CATEGORY_ID);
	}
	
	public static double calculateAmountOfLatestMonthOfType(Integer... typeId){
		return calculateAmountOfLastMonthhOfType(DepositBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAvgMonthAmountOfType(Integer... typeId){
		return calculateAvgMonthAmountOfType(DepositBean.CATEGORY_ID, typeId);
	}
	
	public static double calculateAmountFromThisMonthOfType(Integer... typeId) {
		return calculateAmountFromThisMonthOfType(DepositBean.CATEGORY_ID, typeId);
	}

	public static double calculateAmountOfLastMonthhOfType(Integer... typeId) {
		return calculateAmountOfLastMonthhOfType(DepositBean.CATEGORY_ID, typeId);
	}
	
	/**
	 * @param bean
	 */
	private static boolean doValidation(DepositBean bean) {
		if (bean == null || StringUtil.isEmpty(bean.getSource()) || StringUtil.isEmpty(bean.getAmount().toString())) {
			System.out.println("Invalid Input.");
			return false;
		} else {
			return true;
		}
	}

	public static List<DepositBean> getRecByFilter(String filterName, Comparator selectedComparator, String _filterValue, String _filterValue2) {
		List<DepositBean> list = new ArrayList<DepositBean>();
		
		_filterValue2 = _filterValue2 == null ? "0" : _filterValue2;
		switch (filterName) {
		case "类型":
			list = fetchDepositRecsByType(selectedComparator, new Integer(_filterValue));
			break;
		case "发生时间":
			list = fetchDepositRecsByOccurDate(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "来源":
			list = fetchDepositRecsBySource(selectedComparator, _filterValue);
			break;
		case "备注":
			list = fetchDepositRecsByDesc(selectedComparator, _filterValue);
			break;
		case "数量":
			list = fetchDepositRecsByAmount(selectedComparator, Integer.parseInt(_filterValue));
			break;
		case "最后更新于":
			list = fetchDepositRecsByLastUpdTs(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		case "创建时间":
			list = fetchDepositRecsByCreatedTs(selectedComparator, new Timestamp(Long.parseLong(_filterValue)), new Timestamp(Long.parseLong(_filterValue2)));
			break;
		default:
			break;
		}
		
		return list;
	}
}
