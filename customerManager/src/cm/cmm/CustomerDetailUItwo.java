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

			int page = 1;
			int rows = 10;

			int totalData = dao2.dataCountByName(name);
			if (totalData == 0) {
				System.out.println("검색된 데이터가 없습니다.");
				return;
			}

			int totalPage = (int) (Math.ceil((double) totalData / rows));

			while (true) {
				List<CustomerDetailDTO> list = dao2.customerFindByName(name, page, rows);

				System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
				System.out.println("||고객ID	||		이름		||		연락처		||		이메일		||		주소		||		등록일		||		주민번호		||		고객등급		||		보유마일리지(단위 : 포인트)");
				
				if (list.isEmpty() && page > 1) {
					System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
					page--;
					continue;
				}

				for (CustomerDetailDTO dto : list) {
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
				
				System.out.println("----------------------------------------------------------------------------------");
				System.out.printf("  페이지: %d / %d \n", page, totalPage);
				System.out.print(" [P]이전  [N]다음  [숫자]페이지 이동  [M]메인 : ");
				String ch = br.readLine();
				
				if(ch.equalsIgnoreCase("P")) { 
					if(page > 1) {
						page--;
					} else {
						System.out.println("첫 페이지입니다.");
					}
				} else if (ch.equalsIgnoreCase("N")) {
					if(page < totalPage) {
						page++;
					} else {
						System.out.println("마지막 페이지입니다.");
					}
				} else if (ch.equalsIgnoreCase("M")) {
					break;
				} else {
					try {
						int p = Integer.parseInt(ch);
						if(p >= 1 && p <= totalPage) {
							page = p;
						} else {
							System.out.println("페이지 범위를 벗어났습니다.");
						}
					} catch (NumberFormatException e) {
						System.out.println("잘못된 입력입니다.");
					}
				}
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
			
			int page = 1;
			int rows = 10;

			int totalData = dao2.dataCountByTotalCost(cost);
			if (totalData == 0) {
				System.out.println("검색된 데이터가 없습니다.");
				return;
			}
			int totalPage = (int) (Math.ceil((double) totalData / rows));
			
			while(true) {
				List<CustomerDetailDTO> list = dao2.customerFindByTotalCost(cost, totalPage, rows);
				System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
				System.out.println("||고객ID	||		이름		||		연락처		||		이메일		||		주소		||		등록일		||		주민번호		||		고객등급		||		보유마일리지(단위 : 포인트)");
				if (list.isEmpty() && page > 1) {
					System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
					page--;
					continue;
				}
				
				for (CustomerDetailDTO dto : list) {
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
				System.out.println("----------------------------------------------------------------------------------");
				
				System.out.printf("  페이지: %d / %d \n", page, totalPage);
				System.out.print(" [P]이전  [N]다음  [숫자]페이지 이동  [M]메인 : ");
				String ch = br.readLine();
				
				if(ch.equalsIgnoreCase("P")) { // 이전
					if(page > 1) {
						page--;
					} else {
						System.out.println("첫 페이지입니다.");
					}
				} else if (ch.equalsIgnoreCase("N")) { // 다음
					if(page < totalPage) {
						page++;
					} else {
						System.out.println("마지막 페이지입니다.");
					}
				} else if (ch.equalsIgnoreCase("M")) {
					break; 
				} else {
					try {
						int p = Integer.parseInt(ch);
						if(p >= 1 && p <= totalPage) {
							page = p;
						} else {
							System.out.println("페이지 범위를 벗어났습니다.");
						}
					} catch (NumberFormatException e) {
						System.out.println("잘못된 입력입니다.");
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
