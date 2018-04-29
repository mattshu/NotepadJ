package notepadj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FindDialog extends JFrame {

	private static FindDialog instance;
	
	private Finder finder;
	private JTextArea mainTextArea;
	private JPanel contentPane;
	private JTextField txtFind;
	private JButton btnFindNext;
	private JRadioButton radUp;
	private JRadioButton radDown;
	private JCheckBox chkMatchCase;
	
	public FindDialog() {
		if (instance == null)
			instance = this;
		else return;
		mainTextArea = MainWindow.getMainTextArea();
		finder = MainWindow.getDocumentManager().getFinder();
		contentPane = new JPanel();
		txtFind = new JTextField();
		btnFindNext = new JButton("Find Next");
		radUp = new JRadioButton("Up");
		radDown = new JRadioButton("Down");
		chkMatchCase = new JCheckBox("Match case");
	}
	
	public static FindDialog getInstance() { return instance; }
		
	public void initialize() {
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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
				onOptionsChanged();
			}

			public void removeUpdate(DocumentEvent arg0) {
				if (txtFind.getText().length() == 0)
					btnFindNext.setEnabled(false);
				onOptionsChanged();
			}
		});
		txtFind.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent arg0) {
				onOptionsChanged();
			}
			public void inputMethodTextChanged(InputMethodEvent arg0) {
				onOptionsChanged();
			}
		});
		btnFindNext.setEnabled(false);
		btnFindNext.setBounds(260, 7, 96, 23);
		btnFindNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				finder.find();
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(260, 37, 96, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		radUp.setBounds(6, 24, 50, 23);
		radUp.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				onOptionsChanged();
			}
		});
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

	private void onOptionsChanged() {
		setFindOptions();
	}
	
	private void setFindOptions() {
		FindOptions options = new FindOptions();
		options.setQuery(txtFind.getText());
		options.setDirection(radUp.isSelected() ? FindOptions.UP : FindOptions.DOWN);
		options.setCaretPosition(mainTextArea.getCaretPosition());
		options.setMatchCase(chkMatchCase.isSelected());
		finder.setOptions(options);
	}
	
}
