package pers.bocky.finance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadMeDao extends BaseDao {
	
	public static List<String> fetchAllNotes(){
		List<String> list = new ArrayList<String>();
		Connection con = dbUtil.getCon();
		StringBuffer sql = new StringBuffer(""
				+ "select * "
				+ "from tb_msg "
				+ "where active_flg = 'Y' "
				+ "order by msg_id asc");
		
		try {
			PreparedStatement pstat = con.prepareStatement(sql.toString());
			ResultSet rs = pstat.executeQuery();
			
			while(rs != null && rs.next()){
				list.add(rs.getString("msg_content"));
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
	
}
