package notepadj;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultEditorKit;

public class MenuBar implements ActionListener {
	
	private MainWindow mainWindow;
	private MenuActions menuActions;
	
	protected JMenuBar menuBar;
	private JMenuItem mntmCut;
	private JMenuItem mntmCopy;
	private JMenuItem mntmPaste;
	private JMenuItem mntmDelete;
	private JMenuItem mntmFind;
	private JMenuItem mntmFindNext;
	private JMenuItem mntmReplace;
	private JMenuItem mntmSelectAll;
	private JCheckBoxMenuItem chmntmWordWrap = new JCheckBoxMenuItem("Word Wrap");
	//private JCheckBoxMenuItem chmntmStatusBar = new JCheckBoxMenuItem("Status Bar");
	
	public MenuBar() {
		menuActions = new MenuActions(mainWindow);
	}
	protected JMenuBar getMenuBar() {
		menuBar = new JMenuBar();
		MainWindow.frmNotepadJ.setJMenuBar(menuBar);
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
		//JMenu mnView = new JMenu("View");
		JMenu mnHelp = new JMenu("Help");
		JMenuItem mntmAboutNotepadJ = new JMenuItem("About NotepadJ");
		
		menuBar.add(mnFile);
		menuBar.add(mnEdit);
		//menuBar.add(mnView);
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
		//chmntmStatusBar.addActionListener(this);
		mnFormat.add(chmntmWordWrap);
		//mnView.add(chmntmStatusBar);
		
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
		Action cutAction = MainWindow.mainTextArea.getActionMap().get(DefaultEditorKit.cutAction);
		mntmCut = mnEdit.add(cutAction);
		mntmCut.setText("Cut");
		Action copyAction = MainWindow.mainTextArea.getActionMap().get(DefaultEditorKit.copyAction);
		mntmCopy = mnEdit.add(copyAction);
		mntmCopy.setText("Copy");
		Action pasteAction = MainWindow.mainTextArea.getActionMap().get(DefaultEditorKit.pasteAction);
		mntmPaste = mnEdit.add(pasteAction);
		mntmPaste.setText("Paste");
		mnEdit.add(mntmDelete);
		mnEdit.add(new JSeparator());
		mnEdit.add(mntmFind);
		mnEdit.add(mntmFindNext);
		mnEdit.add(mntmReplace);
		mnEdit.add(mntmGoto);
		mnEdit.add(new JSeparator());
		Action selectAllAction = MainWindow.mainTextArea.getActionMap().get(DefaultEditorKit.selectAllAction);
		mntmSelectAll = mnEdit.add(selectAllAction);
		mntmSelectAll.setText("Select All");
		mnEdit.add(mntmPutTimeDate);
		mnFormat.add(mntmFont);
		mnHelp.add(mntmAboutNotepadJ);
		
		return menuBar;
	}
	
	private final void onEditMenuSelected() {
		if (MainWindow.mainTextArea.getText().isEmpty())
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
		boolean selectedTextExists = MainWindow.mainTextArea.getSelectedText() != null;
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
			// Assume there is no data
			return false;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String menuItem = e.getActionCommand();
		switch (menuItem) {
		case "New":
			MainWindow.documentManager.createNewDocument();
			break;
		case "Open...":
			MainWindow.documentManager.openFile();
			break;
		case "Save":
			MainWindow.documentManager.save();
			break;
		case "Save As...":
			MainWindow.documentManager.saveAs();
			break;
		case "Print...":
			MainWindow.documentManager.print();
			break;
		case "Exit":
			MainWindow.exit();
			break;
		case "Undo":
			MainWindow.documentManager.undo();
			break;
		case "Redo":
			MainWindow.documentManager.redo();
			break;
		case "Delete":
			MainWindow.documentManager.deleteSelectedText();
			break;
		case "Find...":
			menuActions.findDialog();
			break;
		case "Find Next":
			menuActions.findNext();
			break;
		case "Replace...":
			menuActions.replaceDialog();
			break;
		case "Go To...":
			menuActions.gotoDialog();
			break;
		case "Select All":
			menuActions.selectAllText();
			break;
		case "Time/Date":
			menuActions.insertTimeDateAtCaret();
			break;
		case "Word Wrap":
			menuActions.toggleWordWrap();
			break;
		case "Font...":
			menuActions.changeFont();
			break;
		//case "Status Bar":
			//menuActions.toggleStatusBar();
			//break;
		case "About NotepadJ":
			menuActions.showAboutDialog();
			break;
		default:
			ToolDialogs.errorPopup("Unrecognized selection: " + menuItem);
		}

	}
}