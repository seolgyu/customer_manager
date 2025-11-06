package cm.cmm;

import java.util.List;

public interface CustomerDetailDAO {
	/**
	고객 전체를 조회하는 리스트
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerDetailList();
	/**
	고객을 ID로 조회하는 리스트
	@author	김설규
	*/
	public CustomerDetailDTO CustomerDetailFindId(String id);
	/**
	고객 총 구매내역의 A이상 B이하를 조회하는 리스트
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerDetailOrderBtween(int firstNumber, int SecondNumber);
}
