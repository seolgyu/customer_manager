package cm.cmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class CustomerDetailUI {
	private CustomerDetailDAO dao = new CustomerDetailDAOImpl();
	private CustomerDetailDAOtwo dao2 = new CustomerDetailDAOImpltwo();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	//등급
	public void CustomerDetailClass() {
		CustomerClassUnList();
	}
	//휴면
	public void CustomerDetailDormancy() {
		CustomerDormancyUnList();
	}
	
	public void CustomerDetailMeun() {
		boolean starting = true;
		int ch;
		
		while (starting) {
            try {
                System.out.printf("|| 1.고객 전체 내역 조회 ||%n|| 2.고객 ID 조회 ||%n|| 3. 고객 이름 검색 ||%n|| 4.총 구매 금액 고객 검색 ||%n|| 5.고객 총 구매금액 범위 조건 조회 ||%n|| 6.고객 주문내역 ID조회 ||%n||"
                		+ " 7. 일정 값 이상 마일리지 고객 조회 ||%n|| 8.고객 보유마일리지 범위 조건 조회 ||%n|| 9. 등급 통계 ||%n|| 10.생일 고객 조회 ||%n|| 11.년도, 월별 고객 상품 구매 수 ||%n|| 12. 올해 월별 고객 상품 구매 수 ||%n|| 13.상품별 구매 수 ||%n|| 14.로그인 기록 ||%n|| 15.뒤로가기");
                ch = Integer.parseInt(br.readLine());
                 
                if(ch == 15) {
                	// new MainUI().menuCustomer();
                	return;
                }

                switch (ch) {
                    case 1:
                    	CustomerDetailListAll();
                        break;
                    case 2:
                    	CustomerDetailFindId();
                    	break;
                    case 3:
                    	customerFindByName();
                    	break;
                    case 4:
                    	customerFindByTotalCost();
                    	break;
                    case 5:
                    	CustomerDetailOrderBtween();
                    	break;
                    case 6:
                    	CustomerOrderListFindId();
                    	break;
                    case 7:
                    	CustomerMileageMoreList();
                    	break;
                    case 8:
                    	CustomerMileageBtween();
                    	break;
                    case 9:
                    	customerFindByGrade();
                    	break;
                    case 10:
                    	CustomerDetailBirth();
                    	break;
                    case 11:
                    	yearlyMonthlyBuyStats();
                    	break;
                    case 12:
                    	MonthlyThisyearOrderCnt();
                    	break;
                    case 13:
                    	purchaseCountsByProduct();
                    	break;
                    case 14:
                    	customerLoginHistory();
                    	break;
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
		try {
		int page = 1;
		int rows = 10;
		
		int totalData = dao.CustomerDetailListCount();
		if (totalData == 0) {
			System.out.println("검색된 데이터가 없습니다.");
			return;
		}
		
		int totalPage = (int) (Math.ceil((double) totalData / rows));
		while (true) {
			List<CustomerDetailDTO> list = dao.CustomerDetailList(page, rows);
			
			if (list.isEmpty() && page > 1) {
				System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
				page--;
				continue;
			}
			System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
			System.out.println("||고객ID		 이름		연락처		  이메일			 주소				등록일			주민번호		 고객등급		보유마일리지(단위 : 포인트)||");
			for(CustomerDetailDTO dto : list) {
				System.out.printf("||%-15s", dto.getId());
				System.out.printf("%-12s", dto.getName());
				System.out.printf("%-20s", dto.getTel());
				System.out.printf("%-20s", dto.getEmail());
				System.out.printf("%-25s", dto.getAddress());
				System.out.printf("%-25s", dto.getReg());
				System.out.printf("%-23s", dto.getRrn());
				System.out.printf("%-20s", dto.getClass_Level());
				System.out.printf("%-11s", dto.getRemain_Mil() + " 포인트 ||");
				System.out.println();
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
				System.out.println("존재하지 않는 ID입니다");
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
		int page = 1;
		int rows = 10;
		
		System.out.println("원하시는 ? 원 이상");
		firstNumber = Integer.parseInt(br.readLine());
		System.out.println("원하시는 ? 원 이하");
		secondNumber = Integer.parseInt(br.readLine());
		
		int totalData = dao.CustomerDetailOrderBtweenCount(firstNumber, secondNumber);
		
		if(firstNumber > secondNumber) {
			System.out.println("첫 입력된 ? 구매금액 이상 값은 두 번째 ? 구매금액 이하 값보다 작을 수 없습니다.");
			return;
		}
		
		if (totalData == 0) {
			System.out.println("검색된 데이터가 없습니다.");
			return;
		}
		
		int totalPage = (int) (Math.ceil((double) totalData / rows));
		
		while(true) {
			List<CustomerDetailDTO> list = dao.CustomerDetailOrderBtween(firstNumber, secondNumber, page, rows);
			System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
			if (list.isEmpty() && page > 1) {
				System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
				page--;
				continue;
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
		
		} catch (NumberFormatException e) {
			System.out.println("고객 총 구매금액은 숫.자로 입력해주세요.");
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
		
		int page = 1;
		int rows = 10;

		int totalData = dao.CustomerMileageMoreListCount(moreMileage);
		if (totalData == 0) {
			System.out.println("검색된 데이터가 없습니다.");
			return;
		}

		int totalPage = (int) (Math.ceil((double) totalData / rows));
		
		while (true) {
			List<CustomerDetailDTO> list = dao.CustomerMileageMoreList(moreMileage, page, rows);
			
			System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
			System.out.println("||고객ID	||	이름		||	연락처		||	이메일		||		주소		||		등록일		||		주민번호		||		고객등급		||		보유마일리지(단위 : 포인트)");
			
			if (list.isEmpty() && page > 1) {
				System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
				page--;
				continue;
			}
			
			for(CustomerDetailDTO dto : list) {
				System.out.printf("||%-15s", dto.getId());
				System.out.printf("%-15s", dto.getName());
				System.out.printf("%-23s", dto.getTel());
				System.out.printf("%-26s", dto.getEmail());
				System.out.printf("%-29s", dto.getAddress());
				System.out.printf("%-25s", dto.getReg());
				System.out.printf("%-23s", dto.getRrn());
				System.out.printf("%-20s", dto.getClass_Level());
				System.out.printf("%-11s", dto.getRemain_Mil() + "포인트");
				System.out.printf("%-20s", dto.getDormancy());
				System.out.println();
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
		
		} catch (NumberFormatException e) {
			System.out.println("보유 마일리지 조건 검색은 숫.자만 가능합니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	/**
	고객 보유 마일리지 A이상 B이하를 조회하는 리스트
	@author	김설규
	@param	firstNumber, secondNumber
	*/
	protected void CustomerMileageBtween() {
		System.out.println("--------------------------------------------");
		System.out.println("고객 보유 마일리지 범위 조건 조회");
		System.out.println("원하시는 고객 보유 마일리지의 범위를 입력 해주세요.");
		System.out.println("--------------------------------------------");
		int firstNumber = 0, secondNumber = 0;
		
		try {
		int page = 1;
		int rows = 10;
		
		System.out.println("원하시는 ? 포인트 이상");
		firstNumber = Integer.parseInt(br.readLine());
		System.out.println("원하시는 ? 포인트 이하");
		secondNumber = Integer.parseInt(br.readLine());
		if(firstNumber >= secondNumber) {
			System.out.println("이상 값은 이하 값 보다 같거나 적게 입력할 수 없습니다, 재입력 바랍니다.");
			CustomerMileageBtween();
		}
		
		int totalData = dao.CustomerMileageBtweenCount(firstNumber, secondNumber);
		if (totalData == 0) {
			System.out.println("검색된 데이터가 없습니다.");
			return;
		}
		int totalPage = (int) (Math.ceil((double) totalData / rows));
		
		boolean run = true;
		
		while(run) {
		List<CustomerDetailDTO> list = dao.CustomerMileageBtween(firstNumber, secondNumber,  page, rows);
		
		if (list.isEmpty() && page > 1) {
			System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
			page--;
			continue;
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
			System.out.printf("%-10s", dto.getDormancy());
			System.out.println();
		}
		System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
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
		}//while
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	/**
	생일인 고객을 조회하는 리스트
	@author	김설규
	*/
	protected void CustomerDetailBirth() {
		System.out.println("생일 고객 조회");
		List<CustomerDetailDTO> list = dao.CustomerDetailBirth();
		System.out.println("||고객ID		 이름		연락처		  이메일			 주소				등록일			주민번호		고객등급		보유마일리지(단위 : 포인트)		휴면||");
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
			System.out.printf("%-10s", dto.getDormancy());
			System.out.println();
		}
		System.out.println("조회된 건 수 : " + list.size() + "건");
	}
	
	/**
	올해 월별 고객의 상품 구매 수 총계 조회 리스트
	@author	김설규
	@param	dto
	*/
	protected void MonthlyThisyearOrderCnt() {
		System.out.println("올해 월별 고객 상품구매 수 조회");
		List<CustomerDetailDTO> list = dao.MonthlyThisyearOrderCnt();
		System.out.println("||월	 상품 구매 수||");
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-7s", dto.getLvemon());
			System.out.printf("%-5s", dto.getMonths_order());
			System.out.println();
		}
	}
	
	/**
	가장 최근 접속 일자가 1년 이상 된 고객 중 휴면처리가 되지 않은 고객 리스트
	@author	김설규
	@return list
	*/
	public void CustomerDormancyUnList() {
		System.out.println("휴면 대상 고객 중 휴면처리가 되지 않은 고객 리스트");
		List<CustomerDetailDTO> list = dao.CustomerDormancyUnList();
		System.out.println("||고객ID		 이름		등급		고객의 가장 최근 로그인 날짜||");
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-15s", dto.getName());
			System.out.printf("%-16s", dto.getDormancy());
			System.out.printf("%-10s", dto.getMax_log_date());
			System.out.println();
		}
		System.out.println("조회된 건 수 : " + list.size() + "건");
	}
	
	/**
	등급이 상승되지않은 등급 상승 대상 고객 리스트
	@author	김설규
	@return list
	*/
	public void CustomerClassUnList() {
		System.out.println("등급이 상승되지않은 등급 상승 대상 고객 리스트");
		List<CustomerDetailDTO> list = dao.CustomerClassUnList();
		System.out.println("||고객ID		 이름		총 구매금액		등급||");
		for(CustomerDetailDTO dto : list) {
			System.out.printf("||%-15s", dto.getId());
			System.out.printf("%-15s", dto.getName());
			System.out.printf("%-15s", dto.getMax_total_Cost());
			System.out.printf("%-15s", dto.getClass_Id() + "등급");
			
			System.out.println();
		}
		System.out.println("조회된 건 수 : " + list.size() + "건");
	}
	
	private int[] pagingButton(int totalPage, int page){
        int[] result = new int[2];
        int out = 0;
        try {
            System.out.println("----------------------------------------------------------------------------------");
            System.out.printf("  페이지: %d / %d \n", page, totalPage);
            System.out.print(" [P]이전  [N]다음  [숫자]페이지 이동  [M]메인 : ");
            String ch = br.readLine();
            System.out.println();

            if(ch.equalsIgnoreCase("P")) {
                if(page > 1) {
                    page--;
                } else {
                    System.out.println("첫 페이지입니다.\n");
                }
            } else if (ch.equalsIgnoreCase("N")) {
                if(page < totalPage) {
                    page++;
                } else {
                    System.out.println("마지막 페이지입니다.\n");
                }
            } else if (ch.equalsIgnoreCase("M")) {
                out = 1;
            } else if (ch.equalsIgnoreCase("C")) {
                out = 2;
            } else {
                try {
                    int p = Integer.parseInt(ch);
                    if(p >= 1 && p <= totalPage) {
                        page = p;
                    } else {
                        System.out.println("페이지 범위를 벗어났습니다.\n");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다.\n");
                }
            }
            result[0] = page;
            result[1] = out;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
            System.out.printf("|| 고객ID ||  이름  ||       연락처      ||         이메일         ||             주소             ||"
                  + "          등록일          ||      주민번호      ||고객등급||보유마일리지\n");
            
            if (list.isEmpty() && page > 1) {
               System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
               page--;
               continue;
            }

            for (CustomerDetailDTO dto : list) {
               System.out.printf("%-12s", dto.getId());
               System.out.printf("%-8s",dto.getName());
               System.out.printf("%-20s",dto.getTel());
               System.out.printf("%-25s",dto.getEmail());
               System.out.printf("%-25s",dto.getAddress());
               System.out.printf("%-25s",dto.getReg());
               System.out.printf("%-20s",dto.getRrn());
               System.out.printf("%-10s",dto.getClass_Level());
               System.out.printf("%-10s\n",dto.getRemain_Mil());
            }
            
            int[] result = pagingButton(totalPage, page);
                page = result[0];
                if (result[1] == 1) {
                    break;
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
            List<CustomerDetailDTO> list = dao2.customerFindByTotalCost(cost, page, rows);
            System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
            System.out.printf("|| 고객ID ||  이름  ||       연락처      ||         이메일         ||             주소             ||"
                  + "      등록일      ||      주민번호      ||고객등급||보유마일리지\n");
            if (list.isEmpty() && page > 1) {
               System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
               page--;
               continue;
            }
            
            for (CustomerDetailDTO dto : list) {
               System.out.printf("%-12s", dto.getId());
               System.out.printf("%-8s",dto.getName());
               System.out.printf("%-20s",dto.getTel());
               System.out.printf("%-25s",dto.getEmail());
               System.out.printf("%-25s",dto.getAddress());
               System.out.printf("%-25s",dto.getReg());
               System.out.printf("%-20s",dto.getRrn());
               System.out.printf("%-10s",dto.getClass_Level());
               System.out.printf("%-10s\n",dto.getRemain_Mil());
            }

                int[] result = pagingButton(totalPage, page);
                page = result[0];
                if (result[1] == 1) {
                    break;
                }
         }
         
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   
   
   protected void customerFindByGrade() {
      System.out.println("[등급별 고객 조회]");
      
      int page = 1;
      int rows = 10;
      int totalData = dao2.dataCountTotalCus();
      
      if (totalData == 0) {
         System.out.println("데이터가 없습니다.");
      }
      
      int totalPage = (int) (Math.ceil((double) totalData / rows));
      
      go :
      while(true) {
         List<CustomerDetailDTO> list = dao2.customerFindByGrade(page, rows);
         System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
         System.out.printf("||고객등급|| 고객ID ||  이름  \n");
         if (list.isEmpty() && page > 1) {
            System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
            page--;
            continue;
         }
         
         for (CustomerDetailDTO dto : list) {
            System.out.printf("%-10s",dto.getClass_Level());
            System.out.printf("%-12s", dto.getId());
            System.out.printf("%-8s\n",dto.getName());
         }
         System.out.println("----------------------------------------------------------------------------------");
         
         System.out.printf("  페이지: %d / %d \n", page, totalPage);
         System.out.print(" [P]이전  [N]다음  [숫자]페이지 이동  [C] 등급 통계 [M]메인 : ");
         
         
         try {
            String ch = br.readLine();
            System.out.println();
            
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
            } else if (ch.equalsIgnoreCase("C")) {
               while(true) {
                  try {
                     dao2.CustomerCountByGrade();
                     System.out.print("[P]뒤로가기 => ");
                     String ch2 = br.readLine();
                     System.out.println();
                     if(ch2.equalsIgnoreCase("P")) {
                        continue go;
                     }
                  } catch (Exception e) {
                     System.out.println("잘못된 입력입니다.");
                  }
               }
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
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   protected void yearlyMonthlyBuyStats() {
      System.out.println("\n[년도별 월별 구매 통계]");
      String overallStartYearStr;
      String overallEndYearStr;
      
      try {
         System.out.print("▶ 조회 시작 년도 (예: 2020) : ");
         overallStartYearStr = br.readLine();
         System.out.print("▶ 조회 종료 년도 (예: 2030) : ");
         overallEndYearStr = br.readLine();
         
         int overallStartYear = Integer.parseInt(overallStartYearStr);
         int overallEndYear = Integer.parseInt(overallEndYearStr);

         // 2. 페이징 처리 변수 설정
         int page = 1;
         int rows = 1; // 한 페이지에 3년치씩 보여주기 (이 값을 조절)
         
         int totalYears = (overallEndYear - overallStartYear) + 1;
         if (totalYears <= 0) {
             System.out.println("조회 기간이 올바르지 않습니다.");
             return;
         }
         // 년도 기준 전체 페이지 수 계산
         int totalPage = totalYears;
         
         
         // 3. 페이징 루프 시작
         while(true) {
            
            // 4. 현재 페이지에 해당하는 년도 계산
            // (페이지 1, 3년씩) => 2020 + ((1-1) * 3) = 2020년
            int pageStartYear = overallStartYear + ((page - 1) * rows);
            // (페이지 1, 3년씩) => 2020 + 3 - 1 = 2022년

            

            List<YearlyMonthlyStatsDTO> statsList = dao2.getMonthlyBuyStats(
                  String.valueOf(pageStartYear)
            );

            Map<String, YearlyMonthlyStatsDTO> statsMap = new HashMap<>();
            for(YearlyMonthlyStatsDTO dto : statsList) {
               String key = dto.getYear() + "-" + dto.getMonth();
               statsMap.put(key, dto);
            }

            System.out.println("\n--- " + overallStartYearStr + "년 ~ " + overallEndYearStr + "년 월별 구매 통계 ---");
            System.out.printf("--- [현재 페이지: %d / %d] ---\n", 
                           page, totalPage);
            
            // (바깥쪽 년도 루프)
            for(int y = pageStartYear; y <= pageStartYear; y++) {
               
               System.out.println("\n=================================");
               System.out.println("     " + y + "년도 통계");
               System.out.println(" (월 | 구매 건수 | 구매 합계)");
               System.out.println("---------------------------------");
               
               long yearTotalCount = 0; 
               long yearTotalSum = 0;   
               String yearKey = String.valueOf(y);

               // (안쪽 월 루프)
               for (int m = 1; m <= 12; m++) {
                  String monthKey = String.format("%02d", m); 
                  String lookupKey = yearKey + "-" + monthKey;
                  YearlyMonthlyStatsDTO stats = statsMap.get(lookupKey);

                  int count = 0;
                  long sum = 0; 

                  if (stats != null) {
                     count = stats.getCount();
                     sum = stats.getSum();
                  }
                  
                  yearTotalCount += count;
                  yearTotalSum += sum;

                  System.out.printf(" %2d월 | %5d명 | %,12d원\n", m, count, sum);
               } // (월 루프 종료)
               
               System.out.println("---------------------------------");
               System.out.printf(" [소계] | %5d명 | %,12d원\n", yearTotalCount, yearTotalSum);
            } // (년도 루프 종료)

                int[] result = pagingButton(totalPage, page);
                page = result[0];
                if (result[1] == 1) {
                    break;
                }
         } // while 루프
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

    protected void customerLoginHistory(){
        System.out.println("[고객 로그인 기록 조회]");
        String id;
        try {
        	System.out.print("아이디 입력 : ");
            id = br.readLine();

            int page = 1;
            int rows = 10;

            int totalData = dao2.dateCountLoginHistory(id);
            if (totalData == 0) {
               System.out.println("검색된 데이터가 없습니다.");
               return;
            }

            int totalPage = (int) (Math.ceil((double) totalData / rows));

            while (true) {
               List<CustomerDetailDTO> list = dao2.customerLoginHistory(id, page, rows);

               System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
               System.out.printf("|| 관리 번호 ||  날짜  ||       고객ID     \n");
               
               if (list.isEmpty() && page > 1) {
                  System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
                  page--;
                  continue;
               }

               for (CustomerDetailDTO dto : list) {
                  System.out.printf("%-20s", dto.getId());
                  System.out.printf("%-25s",dto.getReg());
                  System.out.printf("%-20s\n",dto.getName());
               }
               
               int[] result = pagingButton(totalPage, page);
               page = result[0];
               if (result[1] == 1) {
                     break;
               }
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void purchaseCountsByProduct() {
    	System.out.println("[상품별 구매 수 조회]");
    	
        try {

            int page = 1;
            int rows = 10;

            int totalData = dao2.dataCountProductPurchases();
            if (totalData == 0) {
               System.out.println("검색된 데이터가 없습니다.");
               return;
            }

            int totalPage = (int) (Math.ceil((double) totalData / rows));

            while (true) {
               List<CustomerDetailDTO> list = dao2.purchaseCountsByProduct(page, rows);

               System.out.println("조회된 건 수 : " + list.size() + "건 (전체: " + totalData + "건)");
               System.out.printf("|| 상품ID ||  상품 이름  ||   구매 수  \n");
               
               if (list.isEmpty() && page > 1) {
                  System.out.println("표시할 데이터가 없습니다. 이전 페이지로 이동합니다.");
                  page--;
                  continue;
               }
               
               for (CustomerDetailDTO dto : list) {
                  System.out.printf("%-10s", dto.getId());
                  System.out.printf("%-15s",dto.getName());
                  System.out.printf("%-5s\n",dto.getTotal_cost());
               }
               
               int[] result = pagingButton(totalPage, page);
               page = result[0];
               if (result[1] == 1) {
                     break;
               }
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

