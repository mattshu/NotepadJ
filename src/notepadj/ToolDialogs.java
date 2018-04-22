package notepadj;

import javax.swing.JOptionPane;

public abstract class ToolDialogs {
	
	protected static final void showFindDialog(MainWindow mainWindow) {
		FindDialog findDialog = new FindDialog();
		findDialog.initialize(mainWindow);
	}
	
	protected static final void showReplaceDialog(MainWindow mainWindow) {
		ReplaceDialog replaceDialog = new ReplaceDialog();
		replaceDialog.initialize(mainWindow);
	}
	
	protected static final void showGotoDialog(MainWindow mainWindow) {
		System.out.println("OK");
		GotoDialog gotoDialog = new GotoDialog();
		gotoDialog.initialize(mainWindow);
	}
	
	protected static final void errorPopup(String msg) {
		errorPopup(msg, "Error");
	}
	protected static final void errorPopup(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
		System.out.println("* Error: " + msg);
	}
	protected static final void confirmDialog(String msg) {
		confirmDialog(msg, "Confirm");
	}
	protected static final int confirmDialog(String msg, String title) {
		return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	protected static final void showAboutDialog() {
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.setVisible(true);
	}
}
