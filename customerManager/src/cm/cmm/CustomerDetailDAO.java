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
	/**
	ID로 검색하여 고객의 모든 주문내역 조회
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerOrderListFindId(String id);
	/**
	입력된 마일리지 값의 이상 보유한 고객 조회
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerMileageMoreList(int moreMileage);
	/**
	고객 보유 마일리지 A이상 B이하를 조회하는 리스트
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerMileageBtween(int firstNumber, int SecondNumber, int page, int rows);
	int CustomerMileageBtweenCount(int firstNumber, int SecondNumber);
	/**
	생일인 고객을 조회하는 리스트
	@author	김설규
	*/
	public List<CustomerDetailDTO> CustomerDetailBirth();
}
