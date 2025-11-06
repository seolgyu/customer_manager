package cm.cmm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
					+ " SELECT c.CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN, cC.CLASS_LEVEL, NVL(REMAIN_MIL, 0)"
					+ "        DORMANCY"
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
}
