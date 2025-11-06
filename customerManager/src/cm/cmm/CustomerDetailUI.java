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
                System.out.println("|| 1.고객 전체 내역 조회 || 2.고객 ID 조회 || 3.뒤로가기 ||");
                ch = Integer.parseInt(br.readLine());

                switch (ch) {
                    case 1:
                    	CustomerDetailListAll();
                        break;
                    case 2:
                    	CustomerDetailFindId();
                    	break;
                    case 3:
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
	고객 전체 내역을 조회하는 메서드
	@author	김설규
	@param	dto
	*/
	protected void CustomerDetailOrderBtween() {
		int firstNumber, secondNumber;
	}
}
