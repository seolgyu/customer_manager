package cm.cmm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class CustomerDetailDAOImpl implements CustomerDetailDAO{
	private Connection conn = DBConn.getConnection();
	/**
	고객 전체를 조회하는 리스트
	@author	김설규
	@return	list
	*/
	@Override
	public List<CustomerDetailDTO> CustomerDetailList() {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN, "
	                + "        cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_Level(rs.getString("class_Level"));
				dto.setRemain_Mil(rs.getString("remain_Mil"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	/**
	고객을 ID로 조회하는 리스트
	@author	김설규
	@param id
	@return	dto
	*/
	@Override
	public CustomerDetailDTO CustomerDetailFindId(String id) {
		CustomerDetailDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN, "
	                + "        cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " WHERE c.CUS_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_Level(rs.getString("class_Level"));
				dto.setRemain_Mil(rs.getString("remain_Mil"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	/**
	고객 총 구매내역의 A이상 B이하를 조회하는 리스트
	@author	김설규
	@param int firstNumber 
	@param int secondNumber
	@return list
	*/
	@Override
	public List<CustomerDetailDTO> CustomerDetailOrderBtween(int firstNumber, int secondNumber) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN, "
	                + "        cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY, or_det.TOTAL_COST AS TOTAL_COST"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " JOIN order_details or_det ON c.CUS_ID = or_det.CUS_ID"
					+ " WHERE or_det.TOTAL_COST BETWEEN ? AND ?"
					+ " ORDER BY NAME";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, firstNumber);
			pstmt.setInt(2, secondNumber);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_Level(rs.getString("class_Level"));
				dto.setRemain_Mil(rs.getString("remain_Mil"));
				dto.setTotal_cost(rs.getString("total_cost"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	/**
	ID로 검색하여 고객의 모든 주문내역 조회
	@author	김설규
	@param String id
	@return dto
	*/
	@Override
	public List<CustomerDetailDTO> CustomerOrderListFindId(String id) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT cus.CUS_ID ,cus.NAME, ord_det.ORDER_CODE, ord_det.ORDER_PRICE, ord_det.ORDER_DATE"
					+ " FROM customer cus, order_Details ord_det"
					+ " WHERE cus.CUS_ID = ord_det.CUS_ID"
					+ " AND cus.CUS_ID = ?"
					+ " ORDER BY ord_det.ORDER_DATE";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setOrder_code(rs.getString("order_code"));
				dto.setOrder_price(rs.getString("order_price"));
				dto.setOrder_date(rs.getString("order_date").toString());
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	/**
	입력된 마일리지 값의 이상 보유한 고객 조회
	@author	김설규
	@param int moreMileage
	@return list
	*/
	@Override
	public List<CustomerDetailDTO> CustomerMileageMoreList(int moreMileage) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN, "
	                + "        cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " AND REMAIN_MIL >= ?"
					+ " ORDER BY NAME";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, moreMileage);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_Level(rs.getString("class_Level"));
				dto.setRemain_Mil(rs.getString("remain_Mil"));
				dto.setDormancy(rs.getString("dormancy"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	/**
	고객 보유 마일리지 A이상 B이하를 조회하는 리스트
	@author	김설규
	@param int firstNumber 
	@param int secondNumber
	@return list
	*/
	@Override
	public List<CustomerDetailDTO> CustomerMileageBtween(int firstNumber, int secondNumber,  int page, int rows) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		// 페이지 계산
		int startRow = (page - 1) * rows + 1;
		int endRow = page * rows;
		
		try {
			sql =  " SELECT * FROM ("
					+ " SELECT ROWNUM rnum, tb.* FROM ("
					+ " with CUS_MILE AS("
					+ "	SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "	SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "			ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "	FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN,"
					+ "		cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " AND REMAIN_MIL BETWEEN ? AND ?"
					+ " ORDER BY NAME"
					+ "    ) tb WHERE ROWNUM <= ?"
					+ " ) WHERE rnum >= ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, firstNumber);
			pstmt.setInt(2, secondNumber);
			pstmt.setInt(3, endRow);
			pstmt.setInt(4, startRow);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_Level(rs.getString("class_Level"));
				dto.setRemain_Mil(rs.getString("remain_Mil"));
				dto.setDormancy(rs.getString("dormancy"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	@Override
	public int CustomerMileageBtweenCount(int firstNumber, int SecondNumber) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "with CUS_MILE AS("
					+ " SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ " SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "         ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ " FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT COUNT(*)"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " AND REMAIN_MIL BETWEEN ? AND ?"
					+ " ORDER BY NAME";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, firstNumber);
			pstmt.setInt(2, SecondNumber);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	/**
	생일인 고객을 조회하는 리스트
	@author	김설규
	@return list
	*/
	@Override
	public List<CustomerDetailDTO> CustomerDetailBirth() {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "with CUS_MILE AS("
					+ "	SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "	SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "			ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "	FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN,"
					+ "		cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " AND substr(c.rrn,3, 4) = TO_CHAR(SYSDATE, 'MMDD')"
					+ " ORDER BY NAME";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setAddress(rs.getString("address"));
				dto.setReg(rs.getString("reg"));
				dto.setRrn(rs.getString("rrn"));
				dto.setClass_Level(rs.getString("class_Level"));
				dto.setRemain_Mil(rs.getString("remain_Mil"));
				dto.setDormancy(rs.getString("dormancy"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	
	}
	
	
}
