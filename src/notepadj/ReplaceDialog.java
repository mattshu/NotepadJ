package notepadj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ReplaceDialog extends JFrame {

	private JPanel contentPane;
	private JTextField txtFind;
	private JTextField txtReplace;
	private JButton btnFind;
	private JButton btnReplace;
	private JButton btnReplaceAll;
	private JCheckBox chkMatchCase;
	private DocumentManager documentManager;
	private JTextArea mainTextArea;
	private Finder finder;
	
	public ReplaceDialog() {
		documentManager = MainWindow.getDocumentManager();
		finder = documentManager.getFinder();
		mainTextArea = MainWindow.getMainTextArea();
		contentPane = new JPanel();
		txtFind = new JTextField();
		txtReplace = new JTextField();
		btnFind = new JButton("Find Next");
		btnReplace = new JButton("Replace");
		btnReplaceAll = new JButton("Replace All");
		chkMatchCase = new JCheckBox("Match case");
	}
	
	public void initialize() {
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Replace");
		setBounds(300, 300, 419, 186);
		JLabel lblFindWhat = new JLabel("Find what:");
		lblFindWhat.setBounds(10, 11, 83, 14);
		txtFind.setBounds(111, 11, 173, 20);
		txtFind.setColumns(10);
		txtFind.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				// Ignore
			}
		
			public void insertUpdate(DocumentEvent arg0) {
				enableButtons();
			}
		
			public void removeUpdate(DocumentEvent arg0) {
				if (txtFind.getText().length() == 0)
					disableButtons();
			}
		});
		JLabel lblReplaceWith = new JLabel("Replace with:");
		lblReplaceWith.setBounds(10, 43, 83, 14);
		txtReplace.setColumns(10);
		txtReplace.setBounds(111, 43, 173, 20);
		btnFind.setEnabled(false);
		btnFind.setBounds(296, 12, 105, 23);
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startFind();
			}	
		});
		btnReplace.setEnabled(false);
		btnReplace.setBounds(296, 42, 105, 23);
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replace();
			}
		});
		btnReplaceAll.setEnabled(false);
		btnReplaceAll.setBounds(296, 73, 105, 23);
		btnReplaceAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				replaceAll();
			}
		});
		chkMatchCase.setBounds(8, 126, 97, 23);
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(296, 102, 105, 23);
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(lblFindWhat);
		contentPane.add(txtFind);
		contentPane.add(lblReplaceWith);
		contentPane.add(txtReplace);
		contentPane.add(btnFind);
		contentPane.add(btnReplace);
		contentPane.add(btnReplaceAll);
		contentPane.add(chkMatchCase);
		contentPane.add(btnCancel);
		setVisible(true);
	}
	
	private void enableButtons() {
		btnFind.setEnabled(true);
		btnReplace.setEnabled(true);
		btnReplaceAll.setEnabled(true);
	}
	private final void disableButtons() {
		btnFind.setEnabled(false);
		btnReplace.setEnabled(false);
		btnReplaceAll.setEnabled(false);
	}
	private void setFinderOptions() {
		FindOptions options = new FindOptions();
		options.setQuery(txtFind.getText());
		options.setMatchCase(chkMatchCase.isSelected());
		finder.setOptions(options);
	}
	private void startFind() {
		if (finder.hasEmptyQuery())
			setFinderOptions();
		finder.find();
	}
	
	private void replace() {
		if (mainTextArea.getSelectedText() == null) startFind();
		if (mainTextArea.getSelectedText() == null) return;
		mainTextArea.replaceSelection(txtReplace.getText());
		startFind();
	}
	
	private void replaceAll() {
		String document = mainTextArea.getText();
		String find = txtFind.getText();
		String replace = txtReplace.getText();
		if (!chkMatchCase.isSelected())
			find = "(?i)" + find; // Case-insensitive regex
		document = document.replaceAll(find, replace);
		mainTextArea.setText(document);
		mainTextArea.setCaretPosition(0);
	}
}
