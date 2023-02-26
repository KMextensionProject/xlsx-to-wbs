package com.gti.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.poi.ss.formula.eval.NotImplementedException;

public class Starter {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Starter::createUI);
	}

	private static void createUI() {
		JFrame frame = createFrame(475, 350);
		JPanel panel = createPanel(475, 350);

		JTextField xlsxLocationField = createTextField(380, 25);
		panel.add(xlsxLocationField);

		FileDialog fileChooser = createFileDialog(frame, "Výber xlsx", FileDialog.LOAD);
		JButton searchButton = createButton("Hľadať", a -> setTextFieldByFileChooser(xlsxLocationField, fileChooser));
		panel.add(searchButton);

		JLabel titleRowLabel = new JLabel("Číslo titulného riadku v xlsx:");
		JLabel dataSheetPositionLabel = new JLabel("Číslo zošitu v xlsx:");
		JTextField xlsxDataSheetPosition = createNumericTextField(60, 25, "1 (prvý zošit)", 1);		
		JTextField xlsxTitleRowPosition = createNumericTextField(60, 25, "1 (prvý riadok)", 1);
		panel.add(dataSheetPositionLabel);
		panel.add(xlsxDataSheetPosition);
		panel.add(titleRowLabel);
		panel.add(xlsxTitleRowPosition);

		JSeparator wbsSectionSeparator = new JSeparator(SwingConstants.HORIZONTAL);
		wbsSectionSeparator.setAlignmentX(SwingConstants.CENTER);
		wbsSectionSeparator.setPreferredSize(new Dimension(465, 4));
		panel.add(wbsSectionSeparator);
		
		JCheckBox stateColorChecker = createCheckBox("Zafarbenie úlohy podľa stavu dokončenia", true);
		JCheckBox boxedChecker = createCheckBox("Zobraziť text bez orámovania");

		panel.add(stateColorChecker);
		panel.add(boxedChecker);

		// add menu bar?
		frame.add(panel);
		frame.setVisible(true);
	}

	private static JFrame createFrame(int width, int height) {
		JFrame frame = new JFrame("xlsx-to-wbs");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		return frame;
	}

	private static JPanel createPanel(int width, int height) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // add layout later
//		JPanel panel = new JPanel(null);
		panel.setSize(width, height);
//		panel.setBackground(Color.LIGHT_GRAY);
		return panel;
	}

	private static JCheckBox createCheckBox(String label) {
		return createCheckBox(label, false);
	}

	private static JCheckBox createCheckBox(String label, boolean checked) {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setText(label);
		checkBox.setSelected(checked);
		checkBox.setVisible(true);
		return checkBox;
	}

	private static JTextField createTextField(int width, int height) {
		return createTextField(width, height, null);
	}

	private static JTextField createTextField (int width, int height, String hint) {
		JTextField textField = new JTextField();
		textField.setFont(new Font("Verdana", Font.PLAIN, 12));
		textField.setPreferredSize(new Dimension(width, height));
		if (hint != null) {
			textField.setToolTipText(hint);
		}
		return textField;
	}

	private static JTextField createNumericTextField(int width, int height) {
		return createNumericTextField(width, height, null, Integer.MIN_VALUE);
	}

	private static JTextField createNumericTextField(int width, int height, String hint, int defaultValue) {
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

	private static JTextField createTextField(JTextField otherTextField) {
		throw new NotImplementedException("text field creation out of other text field is not implemented yet");
	}

	private static FileDialog createFileDialog(Frame parent, String title, int dialogType) {
		FileDialog fileDialog = new FileDialog(parent, title, dialogType);
		fileDialog.setMultipleMode(false);
//		fileDialog.setLocationRelativeTo(frame);
//		fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".xlsx"));
		return fileDialog;
	}

	private static void setTextFieldByFileChooser(JTextField textField, FileDialog fileChooser) {
		fileChooser.setVisible(true);
		File[] files = fileChooser.getFiles();
		if (files.length != 0) {
			// validate file type
			textField.setText(files[0].getAbsolutePath());
		}
	}

	private static JButton createButton(String text, ActionListener action) {
		JButton button = new JButton(text);
		button.setBackground(Color.decode("#228B22")); // forest green
		button.setForeground(Color.WHITE);
		button.addActionListener(action);
		return button;
	}

}
