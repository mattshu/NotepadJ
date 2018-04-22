package notepadj;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReplaceDialog extends JFrame {

	private JTextArea mainTextArea;
	
	private JPanel contentPane;
	private JTextField txtFind;
	private JTextField txtReplace;
	private JButton btnFind;
	private JButton btnReplace;
	private JButton btnReplaceAll;
	private JCheckBox chkMatchCase;
	
	public ReplaceDialog(MainWindow mainWindow) {
		mainTextArea = mainWindow.mainTextArea;		
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Replace");
		setBounds(300, 300, 419, 186);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFindWhat = new JLabel("Find what:");
		lblFindWhat.setBounds(10, 11, 83, 14);
		contentPane.add(lblFindWhat);
		
		txtFind = new JTextField();
		txtFind.setBounds(111, 11, 173, 20);
		contentPane.add(txtFind);
		txtFind.setColumns(10);
		txtFind.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// Ignore
			}
		
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				enableButtons();
			}
		
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (txtFind.getText().length() == 0)
					disableButtons();
			}
		});
		
		JLabel lblReplaceWith = new JLabel("Replace with:");
		lblReplaceWith.setBounds(10, 43, 83, 14);
		contentPane.add(lblReplaceWith);
		
		txtReplace = new JTextField();
		txtReplace.setColumns(10);
		txtReplace.setBounds(111, 43, 173, 20);
		contentPane.add(txtReplace);
		
		btnFind = new JButton("Find Next");
		btnFind.setEnabled(false);
		btnFind.setBounds(296, 12, 105, 23);
		contentPane.add(btnFind);
		btnFind.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Do I share the Find function or reference it directly? Eeek
			}
			
		});
		
		btnReplace = new JButton("Replace");
		btnReplace.setEnabled(false);
		btnReplace.setBounds(296, 42, 105, 23);
		contentPane.add(btnReplace);
		btnReplace.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO replace()
			}
			
		});
		
		btnReplaceAll = new JButton("Replace All");
		btnReplaceAll.setEnabled(false);
		btnReplaceAll.setBounds(296, 73, 105, 23);
		contentPane.add(btnReplaceAll);
		btnReplaceAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO replaceAll()
			}
			
		});
		
		chkMatchCase = new JCheckBox("Match case");
		chkMatchCase.setBounds(8, 126, 97, 23);
		contentPane.add(chkMatchCase);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.hideAllDialogs();
			}
		});
		btnCancel.setBounds(296, 102, 105, 23);
		contentPane.add(btnCancel);
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
}
