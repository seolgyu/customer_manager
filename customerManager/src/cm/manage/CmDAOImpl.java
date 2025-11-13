package cm.manage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;



public class CmDAOImpl implements CmDAO{

	private Connection conn = DBConn.getConnection();
	
	
	@Override // 리스트 
	public List<CmDTO> listCustomer() {
		List<CmDTO> list = new ArrayList<CmDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "with CUS_MILE AS( "
					+ " SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "	SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "	ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "	FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1 "
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN,"
					+ "	cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY, or_det.TOTAL_COST AS TOTAL_COST"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " JOIN order_details or_det ON c.CUS_ID = or_det.CUS_ID";
					
			
			pstmt = conn.prepareCall(sql);

			
			rs = pstmt.executeQuery();

			/*
			total_cost, customer.class_id, class_level, customer.cus_id, name, pwd, 
			tel, email, address, reg, rrn, dormancy, login_date, mileage_id, 
			milaege_YN, CHANGE_mil, remain_mil, Mileage_date
			 */
			
			
			while(rs.next()) {
				CmDTO dto = new CmDTO();
				dto.setCus_id(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_level(rs.getNString("class_level"));
				dto.setRemain_mil(rs.getInt("remain_mil"));
				dto.setDormancy(rs.getString("dormancy"));
				dto.setTotal_cost(rs.getInt("total_cost"));
		
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

	
	
	
	@Override // 등급 업데이트 
	public int updateGrade() throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql;
		String sql2;
		int n;
		
		
		try {
			
		sql = 	"SELECT c.cus_id, NVL(sum(order_price),0) total_cost"
				+ " FROM customer c"
				+ " LEFT JOIN order_Details cD ON c.cus_id = cD.cus_id"
				+ " GROUP BY c.cus_id"
				+ " ORDER BY c.cus_id";
			
		pstmt = conn.prepareStatement(sql);
		
		rs = pstmt.executeQuery();
		
		
		sql2 = "UPDATE customer"
				+ " SET class_id = ?"
				+ " WHERE cus_id = ?";
		
		pstmt2 = conn.prepareStatement(sql2);
		
		
		while(rs.next()) {
		   String cus_id = rs.getString("cus_id");
		   int total_cost = rs.getInt("total_cost");
		   
		   if(total_cost >= 5000000) {
			   n = 1;
		   } else if(total_cost >= 4000000) {
			   n = 2;
		   } else if(total_cost >= 3000000) {
			   n = 3;
		   } else if(total_cost >= 2000000) {
			   n = 4;
		   } else if(total_cost >= 1000000) {
			   n = 5;
		   } else {
			   n = 0;
		   }
		   
		   pstmt2.setInt(1, n);
		   pstmt2.setString(2, cus_id);

		   result = pstmt2.executeUpdate();
	
		   
		} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			DBUtil.close(pstmt2);
		}
			
		return result;
	}

	
	
	public int updateDormancy() throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		String sql;
		String sql2;
		
		try {
			sql = "UPDATE customer c"
					+ " SET c.dormancy = 'Y'"
					+ " WHERE c.cus_id IN ("
					+ " SELECT cus_id"
					+ " FROM ("
					+ " SELECT cus_id,"
					+ " login_date,"
					+ " ROW_NUMBER() OVER (PARTITION BY cus_id ORDER BY login_date DESC, login_id DESC) AS log"
					+ " FROM customer_LoginRecord"
					+ " )"
					+ " WHERE log = 1"
					+ " AND login_date <= ADD_MONTHS(SYSDATE, -12)"
					+ " )";
			
			pstmt = conn.prepareStatement(sql);
				
			result = pstmt.executeUpdate();
			
			
			
			sql2 =  " UPDATE customer c"
					+ " SET c.dormancy = 'N'"
					+ " WHERE c.cus_id IN ("
					+ " SELECT cus_id"
					+ " FROM ("
					+ " SELECT cus_id,"
					+ " login_date,"
					+ " ROW_NUMBER() OVER (PARTITION BY cus_id ORDER BY login_date DESC, login_id DESC) AS log"
					+ " FROM customer_LoginRecord"
					+ " )"
					+ " WHERE log = 1"
					+ " AND login_date > ADD_MONTHS(SYSDATE, -12)"
					+ " )";

					
			pstmt2 = conn.prepareStatement(sql2);
			
			result = pstmt2.executeUpdate();
			
					
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(pstmt2);
			
		}
		
		return result;
	}
	
	
	
	
	@Override //회원 아이디를 기준으로 검색 하여 해당 이름의 회원 마일리지 관리자 임의로 수정
	public int updateMileage(CmDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
// mileage_id, mileage_yn, CHANGE_MIL, remain_mil, Mileage_date, order_code, 
		
		try {
			sql = "UPDATE customer_Mileage SET mileage_yn = ?, CHANGE_MIL = ?, remain_mil = ?, Mileage_date = ?"
					+ " WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
	
			pstmt.setString(1, dto.getMileage_yn());
			pstmt.setInt(2, dto.getChange_mil());
			pstmt.setInt(3, dto.getRemain_mil());
			pstmt.setString(4, dto.getMileage_date());
			pstmt.setString(5, dto.getCus_id());

			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	
	
	

	
	@Override 
	//고객 회원정보 : 고객ID, 고객이름, 주민번호, 등록일은 수정 불가 
	// 연락처, 주소, 이메일 수정 가능
	public int updateInformation(CmDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "UPDATE customer SET tel = ?, address = ?, email = ? "
				+ " WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTel());
			pstmt.setString(2, dto.getAddress());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getCus_id());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
	}	
		return result;
	}

	
	
	
	@Override
	public int deleteCustomer(String id) throws SQLException {
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		String sql;
		
		/*
		고객 로그인 기록 = customer_LoginRecord Cus_id
		문의 상담 = consultation_Log   Cus_id
		고객마일리지 = customer_Mileage cus_id(order_code)
		상품주문내역 = order_Details cus_id
		고객기본정보 = customer cus_id
		 */
		
		try {
			// 고객 로그인 기록 삭제 
			sql = "DELETE FROM customer_LoginRecord WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			
			
			// 문의 상담 내역 삭제
			sql = "DELETE FROM CONSULTATION WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			
			
			// 고객마일리지 내역 삭제
			sql = "DELETE FROM customer_Mileage WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			
			
			// 상품 주문내역 삭제
			sql = "DELETE FROM order_Details WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			
			
			// 고객 기본정보 삭제 
			sql = "DELETE FROM customer WHERE cus_id = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			result = pstmt.executeUpdate();
			
			
			
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}




	@Override // 마일리지 데이터 추가
	public int InsertMileage(CmDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
	
		String sql;
	
		try {
			
		
			sql = " INSERT INTO customer_Mileage(MILEAGE_ID,MILEAGE_YN,CHANGE_MIL,REMAIN_MIL,MILEAGE_DATE,ORDER_CODE,CUS_ID)"
					+ " VALUES(mileage_seq.NEXTVAL, "
					+ " CASE WHEN 0 > ? THEN '관리자회수'"
					+ " WHEN 0 <= ? THEN '관리자추가'"
					+ " ELSE '문제발생'"
					+ " END,"
					+ " ?, (? + (SELECT REMAIN_MIL FROM (SELECT * FROM CUSTOMER_MILEAGE ORDER BY MILEAGE_DATE DESC) WHERE CUS_ID = ? AND ROWNUM = 1)),"
					+ "  SYSDATE, null, ?)";
				
		pstmt = conn.prepareStatement(sql);
			
		pstmt. setInt(1, dto.getChange_mil());
		pstmt. setInt(2, dto.getChange_mil());
		pstmt. setInt(3, dto.getChange_mil());
		pstmt. setInt(4, dto.getChange_mil());
		pstmt. setString(5, dto.getCus_id());
		pstmt. setString(6, dto.getCus_id());
		
		result = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}

	

	@Override // 아이디를 검색하여 마일리지 리스트 조회
	public List<CmDTO> listMileage(String cus_id) {
		List<CmDTO> list = new ArrayList<CmDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT * FROM CUSTOMER_MILEAGE"
					+ " WHERE cus_id = ?"
					+ " ORDER BY mileage_id";
					
					
			
			pstmt = conn.prepareCall(sql);
			
			pstmt.setString(1, cus_id);
			
			rs = pstmt.executeQuery();

			
			while(rs.next()) {
				CmDTO dto = new CmDTO();
				dto.setMileage_id(rs.getString("mileage_id"));
				dto.setMileage_yn(rs.getString("mileage_yn"));
				dto.setChange_mil(rs.getInt("change_mil"));
				dto.setRemain_mil(rs.getInt("remain_mil"));
				dto.setMileage_date(rs.getString("mileage_date"));
				dto.setCus_id(rs.getString("cus_id"));
				
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
