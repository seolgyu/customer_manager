package cm.cmm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class CustomerDetailDAOImpltwo implements CustomerDetailDAOtwo{
	private Connection conn = DBConn.getConnection();
	
	@Override
	public int dataCountByName(String name) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "with CUS_MILE AS(" 
					+ "    SELECT CUS_ID" 
					+ " FROM ("
					+ "    SELECT CUS_ID,"
					+ "           ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
					+ "    FROM customer_Mileage" 
					+ " )" 
					+ " WHERE rn = 1" 
					+ " )"
					+ " SELECT COUNT(*)" 
					+ " FROM customer c"
					+ " JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
					+ " JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
					+ " WHERE INSTR(NAME, ?) > 0";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
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
	
	@Override
	public List<CustomerDetailDTO> customerFindByName(String name, int page, int rows) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		// 페이지 계산
		int startRow = (page - 1) * rows + 1;
		int endRow = page * rows;

		try {
			// 요청하신 ROWNUM 쿼리 템플릿 적용
			sql =  " SELECT * FROM ("
				 + "    SELECT ROWNUM rnum, tb.* FROM ("
				 // -- 안쪽 쿼리 (tb) : 원본 쿼리 + 정렬 --
				 + "        with CUS_MILE AS("
				 + "            SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE"
				 + "            FROM ("
				 + "                SELECT MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID,"
				 + "                       ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn"
				 + "                FROM customer_Mileage"
				 + "            ) WHERE rn = 1"
				 + "        )"
				 + "        SELECT c.CUS_ID AS CUS_ID, NAME, TEL, EMAIL, ADDRESS, REG, RPAD(SUBSTR(RRN, 1, 8), lENGTH(RRN), '*') RRN, "
				 + "               cC.CLASS_LEVEL AS CLASS_LEVEL, NVL(REMAIN_MIL, 0) REMAIN_MIL, DORMANCY"
				 + "        FROM customer c"
				 + "        JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID"
				 + "        JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID"
				 + "        WHERE INSTR(NAME, ?) > 0"
				 + "        ORDER BY c.CUS_ID ASC" // <-- 페이징을 위한 정렬 기준
				 // -- 안쪽 쿼리(tb) 끝 --
				 + "    ) tb WHERE ROWNUM <= ?" // endRow
				 + " ) WHERE rnum >= ?"; // startRow
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, name);
			pstmt.setInt(2, endRow);   // ROWNUM <= ? (endRow)
			pstmt.setInt(3, startRow); // rnum >= ? (startRow)

			rs = pstmt.executeQuery();

			while (rs.next()) {
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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	@Override
	public int dataCountByTotalCost(int cost) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql =  "SELECT COUNT(*) "
					+ "FROM ("
					+ "    SELECT c.CUS_ID, SUM(od.ORDER_PRICE) totalCost"
					+ "    FROM customer c"
					+ "    JOIN ORDER_DETAILS od ON c.CUS_ID = od.CUS_ID"
					+ "    GROUP BY c.CUS_ID"
					+ "    HAVING SUM(od.ORDER_PRICE) > ?"
					+ " )";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cost);
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
	
	@Override
	public List<CustomerDetailDTO> customerFindByTotalCost(int cost, int page, int rows) {
		List<CustomerDetailDTO> list = new ArrayList<CustomerDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		int startRow = (page - 1) * rows + 1;
		int endRow = page * rows;
		
		try {
			sql =  " SELECT * FROM ("
				 + "    SELECT ROWNUM rnum, tb.* FROM ("
				 + "        WITH CUS_MILE AS("
				 + "            SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE "
				 + "            FROM ("
				 + "                SELECT CUS_ID, REMAIN_MIL, MILEAGE_DATE, "
				 + "                       ROW_NUMBER() OVER (PARTITION BY CUS_ID ORDER BY MILEAGE_DATE DESC, MILEAGE_ID DESC) as rn "
				 + "                FROM customer_Mileage"
				 + "            ) "
				 + "            WHERE rn = 1"
				 + "        ) "
				 + "        SELECT c.CUS_ID AS CUS_ID, c.NAME AS NAME, c.TEL AS TEL, c.EMAIL AS EMAIL, c.ADDRESS AS ADDRESS, TO_CHAR(c.REG, 'YYYY-MM-DD') AS REG, "
				 + "               RPAD(SUBSTR(c.RRN, 1, 8), lENGTH(c.RRN), '*') AS RRN, cC.CLASS_LEVEL AS CLASS_LEVEL, "
				 + "               NVL(cM.REMAIN_MIL, 0) AS REMAIN_MIL, c.DORMANCY AS DORMANCY, SUM(od.ORDER_PRICE) AS totalCost "
				 + "        FROM customer c "
				 + "        JOIN CUS_MILE cM ON c.CUS_ID = cM.CUS_ID "
				 + "        JOIN customer_Class cC ON c.CLASS_ID = cC.CLASS_ID "
				 + "        JOIN ORDER_DETAILS od ON c.CUS_ID = od.CUS_ID "
				 + "        GROUP BY c.CUS_ID, c.NAME, c.TEL, c.EMAIL, c.ADDRESS, c.REG, RPAD(SUBSTR(c.RRN, 1, 8), lENGTH(c.RRN), '*'), "
				 + "                 cC.CLASS_LEVEL, NVL(cM.REMAIN_MIL, 0), c.DORMANCY "
				 + "        HAVING SUM(od.ORDER_PRICE) >= ? "
				 + "        ORDER BY c.CUS_ID ASC" 
				 + "    ) tb WHERE ROWNUM <= ?" 
				 + " ) WHERE rnum >= ?"; 
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, cost);
			pstmt.setInt(2, endRow);   // ROWNUM <= ? (endRow)
			pstmt.setInt(3, startRow); // rnum >= ? (startRow)
			
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	@Override
	public List<CustomerDetailDTO> customerFindByGrade(int grade){
		return null;
	}
	
	
}
