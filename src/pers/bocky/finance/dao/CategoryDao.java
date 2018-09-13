package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pers.bocky.finance.bean.CategoryBean;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.StringUtil;

public class CategoryDao extends BaseDao {
	
	public static List<CategoryBean> fetchAllCategories(){
		List<CategoryBean> list = new ArrayList<CategoryBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer("select * from category_dfntn where active_flg = 'Y' order by last_update_ts desc");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			CategoryBean bean = null;
			while(rs != null && rs.next()){
				bean = new CategoryBean();
				bean.setCategoryId(rs.getInt("category_id"));
				bean.setCategoryName(rs.getString("category_name"));
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
	
	public static DaoResponse saveNewCategory(CategoryBean bean){
		if (!doValidation(bean)) return DaoResponse.VALIDATION_ERROR;
		
		DaoResponse response = DaoResponse.SAVE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into category_dfntn(category_name, description, add_ts) values(?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setString(1, bean.getCategoryName());
			pstat.setString(2, bean.getDescription());
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
	private static boolean doValidation(CategoryBean bean) {
		if (bean == null || StringUtil.isEmpty(bean.getCategoryName())) {
			System.out.println("Invalid Input.");
			return false;
		} else {
			return true;
		}
	}
	
}
