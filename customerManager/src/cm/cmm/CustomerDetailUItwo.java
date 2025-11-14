package cm.cmm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetailUItwo {
   private CustomerDetailDAOtwo dao2 = new CustomerDetailDAOImpltwo();
   private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   
   
   public void CustomerDetailMenu() {
      boolean starting = true;
      int ch;
      
      while (starting) {
            try {
                System.out.println("|| 1.고객 이름 검색 || 2.구매 금액 고객 검색 || 3.등급 || 4.년도월별 || 5. 로그인 기록");
                ch = Integer.parseInt(br.readLine());
                if(ch == 6) {
                   System.out.println("임시 뒤로가기(프로그램 종료)");
                  System.exit(0);
                }
                
                switch(ch) {
                case 1:
                   customerFindByName();
                    break;
                case 2:
                   customerFindByTotalCost();
                   break;
                case 3:
                   customerFindByGrade();
                   break;
                case 4:
                   yearlyMonthlyBuyStats();
                   break;
                case 5:
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
}
