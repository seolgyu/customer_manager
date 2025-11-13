package cm.manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

import cm.cmm.CustomerDetailDTO;
import db.util.DBConn;
import cm.cmm.*;

public class CmUI {
	private CmDAO dao = new CmDAOImpl();
	private CustomerDetailDAO dao2 = new CustomerDetailDAOImpl();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	Connection conn = DBConn.getConnection();
	
	public void menu() {
		int ch;
		
		System.out.println("고객 관리");
		
		while(true) {
			try {
				System.out.println("1.고객수정 2.고객삭제 3.뒤로가기");
				ch = Integer.parseInt(br.readLine());
				
				switch(ch) {
				case 1: // 고객 수정 
					updateCustomer();
					break;
				case 2: // 고객 삭제 
					deleteCustomer();
					break;
				case 3: // 관리자 로그인 화면으로 이동 
					break;	
					// menuCustomer();
				}
				
				
			} catch (Exception e) {
			}
		}
	}
	
	
	
	public void updateCustomer() {
		int ch;
		
		System.out.println("고객 수정");
		
		while(true) {
			try {
				System.out.print("1.리스트조회 2.고객등급수정 3.휴먼처리 4.마일리지추가 5.마일리지수정 6.고객정보수정 7.마일리지리스트 8.뒤로가기 ");
				ch = Integer.parseInt(br.readLine());
				
				if(ch == 8) {// 고객관리 화면으로 이동 
					menu();
				}
				
				switch (ch) {
				case 1:
					list();
					break;
				case 2:
					updateGrade();
					break;
				case 3:
					updateDormancy();
					break;
				case 4:
					insertMileage();
					break;	
				case 5:
					updateMileage();
					break;
				case 6:
					updateInformation();
					break;
				case 7:
					mileagelist();
					break;
					
				}
		
			} catch (Exception e) {
			}
		}
		
	}
	
	
	
	protected void list() {
		System.out.println("\n[리스트 조회]");
		
		List<CmDTO> list = dao.listCustomer();
	
		System.out.println("전체 인원수 : " + list.size());
		
		for(CmDTO dto : list) {
			System.out.print(dto.getCus_id() + "\t");
			System.out.print(dto.getName() + "\t");
			System.out.print(dto.getTel() + "\t");
			System.out.print(dto.getEmail() + "\t");
			System.out.print(dto.getAddress() + "\t");
			System.out.print(dto.getReg() + "\t");
			System.out.print(dto.getRrn() + "\t");
			System.out.print(dto.getClass_level() + "\t");
			System.out.print(dto.getRemain_mil() + "\t");
			System.out.print(dto.getDormancy() + "\t");
			System.out.println(dto.getTotal_cost());
			
		}
		System.out.println();
		
	}
	
	
	
