package cm.man;
// 문의,상담 정보
import java.time.LocalDate;

public class QnaDTO {
	private String id;   	// CUS_ID
	private String inq;       // INQUIRY_ID
	private LocalDate inqDate;   // INQUIRY_DATE
	private String content;
	private String status;
	private String answer;
	private LocalDate answerDate;
	private String admId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInq() {
		return inq;
	}
	public void setInq(String inq) {
		this.inq = inq;
	}
	public LocalDate getInqDate() {
		return inqDate;
	}
	public void setInqDate(LocalDate inqDate) {
		this.inqDate = inqDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public LocalDate getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(LocalDate answerDate) {
		this.answerDate = answerDate;
	}
	public String getAdmId() {
		return admId;
	}
	public void setAdmId(String admId) {
		this.admId = admId;
	}
	

	
}
