package cm.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.util.DBConn;


public class ProductManageImpl {
	 
	private Connection conn = DBConn.getConnection();
	
    // 상품 등록
    public void insertProduct(String id, String name, int price, String className, String classId) throws SQLException {
        String sql = "INSERT INTO product_Tab (product_id, product_name, product_price, product_class_name, product_class_id) VALUES (?, ?, ?, ?, ?)";
       
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, price);
            pstmt.setString(4, className);
            pstmt.setString(5, classId);

            int result = pstmt.executeUpdate();
            if (result > 0)
                System.out.println("상품이 성공적으로 등록되었습니다!");
            else
                System.out.println("상품 등록 실패.");
        }
    }

    // 상품 수정
    public void updateProduct(String id, String name, int price, String className, String classId) throws SQLException {
        String sql = "UPDATE product_Tab SET product_name=?, product_price=?, product_class_name=?, product_class_id=? WHERE product_id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, price);
            pstmt.setString(3, className);
            pstmt.setString(4, classId);
            pstmt.setString(5, id);

            int result = pstmt.executeUpdate();
            if (result > 0)
                System.out.println("상품정보가 수정되었습니다.");
            else
                System.out.println("해당 상품ID가 존재하지 않습니다.");
        }
    }

    // 상품 검색
    public void searchProduct(String keyword) throws SQLException {
        String sql = "SELECT * FROM product_Tab WHERE product_name LIKE ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n[검색결과]");
            System.out.println("상품ID\t상품명\t가격\t분류명\t분류ID");
            System.out.println("---------------------------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%s\t%s\t%d\t%s\t%s\n",
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("product_price"),
                        rs.getString("product_class_name"),
                        rs.getString("product_class_id"));
            }
            if (!found)
                System.out.println("검색 결과가 없습니다.");

            rs.close();
        }
    }
    
    // 상품 구매
    public boolean productBuy(String product_id, int qty, String buyerId, String buyerName) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        	
        	conn.setAutoCommit(false);
        	
        	// 상품 정보 조회
            String sqlProduct = "SELECT product_name, product_price FROM product_Tab WHERE product_id = ?";
            pstmt = conn.prepareStatement(sqlProduct);
            pstmt.setString(1, product_id);
            rs = pstmt.executeQuery();

            if (!rs.next()) {
                System.out.println("해당 상품이 존재하지 않습니다.");
                return false;
            }

            String productName = rs.getString("product_name");
            int price = rs.getInt("product_price");
            int total = price * qty;

            rs.close();
            pstmt.close();

            // 주문 코드 및 판매상세번호 생성
            String orderCode = "ORD" + System.currentTimeMillis();
            String saleDetailCode = "SALE" + System.nanoTime();

            // 주문내역 등록 (order_Details)
            String sqlOrder = "INSERT INTO order_Details (ORDER_CODE, ORDER_PRICE, ORDER_DATE, total_Cost, CUS_ID) "
                    + "VALUES (?, ?, SYSDATE, ?, ?)";
            pstmt = conn.prepareStatement(sqlOrder);
            pstmt.setString(1, orderCode);
            pstmt.setInt(2, price);
            pstmt.setInt(3, total);
            pstmt.setString(4, buyerId);
            pstmt.executeUpdate();
            pstmt.close();

            // 판매상세 등록 (sale_Details)
            String sqlSale = "INSERT INTO sale_Details (ORDER_DETAIL, qty, product_id, ORDER_CODE) "
                    + "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlSale);
            pstmt.setString(1, saleDetailCode);
            pstmt.setInt(2, qty);
            pstmt.setString(3, product_id);
            pstmt.setString(4, orderCode);
            pstmt.executeUpdate();
            pstmt.close();

         // 마일리지 적립 (2%)
            int mileage = (int) (total * 0.02);
            long mileageId = System.nanoTime();

            // 기존 잔여 마일리지 조회
            int currentMileage = 0;
            String sqlSelectMileage = "SELECT NVL(MAX(REMAIN_MIL), 0) AS REMAIN_MIL FROM customer_Mileage WHERE CUS_ID = ?";
            pstmt = conn.prepareStatement(sqlSelectMileage);
            pstmt.setString(1, buyerId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                currentMileage = rs.getInt("REMAIN_MIL");
            }
            rs.close();
            pstmt.close();

            // 누적된 마일리지 계산
            int updatedMileage = currentMileage + mileage;

            // 새 마일리지 내역 INSERT
            String sqlMileage = "INSERT INTO customer_Mileage "
                    + "(MILEAGE_ID, MILEAGE_YN, CHANGE_MIL, REMAIN_MIL, MILEAGE_DATE, ORDER_CODE, CUS_ID) "
                    + "VALUES (?, ?, ?, ?, SYSDATE, ?, ?)";

            pstmt = conn.prepareStatement(sqlMileage);
            pstmt.setLong(1, mileageId);
            pstmt.setString(2, "적립");
            pstmt.setInt(3, mileage);
            pstmt.setInt(4, updatedMileage); // 누적 잔여 마일리지 반영
            pstmt.setString(5, orderCode);
            pstmt.setString(6, buyerId);
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit(); // 모든 작업 정상 처리 시 커밋

            // 결과 메시지 출력
            System.out.println("\n[구매 완료]");
            System.out.println("구매자 : " + buyerName);
            System.out.println("상품명 : " + productName);
            System.out.println("수량 : " + qty);
            System.out.println("총 결제 금액 : " + total + "원");
            System.out.println("적립된 마일리지 : " + mileage + "점");
            System.out.println("주문 코드 : " + orderCode);
            System.out.println("판매 상세 번호 : " + saleDetailCode);

            return true;

        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println("트랜잭션 롤백 완료");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("상품 구매 중 오류 발생: " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                conn.setAutoCommit(true);
            } catch (Exception e) {}
        }
    }

    // 상품 리스트
    public void listProduct() throws SQLException {
    	
    	String sql = "SELECT * FROM product_Tab ORDER BY product_name";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n[상품리스트]");
            System.out.println("상품ID\t상품명\t가격\t분류명\t분류ID");
            System.out.println("---------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%s\t%s\t%d\t%s\t%s\n",
                        rs.getString("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("product_price"),
                        rs.getString("product_class_name"),
                        rs.getString("product_class_id"));
            }
        }
    }

    // 상품 분류 리스트
    public void listProductClass() throws SQLException {
        String sql = "SELECT PRODUCT_CLASS_ID, PRODUCT_CLASS_NAME, NVL(PRODUCT_CLASS_ID2, '-') AS PARENT_ID FROM product_Class";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n[상품 분류 리스트]");
            System.out.println("분류ID\t분류명\t상위분류ID");
            System.out.println("------------------------------------");

            while (rs.next()) {
                System.out.printf("%s\t%s\t%s\n",
                        rs.getString("PRODUCT_CLASS_ID"),
                        rs.getString("PRODUCT_CLASS_NAME"),
                        rs.getString("PARENT_ID"));
            }
        }
    }
}
