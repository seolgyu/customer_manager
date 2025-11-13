package cm.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db.util.DBConn;
import db.util.DBUtil;

public class LoginDAOImpl implements LoginDAO {
	private Connection conn = DBConn.getConnection();	
	
	// 데이터 입력
	@Override
	public void insertMember(LoginDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		// 자료 입력
		try {
			conn.setAutoCommit(false);

			sql = "INSERT INTO customer(cus_id, pwd, name, tel, email, address, reg, rrn, class_id, dormancy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getCus_id());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getName());
			pstmt.setString(4, dto.getTel());
			pstmt.setString(5, dto.getEmail());
			pstmt.setString(6, dto.getAddress());
			pstmt.setString(7, dto.getReg());
			pstmt.setString(8, dto.getRrn());
			pstmt.setString(9, dto.getClass_id());
			pstmt.setString(10, dto.getDormancy());

			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			conn.commit();
		} catch (SQLException e) {
			// e.printStackTrace();

			DBUtil.rollback(conn);

			throw e;
		} finally {
			DBUtil.close(pstmt);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}
	
	// 데이터 수정
	@Override
	public void updateMember(LoginDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "UPDATE customer " + "SET pwd = ?, tel = ?, email = ?, address = ? WHERE cus_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPwd());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getAddress());
			pstmt.setString(5, dto.getCus_id());

			pstmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			// e.printStackTrace();
			DBUtil.rollback(conn);

			throw e;
		} finally {
			DBUtil.close(pstmt);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}
	
	// 고객 아이디 삭제
	@Override
	public void deleteMember(String cus_id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "DELETE FROM customer WHERE cus_id=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cus_id);

			pstmt.executeUpdate();

			conn.commit();

		} catch (SQLException e) {
			// e.printStackTrace();

			DBUtil.rollback(conn);

			throw e;
		} finally {
			DBUtil.close(pstmt);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}
	//  admin 로그인
	@Override
	public LoginDTO adminfindById(String cus_id) {
		LoginDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT * FROM admin_tab WHERE adm_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cus_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new LoginDTO();

				dto.setCus_id(rs.getString("adm_id"));
				dto.setPwd(rs.getString("adm_pwd"));
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	// 고객 아이디 검색
	@Override
	public LoginDTO findById(String cus_id) {
		LoginDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT * FROM customer WHERE cus_id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cus_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new LoginDTO();

				dto.setCus_id(rs.getString("cus_id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_id(rs.getString("class_id"));
				dto.setDormancy(rs.getString("dormancy"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	// 고객 전체 리스트
	@Override
	public List<LoginDTO> listMember() {
		List<LoginDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM customer ORDER BY cus_id";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();
				dto.setCus_id(rs.getString("cus_id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_id(rs.getString("class_id"));
				dto.setDormancy(rs.getString("dormancy"));

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
	
	// 고객 이름 검색
	@Override
	public List<LoginDTO> listMember(String name) {
		List<LoginDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT * FROM customer WHERE name LIKE ? ORDER BY cus_id";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + name + "%");

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoginDTO dto = new LoginDTO();
				dto.setCus_id(rs.getString("cus_id"));
				dto.setPwd(rs.getString("pwd"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_id(rs.getString("class_id"));
				dto.setDormancy(rs.getString("dormancy"));

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
	
	// 로그인 이력 저장
	@Override
	public void insertLoginHistory(String cus_id) throws Exception {
		PreparedStatement pstmt = null;
	    String sql = null;

	    try {
	 
	        String loginId = "LOGIN_" + System.currentTimeMillis();

	        sql = "INSERT INTO customer_LoginRecord (Login_ID, Login_Date, CUS_ID) "
	            + "VALUES (?, SYSDATE, ?)";

	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, loginId);
	        pstmt.setString(2, cus_id);

	        int result = pstmt.executeUpdate();

	        if (result > 0) {
	            System.out.println("로그인 이력 저장 완료 → ID: " + cus_id);
	        } else {
	            System.out.println("로그인 이력 저장 실패...");
	        }

	        conn.commit();

	    } catch (Exception e) {
	        e.printStackTrace();
	        conn.rollback();
	    } finally {
	        if (pstmt != null) try { pstmt.close(); } catch (Exception ignored) {}
	    }
	}

	}


