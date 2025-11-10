package cm.cmm;

import java.util.List;

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
	회원 등급으로 조회하는 메소드
	@param int
	@return List<CustomerDetailDTO>
	@author	권혁찬
	*/
	List<CustomerDetailDTO> customerFindByGrade(int grade);

	
}