	protected void mileagelist() {
		System.out.println("\n[마일리지 리스트 조회]");
		
		String cus_id;
		
		
		try {
			System.out.print("검색할 아이디를 입력해주세요");
		cus_id = br.readLine();
		
		List<CmDTO> list = dao.listMileage(cus_id);
		if(list.size() == 0) {
			System.out.println("등록된 아이디가 없습니다.\n");
			return;
		}
		
		System.out.println("아이디  마일리지ID 변동구문 변동마일리지 남은마일리지 날짜 ");
		
			for(CmDTO dto : list) {
				System.out.print(dto.getCus_id() + "\t");
				System.out.print(dto.getMileage_id() + "\t");
				System.out.print(dto.getMileage_yn() + "\t");
				System.out.print(dto.getChange_mil() + "\t");
				System.out.print(dto.getRemain_mil() + "\t");
				System.out.println(dto.getMileage_date());
			
		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println();
	
		
	}
	
	
	
	protected void updateGrade() {
		System.out.println("\n[고객등급 수정]");
		
		try {
			
			int result = dao.updateGrade();
			
			if(result == 0) {
				System.out.println("이미 완료되었거나, 문제가 있습니다.");
			} else {
				System.out.println("등급수정이 완료되었습니다.");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	}
	
	
	
	
	
	protected void updateDormancy() {
		System.out.println("\n[휴면처리]");
		
		try {
			
			int result = dao.updateDormancy();
			
			if(result == 0) {
				System.out.println("이미 완료되었거나, 문제가 있습니다.");
			} else {
				System.out.println("휴면처리가 완료 되었습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	protected void insertMileage() {
		System.out.println("\n[마일리지 추가]");
		
		CmDTO dto = new CmDTO();
		String Cus_id;
		
		try {
					
			System.out.print("추가할 회원 아이디 ?");
			Cus_id = br.readLine();
			dto.setCus_id(Cus_id);
			
			
			CustomerDetailDTO dto2 = dao2.CustomerDetailFindId(Cus_id);
			if(dto2 == null) {
				System.out.println("존재하지 않는 데이터 입니다.");
				return;
			}
			
			System.out.println("변동할 마일리지 값?");
			dto.setChange_mil(Integer.parseInt(br.readLine()));
			
			
			dao.InsertMileage(dto);
			
			System.out.println("마일리지 등록이 완료 되었습니다.");
			
		
			
		} catch (NumberFormatException e) {
			System.out.println("변동마일리지에는 숫자를 입력해주세요");
		} catch (SQLException e) {
			if (e.getErrorCode() == 1400) {// NOT NULL 제약위반
				System.out.println("에러-필수 입력사항을 입력하지 않았습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
	
	
	
	protected void updateMileage() {
		System.out.println("\n[마일리지 수정]");
		
		try {
			CmDTO dto = new CmDTO();
			
			System.out.println("수정할 아이디를 입력해주세요 ");
			dto.setCus_id(br.readLine());
			
			
			System.out.print("변동구분 ? ");
			dto.setMileage_yn(br.readLine());
			
			System.out.print("변동마일리지 ? ");
			dto.setChange_mil(Integer.parseInt(br.readLine()));
			
			System.out.print("남은마일리지 ? ");
			dto.setRemain_mil(Integer.parseInt(br.readLine()));
			
			System.out.print("날짜 ? ");
			dto.setMileage_date(br.readLine());
			
			
			int result = dao.updateMileage(dto);
			
			if(result == 0) {
				System.out.println("등록된 아이디가 없습니다.");
			} else {
				System.out.println("마일리지 수정이 완료되었습니다.");
			}
		
		} catch (SQLDataException e) {
			if(e.getErrorCode() == 1840 || e.getErrorCode() == 1861) {
				System.out.println("에러-날짜형식 입력이 잘못되었습니다");
			} else {
				System.out.println(e.toString());
			}
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력해주세요");
		} catch (SQLException e) {
			if(e.getErrorCode() == 1407) {
				System.out.println("에러-필수 입력사항이 입력되지 않았습니다.");
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateInformation() {
		System.out.println("\n[고객정보 수정]");
		
		try {
			CmDTO dto = new CmDTO();
			
			System.out.print("수정할 고객 아이디를 입력해주세요 ");
			dto.setCus_id(br.readLine());
			
			System.out.print("수정할 전화번호를 입력해주세요");
			dto.setTel(br.readLine());
			
			System.out.print("수정할 주소를 입력해주세요");
			dto.setAddress(br.readLine());

			System.out.print("수정할 이메일을 입력해주세요");
			dto.setEmail(br.readLine());
			
			int result = dao.updateInformation(dto);
			
			if(result == 0) {
				System.out.println("등록된 아이디가 없습니다.");
			} else {
				System.out.println("데이터 수정이 완료 되었습니다.");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	protected void deleteCustomer() {
		System.out.println("\n[고객 삭제]");
		String id;
		
		try {
			System.out.print("삭제할 고객의 아이디를 입력해주세요");
			id = br.readLine();
			
			int result = dao.deleteCustomer(id);
			
			if(result == 0) {
				System.out.println("등록된 자료가 없습니다.");
			} else {
				System.out.println("데이터가 삭제 되었습니다.");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
