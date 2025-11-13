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

		System.out.println();


		while (true) {
			try {
				System.out.println("┌────────────────────────┐");
				System.out.println("│==== 관리자 등록 및 조회 ====│");
				System.out.println("│  1. 관리자 등록           │");
				System.out.println("│  2. 관리자 조회           │");
				System.out.println("│  0. 종료(뒤로가기)         │ ");
				System.out.println("└────────────────────────┘");

				System.out.print("선택 > ");
				ch = Integer.parseInt(br.readLine());

				if (ch == 0) {
					DBConn.close();
					return;
				}

				switch (ch) {
				case 1:
					insert();
					break;
				case 2:
					listAdmin();
					break;
				}

			} catch (Exception e) {
			}
		}
	}

	protected void insert() {	
		System.out.println();
		

		try  {
			AdminDTO dto = new AdminDTO();

			boolean valid = false;
			String id = null;

			do {
				System.out.println("┌──────────────────────────────────────────────┐");
				System.out.println("│\t====== 관리자 및 직원을 등록합니다 ======      │");
				System.out.println("│ ⚠️ 관리자는 'admin + 숫자 한자리' 조합으로 생성해야합니다. │");
				System.out.println("└──────────────────────────────────────────────┘");
				System.out.print("아이디를 입력하세요  > ");
				id = br.readLine();

				if (!id.matches("^admin[0-9]$")) {
					System.out.println(" ⚠️ 아이디 형식이 잘못되었습니다. (admin + 숫자 한자리)");
					continue;
				}

				valid = true; // 두 조건 모두 통과 시 반복 종료
			} while (!valid);
			
			
			String pwd = null;	
			do {
				System.out.print("패스워드를 입력하세요  > ");
				pwd = br.readLine();

				if (!pwd.equals(id)) {
					System.out.println("┌────────────────────────────────┐");
					System.out.println("│ ⚠️ 패스워드는 아이디와 동일해야 합니다 │");
					System.out.println("└────────────────────────────────┘");
					pwd = null; //
				}
			} while (pwd == null);

			dto.setAdmId(id);
			dto.setAdmPwd(pwd);

			dao.insertAdmin(dto);
			System.out.println("");
			System.out.println("✨ 관리자 등록 완료 ✨");
			
		} catch (SQLIntegrityConstraintViolationException e) {
			// 기본키 중복, not null 예외
			if (e.getErrorCode() == 1) {
				System.out.println("⚠️ 이미 존재하는 아이디입니다 ");
			} else if (e.getErrorCode() == 1400) { // INSERT-NOT NULL 위반
				System.out.println("⚠️ 필수 입력사항을 입력하지 않았습니다.");
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
		System.out.println();
		System.out.println();
		System.out.println("==== 관리자 조회하기 ====");
		System.out.println("----------------------");
		System.out.println("  ID\tpassword");
		List<AdminDTO> list = dao.listAdmin();
		for (AdminDTO dto : list) {
			System.out.print(dto.getAdmId() + "\t");
			System.out.println(dto.getAdmPwd());
		}
		System.out.println();
		System.out.println();
	}
}
