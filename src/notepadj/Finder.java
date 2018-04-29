package notepadj;

import javax.swing.JTextArea;

public class Finder {

	private JTextArea mainTextArea;
	
	private String document;
	private String query;
	private int caretPosition;
	private int direction;
	private boolean matchCase;

	public Finder() {
		setOptions(new FindOptions());
		mainTextArea = MainWindow.getMainTextArea();
	}
	
	public void setOptions(FindOptions options) {
		query = options.getQuery();
		caretPosition = options.getCaretPosition();
		direction = options.getDirection();
		matchCase = options.isMatchCase();
	}
	
	public void find() {
		document = mainTextArea.getText();
		int resultStart = getResult();
		if (resultStart == -1) {
			ToolDialogs.msgDialog("No results found.");
			return;
		}
		int resultEnd = resultStart + query.length();
		setMainTextAreaSelection(resultStart, resultEnd);
	}
	
	public int getResult() {
		if (!matchCase) {
			document = document.toLowerCase();
			query = query.toLowerCase();
		}
		updateCaretPosition();
		if (direction == FindOptions.DOWN)
			return document.indexOf(query, caretPosition);
		else 
			return document.lastIndexOf(query, caretPosition - query.length() - 1);
	}
	private void updateCaretPosition() {
		caretPosition = mainTextArea.getCaretPosition();
	}
	public boolean hasEmptyQuery() {
		return query == null || query.isEmpty();
	}
	
	private void setMainTextAreaSelection(int start, int end) {
		mainTextArea.setSelectionStart(start);
		mainTextArea.setSelectionEnd(end);
	}


	
}
