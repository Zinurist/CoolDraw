package cool;

public class StartCool {

	public static void main(String[] args) {
		DrawCool dc=new DrawCool();
		EmptyWindow w=new EmptyWindow(dc);
		w.setVisible(true);
		new Thread(dc).start();
	}
}
