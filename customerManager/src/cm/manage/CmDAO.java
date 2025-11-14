package cm.manage;

import java.sql.SQLException;
import java.util.List;

public interface CmDAO {
	 
	
	public List<CmDTO> listMileage(String cus_id);
	// 아이디름 검색하여 고객 마일리지 리스트 조회 
	
	public int updateGrade() throws SQLException;
	// 고객 등급 일괄 업데이트 
	
	public int updateDormancy() throws SQLException;
	// 휴면 처리 일괄 업데이트 
	
	public int InsertMileage(CmDTO dto) throws SQLException;
	// 마일리지 관리자 임의로 추가
	
	public int updateInformation(CmDTO dto) throws SQLException;
	// 고객 개인정보 수정 
	
	public int deleteCustomer(String id) throws SQLException;
	// 고객 삭제 
	
		


	
}
