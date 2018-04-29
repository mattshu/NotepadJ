package notepadj;

import java.awt.Event;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

public class MainWindow extends JFrame {
	
	private static MainWindow mainWindow;
	private static boolean isEdited = false;
	private static JFrame frmNotepadJ;
	protected static final String TITLE_SIGNATURE = " - NotepadJ";

	private static MenuToolBar menuToolBar;
	private static JTextArea mainTextArea;
	private static JScrollPane mainScrollPane;
	private static DocumentManager documentManager;

	public static MainWindow getInstance() {
		return mainWindow;
	}

	public static MenuToolBar getMenuToolBar() {
		return menuToolBar;
	}

	public static JTextArea getMainTextArea() {
		return mainTextArea;
	}

	public static JScrollPane getMainScrollPane() {
		return mainScrollPane;
	}

	public static JFrame getFrame() {
		return frmNotepadJ;
	}

	public static DocumentManager getDocumentManager() {
		return documentManager;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setIsEdited(boolean condition) {
		isEdited = condition;
	}

	public MainWindow() {
		if (mainWindow == null)
			mainWindow = this;
		else return;
		
		/*StringBuilder sb = new StringBuilder();
		String doc = mainTextArea.getText();
		int len = doc.length();
		int i = 0;
		do {
			if (len < 1024) {
				sb.append(doc);
				break;
			}
			String snip = doc.substring(i * 1024, (i + 1) * 1024);
			sb.append(snip + "\n");
			i++;
			len /= 2;
		} while (len > 1024);
		mainTextArea.setText(sb.toString());
		int lenAfter = mainTextArea.getText().length();*/
		mainTextArea = new JTextArea();
		mainScrollPane = new JScrollPane();
		documentManager = new DocumentManager();
		menuToolBar = new MenuToolBar();
	}

	public void initialize() {
		frmNotepadJ = new JFrame();
		frmNotepadJ.setTitle(DocumentManager.DEFAULT_DOCUMENT_NAME + TITLE_SIGNATURE);
		frmNotepadJ.setBounds(100, 100, 1155, 546);
		frmNotepadJ.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmNotepadJ.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frmNotepadJ.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				exit();
			}
		});
		mainTextArea.addKeyListener(new KeyAdapter() {
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
		mainTextArea.setFont(DocumentManager.DEFAULT_FONT);

		frmNotepadJ.setJMenuBar(menuToolBar.getMenuToolBar());
		documentManager.initialize();
		frmNotepadJ.setVisible(true);
	}

	public final void updateDocumentTitle(String name) {
		frmNotepadJ.setTitle(name + TITLE_SIGNATURE);
	}

	public void resetDocumentTitle() {
		frmNotepadJ.setTitle(DocumentManager.DEFAULT_DOCUMENT_NAME + TITLE_SIGNATURE);
	}

	public final static void exit() {
		if (isEdited) {
			if (!documentManager.trySaveChanges())
				return;
		}
		System.exit(0);
	}

}
