package notepadj;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FindDialog extends JFrame {

	private JTextArea mainTextArea;
	
	private JPanel contentPane;
	private JTextField txtFind;
	private JButton btnFindNext;
	private JRadioButton radUp;
	private JRadioButton radDown;
	private JCheckBox chkMatchCase;
	
	public FindDialog(MainWindow mainWindow) {
		mainTextArea = mainWindow.mainTextArea;
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle("Find");
		setBounds(300, 300, 382, 145);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
				
		JLabel lblFindWhat = new JLabel("Find what:");
		lblFindWhat.setBounds(10, 11, 57, 14);
		contentPane.add(lblFindWhat);
		
		txtFind = new JTextField();
		txtFind.setBounds(77, 8, 173, 20);
		contentPane.add(txtFind);
		txtFind.setColumns(10);
		txtFind.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				// Ignore
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				btnFindNext.setEnabled(true);
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (txtFind.getText().length() == 0)
					btnFindNext.setEnabled(false);
			}
		});
		
		btnFindNext = new JButton("Find Next");
		btnFindNext.setEnabled(false);
		btnFindNext.setBounds(260, 7, 96, 23);
		contentPane.add(btnFindNext);
		btnFindNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startFind();
				
			}
			
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.hideAllDialogs();
			}
		});
		btnCancel.setBounds(260, 37, 96, 23);
		contentPane.add(btnCancel);
		
		JPanel panel = new JPanel();
		panel.setName("");
		panel.setBorder(new TitledBorder(null, "Direction", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(122, 40, 128, 54);
		contentPane.add(panel);
		panel.setLayout(null);
		
		radUp = new JRadioButton("Up");
		radUp.setBounds(6, 24, 50, 23);
		panel.add(radUp);
		
		radDown = new JRadioButton("Down");
		radDown.setSelected(true);
		radDown.setBounds(58, 24, 64, 23);
		panel.add(radDown);
		
		chkMatchCase = new JCheckBox("Match case");
		chkMatchCase.setBounds(6, 71, 97, 23);
		contentPane.add(chkMatchCase);
		
		ButtonGroup group = new ButtonGroup();
		group.add(radUp);
		group.add(radDown);
	}

	protected void startFind() {
		String query = txtFind.getText();
		if (mainTextArea == null || query.length() == 0) return;
		String mainText = mainTextArea.getText();
		if (mainText.length() == 0) return;
		int caretPos = mainTextArea.getCaretPosition();
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
		mainTextArea.setSelectionStart(start);
		mainTextArea.setSelectionEnd(end);
	}
}
