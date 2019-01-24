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

public class ReportDao extends BaseDao {
	
	private final static String FAV_REPORT_OPTIONS = "FAV_REPORT_OPTIONS";
	
	public static List<TypeBean> fetchFavoriteReportOptions(Integer categoryId){
		List<TypeBean> list = new ArrayList<TypeBean>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"SELECT d.id, d.ref_key, d.ref_value, d.ref_desc, d.add_ts, d.last_update_ts"
				+ " FROM lk_ref d"
				+ " WHERE d.active_flg = 'Y'"
				+ " AND d.ref_type = '" + FAV_REPORT_OPTIONS + "'"
				+ " AND d.ref_key = '"+ categoryId + "'"
				+ " ORDER BY last_update_ts DESC");

		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			TypeBean bean = null;
			if(rs != null && rs.next()){
				String typeIds = rs.getString("ref_value");
				if (typeIds != null && !"".equals(typeIds)) {
					String[] typeIdArr = typeIds.split(",");
					for (String typeId : typeIdArr) {
						bean = new TypeBean();
						bean.setTypeId(Integer.parseInt(typeId));
						list.add(bean);
					}
				}
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
	
	public static DaoResponse saveFavReportOptions(Integer categoryId, List<TypeBean> list){
		DaoResponse response = DaoResponse.SAVE_ERROR;
		if (deleteFavReportOptions(categoryId) != DaoResponse.DELETE_SUCCESS) {
			return response;
		}
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"insert into lk_ref(ref_type, ref_key, ref_value, ref_desc, add_ts) values(?, ?, ?, ?, now())");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setString(1, FAV_REPORT_OPTIONS);
			pstat.setString(2, "" + categoryId);
			pstat.setString(3, String.join(",", list.stream().map(bean -> "" + bean.getTypeId()).collect(Collectors.toList()) ));
			pstat.setString(4, "Customized selected types option for user when report calculations");
			if (pstat.executeUpdate() == 1) {
				System.out.println("ReportDao.saveFavReportOptions executeUpdate == 1");
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
	
	public static DaoResponse deleteFavReportOptions(Integer categoryId){
		DaoResponse response = DaoResponse.DELETE_ERROR;
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(
				"delete from lk_ref where ref_type = ? and ref_key = ?");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			pstat.setString(1, FAV_REPORT_OPTIONS);
			pstat.setInt(2, categoryId);
			if (pstat.executeUpdate() >= 0) {
				System.out.println("ReportDao.deleteFavReportOptions result >=0");
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
