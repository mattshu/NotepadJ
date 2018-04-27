package notepadj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FindDialog extends JFrame {

	private JPanel contentPane;
	private JTextField txtFind;
	private JButton btnFindNext;
	private JRadioButton radUp;
	private JRadioButton radDown;
	private JCheckBox chkMatchCase;
	
	public FindDialog() {
		contentPane = new JPanel();
		txtFind = new JTextField();
		btnFindNext = new JButton("Find Next");
		radUp = new JRadioButton("Up");
		radDown = new JRadioButton("Down");
		chkMatchCase = new JCheckBox("Match case");
	}
	
	// TODO Redo this system??
	
	public void initialize() {
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Find");
		setBounds(300, 300, 382, 145);
		JLabel lblFindWhat = new JLabel("Find what:");
		lblFindWhat.setBounds(10, 11, 57, 14);
		txtFind.setBounds(77, 8, 173, 20);
		txtFind.setColumns(10);
		txtFind.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				// Ignore
			}

			public void insertUpdate(DocumentEvent arg0) {
				btnFindNext.setEnabled(true);
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (txtFind.getText().length() == 0)
					btnFindNext.setEnabled(false);
			}
		});
		btnFindNext.setEnabled(false);
		btnFindNext.setBounds(260, 7, 96, 23);
		btnFindNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startFind();
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(260, 37, 96, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		radUp.setBounds(6, 24, 50, 23);
		radDown.setBounds(58, 24, 64, 23);
		radDown.setSelected(true);
		JPanel panel = new JPanel();
		panel.setName("");
		panel.setBorder(new TitledBorder(null, "Direction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(122, 40, 128, 54);
		panel.setLayout(null);
		panel.add(radUp);
		panel.add(radDown);
		chkMatchCase.setBounds(6, 71, 97, 23);
		ButtonGroup group = new ButtonGroup();
		group.add(radUp);
		group.add(radDown);
		setContentPane(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		contentPane.add(lblFindWhat);
		contentPane.add(txtFind);
		contentPane.add(btnFindNext);
		contentPane.add(btnCancel);
		contentPane.add(panel);
		contentPane.add(chkMatchCase);
		setVisible(true);
	}

	protected void startFind() {
		String query = txtFind.getText();
		if (MainWindow.mainTextArea == null || query.length() == 0) return;
		String mainText = MainWindow.mainTextArea.getText();
		if (mainText.length() == 0) return;
		int caretPos = MainWindow.mainTextArea.getCaretPosition();
		int queryLen = query.length();
		if (radDown.isSelected()) {
			for (int i = caretPos; i < mainText.length() - queryLen; i++) {
				if (isQueryInTextAtPosition(query, mainText, i)) {
					System.out.println(i + " [d] " + i + queryLen);
					//highlightFoundString(i, i + queryLen);
					break;
				}
			}
		}
		else {
			for (int i = caretPos - 1 - queryLen; i >= 0; i--) {
				if (isQueryInTextAtPosition(query, mainText, i)) {
					highlightFoundString(i - queryLen - 1, i);
					System.out.println(i + " [u] " + i + queryLen);
					break;
				}
			}
		}
	}
	private boolean isQueryInTextAtPosition(String query, String text, int position) {
		int queryLen = query.length();
		System.out.println("query: " + query + " text: " + text + " position: " + position);
		String sub = text.substring(position, position + queryLen);
		return ((chkMatchCase.isSelected() && sub.equals(query)) || (sub.equalsIgnoreCase(query))); 
	}
	private void highlightFoundString(int start, int end) {
		MainWindow.mainTextArea.setSelectionStart(start);
		MainWindow.mainTextArea.setSelectionEnd(end);
	}
}
