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
					+ " WHERE or_det.TOTAL_COST BETWEEN ? AND ?";
			
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
	
}
