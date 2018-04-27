package notepadj;

import java.awt.EventQueue;

public class Program {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow mainWindow = new MainWindow();
					mainWindow.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
