package notepadj;

import java.awt.Event;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import java.awt.GridLayout;
import javax.swing.JScrollPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
	protected static MainWindow mainWindow;
	protected static boolean isEdited = false;
	protected final String TITLE_SIGNATURE = " - NotepadJ";
	
	protected JFrame frmNotepadJ;
	protected MenuBar menuBar;
	protected static JTextArea mainTextArea;
	protected JScrollPane mainScrollPane;
	protected static DocumentManager documentManager;

	public MainWindow() {
		if (mainWindow == null)
			mainWindow = this;
		else return;
		mainTextArea = new JTextArea();
		mainScrollPane = new JScrollPane();
		documentManager = new DocumentManager();
	}
	
	protected void initialize() {
		frmNotepadJ = new JFrame();
		frmNotepadJ.setTitle(documentManager.DEFAULT_DOCUMENT_NAME + TITLE_SIGNATURE);
		frmNotepadJ.setBounds(100, 100, 1155, 546);
		frmNotepadJ.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmNotepadJ.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frmNotepadJ.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				exit();
			}
		});
		mainTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				documentManager.onTextChanged(arg0);
			}
		});
		mainScrollPane.setViewportView(mainTextArea);
		frmNotepadJ.getContentPane().add(mainScrollPane);
		mainTextArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
				public void undoableEditHappened(UndoableEditEvent e) {
					documentManager.undoManager.addEdit(e.getEdit());
				}
			});
		mainTextArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK), "undo");
		mainTextArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK), "redo");
		mainTextArea.getActionMap().put("undo", documentManager.getUndoAction());
		mainTextArea.getActionMap().put("redo", documentManager.getRedoAction());
		menuBar = new MenuBar();		
		frmNotepadJ.setJMenuBar(menuBar.getMenuBar(mainWindow));
		documentManager.initialize(mainWindow);
	}

	protected final void updateDocumentTitle(String name) {
		frmNotepadJ.setTitle(name + TITLE_SIGNATURE);
	}
	
	public void resetDocumentTitle() {
		frmNotepadJ.setTitle(documentManager.DEFAULT_DOCUMENT_NAME + TITLE_SIGNATURE);
	}
	
	protected final static void exit() {
		if (isEdited) {
			if (!documentManager.trySaveChanges()) return;
		}
		System.exit(0);
	}


}
