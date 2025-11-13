package cm.man;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.util.DBConn;
import db.util.DBUtil;

public class QnaDAOimpl implements QnaDAO {
	private Connection conn = DBConn.getConnection();

	/*
	 * 이름 널? 유형
	 * 
	 * ------------ -------- --------------
	 * 
	 * CUS_ID NOT NULL VARCHAR2(20) INQUIRY_ID NOT NULL VARCHAR2(20) INQUIRY_DATE
	 * NOT NULL DATE CONTENT VARCHAR2(1000) STATUS NOT NULL VARCHAR2(20) ANSWER
	 * VARCHAR2(1000) ANS_DATE DATE ADM_ID NOT NULL VARCHAR2(30)
	 */

	@Override // 1. 고객 상담 답변 작성
	public int reply(QnaDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
	//
		try {		
			sql = "UPDATE consultation " + "SET STATUS = ? ,  " + " ANSWER = ?, " + "ANS_DATE = SYSDATE,"
					+ " ADM_ID = ? " + "WHERE INQUIRY_ID = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getStatus());
			pstmt.setString(2, dto.getAnswer());
			pstmt.setString(3, dto.getAdmId());
			pstmt.setString(4, dto.getInq());
//			pstmt.setDate(3, java.sql.Date.valueOf(dto.getAnswerDate()));
			// LocalDate → SQL Date 변환 --> SQL문에서 SYSDATE로 대신함

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		return result;
	}

// 2. 고객 상담 미완료 조회 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@Override // sql문을 try밖으로, pstmt와 resultset 을 try안으로 써서 짧게씀
	public List<QnaDTO> listPending() {
		List<QnaDTO> list = new ArrayList<QnaDTO>();
		String sql = "SELECT * FROM vw_pending_consultation";

		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				QnaDTO dto = new QnaDTO();

				dto.setInq(rs.getString("INQUIRY_ID"));
				dto.setInqDate(rs.getDate("INQUIRY_DATE").toLocalDate());
				dto.setAdmId(rs.getString("ADM_ID"));
				dto.setId(rs.getString("CUS_ID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setAnswer(rs.getString("ANSWER"));
				dto.setAnswerDate(rs.getDate("ANS_DATE") != null ? rs.getDate("ANS_DATE").toLocalDate() : null);
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

//	ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@Override
	public List<QnaDTO> listCompleted() throws SQLException {
		List<QnaDTO> list = new ArrayList<QnaDTO>();
		String sql = "SELECT * " + "FROM CONSULTATION " + "WHERE STATUS = '완료' ";

		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				QnaDTO dto = new QnaDTO();

				dto.setInq(rs.getString("INQUIRY_ID"));
				dto.setInqDate(rs.getDate("INQUIRY_DATE").toLocalDate());
				dto.setId(rs.getString("CUS_ID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setAnswer(rs.getString("ANSWER"));
				dto.setAnswerDate(rs.getDate("ANS_DATE") != null ? rs.getDate("ANS_DATE").toLocalDate() : null);
				dto.setAdmId(rs.getString("ADM_ID"));
				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 전체리스트 조회 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	@Override
	public List<QnaDTO> listAll() throws SQLException {
		List<QnaDTO> list = new ArrayList<QnaDTO>();
		String sql = "SELECT * " + "FROM CONSULTATION ";

		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				QnaDTO dto = new QnaDTO();

				dto.setInq(rs.getString("INQUIRY_ID"));
				dto.setInqDate(rs.getDate("INQUIRY_DATE").toLocalDate());
				dto.setAdmId(rs.getString("ADM_ID"));
				dto.setId(rs.getString("CUS_ID"));
				dto.setStatus(rs.getString("STATUS"));
				dto.setContent(rs.getString("CONTENT"));
				dto.setAnswer(rs.getString("ANSWER"));
				dto.setAnswerDate(rs.getDate("ANS_DATE") != null ? rs.getDate("ANS_DATE").toLocalDate() : null);
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}
