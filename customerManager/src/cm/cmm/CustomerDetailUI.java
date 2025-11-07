package cm.cmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CustomerDetailUI {
	private CustomerDetailDAO dao = new CustomerDetailDAOImpl();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public CustomerDetailUI() {
		CustomerDetailMeun();
	}
	
	public void CustomerDetailMeun() {
		boolean starting = true;
		int ch;
		
		while (starting) {
            try {
                System.out.printf("|| 1.고객 전체 내역 조회 || 2.고객 ID 조회 || 3.고객 총 구매금액 범위 조건 조회 || 4.고객 주문내역 ID조회 ||5. 일정 값 이상 마일리지 고객 조회 ||%n||6.뒤로가기");
                ch = Integer.parseInt(br.readLine());

                switch (ch) {
                    case 1:
                    	CustomerDetailListAll();
                        break;
                    case 2:
                    	CustomerDetailFindId();
                    	break;
                    case 3:
                    	CustomerDetailOrderBtween();
                    	break;
                    case 4:
                    	CustomerOrderListFindId();
                    	break;
                    case 5:
                    	CustomerMileageMoreList();
                    	break;
                    case 6:
                		System.out.println("임시 뒤로가기(프로그램 종료)");
                		System.exit(0);
                }
            } catch (NumberFormatException e) {
            		e.printStackTrace();
            } catch (Exception e) {
            		e.printStackTrace();
            }
        }
	}
	/**
	고객 전체 내역을 조회하는 메서드
	@author	김설규
	@param	dto
	*/
	protected void CustomerDetailListAll() {
		System.out.println("고객 전체 내역 조회");
		List<CustomerDetailDTO> list = dao.CustomerDetailList();
		System.out.println("||고객ID		 이름		연락처		  이메일			 주소				등록일			주민번호		고객등급		보유마일리지(단위 : 포인트)||");
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-12s", dto.getName());
			System.out.printf("%-20s", dto.getTel());
			System.out.printf("%-20s", dto.getEmail());
			System.out.printf("%-25s", dto.getAddress());
			System.out.printf("%-25s", dto.getReg());
			System.out.printf("%-23s", dto.getRrn());
			System.out.printf("%-20s", dto.getClass_Level());
			System.out.printf("%-11s", dto.getRemain_Mil() + " 포인트");
			System.out.println();
		}
		System.out.println("조회된 건 수 : " + list.size() + "건");
	}
	
	/**
	고객을 ID로 조회하는 리스트
	@author	김설규
	@return	dto
	*/
	protected void CustomerDetailFindId() {
		System.out.println("고객 ID 검색 조회");
		String id;
		try {
			System.out.println("검색 고객의 ID를 입력해주세요.");
			id = br.readLine();
			
			CustomerDetailDTO dto = dao.CustomerDetailFindId(id);
			
			if(dto == null) {
				System.out.println("존재하지 않는 ID입니다.");
				return;
			}
			System.out.println("||고객ID		 이름		연락처		  이메일			 주소				등록일			주민번호		고객등급		보유마일리지(단위 : 포인트)||");
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-12s", dto.getName());
			System.out.printf("%-20s", dto.getTel());
			System.out.printf("%-20s", dto.getEmail());
			System.out.printf("%-25s", dto.getAddress());
			System.out.printf("%-25s", dto.getReg());
			System.out.printf("%-23s", dto.getRrn());
			System.out.printf("%-20s", dto.getClass_Level());
			System.out.printf("%-11s", dto.getRemain_Mil() + " 포인트");
			System.out.println();
			System.out.println("조회된 건 수 : " + "1건");
			
			
		}catch (NullPointerException e) {
			System.out.println("재입력 해주세요.");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	고객 총 구매금액 범위 조건 조회
	@author	김설규
	@param	firstNumber, secondNumber
	*/
	protected void CustomerDetailOrderBtween() {
		System.out.println("고객 총 구매금액 범위 조건 조회");
		System.out.println("원하시는 총 구매금액의 범위를 입력 해주세요.");
		int firstNumber, secondNumber;
		
		try {
		System.out.println("원하시는 ? 원 이상");
		firstNumber = Integer.parseInt(br.readLine());
		System.out.println("원하시는 ? 원 이하");
		secondNumber = Integer.parseInt(br.readLine());
			
		List<CustomerDetailDTO> list = dao.CustomerDetailOrderBtween(firstNumber, secondNumber);
		
		if(list.size() == 0) {
			System.out.println("해당 범위 내에 고객은 존재하지 않습니다.");
			return;
		}
		
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-12s", dto.getName());
			System.out.printf("%-20s", dto.getTel());
			System.out.printf("%-20s", dto.getEmail());
			System.out.printf("%-25s", dto.getAddress());
			System.out.printf("%-25s", dto.getReg());
			System.out.printf("%-23s", dto.getRrn());
			System.out.printf("%-20s", dto.getClass_Level());
			System.out.printf("%-11s", dto.getRemain_Mil() + "포인트");
			System.out.printf("%-10s", dto.getTotal_cost() + "원");
			System.out.println();
		}
			System.out.println("조회된 건 수 : " + list.size() + "건");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	/**
	고객ID로 모든 주문내역 조회 
	@author	김설규
	@param	id
	@return list
	*/
	protected void CustomerOrderListFindId() {
		System.out.println("고객 주문내역 조회");
		String id;
		
		try {
			System.out.println("조회하고자 하는 ID를 입력해주세요");
		id = br.readLine();
			
		List<CustomerDetailDTO> list = dao.CustomerOrderListFindId(id);
		
		if(list.size() == 0) {
			System.out.println("존재하지 않는 ID입니다.");
			return;
		}
		
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-12s", dto.getName());
			System.out.printf("%-20s", dto.getOrder_code());
			System.out.printf("%-20s", dto.getOrder_price());
			System.out.printf("%-25s", dto.getOrder_date());
			System.out.println();
		}
			System.out.println("조회된 건 수 : " + list.size() + "건");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	입력된 마일리지 값의 이상 보유한 고객 조회
	@author	김설규
	@param int moreMileage
	@return list
	*/
	protected void CustomerMileageMoreList() {
		System.out.println("입력된 마일리지 값의 이상 보유한 고객 조회");
		System.out.println("검색하고자 하는 마일리지의 값을 입력해주세요.");
		int moreMileage;
		
		try {
		System.out.println("원하시는 ? 포인트 입력");
		moreMileage = Integer.parseInt(br.readLine());
			
		List<CustomerDetailDTO> list = dao.CustomerMileageMoreList(moreMileage);
		
		if(list.size() == 0) {
			System.out.println("해당 범위 내에 고객은 존재하지 않습니다.");
			return;
		}
		
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-12s", dto.getName());
			System.out.printf("%-20s", dto.getTel());
			System.out.printf("%-20s", dto.getEmail());
			System.out.printf("%-25s", dto.getAddress());
			System.out.printf("%-25s", dto.getReg());
			System.out.printf("%-23s", dto.getRrn());
			System.out.printf("%-20s", dto.getClass_Level());
			System.out.printf("%-11s", dto.getRemain_Mil() + "포인트");
			System.out.printf("%-20s", dto.getDormancy());
			System.out.println();
		}
			System.out.println("조회된 건 수 : " + list.size() + "건");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
}
