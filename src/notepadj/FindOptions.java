package notepadj;

public class FindOptions {

	public static final int DOWN = 0;
	public static final int UP = 1;
	
	private String query = "";
	private int caretPosition = MainWindow.getMainTextArea().getCaretPosition();
	private int direction = DOWN;
	private boolean matchCase = false;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getCaretPosition() {
		return caretPosition;
	}
	public void setCaretPosition(int caretPosition) {
		this.caretPosition = caretPosition;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public boolean isMatchCase() {
		return matchCase;
	}
	public void setMatchCase(boolean matchCase) {
		this.matchCase = matchCase;
	}
}
