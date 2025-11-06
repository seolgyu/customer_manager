package cm.man;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.util.DBUtil;

public class CustomerDetailDAOImpl implements CustomerDetailDAO{
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
			sql = "";
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
}
