package notepadj;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AboutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		setType(Type.POPUP);
		setTitle("About - NotepadJ");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(150, 175, 219, 203);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblProgramName = new JLabel("PROGRAM_NAME");
			lblProgramName.setBounds(35, 24, 134, 14);
			contentPanel.add(lblProgramName);
		}
		{
			JLabel lblProgramVersion = new JLabel("PROGRAM_VERSION");
			lblProgramVersion.setBounds(35, 49, 134, 14);
			contentPanel.add(lblProgramVersion);
		}
		{
			JLabel lblProgramAuthor = new JLabel("PROGRAM_AUTHOR");
			lblProgramAuthor.setBounds(35, 74, 134, 14);
			contentPanel.add(lblProgramAuthor);
		}
		{
			JLabel lblProgramAuthorEmail = new JLabel("PROGRAM_AUTHOR_EMAIL");
			lblProgramAuthorEmail.setBounds(35, 99, 134, 14);
			contentPanel.add(lblProgramAuthorEmail);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeButton = new JButton("Close");
				closeButton.setSize(200, 100);
				closeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						exit();
					}
				});
				closeButton.setActionCommand("Cancel");
				buttonPane.add(closeButton);
			}
		}
	}

	protected void exit() {
		dispose();
	}

}
