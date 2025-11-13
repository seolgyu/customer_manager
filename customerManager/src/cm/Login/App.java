package cm.Login;

public class App {

	public static void main(String[] args) {
		MainUI ui = new MainUI();
		ui.loadingEffect("시스템 초기화 중");
        ui.colorfulBanner();
		ui.menu();
	}
}