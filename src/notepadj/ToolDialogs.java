package notepadj;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public abstract class ToolDialogs {
	
	protected static final void showFindDialog() {
		if (FindDialog.getInstance() == null) {
			FindDialog findDialog = new FindDialog();
			findDialog.initialize();
		}
		else FindDialog.getInstance().setVisible(true);
	}
	
	protected static final void showReplaceDialog() {
		ReplaceDialog replaceDialog = new ReplaceDialog();
		replaceDialog.initialize();
	}
	
	protected static final void showGotoDialog() {
		GotoDialog gotoDialog = new GotoDialog();
		gotoDialog.initialize();
	}
	
	protected static final void errorPopup(String msg) {
		errorPopup(msg, "Error");
	}
	protected static final void errorPopup(String msg, String title) {
		JOptionPane.showMessageDialog(MainWindow.getInstance(), msg, title, JOptionPane.ERROR_MESSAGE);
		System.out.println("* Error: " + msg);
	}
	protected static final void confirmDialog(String msg) {
		confirmDialog(msg, "Confirm");
	}
	protected static final int confirmDialog(String msg, String title) {
		return JOptionPane.showConfirmDialog(MainWindow.getInstance(), msg, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
	protected static final void showAboutDialog() {
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.setVisible(true);
	}
	
	protected static final File getFileDialog() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(MainWindow.getInstance());
		if (option == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return new File("");
	}
	
	protected static final void showFontDialog() {
		FontDialog fontDialog = new FontDialog();
		fontDialog.initialize();
	}
	
	protected static void msgDialog(String msg) {
		msgDialog(msg, "NotepadJ");
	}
	
	protected static void msgDialog(String msg, String title) {
		JOptionPane.showMessageDialog(MainWindow.getInstance(), msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	protected static final int confirmSaveChanges() {
		final String msg = "Do you wish to save changes?";
		final String title = "Confirm - Save Changes";
		int confirmDialog = ToolDialogs.confirmDialog(msg, title);
		return confirmDialog;
	}

}
