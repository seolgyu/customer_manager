package cm.cmm;

import java.util.List;

public interface CustomerDetailDAO {
	/**
	고객 전체를 조회하는 리스트
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerDetailList();
}
