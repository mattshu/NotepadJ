package notepadj;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class GotoDialog extends JDialog {

	private JPanel contentPanel;
	private JTextArea mainTextArea;
	private JTextField txtLineNumber;
	private JButton btnGoTo;

	public GotoDialog() {
		contentPanel = new JPanel();
		txtLineNumber = new JTextField();
		btnGoTo = new JButton("Go To");
	}
	
	protected void initialize(MainWindow mainWindow) {
		mainTextArea = MainWindow.mainTextArea;
		setTitle("Go To Line");
		setBounds(300, 300, 235, 162);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getRootPane().setDefaultButton(btnGoTo);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		JLabel lblLineNumber = new JLabel("Line number:");
		lblLineNumber.setBounds(12, 12, 82, 16);
		txtLineNumber.setBounds(12, 40, 195, 20);
		txtLineNumber.setColumns(10);
		btnGoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToLine(txtLineNumber.getText());
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPane.add(btnGoTo);
		buttonPane.add(btnCancel);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		contentPanel.add(lblLineNumber);
		contentPanel.add(txtLineNumber);
	}

	protected void goToLine(String line) {
		gotoLine(Integer.parseInt(line));
		txtLineNumber.setText("");
	}
	protected void gotoLine(int line) {
		if (line <= 0) return;
		int getLineCount = mainTextArea.getLineCount();
		if (line > getLineCount) {
			JOptionPane.showMessageDialog(null, "The line number is beyond the total number of lines", "NotepadJ - Goto Line", JOptionPane.OK_OPTION);
			return;
		}
		mainTextArea.setCaretPosition(mainTextArea.getDocument().getDefaultRootElement().getElement(line - 1).getStartOffset());
		mainTextArea.requestFocus();
		// TODO --
	}
}
