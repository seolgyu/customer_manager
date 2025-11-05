package cm.man;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CustomerDetailUI {
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
                System.out.println("|| 1.초기설정 || 2.뒤로가기 ||");
                ch = Integer.parseInt(br.readLine());

                switch (ch) {
                    case 1:
                        System.out.println("초기설정");;
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
}
