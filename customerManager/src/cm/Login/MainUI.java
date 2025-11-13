package cm.Login;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import db.util.DBConn;

import cm.cmm.*;
import cm.man.*;
import cm.manage.*;

public class MainUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	private LoginInfo login = new LoginInfo();
	private CustomerLoginUI customerUI = new CustomerLoginUI(login);
	private AdminLoginUI adminloginUI = new AdminLoginUI(login);
	private ProductManageUI manageUI = new ProductManageUI(login);
	
	public void menu() {
		while(true) {
			LoginDTO loginMember = login.loginMember();
			
			if(loginMember == null) {
				menuGuest();
			} else if(loginMember.getCus_id().equals("admin1")){
				menuAdmin();
			} else {
				menuUser();
			}
		}
	}
	
	private void menuGuest() {
		int ch;
		
		do {
			ch = 0;
			try {
				System.out.print("1.로그인 2.회원가입 3.종료 => ");
				ch = Integer.parseInt(br.readLine());
			} catch (Exception e) {
			}
		} while(ch < 1 || ch > 3);
		
		if(ch == 3) {
			DBConn.close();
			System.exit(0);
		}
		
		switch(ch) {
		case 1: customerUI.login(); break;
		case 2: customerUI.register(); break;
		
		}
	}
	
	private void menuUser() {
		int ch;
		
		try {
			LoginDTO loginMember = login.loginMember();
			
			System.out.println("\n["+loginMember.getName()+"] 님");
			
			do {
				System.out.print("1.상품리스트 2.상품구매 3.정보수정 4.로그아웃 5.회원탈퇴 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch < 1 || ch > 5);
			
			switch(ch) {
			case 1: manageUI.productList(); break;
			case 2: manageUI.productBuy(); break;
			case 3: adminloginUI.update(); break;
			case 4: login.logout(); System.out.println(); break;
			case 5: customerUI.delete(); break;
			}
		} catch (Exception e) {
		}
	}

	private void menuAdmin() {
		int ch;
		
		try {
			System.out.println("\n[관리자] 님");
			
			do {
				System.out.print("1.상품등록 2.상품수정 3.상품검색 4.상품리스트 5.id검색 6.이름검색 7.회원리스트 8.고객관리 9.로그아웃 => ");
				ch = Integer.parseInt(br.readLine());
			} while(ch<1 || ch>9);
			
			switch(ch) {
			case 1: manageUI.productAdd(); break;
			case 2: manageUI.productUpdate(); break;
			case 3: manageUI.productSearch(); break;
			case 4: manageUI.productList(); break;
			case 5: adminloginUI.findById(); break;
			case 6: adminloginUI.findByName(); break;
			case 7: adminloginUI.listAll(); break;
			case 8: menuCustomer();
                break;
			case 9: login.logout(); System.out.println(); break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void menuCustomer() {
	    int ch;

	    try {
	        System.out.println("\n[고객관리 메뉴]");
	        
	        do {
	            System.out.print("1.고객관리  2.고객조회  3.고객상담  4.뒤로가기 => ");
	            ch = Integer.parseInt(br.readLine());
	        } while (ch < 1 || ch > 4);

	        switch (ch) {
	            case 1:
	            	new CmUI().menu();
	                break;
	            case 2:
	            	new CustomerDetailUI().CustomerDetailMeun();
	                break;
	            case 3:
	                new QnaUI().menu();
	                break;
	            case 4:
	                System.out.println("관리자 메뉴로 돌아갑니다.");
	                return; 
	        }
	        
	        menuCustomer();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
