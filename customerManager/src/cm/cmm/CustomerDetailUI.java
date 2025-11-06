package cm.cmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CustomerDetailUI {
	private CustomerDetailDAO dao = new CustomerDetailDAOImpl();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	/**
	메뉴를 실행하는 메서드
	@author	김설규
	@param	라파미터1
	@param	라파미터2
	@return	리턴 값
	*/
	public CustomerDetailUI() {
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
                    	CustomerDetailListAll();
                        break;
                    case 2:
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
	protected void CustomerDetailListAll() {
		System.out.println("고객 전체 내역 조회");
		List<CustomerDetailDTO> list = dao.CustomerDetailList();
		System.out.println("조회된 건 수" + list.size());
		
		for(CustomerDetailDTO dto : list) {
			System.out.print(dto.getId() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getTel() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.print(dto.getAddress() + "\t");
			System.out.print(dto.getReg() + "\t");
			System.out.print(dto.getRrn() + "\t");
			System.out.print(dto.getClass_Level() + "\t");
			System.out.println(dto.getRemain_Mil());
		}
	}
}
