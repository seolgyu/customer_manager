package cm.man;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class AdminDAOImpl  implements AdminDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	public int insertAdmin(AdminDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO ADMIN_TAB( ADM_ID, ADM_PWD) VALUES (?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getAdm_Id());
			pstmt.setString(2, dto.getAdm_Pwd());
			result = pstmt.executeUpdate();			
			conn.commit();
			
			pstmt.close();
			pstmt = null;
			
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw e;
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
				
			}
			DBUtil.close(pstmt);
		}

		return result;
	}


	@Override
	public List<AdminDTO> listAdmin() {
		List<AdminDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT adm_id, adm_pwd FROM admin_tab ";
					
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				
				dto.setAdm_Id(rs.getString("Adm_Id"));
				dto.setAdm_Pwd(rs.getString("Adm_Pwd"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
}


//		아이디로 관리자찾기 <- 필요하면 추가 예정
//	@Override
//	public AdminDTO AdminId(String AdmId) {
//		AdminDTO dto = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String sql;
//		
//		try {
//			sql = "SELECT * FROM admin_tab WHERE adm_Id = ";
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		return null;
//	}