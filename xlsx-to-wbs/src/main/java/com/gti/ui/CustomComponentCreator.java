package com.gti.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CustomComponentCreator {

	public static JFrame createFrame(int width, int height) {
		JFrame frame = new JFrame("xlsx-to-wbs");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	public static JPanel createPanel(int width, int height) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // uz jebat layout
		panel.setSize(width, height);
		return panel;
	}

	public static JCheckBox createCheckBox(String label) {
		return createCheckBox(label, false);
	}

	public static JCheckBox createCheckBox(String label, boolean checked) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setText(label);
		checkBox.setSelected(checked);
		checkBox.setVisible(true);
		return checkBox;
	}

	public static JTextField createTextField(int width, int height) {
		return createTextField(width, height, null);
	}

	public static JTextField createTextField (int width, int height, String hint) {
		JTextField textField = new JTextField();
		textField.setFont(new Font("Verdana", Font.PLAIN, 12));
		textField.setPreferredSize(new Dimension(width, height));
		if (hint != null) {
			textField.setToolTipText(hint);
		}
		return textField;
	}

	public static JTextField createNumericTextField(int width, int height) {
		return createNumericTextField(width, height, null, Integer.MIN_VALUE);
	}

	public static JTextField createNumericTextField(int width, int height, String hint, int defaultValue) {
		JTextField textField = new JTextField();
		textField.setFont(new Font("Verdana", Font.PLAIN, 12));
		textField.setPreferredSize(new Dimension(width, height));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				int keyChar = keyEvent.getKeyChar();
				if ((keyChar == KeyEvent.VK_BACK_SPACE || keyChar == KeyEvent.VK_DELETE) || (keyChar <= '9' && keyChar >= '0')) {
					textField.setEditable(true);
				} else {
					textField.setEditable(false);
				}
			}
		});
		if (Integer.MIN_VALUE != defaultValue) {
			textField.setText(defaultValue + "");
		}
		if (hint != null) {
			textField.setToolTipText(hint);
		}
		return textField;
	}

	public static FileDialog createFileDialog(Frame parent, String title, int dialogType) {
		FileDialog fileDialog = new FileDialog(parent, title, dialogType);
		fileDialog.setMultipleMode(false);
		return fileDialog;
	}

	public static JButton createButton(String text, ActionListener action) {
		JButton button = new JButton(text);
		button.setBackground(Color.decode("#228B22")); // forest green
		button.setForeground(Color.WHITE);
		button.addActionListener(action);
		return button;
	}

	@SafeVarargs
	public static <T> JComboBox<T> createBasicComboBox(String comboBoxName, int width, int height, T... options) {
		JComboBox<T> comboBox = new JComboBox<>();
		comboBox.setName(comboBoxName);
		if (width != -1 && height != -1) {
			comboBox.setPreferredSize(new Dimension(width, height));
		}
		comboBox.setModel(new DefaultComboBoxModel<>(options));
		return comboBox;
	}

	public static JLabel createEmptyLabel(int width) {
		JLabel label = new JLabel("");
		label.setPreferredSize(new Dimension(width, 20));
		return label;
	}

}
