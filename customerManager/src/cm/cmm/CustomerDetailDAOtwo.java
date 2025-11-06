package cm.cmm;

import java.util.List;

public interface CustomerDetailDAOtwo {
	/**
	이름으로 고객을 검색하는 메소드
	@param String
	@return List<CustomerDetailDTO>
	@author	권혁찬
	*/
	public List<CustomerDetailDTO> customerFindByName(String name);
	
	/**
	이름으로 고객을 검색하는 메소드
	@param int
	@return List<CustomerDetailDTO>
	@author	권혁찬
	*/
	public List<CustomerDetailDTO> customerFindByTotalCost(int cost);
}
