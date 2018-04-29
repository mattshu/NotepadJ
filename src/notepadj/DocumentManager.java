package notepadj;

import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.undo.UndoManager;

public class DocumentManager {
	
	private MainWindow mainWindow;
	private JTextArea mainTextArea;
	private JScrollPane mainScrollPane;
	
	private Finder finder;
	
	protected final static String DEFAULT_DOCUMENT_NAME = "Untitled Document";
	protected final static Font DEFAULT_FONT = new Font("Tahoma", Font.PLAIN, 14);
	protected final static int DEFAULT_FONT_SIZE = 14;
	protected UndoManager undoManager;
	protected boolean cancelExit = false;
	private String documentPath = null;
	private String documentName = DEFAULT_DOCUMENT_NAME;
	private boolean wordWrapState = false;
	
	public DocumentManager() {
		undoManager = new UndoManager();
	}
	
	public void initialize() {
		mainWindow = MainWindow.getInstance();
		mainTextArea = MainWindow.getMainTextArea();
		mainScrollPane = MainWindow.getMainScrollPane();
		finder = new Finder();
	}
	
	public Finder getFinder() { return finder; }
	
	protected final void createNewDocument() {
		if (mainWindow.isEdited()) {
			int saveChangesResult = ToolDialogs.confirmSaveChanges();
			if (saveChangesResult == JOptionPane.YES_OPTION)
				save();
			else if (saveChangesResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		resetDocument();
	}
	
	protected final void openFile() {
		File file = ToolDialogs.getFileDialog();
		if (file != null && file.exists()) {
			if (mainWindow.isEdited())
				if (!trySaveChanges()) return;
			resetDocument();
			setDocumentVarsFromFile(file);
			mainWindow.updateDocumentTitle(file.getName());
			printFileToTextArea(file.getPath());
		}
	}
	
	protected final void saveDocumentInfo() {
		if (documentPath == null) {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showSaveDialog(mainWindow);
			if (option == JFileChooser.APPROVE_OPTION)
				setDocumentVarsFromFile(fileChooser.getSelectedFile());
			else {
				cancelExit = true;
			}
		}
		else if (documentName == null || documentName == DEFAULT_DOCUMENT_NAME)
			documentName = new File(documentPath).getName();
	}
	
	private final void setDocumentVarsFromFile(File documentFile) {
		documentPath = documentFile.getPath();
		documentName = documentFile.getName(); 
	}
	
	protected final void printFileToTextArea(String path) {
		try {
			byte[] buffer = new byte[1000];
			FileInputStream inputStream = new FileInputStream(path);
			@SuppressWarnings("unused")
			int nRead = 0;
			while ((nRead = inputStream.read(buffer)) != -1)
				mainTextArea.append(new String(buffer));
			inputStream.close();
			
		} catch (Exception e) {
			ToolDialogs.errorPopup("Unable to open document.\n" + e.getLocalizedMessage(), "Error");
			e.printStackTrace();
		}
	}
	
	protected final void print() {
		try {
			mainTextArea.print();
		} catch (PrinterException e) {
			ToolDialogs.errorPopup(e.getMessage());
			e.printStackTrace();
		}

	}
	
	protected final void save() {
		saveDocumentInfo();
		writeDocumentToFile();
		mainWindow.updateDocumentTitle(documentName);
		mainWindow.setIsEdited(false);
	}
	
	protected final void saveAs() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(mainWindow);
		if (option == JFileChooser.APPROVE_OPTION) {
			setDocumentVarsFromFile(fileChooser.getSelectedFile());
			save();
		}
	}

	protected boolean trySaveChanges() {
		int saveChangesResult = ToolDialogs.confirmSaveChanges();
		if (saveChangesResult == JOptionPane.YES_OPTION) {
			save();
			if (cancelExit) {
				cancelExit = false;
				mainWindow.setIsEdited(true);
				return false;
			}
			return true;
		}
		else if (saveChangesResult == JOptionPane.CANCEL_OPTION)
			return false;
		return true;
	}
	

	protected void onTextChanged(KeyEvent arg0) {
		mainWindow.setIsEdited(true);
	}
	
	protected final void writeDocumentToFile() {
		if (documentPath == null) return;
		try {
			String documentText = mainTextArea.getText();
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
			java.awt.Robot robot = new java.awt.Robot();
			robot.keyPress(KeyEvent.VK_DELETE);
			return true;
		}
		catch (java.awt.AWTException e) {
			return false;
		}
	}
	
	private final void resetDocumentVars() {
		documentPath = null;
		documentName = DEFAULT_DOCUMENT_NAME;
	}
	
	private final void resetDocument() {
		mainWindow.setIsEdited(false);
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
	
	protected void findNext() {
		if (finder.hasEmptyQuery()) {
			ToolDialogs.showFindDialog();
		}
		else finder.find();
	}
	
	protected void toggleWordWrap() {
		wordWrapState = !wordWrapState;
		mainTextArea.setWrapStyleWord(wordWrapState);
		mainTextArea.setLineWrap(wordWrapState);
		mainScrollPane.setVerticalScrollBarPolicy(wordWrapState ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		mainScrollPane.setHorizontalScrollBarPolicy(wordWrapState ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER : ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
}
