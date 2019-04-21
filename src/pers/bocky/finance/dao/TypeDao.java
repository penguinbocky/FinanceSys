package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.StringUtil;

public class TypeDao extends BaseDao {
	
	/**
	 * Fetch type(s) by specific condition, any condition as null will be ignored during selecting data from database.
	 * @param categoryId
	 * @param typeName
	 * @return
	 */
	public static List<TypeBean> fetchTypeBy(Integer categoryId, String typeName){
		List<TypeBean> list = new ArrayList<TypeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer("select * from type_dfntn where active_flg = 'Y'");
		if (categoryId != null) {
			sql.append(" and category_id = " + categoryId);
		} else if (!StringUtil.isEmpty(typeName)) {
			sql.append(" and type_name = '" + typeName + "'");
		}else {
			sql.append(" order by last_update_ts desc");
		}
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			TypeBean bean = null;
			while(rs != null && rs.next()){
				bean = new TypeBean();
				bean.setCategoryId(rs.getInt("category_id"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDescription(rs.getString("description"));
				bean.setAddTs(rs.getTimestamp("add_ts"));
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
	
	public static List<TypeBean> fetchTypeByName(String typeName, Integer excludedTypeId){
		List<TypeBean> list = fetchTypeBy(null, typeName);
		return list.stream().filter(bean -> bean.getTypeId() != excludedTypeId).collect(Collectors.toList());
	}
	
	public static List<TypeBean> fetchAllTypesAndCategories(){
		List<TypeBean> list = new ArrayList<TypeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(""
				+ "SELECT t.category_id, t.type_id, t.type_name, t.description, t.add_ts, t.last_update_ts, c.category_name"
				+ " FROM type_dfntn t, category_dfntn c"
				+ " WHERE"
				+ " t.category_id = c.category_id"
				+ " AND t.active_flg = 'Y'"
				+ " AND c.active_flg = 'Y'");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			TypeBean bean = null;
			while(rs != null && rs.next()){
				bean = new TypeBean();
				bean.setCategoryId(rs.getInt("category_id"));
				bean.setCategoryName(rs.getString("category_name"));
				bean.setTypeId(rs.getInt("type_id"));
				bean.setTypeName(rs.getString("type_name"));
				bean.setDescription(rs.getString("description"));
				bean.setAddTs(rs.getTimestamp("add_ts"));
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
	
	public static DaoResponse saveNewType(TypeBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		List<TypeBean> existingTypes = fetchTypeByName(bean.getTypeName(), null);
		if (existingTypes != null && existingTypes.size() > 0) {
			return DaoResponse.EXISTED;
		}
		
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into type_dfntn(category_id, type_name, description, add_ts) values(?, ?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setString(1, bean.getCategoryId().toString());
			pstat.setString(2, bean.getTypeName());
			pstat.setString(3, bean.getDescription());
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
	
	/**
	 * There is already a check in UI end, this is a double check.
	 * @param bean
	 * @return
	 */
	private static boolean doValidation(TypeBean bean) {
		if (bean == null || StringUtil.isEmpty(bean.getTypeName()) || StringUtil.isEmpty(bean.getCategoryId().toString())) {
			System.out.println("Invalid Input.");
			return false;
		} else {
			return true;
		}
	}
	
	public static DaoResponse updateRecord(TypeBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		List<TypeBean> existingTypes = fetchTypeByName(bean.getTypeName(), bean.getTypeId());
		if (existingTypes != null && existingTypes.size() > 0) {
			return DaoResponse.EXISTED;
		}
		
		DaoResponse responseCode = DaoResponse.UPDATE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"update type_dfntn set type_name = ?, description = ?, last_update_ts = now()"
				+ " where type_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setString(1, bean.getTypeName());
			pstat.setString(2, bean.getDescription());
//			pstat.setInt(3, bean.getCategoryId());
			pstat.setInt(3, bean.getTypeId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("TypeDao.updateRecord result == 1");
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
	
	public static DaoResponse deleteRecord(TypeBean bean) {
		return deleteRecord(bean, false);
	}
	
	public static DaoResponse deleteRecord(TypeBean bean, boolean realDelete){
		if (bean.getTypeId() == null || bean.getTypeId() < 0) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.DELETE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				realDelete ? "delete from type_dfntn where type_id = ?" : "update type_dfntn set active_flg = 'N' where type_id = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setInt(1, bean.getTypeId());
			if (pstat.executeUpdate() == 1) {
				System.out.println("TypeDao.deleteRecord result == 1");
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
	
}
