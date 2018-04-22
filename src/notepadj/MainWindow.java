package notepadj;

import java.awt.AWTException;
import java.awt.Event;
import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainWindow implements ActionListener {

	private JFrame frmNotepadJDocument;
	
	protected JTextArea mainTextArea = new JTextArea();
	private JScrollPane mainScrollPane = new JScrollPane();
	private JCheckBoxMenuItem chmntmWordWrap = new JCheckBoxMenuItem("Word Wrap");
	private JCheckBoxMenuItem chmntmStatusBar = new JCheckBoxMenuItem("Status Bar");
	private JMenuItem mntmCut;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JMenuItem mntmDelete;
	private JMenuItem mntmFind;
	private JMenuItem mntmFindNext;
	private JMenuItem mntmReplace;
	private JMenuItem mntmSelectAll;
	
	protected FindDialog findDialog = new FindDialog(this);
	protected ReplaceDialog replaceDialog = new ReplaceDialog(this);
	protected GoToDialog goToDialog = new GoToDialog(this);
	
	private UndoManager undoManager = new UndoManager();
	
	private final String TITLE_SIGNATURE = " - NotepadJ";
	private final String DEFAULT_DOCUMENT_NAME = "Untitled Document";
	
	private boolean isEdited = false;
	private boolean isWordWrapEnabled = false;
	
	private String documentPath = null;
	private String documentName = DEFAULT_DOCUMENT_NAME;

	private final void createNewDocument() {
		if (isEdited) {
			int saveChangesResult = confirmSaveChanges();
			if (saveChangesResult == JOptionPane.YES_OPTION)
				save();
			else if (saveChangesResult == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}
		resetDocument();
	}
	private final void openFile() {
		File file = getFileByDialog();
		if (file != null && file.exists()) {
			setDocumentVarsFromFile(file);
			updateDocumentTitle();
			printFileToTextArea(file.getPath());
		}
	}
	private final File getFileByDialog() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(frmNotepadJDocument);
		if (option == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	private final void printFileToTextArea(String path) {
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
			mainTextArea.setText(sb.toString());
			
			
		} catch (Exception e) {
			errorPopup("Unable to open document.\n" + e.getLocalizedMessage(), "Error");
			e.printStackTrace();
		}
	}
	private final void save() {
		setupDocumentInfo();
		writeDocumentToFile();
		updateDocumentTitle();
	}
	private final void saveAs() {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(frmNotepadJDocument);
		if (option == JFileChooser.APPROVE_OPTION) {
			setDocumentVarsFromFile(fileChooser.getSelectedFile());
			save();
		}
	}
	private final int confirmSaveChanges() {
		final String msg = "Do you wish to save changes?";
		final String title = "Confirm - Save Changes";
		int confirmDialog = showConfirmDialog(msg, title);
		return confirmDialog;
	}
	private final void writeDocumentToFile() {
		if (documentPath == null) return;
		try {
			String documentText = mainTextArea.getText();
			FileOutputStream outputStream = new FileOutputStream(documentPath);
			byte[] documentData = documentText.getBytes();
			outputStream.write(documentData);
			outputStream.close();
		} catch (Exception e) {
			errorPopup("Unable to open document.\n" + e.getLocalizedMessage(), "Error");
			e.printStackTrace();
		}

	}
	private final void updateDocumentTitle() {
		frmNotepadJDocument.setTitle(documentName + TITLE_SIGNATURE);
	}
	private final void print() {
		try {
			mainTextArea.print();
		} catch (PrinterException e) {
			errorPopup(e.getMessage());
			e.printStackTrace();
		}

	}
	private final void exit() {
		if (isEdited) {
			int saveChangesResult = confirmSaveChanges();
			if (saveChangesResult == JOptionPane.YES_OPTION)
				save();
			else if (saveChangesResult == JOptionPane.CANCEL_OPTION)
				return;
		}
		System.exit(0);
	}
	private final void showAbout() {
		AboutDialog aboutDialog = new AboutDialog();
		aboutDialog.setVisible(true);
	}
	private final void undo() {
		if (undoManager.canUndo())
			undoManager.undo();
	}
	private final void redo() {
		if (undoManager.canRedo())
			undoManager.redo();
	}
	private final boolean tryDeleteSelectedTextWithDeleteKey() {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_DELETE);
			return true;
		}
		catch (AWTException e) {
			return false;
		}
	}
	private final void deleteSelectedTextManually() {
		int startPos = mainTextArea.getSelectionStart();
		int endPos = mainTextArea.getSelectionEnd();
		int caretPos = mainTextArea.getCaretPosition();
		String text = mainTextArea.getText();
		String outerLeftText = text.substring(0, startPos);
		String outerRightText = text.substring(endPos, text.length());
		mainTextArea.setText(outerLeftText + outerRightText);
		mainTextArea.setCaretPosition(caretPos);
	}
	private final void deleteSelectedText() {
		if (!tryDeleteSelectedTextWithDeleteKey())
			deleteSelectedTextManually();
	}
	protected final void hideAllDialogs() {
		findDialog.setVisible(false);
		replaceDialog.setVisible(false);
		goToDialog.setVisible(false);
	}
	private final void showFindDialog() {
		hideAllDialogs();
		findDialog.setVisible(true);
	}
	private void findNext() {
		// TODO
		showFindDialog();
	}
	private final void showReplaceDialog() {
		hideAllDialogs();
		replaceDialog.setVisible(true);
	}
	private final void showGoToDialog() {
		hideAllDialogs();
		goToDialog.setVisible(true);
	}
	private final void selectAllText() {
		mainTextArea.setSelectionStart(0);
		mainTextArea.setSelectionEnd(-1);
	}
	private final void insertTimeDateAtCaret() {
		LocalDateTime dateTime = LocalDateTime.now();
		// 11:08 PM 3/20/2018
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a M/d/yyyy");
		mainTextArea.insert(dateTime.format(dateTimeFormatter), mainTextArea.getCaretPosition());
	}
	private void changeFont() {
		// TODO Auto-generated method stub

	}
	private final void toggleWordWrap() {
		if (isWordWrapEnabled)
			disableWordWrap();
		else
			enableWordWrap();
	}
	private final void enableWordWrap() {
		mainTextArea.setWrapStyleWord(true);
		mainTextArea.setLineWrap(true);
		mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		isWordWrapEnabled = true;
	}
	private final void disableWordWrap() {
		mainTextArea.setWrapStyleWord(false);
		mainTextArea.setLineWrap(false);
		mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		isWordWrapEnabled = false;
	}
	private void toggleStatusBar() {
		// TODO Auto-generated method stub
	}
	
	private final void setupDocumentInfo() {
		if (documentPath == null) {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showSaveDialog(frmNotepadJDocument);
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
	private final void resetDocumentVars() {
		documentPath = null;
		documentName = DEFAULT_DOCUMENT_NAME;
	}
	private final void resetDocument() {
		isEdited = false;
		mainTextArea.setText("");
		resetDocumentVars();
		updateDocumentTitle();
	}
	private final void errorPopup(String msg) {
		errorPopup(msg, "Error");
	}
	private final void errorPopup(String msg, String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
		System.out.println("* Error: " + msg);
	}
	private final int showConfirmDialog(String msg, String title) {
		return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
	private final void onTextChanged(KeyEvent arg0) {
		isEdited = true;
	}
	private final void onEditMenuSelected() {
		if (mainTextArea.getText().isEmpty())
			disableSpecialEditMenuItems();
		else
		{
			setSpecialEditMenuStates();
			enableFindReplaceMenuStates();
		}
		setPasteMenuState();

	}
	private final void disableSpecialEditMenuItems() {
		mntmCut.setEnabled(false);
		mntmCopy.setEnabled(false);
		mntmDelete.setEnabled(false);
		mntmFind.setEnabled(false);
		mntmFindNext.setEnabled(false);
		mntmReplace.setEnabled(false);
	}
	private final void setSpecialEditMenuStates() {
		boolean selectedTextExists = mainTextArea.getSelectedText() != null;
		mntmCut.setEnabled(selectedTextExists);
		mntmCopy.setEnabled(selectedTextExists);
		mntmDelete.setEnabled(selectedTextExists);
	}
	private final void setPasteMenuState() {
		mntmPaste.setEnabled(hasDataInClipboard());
	}
	private final void enableFindReplaceMenuStates() {
		mntmFind.setEnabled(true);
		mntmFindNext.setEnabled(true);
		mntmReplace.setEnabled(true);
	}
	private final boolean hasDataInClipboard() {
	    try {
			Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			String clipboardContents = (String) systemClipboard.getData(DataFlavor.stringFlavor);
			return clipboardContents.isEmpty() == false;
		} catch (Exception e) {
			// Assume there is data otherwise
			return true;
		}
	}
	private final void buildMenuAndItems() {
		JMenuBar menuBar = new JMenuBar();
		frmNotepadJDocument.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		JMenuItem mntmNew = new JMenuItem("New");
		JMenuItem mntmOpen = new JMenuItem("Open...");
		JMenuItem mntmSave = new JMenuItem("Save");
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		JMenuItem mntmPrint = new JMenuItem("Print...");
		JMenuItem mntmExit = new JMenuItem("Exit");
		JMenu mnEdit = new JMenu("Edit");
		JMenuItem mntmUndo = new JMenuItem("Undo");
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmDelete = new JMenuItem("Delete");
		mntmFind = new JMenuItem("Find...");
		mntmFindNext = new JMenuItem("Find Next");
		mntmReplace = new JMenuItem("Replace...");
		JMenuItem mntmGoto = new JMenuItem("Go To...");
		mntmSelectAll = new JMenuItem("Select All");
		JMenuItem mntmPutTimeDate = new JMenuItem("Time/Date");
		JMenu mnFormat = new JMenu("Format");
		JMenuItem mntmFont = new JMenuItem("Font...");
		JMenu mnView = new JMenu("View");
		JMenu mnHelp = new JMenu("Help");
		JMenuItem mntmAboutNotepadJ = new JMenuItem("About NotepadJ");
		
		menuBar.add(mnFile);
		menuBar.add(mnEdit);
		menuBar.add(mnView);
		menuBar.add(mnFormat);
		menuBar.add(mnHelp);
		
		mntmNew.addActionListener(this);
		mntmOpen.addActionListener(this);
		mntmSave.addActionListener(this);
		mntmSaveAs.addActionListener(this);
		mntmPrint.addActionListener(this);
		mntmExit.addActionListener(this);
		mntmUndo.addActionListener(this);
		mntmRedo.addActionListener(this);
		mntmDelete.addActionListener(this);
		mntmFind.addActionListener(this);
		mntmFindNext.addActionListener(this);
		mntmReplace.addActionListener(this);
		mntmGoto.addActionListener(this);
		mntmPutTimeDate.addActionListener(this);
		mntmFont.addActionListener(this);
		mntmAboutNotepadJ.addActionListener(this);
		
		chmntmWordWrap.addActionListener(this);
		chmntmStatusBar.addActionListener(this);
		mnFormat.add(chmntmWordWrap);
		mnView.add(chmntmStatusBar);
		
		mnEdit.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				onEditMenuSelected();
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
				;
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				;
			}
		});
		
		mnFile.add(mntmNew);
		mnFile.add(mntmOpen);
		mnFile.add(mntmSave);
		mnFile.add(mntmSaveAs);
		mnFile.add(new JSeparator());
		mnFile.add(mntmPrint);
		mnFile.add(new JSeparator());
		mnFile.add(mntmExit);
		mnEdit.add(mntmUndo);
		mnEdit.add(mntmRedo);
		mnEdit.add(new JSeparator());
		Action cutAction = mainTextArea.getActionMap().get(DefaultEditorKit.cutAction);
		mntmCut = mnEdit.add(cutAction);
		mntmCut.setText("Cut");
		Action copyAction = mainTextArea.getActionMap().get(DefaultEditorKit.copyAction);
		mntmCopy = mnEdit.add(copyAction);
		mntmCopy.setText("Copy");
		Action pasteAction = mainTextArea.getActionMap().get(DefaultEditorKit.pasteAction);
		mntmPaste = mnEdit.add(pasteAction);
		mntmPaste.setText("Paste");
		mnEdit.add(mntmDelete);
		mnEdit.add(new JSeparator());
		mnEdit.add(mntmFind);
		mnEdit.add(mntmFindNext);
		mnEdit.add(mntmReplace);
		mnEdit.add(mntmGoto);
		mnEdit.add(new JSeparator());
		Action selectAllAction = mainTextArea.getActionMap().get(DefaultEditorKit.selectAllAction);
		mntmSelectAll = mnEdit.add(selectAllAction);
		mntmSelectAll.setText("Select All");
		mnEdit.add(mntmPutTimeDate);
		mnFormat.add(mntmFont);
		mnHelp.add(mntmAboutNotepadJ);
	}
	
	public MainWindow() {
		initialize();
	}
	
	private void initialize() {
		frmNotepadJDocument = new JFrame();
		frmNotepadJDocument.setTitle(DEFAULT_DOCUMENT_NAME + TITLE_SIGNATURE);
		frmNotepadJDocument.setBounds(100, 100, 1155, 546);
		frmNotepadJDocument.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmNotepadJDocument.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frmNotepadJDocument.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				exit();
			}
		});

		mainTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				onTextChanged(arg0);
			}
		});

		mainScrollPane.setViewportView(mainTextArea);
		frmNotepadJDocument.getContentPane().add(mainScrollPane);
		
		mainTextArea.getDocument().addUndoableEditListener(
				new UndoableEditListener() {
					public void undoableEditHappened(UndoableEditEvent e) {
						undoManager.addEdit(e.getEdit());
					}
				});

		mainTextArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK), "undo");
		mainTextArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK), "redo");
		Action undo = new AbstractAction() { public void actionPerformed(ActionEvent e) { undo(); } };
		Action redo = new AbstractAction() { public void actionPerformed(ActionEvent e) { redo(); } };
		mainTextArea.getActionMap().put("undo", undo);
		mainTextArea.getActionMap().put("redo", redo);
		
		buildMenuAndItems();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String menuItem = e.getActionCommand();
		switch (menuItem) {
		case "New":
			createNewDocument();
			break;
		case "Open...":
			openFile();
			break;
		case "Save":
			save();
			break;
		case "Save As...":
			saveAs();
			break;
		case "Print...":
			print();
			break;
		case "Exit":
			exit();
			break;
		case "Undo":
			undo();
			break;
		case "Redo":
			redo();
			break;
		case "Delete":
			deleteSelectedText();
			break;
		case "Find...":
			showFindDialog();
			break;
		case "Find Next":
			findNext();
			break;
		case "Replace...":
			showReplaceDialog();
			break;
		case "Go To...":
			showGoToDialog();
			break;
		case "Select All":
			selectAllText();
			break;
		case "Time/Date":
			insertTimeDateAtCaret();
			break;
		case "Word Wrap":
			toggleWordWrap();
			break;
		case "Font...":
			changeFont();
			break;
		case "Status Bar":
			toggleStatusBar();
			break;
		case "About NotepadJ":
			showAbout();
			break;
		default:
			errorPopup("Unrecognized selection: " + menuItem);
		}

	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmNotepadJDocument.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
