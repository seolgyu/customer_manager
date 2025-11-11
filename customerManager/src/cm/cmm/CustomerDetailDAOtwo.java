package cm.cmm;

import java.util.List;

// dataCount는 페이징 처리하기 위해 데이터 건수 구하는 메소드(각 기능마다 구현됨)

public interface CustomerDetailDAOtwo {
	/**
	이름으로 고객을 검색하는 메소드
	@param String
	@return List<CustomerDetailDTO>
	@author	권혁찬
	*/
	public List<CustomerDetailDTO> customerFindByName(String name, int page, int rows);
	int dataCountByName(String name);
	
	
	/**
	이름으로 고객을 검색하는 메소드
	@param int
	@return List<CustomerDetailDTO>
	@author	권혁찬
	*/
	public List<CustomerDetailDTO> customerFindByTotalCost(int cost, int page, int rows);
	public int dataCountByTotalCost(int cost);
	
	
	
	/**
	고객 등급으로 조회하는 메소드
	@param int
	@return List<CustomerDetailDTO>
	@author	권혁찬
	*/
	List<CustomerDetailDTO> customerFindByGrade(int page, int rows);
	/**
	등급별 합계와 총계
	@author	권혁찬
	*/
	void CustomerCountByGrade();
	int dataCountTotalCus();

	
}
