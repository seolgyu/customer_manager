package cm.man;

import java.sql.SQLException;
import java.util.List;

public interface QnaDAO {
	// 1. 고객 상담 답변 작성하기
	public int reply (QnaDTO dto) throws SQLException;
	
	// 2. 고객 상담 대기/진행중 조회
	public List<QnaDTO> listPending() throws SQLException;
	
	// 3. 고객 상담 완료 조회
	public List<QnaDTO> listCompleted() throws SQLException;
	
	// 4. 고객 상담 전체 리스트 
	public List<QnaDTO> listAll() throws SQLException;
	
}
