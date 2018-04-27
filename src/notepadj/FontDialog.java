package notepadj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FontDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	protected JTextField txtFont;
	protected JTextField txtFontStyle;
	protected JTextField txtFontSize;
	protected JList<String> lstFonts;
	protected JList<String> lstFontStyles;
	protected JList<Integer> lstFontSizes;
	protected JLabel lblSample;
	protected JComboBox<String> cbxFontScript;

	public FontDialog() {
		txtFont = new JTextField();
		txtFontStyle = new JTextField();
		txtFontSize = new JTextField();
		lblSample = new JLabel("AaBbYyZz");
		cbxFontScript = new JComboBox<String>();
		lstFonts = new JList<String>();
		lstFontStyles = new JList<String>();
		lstFontSizes = new JList<Integer>();
	}
	public void initialize() {
		setTitle("Font");
		setBounds(100, 100, 431, 454);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		JLabel lblFont = new JLabel("Font:");
		lblFont.setBounds(10, 11, 46, 14);
		JLabel lblFontStyle = new JLabel("Font style:");
		lblFontStyle.setBounds(190, 10, 69, 16);
		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(325, 10, 33, 16);
		txtFont.setBounds(10, 37, 144, 20);
		txtFontStyle.setBounds(186, 37, 114, 20);
		txtFontSize.setBounds(325, 37, 54, 20);
		txtFont.setColumns(10);
		txtFontStyle.setColumns(10);
		txtFontSize.setColumns(10);
		lblSample.setHorizontalAlignment(SwingConstants.CENTER);
		lblSample.setFont(new Font("Dialog", Font.BOLD, 12));
		JPanel panelSample = new JPanel();
		panelSample.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Sample", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panelSample.setBounds(186, 163, 193, 85);
		panelSample.add(lblSample);
		JLabel lblScript = new JLabel("Script:");
		lblScript.setBounds(190, 260, 46, 16);
		cbxFontScript.setBounds(186, 288, 193, 20);
		JScrollPane scrollFonts = new JScrollPane();
		scrollFonts.setBounds(10, 58, 144, 291);
		scrollFonts.setViewportView(lstFonts);
		JScrollPane scrollFontStyles = new JScrollPane();
		scrollFontStyles.setBounds(186, 58, 114, 93);
		scrollFontStyles.setViewportView(lstFontStyles);
		JScrollPane scrollFontSizes = new JScrollPane();
		scrollFontSizes.setBounds(325, 58, 54, 93);
		scrollFontSizes.setViewportView(lstFontSizes);
		lstFonts.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) return;
				onFontChanged();
			}
		});
		lstFontStyles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) return;
				onFontStyleChanged();
			}
		});
		lstFontSizes.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (arg0.getValueIsAdjusting()) return;
				onFontSizeChanged();
			}
		});
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(190, 377, 98, 26);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMainFont();
				dispose();
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(300, 377, 98, 26);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(lblFont);
		contentPanel.add(lblFontStyle);
		contentPanel.add(lblSize);
		contentPanel.add(txtFont);
		contentPanel.add(txtFontStyle);
		contentPanel.add(txtFontSize);
		contentPanel.add(panelSample);
		contentPanel.add(lblScript);
		contentPanel.add(cbxFontScript);
		contentPanel.add(scrollFonts);
		contentPanel.add(scrollFontStyles);
		contentPanel.add(scrollFontSizes);
		contentPanel.add(btnOk);
		contentPanel.add(btnCancel);
		

		




		
		loadFontLists();
		getMainFont();
		setSampleTextFont();
		setVisible(true);
	}

	private void onFontChanged() {
		txtFont.setText(lstFonts.getSelectedValue());
		setSampleTextFont();
	}
	
	protected void onFontStyleChanged() {
		txtFontStyle.setText(lstFontStyles.getSelectedValue());
		setSampleTextFont();
	}
	
	protected void onFontSizeChanged() {
		txtFontSize.setText(String.valueOf(lstFontSizes.getSelectedValue()));
		setSampleTextFont();
	}
	
	private void setSampleTextFont() {
		Font font = buildFont();
		lblSample.setFont(font);
	}
	
	private Font buildFont() {
		String fontName = txtFont.getText();
		int fontStyle = getFontStyleFromText();
		int fontSize = getFontSizeFromText();
		Font font = new Font(fontName, fontStyle, fontSize);
		return font;
	}
	
	private int getFontSizeFromText() {
		try {
			return Integer.parseInt(txtFontSize.getText());
			
		} catch (NumberFormatException ex) {
			return DocumentManager.DEFAULT_FONT_SIZE;
		}
	}
	
	private void setMainFont() {
		try {
			Font selectedFont = new Font(txtFont.getText(), getFontStyleFromText(), getFontSizeFromText());
			MainWindow.mainTextArea.setFont(selectedFont);
			
		} catch (Exception ex) {
			System.out.println("Ignoring invalid font");
		}
	}
	
	private int getFontStyleFromText() {
		String fontStyleText = txtFontStyle.getText();
		int fontStyle = 0; // Regular
		if (fontStyleText.equals("Bold"))
			fontStyle = 1;
		else if (fontStyleText.equals("Italic"))
			fontStyle = 2;
		else if (fontStyleText.equals("Bold Italic"))
			fontStyle = 1 | 2;
		return fontStyle;
	}
	
	private String getFontStyleToString(int fontStyle) {
		String fontStyleText = "Regular";
		if (fontStyle == 1)
			fontStyleText = "Bold";
		else if (fontStyle == 2)
			fontStyleText = "Italic";
		else if (fontStyle == (1 | 2))
			fontStyleText = "Bold Italic";
		return fontStyleText;
	}
	
	private void getMainFont() {
		Font mainFont = MainWindow.mainTextArea.getFont();
		String fontName = mainFont.getFontName();
		ArrayList<String> fontList = new ArrayList<String>();
		ListModel<String> model = lstFonts.getModel();
		for (int i = 0; i < model.getSize(); i++) 
			fontList.add(String.valueOf(model.getElementAt(i)));
		int indexOfFontName = fontList.indexOf(fontName);
		lstFonts.ensureIndexIsVisible(indexOfFontName);
		txtFont.setText(fontName);
		
		int fontStyle = mainFont.getStyle();
		String fontStyleText = getFontStyleToString(fontStyle);
		txtFontStyle.setText(fontStyleText);
		
		int fontSize = mainFont.getSize();
		txtFontSize.setText(String.valueOf(fontSize));
		lstFontSizes.ensureIndexIsVisible(fontSize - 5);
	}
	
	private void loadFontLists() {
		DefaultListModel<String> dlmFonts = new DefaultListModel<String>();
		DefaultListModel<String> dlmFontStyles = new DefaultListModel<String>();
		DefaultListModel<Integer> dlmFontSizes = new DefaultListModel<Integer>();
		GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (Font font : graphicsEnv.getAllFonts()) {
			String fontName = font.getFontName();
			dlmFonts.addElement(fontName);
		}
		dlmFontStyles.addElement("Regular");
		dlmFontStyles.addElement("Bold");
		dlmFontStyles.addElement("Italic");
		dlmFontStyles.addElement("Bold Italic");
		for (int i = 5; i <= 72; i++)
			dlmFontSizes.addElement(Integer.valueOf(i));
		lstFonts.setModel(dlmFonts);
		lstFonts.setCellRenderer(new FontListCellRenderer());
		lstFontStyles.setModel(dlmFontStyles);
		lstFontStyles.setCellRenderer(new FontStyleCellRenderer());
		lstFontSizes.setModel(dlmFontSizes);
	}
	
	
	private class FontListCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			label.setFont(new Font(label.getText(), Font.PLAIN, 14));
			return label;
		}
	} 
	
	private class FontStyleCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			String fontName = txtFont.getText();
			if (fontName == null) fontName = "Dialog.Plain";
			switch (label.getText()) {
				case "Bold":
					label.setFont(new Font(fontName, Font.BOLD, 14));
					break;
				case "Italic":
					label.setFont(new Font(fontName, Font.ITALIC, 14));
					break;
				case "Bold Italic":
					label.setFont(new Font(fontName, Font.BOLD | Font.ITALIC, 14));
					break;
				default:
					label.setFont(new Font(fontName, Font.PLAIN, 14));
					break;
			}
			return label;
		}
	}
}
