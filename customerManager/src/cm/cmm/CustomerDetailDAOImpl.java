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
	public List<CustomerDetailDTO> CustomerDetailList(int page, int rows) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		// 페이지 계산
		int startRow = (page - 1) * rows + 1;
		int endRow = page * rows;
		
		try {
			sql =   " SELECT * FROM ("
					+ "    SELECT ROWNUM rnum, tb.* FROM ("
					+ " with CUS_MILE AS("
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
					+ "    ) tb WHERE ROWNUM <= ?" // endRow
					+ " ) WHERE rnum >= ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, endRow);
			pstmt.setInt(2, startRow);
			
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
	고객 전체를 조회하는 리스트 페이징을 위한 count
	@author	김설규
	@return	result
	*/
	@Override
	public int CustomerDetailListCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT count(*)"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return result;
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
	public List<CustomerDetailDTO> CustomerDetailOrderBtween(int firstNumber, int secondNumber, int page, int rows) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		// 페이지 계산
		int startRow = (page - 1) * rows + 1;
		int endRow = page * rows;
		try {
			sql =  " SELECT * FROM ("
					+ "    SELECT ROWNUM rnum, tb.* FROM ("
					+ " with CUS_MILE AS("
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
	고객 총 구매내역의 A이상 B이하를 조회하는 리스트 페이징을 위한 count
	@author	김설규
	@return	result
	*/
	@Override
	public int CustomerDetailOrderBtweenCount(int firstNumber, int secondNumber) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT count(*)"
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

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return result;
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
	public List<CustomerDetailDTO> CustomerMileageMoreList(int moreMileage, int page, int rows) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		// 페이지 계산
		int startRow = (page - 1) * rows + 1;
		int endRow = page * rows;
		
		try {
			sql =   " SELECT * FROM ("
					+ "    SELECT ROWNUM rnum, tb.* FROM ("
					+ " with CUS_MILE AS("
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
					+ " ORDER BY NAME"
					+ "    ) tb WHERE ROWNUM <= ?"
					+ " ) WHERE rnum >= ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, moreMileage);
			pstmt.setInt(2, endRow);
			pstmt.setInt(3, startRow);
			
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
	입력된 마일리지 값의 이상 보유한 고객 조회 페이징을 위한 count
	@author	김설규
	@param int moreMileage
	@return result
	*/
	@Override
	public int CustomerMileageMoreListCount(int moreMileage) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "with CUS_MILE AS("
					+ "    SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
					+ " FROM ("
					+ "    SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage"
					+ " )"
					+ " WHERE rn = 1"
					+ " )"
					+ " SELECT count(*)"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " AND REMAIN_MIL >= ?"
					+ " ORDER BY NAME";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, moreMileage);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return result;
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
	/**
	고객 보유 마일리지 A이상 B이하를 조회하는 리스트 페이징을 위한 count
	@author	김설규
	@param int firstNumber, int SecondNumber
	@return list
	*/
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
	/**
	올해 월별 고객의 상품 구매 수 총계 조회 리스트
	@author	김설규
	@return list
	*/
	@Override
	public List<CustomerDetailDTO> MonthlyThisyearOrderCnt() {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "WITH Months_tb AS ("
					+ "    SELECT"
					+ "        TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'YYYY'), LEVEL - 1), 'MM') AS Lvemon"
					+ "    FROM DUAL"
					+ "    CONNECT BY LEVEL <= 12"
					+ " ),"
					+ " ORDER_COUNTS AS ("
					+ "    SELECT"
					+ "        TO_CHAR(ORDER_DATE, 'MM') AS Coumon,"
					+ "        COUNT(*) AS Months_order"
					+ "    FROM order_Details"
					+ "    WHERE ORDER_DATE >= TRUNC(SYSDATE, 'YYYY')"
					+ "        AND ORDER_DATE < ADD_MONTHS(TRUNC(SYSDATE, 'YYYY'), 12)"
					+ "    GROUP BY TO_CHAR(ORDER_DATE, 'MM')"
					+ " )"
					+ " SELECT"
					+ "    MON.Lvemon as lvemon,"
					+ "    NVL(ORDER_C.Months_order, 0) AS months_order"
					+ " FROM Months_tb MON, ORDER_COUNTS ORDER_C"
					+ " WHERE MON.Lvemon = ORDER_C.Coumon(+)"
					+ " UNION ALL"
					+ " SELECT"
					+ "    '올해 총 구매 수 : ' as a,"
					+ "    COUNT(*)"
					+ " FROM order_Details"
					+ " ORDER BY Lvemon";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setLvemon(rs.getString("lvemon"));
				dto.setMonths_order(rs.getString("months_order"));
				
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
	가장 최근 접속 일자가 1년 이상 된 고객 중 휴면처리가 되지 않은 고객 리스트
	@author	김설규
	@return list
	*/
	@Override
	public List<CustomerDetailDTO> CustomerDormancyUnList() {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "select cus.CUS_ID, cus.NAME, cus.DORMANCY, MAX(cus_log.login_date) as MAX_LOG_DATE"
					+ " from customer_LoginRecord cus_log, customer cus"
					+ " where cus_log.CUS_ID = cus.CUS_ID"
					+ " AND cus.DORMANCY = 'N'"
					+ " GROUP BY cus.CUS_ID, cus.NAME, cus.DORMANCY"
					+ " HAVING ADD_MONTHS(SYSDATE, -12) > MAX(cus_log.login_date)";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setDormancy(rs.getString("dormancy"));
				dto.setMax_log_date(rs.getString("max_log_date"));
				
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
	public List<CustomerDetailDTO> CustomerClassUnList() {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "SELECT"
					+ "    cus.CUS_ID,"
					+ "    cus.Name,"
					+ "    MAX(ord_Der.total_Cost) AS max_total_Cost,"
					+ "    cus.CLASS_ID"
					+ "    FROM order_Details ord_Der, customer cus"
					+ "    WHERE ord_Der.CUS_ID = cus.CUS_ID"
					+ "    GROUP BY cus.CUS_ID, cus.CLASS_ID, cus.Name"
					+ "    HAVING"
					+ "    (MAX(ord_Der.total_Cost) >= 100000 AND MAX(ord_Der.total_Cost) < 200000 AND cus.CLASS_ID != '5') OR"
					+ "    (MAX(ord_Der.total_Cost) >= 200000 AND MAX(ord_Der.total_Cost) < 300000 AND cus.CLASS_ID != '4') OR"
					+ "    (MAX(ord_Der.total_Cost) >= 300000 AND MAX(ord_Der.total_Cost) < 400000 AND cus.CLASS_ID != '3') OR"
					+ "    (MAX(ord_Der.total_Cost) >= 400000 AND MAX(ord_Der.total_Cost) < 500000 AND cus.CLASS_ID != '2') OR"
					+ "    (MAX(ord_Der.total_Cost) >= 500000 AND cus.CLASS_ID != '1')";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CustomerDetailDTO dto = new CustomerDetailDTO();
				
				dto.setId(rs.getString("cus_id"));
				dto.setName(rs.getString("name"));
				dto.setMax_total_Cost(rs.getString("max_total_Cost"));
				dto.setClass_Id(rs.getString("class_Id"));
				
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
