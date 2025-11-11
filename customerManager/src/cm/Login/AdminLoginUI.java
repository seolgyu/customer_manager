package cm.Login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class AdminLoginUI {
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private LoginDAO dao = new LoginDAOImpl();
	private LoginInfo loginInfo = null;

	public AdminLoginUI(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	// 고객정보 수정
	public void update() {
		System.out.println("\n[정보수정]");

		try {
			LoginDTO dto = loginInfo.loginMember();
			
			System.out.print("패스워드 ? ");
			dto.setPwd(br.readLine());
			
			System.out.print("전화번호 ? ");
			dto.setTel(br.readLine());
			
			System.out.print("이메일 ? ");
			dto.setEmail(br.readLine());
			
			System.out.print("주소 ? ");
			dto.setAddress(br.readLine());
			
			System.out.print("id ? ");
			dto.setCus_id(br.readLine());					

			dao.updateMember(dto);

			System.out.println("회원정보가 수정되었습니다.");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1407) { 
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
		
	 // 고객 아이디 검색
	public void findById() {
		System.out.println("\n[아이디 검색]");

		try {
			String id;
			System.out.print("검색할 아이디 ? ");
			id = br.readLine();

			LoginDTO dto = dao.findById(id);
			if (dto == null) {
				System.out.println("등록된 정보가 아닙니다.\n");
				return;
			}

			System.out.println("아이디\t이름\t전화번호\t이메일\t주소");
			System.out.println("---------------------------------------------------------------");

			System.out.print(dto.getCus_id() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getTel() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.println(dto.getAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	}
	
	// 고객 이름 검색
	public void findByName() {
		System.out.println("\n[이름 검색]");

		String name;
		try {
			System.out.print("검색할 이름 ? ");
			name = br.readLine();

			List<LoginDTO> list = dao.listMember(name);
			if (list.size() == 0) {
				System.out.println("등록된 정보가 없습니다.\n");
				return;
			}

			System.out.println("아이디\t이름\t전화번호\t이메일\t주소");
			System.out.println("---------------------------------------------------------------");
			for (LoginDTO dto : list) {
				System.out.print(dto.getCus_id() + "\t");
				System.out.print(dto.getName() + "\t");
				System.out.print(dto.getTel() + "\t");
				System.out.print(dto.getEmail() + "\t");
				System.out.println(dto.getAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	// 고객 전체 리스트
	public void listAll() {
		System.out.println("\n[전체 리스트]");

		List<LoginDTO> list = dao.listMember();

		System.out.println("아이디\t이름\t전화번호\t이메일\t주소");
		System.out.println("---------------------------------------------------------------");
		for (LoginDTO dto : list) {
			System.out.print(dto.getCus_id() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getTel() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.println(dto.getAddress());
		}
		System.out.println();
	}
}
