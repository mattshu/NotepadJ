package notepadj;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.undo.UndoManager;

public class DocumentManager {
	
	private MainWindow mainWindow;
	private JTextArea mainTextArea;
	protected final String DEFAULT_DOCUMENT_NAME = "Untitled Document";
	protected UndoManager undoManager;
	protected String documentPath = null;
	protected String documentName = DEFAULT_DOCUMENT_NAME;
	private boolean wordWrapState = false;
	
	public DocumentManager() {
		undoManager = new UndoManager();
	}
	
	public void initialize(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		mainTextArea = MainWindow.mainTextArea;
	}
	
	protected final void createNewDocument() {
		if (MainWindow.isEdited) {
			int saveChangesResult = confirmSaveChanges();
			if (saveChangesResult == JOptionPane.YES_OPTION)
				save();
			else if (saveChangesResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		resetDocument();
	}
	
	protected final void openFile() {
		File file = getFileByDialog();
		if (file != null && file.exists()) {
			if (MainWindow.isEdited)
				if (!trySaveChanges()) return;
			MainWindow.documentManager.resetDocument();
			setDocumentVarsFromFile(file);
			mainWindow.updateDocumentTitle(file.getName());
			printFileToTextArea(file.getPath());
		}
	}
	
	protected final void setupDocumentInfo() {
		if (documentPath == null) {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showSaveDialog(mainWindow.frmNotepadJ);
			if (option == JFileChooser.APPROVE_OPTION)
				setDocumentVarsFromFile(fileChooser.getSelectedFile());
		}
		else if (documentName == null || documentName == DEFAULT_DOCUMENT_NAME)
			documentName = new File(documentPath).getName();
	}
	private final void setDocumentVarsFromFile(File documentFile) {
		documentPath = documentFile.getPath();
		documentName = documentFile.getName(); 
	}
	


	protected final File getFileByDialog() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(mainWindow.frmNotepadJ);
		if (option == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	protected final void printFileToTextArea(String path) {
		try {
			StringBuffer sb = new StringBuffer();
			byte[] buffer = new byte[1000];
			FileInputStream inputStream = new FileInputStream(path);
			@SuppressWarnings("unused")
			int nRead = 0;
			while ((nRead = inputStream.read(buffer)) != -1) {
				sb.append(new String(buffer));
			}
			inputStream.close();
			MainWindow.mainTextArea.setText(sb.toString());
			
			
		} catch (Exception e) {
			ToolDialogs.errorPopup("Unable to open document.\n" + e.getLocalizedMessage(), "Error");
			e.printStackTrace();
		}
	}
	
	protected final void print() {
		try {
			MainWindow.mainTextArea.print();
		} catch (PrinterException e) {
			ToolDialogs.errorPopup(e.getMessage());
			e.printStackTrace();
		}

	}
	
	protected final void save() {
		setupDocumentInfo();
		writeDocumentToFile();
		mainWindow.updateDocumentTitle(documentName);
		MainWindow.isEdited = false;
	}
	
	protected final void saveAs() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(mainWindow.frmNotepadJ);
		if (option == JFileChooser.APPROVE_OPTION) {
			setDocumentVarsFromFile(fileChooser.getSelectedFile());
			save();
		}
	}

	protected final int confirmSaveChanges() {
		final String msg = "Do you wish to save changes?";
		final String title = "Confirm - Save Changes";
		int confirmDialog = ToolDialogs.confirmDialog(msg, title);
		return confirmDialog;
	}

	protected void onTextChanged(KeyEvent arg0) {
		MainWindow.isEdited = true;
	}
	
	protected final void writeDocumentToFile() {
		if (documentPath == null) return;
		try {
			String documentText = MainWindow.mainTextArea.getText();
			FileOutputStream outputStream = new FileOutputStream(documentPath);
			byte[] documentData = documentText.getBytes();
			outputStream.write(documentData);
			outputStream.close();
		} catch (Exception e) {
			ToolDialogs.errorPopup("Unable to open document.\n" + e.getLocalizedMessage(), "Error");
			e.printStackTrace();
		}

	}
	
	protected final void deleteSelectedTextManually() {
		int startPos = mainTextArea.getSelectionStart();
		int endPos = mainTextArea.getSelectionEnd();
		int caretPos = mainTextArea.getCaretPosition();
		String text = mainTextArea.getText();
		String outerLeftText = text.substring(0, startPos);
		String outerRightText = text.substring(endPos, text.length());
		mainTextArea.setText(outerLeftText + outerRightText);
		mainTextArea.setCaretPosition(caretPos);
	}
	
	protected final void deleteSelectedText() {
		if (!tryDeleteSelectedTextWithDeleteKey())
			deleteSelectedTextManually();
	}
	
	protected final boolean tryDeleteSelectedTextWithDeleteKey() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_DELETE);
			return true;
		}
		catch (AWTException e) {
			return false;
		}
	}
	

	private final void resetDocumentVars() {
		documentPath = null;
		documentName = DEFAULT_DOCUMENT_NAME;
	}
	
	private final void resetDocument() {
		MainWindow.isEdited = false;
		mainTextArea.setText("");
		resetDocumentVars();
		mainWindow.resetDocumentTitle();
	}
	
	public Action getUndoAction() {
		return new AbstractAction() { public void actionPerformed(ActionEvent e) { undo(); } };
	}

	protected final void undo() {
		if (undoManager.canUndo())
			undoManager.undo();
	}
	
	public Action getRedoAction() {
		return new AbstractAction() { public void actionPerformed(ActionEvent e) { redo(); } };
	}
	
	protected final void redo() {
		if (undoManager.canRedo())
			undoManager.redo();
	}
	
	protected boolean trySaveChanges() {
		int saveChangesResult = confirmSaveChanges();
		if (saveChangesResult == JOptionPane.YES_OPTION) {
			save();
			return true;
		}
		else if (saveChangesResult == JOptionPane.CANCEL_OPTION)
			return false;
		return true;
	}
	
	protected void findNext() {
		// TODO Auto-generated method stub
		
	}
	
	protected void toggleWordWrap() {
		MainWindow.mainTextArea.setWrapStyleWord(wordWrapState);
		MainWindow.mainTextArea.setLineWrap(wordWrapState);
		mainWindow.mainScrollPane.setVerticalScrollBarPolicy(wordWrapState ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		mainWindow.mainScrollPane.setHorizontalScrollBarPolicy(wordWrapState ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER : ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		wordWrapState = !wordWrapState;
	}
}
