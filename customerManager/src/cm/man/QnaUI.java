package cm.man;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import db.util.DBConn;

public class QnaUI {
	private QnaDAO dao = new QnaDAOimpl();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public void menu() {
		int ch;

		System.out.println("=== 고객 불만 및 상담 관리 ===");

		while (true) {
			try {
//			    System.out.println("=== 고객 상담 메뉴 ===");
				System.out.println("1. 답변하기");
				System.out.println("2. 대기/진행중인 문의");
				System.out.println("3. 완료된 문의");
				System.out.println("4. 모든 문의내역");
				System.out.println("0. 뒤로가기");

				System.out.print("선택 > ");
				ch = Integer.parseInt(br.readLine());

				if (ch == 0) {
					DBConn.close();
					return;
				}

				switch (ch) {
				case 1:
					reply();
					break;
				case 2:
					listPending();
					break;
				case 3:
					listCompleted();
					break;
				case 4:
					listAll();
					break;
				}

			} catch (Exception e) {
			}
		}

	}

	protected void reply() {
		//
		System.out.println(" -------------------------------------------- ");
		try {
			List<QnaDTO> list = dao.listPending();

			if (list.isEmpty()) {
				System.out.println("답변할 문의가 없습니다 \n");
				return;
			}
			System.out.println("문의번호\t문의날짜\t\t담당자\t\t고객ID\t진행상태\t문의\t\t\t답변\t답변날짜");
			for (QnaDTO dto : list) {
				System.out.print(dto.getInq() + "\t");
				System.out.print(dto.getInqDate() + "\t");
				System.out.print(dto.getAdmId() + "\t");
				System.out.print(dto.getId() + "\t");
				System.out.print(dto.getStatus() + "\t");
				System.out.print(dto.getContent() + "\t");
				System.out.print(dto.getAnswer() + "\t");
				System.out.println(dto.getAnswerDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();

		//
		try {
			QnaDTO dto = new QnaDTO();
			System.out.println("\n=========== [답변하기] =========== (뒤로가기:0) ");
			System.out.print("답변할 문의번호를 입력하세요 > ");
			dto.setInq(br.readLine().toUpperCase());
			System.out.print("작성자 직원ID를 입력하세요 >");
			dto.setAdmId(br.readLine().toUpperCase());

			System.out.print("답변 작성 👉  ");
			dto.setAnswer(br.readLine());

			// 상태 입력 검증을 반복하도록 수정
			String s;
			
			do {
				System.out.print("\n상태를 [진행중] 또는 [완료]로 변경해주세요 >");
				s = br.readLine();
				s = s.trim();
				if(s.length() == 0) {
					s = "대기";
				}
			} while(! s.equals("진행중") && ! s.equals("완료") && ! s.equals("대기"));
			
			dto.setStatus(s);
			
			int result = dao.reply(dto);

			if (result == 0) {
				System.out.println("조회된 문의번호가 없습니다.");
			} else {
				System.out.println("답변이 완료되었습니다");
			}
		} catch (Exception e) {
			// 그 외 예상 못 한 예외는 이렇게 간단히 출력
			System.out.println("⚠ 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
			// e.printStackTrace(); // ← 이건 개발용일 때만 잠깐 열어둬도 됨
		}
		System.out.println();
	}

// 2. 고객 상담 미완료 조회 (대기/진행중) ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	protected void listPending() {
		System.out.println("\n\t ============ 미완료 답변 조회하기 =========== ");
		try {
			List<QnaDTO> list = dao.listPending(); // DAO호출

			if (list.isEmpty()) {
				System.out.println("대기/진행중인 상담이 없습니다 \n");
				return;
			}
			System.out.println("문의번호\t문의날짜\t\t담당자\t고객ID\t진행상태\t문의\t\t\t답변\t답변날짜");
			for (QnaDTO dto : list) {
				System.out.print(dto.getInq() + "\t");
				System.out.print(dto.getInqDate() + "\t");
				System.out.print(dto.getAdmId() + "\t");
				System.out.print(dto.getId() + "\t");
				System.out.print(dto.getStatus() + "\t");
				System.out.print(dto.getContent() + "\t");
				System.out.print(dto.getAnswer() + "\t");
				System.out.println(dto.getAnswerDate());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

// 3. 고객 상담 완료 조회 UI
	protected void listCompleted() {
		System.out.println("\n ===== 완료된 상담 조회하기===== ");
		try {
			List<QnaDTO> list = dao.listCompleted(); // DAO호출

			if (list.isEmpty()) {
				System.out.println("답변이 완료된 상담이 없습니다 \n");
				return;
			}
			System.out.println("문의번호\t문의날짜\t\t담당자\t고객ID\t진행상태\t문의\t\t\t답변\t답변날짜");
			for (QnaDTO dto : list) {
				System.out.print(dto.getInq() + "\t");
				System.out.print(dto.getInqDate() + "\t");
				System.out.print(dto.getAdmId() + "\t");
				System.out.print(dto.getId() + "\t");
//				System.out.print(dto.getStatus() + "\t");
				System.out.print(dto.getContent() + "\t");
				System.out.print(dto.getAnswer() + "\t");
				System.out.println(dto.getAnswerDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

// 4. 고객 상담 전체 리스트 UI
	protected void listAll() {
		System.out.println("\n\t\t\t ===== 모든 상담 조회하기===== ");
		try {
			List<QnaDTO> list = dao.listAll(); // DAO호출

			if (list.isEmpty()) {
				System.out.println("조회된 상담이 없습니다 \n");
				return;
			}
			System.out.println("문의번호\t문의날짜\t\t담당자\t고객ID\t진행상태\t문의\t\t\t답변\t답변날짜");
			for (QnaDTO dto : list) {
				System.out.print(dto.getInq() + "\t");
				System.out.print(dto.getInqDate() + "\t");
				System.out.print(dto.getAdmId() + "\t");
				System.out.print(dto.getId() + "\t");
				System.out.print(dto.getStatus() + "\t");
				System.out.print(dto.getContent() + "\t");
				System.out.print(dto.getAnswer() + "\t");
				System.out.println(dto.getAnswerDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
}
