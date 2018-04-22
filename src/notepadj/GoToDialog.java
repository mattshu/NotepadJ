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

public class GoToDialog extends JDialog {

	private MainWindow mainWindow;
	
	private final JPanel contentPanel = new JPanel();
	private JTextArea mainTextArea;
	private JTextField txtLineNumber;
	private JButton btnGoTo;

	/**
	 * Create the dialog.
	 */
	public GoToDialog(MainWindow mnWnd) {
		mainWindow = mnWnd;
		mainTextArea = mainWindow.mainTextArea;
		setTitle("Go To Line");
		setBounds(300, 300, 235, 162);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		contentPanel.setLayout(null);
		
		JLabel lblLineNumber = new JLabel("Line number:");
		lblLineNumber.setBounds(12, 12, 82, 16);
		contentPanel.add(lblLineNumber);
		
		txtLineNumber = new JTextField();
		txtLineNumber.setBounds(12, 40, 195, 20);
		contentPanel.add(txtLineNumber);
		txtLineNumber.setColumns(10);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		btnGoTo = new JButton("Go To");
		btnGoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToLine(txtLineNumber.getText());
			}
		});
		buttonPane.add(btnGoTo);
		getRootPane().setDefaultButton(btnGoTo);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainWindow.hideAllDialogs();
			}
		});
		buttonPane.add(btnCancel);
	}

	protected void goToLine(String line) {
		goToLine(Integer.parseInt(line));
		txtLineNumber.setText("");
	}
	protected void goToLine(int line) {
		if (line <= 0) return;
		int getLineCount = mainTextArea.getLineCount();
		if (line > getLineCount) {
			JOptionPane.showMessageDialog(null, "The line number is beyond the total number of lines", "NotepadJ - Goto Line", JOptionPane.OK_OPTION);
			return;
		}
		mainTextArea.setCaretPosition(mainTextArea.getDocument().getDefaultRootElement().getElement(line - 1).getStartOffset());
		mainTextArea.requestFocus();
		mainWindow.hideAllDialogs();
	}
}
