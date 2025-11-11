package cm.Login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class ProductManageUI {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private ProductManageImpl productManage = new ProductManageImpl();
    private LoginInfo loginInfo;

    
    public ProductManageUI(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    // [상품 등록]
    public void productAdd() throws IOException {
        System.out.println("\n[상품등록]");
        System.out.print("상품ID: ");
        String product_id = br.readLine();
        System.out.print("상품명: ");
        String product_name = br.readLine();
        System.out.print("가격: ");
        int product_price = Integer.parseInt(br.readLine());
        System.out.print("상품 분류명: ");
        String product_class_name = br.readLine();
        System.out.print("상품분류ID: ");
        String product_class_id = br.readLine();

        LoginDTO loginMember = loginInfo.loginMember();
        System.out.println("등록유저 : " + loginMember.getName() + "\n");

        try {
            productManage.insertProduct(product_id, product_name, product_price, product_class_name, product_class_id);
        } catch (SQLException e) {
            System.out.println("상품 등록 중 오류 발생: " + e.getMessage());
        }
    }

    // [상품 수정]
    public void productUpdate() throws IOException {
        System.out.println("\n[상품수정]");
        System.out.print("수정할 상품 ID: ");
        String product_id = br.readLine();
        System.out.print("새 상품명: ");
        String product_name = br.readLine();
        System.out.print("새 가격: ");
        int product_price = Integer.parseInt(br.readLine());
        System.out.print("새 상품 분류명: ");
        String product_class_name = br.readLine();
        System.out.print("새 상품분류ID: ");
        String product_class_id = br.readLine();

        try {
            productManage.updateProduct(product_id, product_name, product_price, product_class_name, product_class_id);
        } catch (SQLException e) {
            System.out.println("상품 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // [상품 검색]
    public void productSearch() throws IOException {
        System.out.println("\n[상품검색]");
        System.out.print("검색할 상품명 키워드: ");
        String keyword = br.readLine();

        try {
            productManage.searchProduct(keyword);
        } catch (SQLException e) {
            System.out.println("상품 검색 중 오류 발생: " + e.getMessage());
        }
    }
    // 상품 구매
    public void productBuy() {
        System.out.println("\n[상품구매]");
        LoginDTO loginMember = loginInfo.loginMember();
        System.out.println("구매유저 : " + loginMember.getName() + "\n");

        try {
            System.out.print("구매할 상품 ID: ");
            String product_id = br.readLine();

            System.out.print("구매 수량: ");
            int qty = Integer.parseInt(br.readLine());

            boolean success = productManage.productBuy(
                product_id,
                qty,
                loginMember.getCus_id(),
                loginMember.getName()
            );

            if (!success) {
                System.out.println(" 상품 구매에 실패했습니다.");
            }

        } catch (IOException e) {
            System.out.println("입력 오류: " + e.getMessage());
        }
    }

    // [상품 리스트]
    public void productList() {
        try {
            productManage.listProduct();
        } catch (SQLException e) {
            System.out.println("상품 리스트 조회 중 오류: " + e.getMessage());
        }
    }

    // [상품 분류 리스트]
    public void productClass() {
        try {
            productManage.listProductClass();
        } catch (SQLException e) {
            System.out.println("상품 분류 조회 중 오류: " + e.getMessage());
        }
    }
}
