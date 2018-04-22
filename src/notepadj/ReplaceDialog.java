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
	
	public ReplaceDialog() {
		contentPane = new JPanel();
		txtFind = new JTextField();
		txtReplace = new JTextField();
		btnFind = new JButton("Find Next");
		btnReplace = new JButton("Replace");
		btnReplaceAll = new JButton("Replace All");
		chkMatchCase = new JCheckBox("Match case");
	}
	
	protected void initialize(MainWindow mainWindow) {
		mainTextArea = MainWindow.mainTextArea;		
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
		txtReplace.setColumns(10);
		txtReplace.setBounds(111, 43, 173, 20);
		btnFind.setEnabled(false);
		btnFind.setBounds(296, 12, 105, 23);
		btnFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO
			}	
		});
		btnReplace.setEnabled(false);
		btnReplace.setBounds(296, 42, 105, 23);
		btnReplace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO replace()
			}
		});
		btnReplaceAll.setEnabled(false);
		btnReplaceAll.setBounds(296, 73, 105, 23);
		btnReplaceAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO replaceAll()
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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
