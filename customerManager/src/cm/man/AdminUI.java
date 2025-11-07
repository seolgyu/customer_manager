package cm.man;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import db.util.DBConn;

public class AdminUI {
	private AdminDAO dao = new AdminDAOImpl();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public void menu() {
		int ch;
		
		System.out.println("[ 관리자 등록 및 조회 ] ");
		
		while(true) {
			try {
				System.out.print("(1)등록  (2)조회  (3)종료");
				ch = Integer.parseInt(br.readLine());
				
				if( ch == 3) {
					DBConn.close();
					return;
				}
				
				switch(ch) {
				case 1 : insert(); break;
				case 2 : listAdmin(); break;
				}
				
			} catch (Exception e) {
			} 
		}
	}
	
	protected void insert() {
		System.out.println("\n[관리자로서 직원등록]");
		
		try {
			AdminDTO dto = new AdminDTO();
			
			System.out.print("아이디 ?  * admin + 숫자 조합으로 생성하세요");
			dto.setAdmId(br.readLine());
			
			System.out.print("패스워드 ? ");
			dto.setAdmPwd(br.readLine());
			
			dao.insertAdmin(dto);
			System.out.println("관리자로 등록되었습니👍");
		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 중복, not null 예외
			if(e.getErrorCode()==1) {
				System.out.println("❌❌❌ 아이디 중복입니다 ❌❌❌");
			} else if(e.getErrorCode() == 1400){ // INSERT-NOT NULL 위반
				System.out.println("필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	protected void listAdmin() {
		System.out.println("\n[관리자 전체 리스트]");
		List<AdminDTO> list = dao.listAdmin();
		for(AdminDTO dto : list) {
			System.out.print(dto.getAdmId() + "\t");
			System.out.println(dto.getAdmPwd());
		}
		
		System.out.println();
	}	
}
