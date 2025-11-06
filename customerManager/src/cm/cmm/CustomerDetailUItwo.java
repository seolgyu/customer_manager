package cm.cmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CustomerDetailUItwo {
	private CustomerDetailDAOtwo dao2 = new CustomerDetailDAOImpltwo();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	/**
	메뉴를 실행하는 메서드
	@author	김설규
	@param	라파미터1
	@param	라파미터2
	@return	리턴 값
	*/
	public CustomerDetailUItwo() {
		CustomerDetailMeun();
	}
	
	public void CustomerDetailMeun() {
		boolean starting = true;
		int ch;
		
		while (starting) {
            try {
                System.out.println("|| 1.고객 전체 내역 조회 || 2.뒤로가기 ||");
                ch = Integer.parseInt(br.readLine());

                switch (ch) {
                    case 1:
                    	customerFindByName();
                        break;
                    case 2:
                    	customerFindByTotalCost();
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
	protected void customerFindByName() {
		System.out.println("[고객 이름 검색]");
		String name;
		
		try {
			System.out.print("이름을 입력 : ");
			name = br.readLine();
			
			List<CustomerDetailDTO> list = dao2.customerFindByName(name);
			
			System.out.println("조회된 건 수 : " + list.size() + "건");
			System.out.println("||고객ID	||		이름		||		연락처		||		연락처		||		이메일		||		주소		||		등록일		||		주민번호		||		고객등급		||		보유마일리지(단위 : 포인트)");
			for(CustomerDetailDTO dto : list) {
				System.out.print(dto.getId() + "\t\t");
				System.out.print(dto.getName() + "\t\t");
				System.out.print(dto.getTel() + "\t\t");
				System.out.print(dto.getEmail() + "\t\t");
				System.out.print(dto.getAddress() + "\t\t");
				System.out.print(dto.getReg() + "\t\t");
				System.out.print(dto.getRrn() + "\t\t");
				System.out.print(dto.getClass_Level() + "\t\t");
				System.out.println(dto.getRemain_Mil());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void customerFindByTotalCost() {
		System.out.println("[총 금액 검색]");
		int cost;
		
		try {
			System.out.print("금액 입력 : ");
			cost = Integer.parseInt(br.readLine());
			
			List<CustomerDetailDTO> list = dao2.customerFindByTotalCost(cost);
			
			System.out.println("조회된 건 수 : " + list.size() + "건");
			System.out.println("||고객ID	||		이름		||		연락처		||		연락처		||		이메일		||		주소		||		등록일		||		주민번호		||		고객등급		||		보유마일리지(단위 : 포인트)");
			for(CustomerDetailDTO dto : list) {
				System.out.print(dto.getId() + "\t\t");
				System.out.print(dto.getName() + "\t\t");
				System.out.print(dto.getTel() + "\t\t");
				System.out.print(dto.getEmail() + "\t\t");
				System.out.print(dto.getAddress() + "\t\t");
				System.out.print(dto.getReg() + "\t\t");
				System.out.print(dto.getRrn() + "\t\t");
				System.out.print(dto.getClass_Level() + "\t\t");
				System.out.println(dto.getRemain_Mil());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
