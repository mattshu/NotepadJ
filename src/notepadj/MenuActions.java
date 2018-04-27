package notepadj;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MenuActions {
	
	private MainWindow mainWindow;
	
	public MenuActions(MainWindow mainWindow) { 
		this.mainWindow = mainWindow;
	}

	protected final void selectAllText() {
		MainWindow.mainTextArea.setSelectionStart(0);
		MainWindow.mainTextArea.setSelectionEnd(-1);
	}
	
	protected final void insertTimeDateAtCaret() {
		LocalDateTime dateTime = LocalDateTime.now();
		// 11:08 PM 3/20/2018
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a M/d/yyyy");
		MainWindow.mainTextArea.insert(dateTime.format(dateTimeFormatter), MainWindow.mainTextArea.getCaretPosition());
	}
	
	protected void changeFont() {
		ToolDialogs.showFontDialog();

	}
	
	protected void findDialog() {
		ToolDialogs.showFindDialog();
	}
	
	protected void findNext() {
		MainWindow.documentManager.findNext();	
	}
	
	protected void replaceDialog() {
		ToolDialogs.showReplaceDialog();
	}
	
	protected void gotoDialog() {
		ToolDialogs.showGotoDialog();
	}

	protected void toggleWordWrap() {
		MainWindow.documentManager.toggleWordWrap();
	}
	
	public void showAboutDialog() {
		ToolDialogs.showAboutDialog();
	}
	
}
