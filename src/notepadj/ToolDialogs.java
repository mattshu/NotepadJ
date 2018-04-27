package notepadj;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public abstract class ToolDialogs {
	
	protected static final void showFindDialog() {
		FindDialog findDialog = new FindDialog();
		findDialog.initialize();
	}
	
	protected static final void showReplaceDialog() {
		ReplaceDialog replaceDialog = new ReplaceDialog();
		replaceDialog.initialize();
	}
	
	protected static final void showGotoDialog() {
		System.out.println("OK");
		GotoDialog gotoDialog = new GotoDialog();
		gotoDialog.initialize();
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
	
	protected static final File getFileDialog(MainWindow mainWindow) {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(MainWindow.frmNotepadJ);
		if (option == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	protected static final void showFontDialog() {
		FontDialog fontDialog = new FontDialog();
		fontDialog.initialize();
	}
}
