package cm.Login;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CustomerLoginUI {

		private BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		private LoginDAO dao = new LoginDAOImpl();
		private LoginInfo loginInfo = null;
		
		public CustomerLoginUI(LoginInfo loginInfo) {
			this.loginInfo = loginInfo;
		}
		
		// 회원 가입
		public void register() {
			System.out.println("\n[회원가입]");
			
			try {
				LoginDTO dto = new LoginDTO();
				
				System.out.println("아이디 ? ");
				dto.setCus_id(br.readLine());
				
				System.out.println("비밀번호 ? ");
				dto.setPwd(br.readLine());
				
				System.out.println("이름 ? ");
				dto.setName(br.readLine());
				
				System.out.println("전화번호 ? ");
				dto.setTel(br.readLine());
				
				System.out.println("이메일 ? ");
				dto.setEmail(br.readLine());
				
				System.out.println("주소 ? ");
				dto.setAddress(br.readLine());
				
				System.out.println("등록일 ? ");
				dto.setReg(br.readLine());
				
				System.out.println("주민번호 ? ");
				dto.setRrn(br.readLine());
				
				System.out.println("등급아이디 ? ");
				dto.setClass_id(br.readLine());
				
				System.out.println("휴면 ? ");
				dto.setDormancy(br.readLine());
				
				dao.insertMember(dto);
				
				System.out.println("회원가입이 완료 되었습니다.");
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println();						
		}
		
		// 로그인 화면
		public void login() {
			System.out.println("\n[로그인]");

			String id, pwd;
			
			try {
				System.out.print("아이디 ? ");
				id = br.readLine();
				System.out.print("패스워드 ? ");
				pwd = br.readLine();								
				
				LoginDTO dto = null;
				if(id.equals("admin1")) {
					dto = dao.adminfindById(id);
				}else {
					dto = dao.findById(id);
				}
				
				
				if(dto == null || ! dto.getPwd().equals(pwd)) {
					System.out.println("아이디 또는 패스워드가 일치하지 않습니다.");
					return;
				}
				
				// 로그인 성공
				loginInfo.login(dto);
				System.out.println("로그인 성공!");
				
				// 로그인 이력 저장
	            dao.insertLoginHistory(dto.getCus_id());
															
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println();
			
		}
}
	

