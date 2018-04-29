package notepadj;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JTextArea;

public class MenuActions {
	
	private JTextArea mainTextArea;
	private DocumentManager documentManager;
	
	public MenuActions() { 
		mainTextArea = MainWindow.getMainTextArea();
		documentManager = MainWindow.getDocumentManager();
	}

	public final void selectAllText() {
		mainTextArea.setSelectionStart(0);
		mainTextArea.setSelectionEnd(-1);
	}
	
	public final void insertTimeDateAtCaret() {
		LocalDateTime dateTime = LocalDateTime.now();
		// 11:08 PM 3/20/2018
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a M/d/yyyy");
		mainTextArea.insert(dateTime.format(dateTimeFormatter), mainTextArea.getCaretPosition());
	}
	
	public void changeFont() { ToolDialogs.showFontDialog(); }
	public void findDialog() { ToolDialogs.showFindDialog(); }
	public void findNext() { documentManager.findNext(); }
	public void replaceDialog() { ToolDialogs.showReplaceDialog(); }
	public void gotoDialog() { ToolDialogs.showGotoDialog(); }
	public void toggleWordWrap() { documentManager.toggleWordWrap(); }
	public void showAboutDialog() { ToolDialogs.showAboutDialog(); }
	
}
