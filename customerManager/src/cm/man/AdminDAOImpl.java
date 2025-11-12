package cm.man;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class AdminDAOImpl implements AdminDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	public int insertAdmin(AdminDTO dto) throws SQLException {
		int result = 0;
		String sql = "INSERT INTO ADMIN_TAB( ADM_ID, ADM_PWD) VALUES (?,?)";

		conn.setAutoCommit(false);

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, dto.getAdmId());
			pstmt.setString(2, dto.getAdmPwd());

			result = pstmt.executeUpdate(); // SQL 실행
			conn.commit(); // ✅ 성공 시 커밋

		} catch (SQLException e) {
			DBUtil.rollback(conn); // ✅ 실패 시 롤백
			throw e; // 상위로 예외 전달
		}

		return result;
	}

	@Override
	public List<AdminDTO> listAdmin() {
		List<AdminDTO> list = new ArrayList<>();		
		String sql = "SELECT adm_id, adm_pwd FROM admin_tab ";
		
		try (
			PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rs = pstmt.executeQuery(); ){
			
			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				
				dto.setAdmId(rs.getString("ADM_ID"));
				dto.setAdmPwd(rs.getString("ADM_PWD"));				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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