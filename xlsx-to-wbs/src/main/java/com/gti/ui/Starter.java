package com.gti.ui;

import static com.gti.ui.CustomComponentCreator.createBasicComboBox;
import static com.gti.ui.CustomComponentCreator.createButton;
import static com.gti.ui.CustomComponentCreator.createCheckBox;
import static com.gti.ui.CustomComponentCreator.createEmptyLabel;
import static com.gti.ui.CustomComponentCreator.createFileDialog;
import static com.gti.ui.CustomComponentCreator.createFrame;
import static com.gti.ui.CustomComponentCreator.createNumericTextField;
import static com.gti.ui.CustomComponentCreator.createPanel;
import static com.gti.ui.CustomComponentCreator.createTextField;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.gti.util.FileUtils;
import com.gti.wbs.NodeStyle;
import com.gti.wbs.NodeStyle.StyleBuilder;
import com.gti.wbs.Wbs;
import com.gti.xlsx.ActivityLoader;
import com.gti.xlsx.ColumnProperty;
import com.gti.xlsx.XlsxMetadata;

import net.sourceforge.plantuml.FileFormat;

public class Starter {

	private JFrame frame;
	private JPanel panel;
	private JTextField xlsxLocationField;
	private JTextField xlsxDataSheetPosition;
	private JTextField xlsxParentColumnsField;
	private JTextField xlsxPropertyColumnsField;
	private JTextField xlsxTitleRowPosition;
	private JTextField topElementNameField;
	private JTextField nodeTextPixelWidthField;
	private JLabel dataSheetPositionLabel;
	private JLabel titleRowLabel;
	private JLabel columnHierarchyLabel;
	private JLabel columnPropertiesLabel;
	private JLabel topElementNameLabel;
	private JLabel nodeThemeLabel;
	private JLabel nodeTextPixelWidthLabel;
	private JLabel outputFileTypeLabel;
	private JLabel emptyLabel;
	private JCheckBox stateColorChecker;
	private JCheckBox boxedChecker;
	private JComboBox<FileFormat> outputFileTypeCombo;
	private JComboBox<String> nodeThemeCombo;
	private FileDialog fileDialog;
	private FileDialog saveDialog;
	private JButton searchButton;
	private JButton startButton;
	private JSeparator wbsSectionSeparator;
	private ActivityLoader activityLoader;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Starter::new);
	}

	public Starter() {
		activityLoader = new ActivityLoader();
		createUI();
	}

	private void createUI() {
		frame = createFrame(485, 420);
		panel = createPanel(485, 420);
		addFrameMenuBar();

		xlsxLocationField = createTextField(380, 25);
		panel.add(xlsxLocationField);

		fileDialog = createFileDialog(frame, "Výber xlsx", FileDialog.LOAD);
		searchButton = createButton("Hľadať", a -> setTextFieldByFileDialogResult(xlsxLocationField, fileDialog));
		panel.add(searchButton);

		dataSheetPositionLabel = new JLabel("Číslo zošitu v xlsx:");
		xlsxDataSheetPosition = createNumericTextField(60, 25, "1 (prvý zošit)", 1);
		panel.add(dataSheetPositionLabel);
		panel.add(xlsxDataSheetPosition);

		titleRowLabel = new JLabel("Číslo titulného riadku v xlsx:");
		xlsxTitleRowPosition = createNumericTextField(60, 25, "1 (prvý riadok)", 1);		
		panel.add(titleRowLabel);
		panel.add(xlsxTitleRowPosition);

		columnHierarchyLabel = new JLabel("Hierarchické označenie stĺpcov:");
		columnHierarchyLabel.setPreferredSize(new Dimension(190, 20));
		xlsxParentColumnsField = createTextField(225, 25, "Príklad: C, A, D, E");
		panel.add(columnHierarchyLabel);
		panel.add(xlsxParentColumnsField);

		columnPropertiesLabel = new JLabel("Vlastnosti posledného stĺpca:");
		columnPropertiesLabel.setPreferredSize(new Dimension(190, 20));
		xlsxPropertyColumnsField = createTextField(225, 25, "Príklad: G, H, I, M, P, N");
		panel.add(columnPropertiesLabel);
		panel.add(xlsxPropertyColumnsField);

		wbsSectionSeparator = new JSeparator(SwingConstants.HORIZONTAL);
		wbsSectionSeparator.setAlignmentX(SwingConstants.CENTER);
		wbsSectionSeparator.setPreferredSize(new Dimension(465, 4));
		panel.add(wbsSectionSeparator);

		topElementNameLabel = new JLabel("Názov hlavného (vrchného) prvku WBS:");
		topElementNameField = createTextField(180, 25, "Zvyčajne sa udáva názov projektu");
		panel.add(topElementNameLabel);
		panel.add(topElementNameField);

		nodeTextPixelWidthLabel = new JLabel("Šírka WBS prvku: ");
		nodeTextPixelWidthField = createNumericTextField(60, 25, "Udáva sa v pixeloch. (prázdne = bez limitu/zalomenia textu)", 400);
		panel.add(nodeTextPixelWidthLabel);
		panel.add(nodeTextPixelWidthField);

		stateColorChecker = createCheckBox("Zafarbenie úlohy vo WBS podľa stavu dokončenia", true);
		boxedChecker = createCheckBox("Zobraziť textové prvky WBS bez orámovania");
		panel.add(stateColorChecker);
		panel.add(boxedChecker);
		panel.add(createEmptyLabel(100)); // until normal layout is implemented..  TODO: get rid of these

		nodeThemeLabel = new JLabel("Dizajn WBS prvku:");
		nodeThemeCombo = createBasicComboBox("NT", 232, 20, "Štandard, bez farby", "Žlté pozadie, červený rám");
		panel.add(nodeThemeLabel);
		panel.add(nodeThemeCombo);

		outputFileTypeLabel = new JLabel("Formát výstupného súboru pre WBS:");
		outputFileTypeCombo = createBasicComboBox("outFT", 100, 20, FileFormat.SVG, FileFormat.EPS);
		panel.add(outputFileTypeLabel);
		panel.add(outputFileTypeCombo);

		emptyLabel = new JLabel("");
		emptyLabel.setPreferredSize(new Dimension(450, 20));
		panel.add(emptyLabel);

		saveDialog = createFileDialog(frame, "Uloženie WBS", FileDialog.SAVE);
		startButton = createButton("Vytvoriť WBS", a -> generateAndSaveWbs());
		startButton.setPreferredSize(new Dimension(465, 28));
		panel.add(startButton);

		frame.add(panel);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private void addFrameMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Možnosti");
		JMenuItem show = new JMenuItem("Zobraziť manuál");
		JMenuItem save = new JMenuItem("Uložiť manuál");
		show.addActionListener(e -> displayManual());
		menu.add(show);
		save.addActionListener(e -> saveManual());
		menu.add(save);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	}

	// TODO: refactor
	private void displayManual() {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.OPEN)) {
			try {
				Desktop.getDesktop().open(new File(getClass().getClassLoader().getResource("/Manual.pdf").toURI()));
			} catch (Exception ioe) {
				showErrorDialog("Nie je možné zobraziť manuál, použi možnosť uloženia.");
			}
		} else {
			showErrorDialog("Nie je možné zobraziť manuál, použi možnosť uloženia.");
		}
	}

	private void saveManual() {
		String outputPath = validateAndGetOutputFile("Manual", FileFormat.PDF);
		if (outputPath.isEmpty()) {
			return;
		}
		try {
			Path target = Paths.get(outputPath);
			Files.copy(getClass().getClassLoader().getResourceAsStream("Manual.pdf"), target);
			showInfoDialog("Uloženie", "Manuál bol úspešne uložený.");
		} catch (Exception ioex) {
			showErrorDialog("Nie je možné uložiť manuál, použi možnosť zobrazenia.");
		}
	}

	private void setTextFieldByFileDialogResult(JTextField textField, FileDialog fileDialog) {
		fileDialog.setVisible(true);
		File[] files = fileDialog.getFiles();
		if (files.length != 0) {
			if (files[0].getAbsolutePath().endsWith("xlsx")) {
				textField.setText(files[0].getAbsolutePath());
			} else {
				showErrorDialog("Súbor musí byť typu xlsx");
			}
		}
	}

	public void generateAndSaveWbs() {
		if (missingMandatoryFields() || wronglyChecked() || badHierarchyDefinitionSyntax()) {
			return;
		}
		String outFile = validateAndGetOutputFile("WBS", (FileFormat) outputFileTypeCombo.getSelectedItem());
		if (outFile.isEmpty()) {
			// user didn't select any output file, quietly get out
			return;
		}
		try {
			Wbs wbs = createWbs(activityLoader.loadFromXlsx(readXlsxMetadata())); 
			wbs.save(outFile, (FileFormat) outputFileTypeCombo.getSelectedItem());
			showInfoDialog("Generovanie dokončené", "WBS vytvorené: " + outFile);
		} catch (Exception ioex) {
			ioex.printStackTrace();
			showErrorDialog(ioex.getMessage());
		}
	}

	private XlsxMetadata readXlsxMetadata() {
		XlsxMetadata xlsxMeta = new XlsxMetadata();
		xlsxMeta.setDataSheetIndex(parsePositionOrElse(xlsxDataSheetPosition.getText(), 0));
		xlsxMeta.setTitleRowIndex(parsePositionOrElse(xlsxTitleRowPosition.getText(), 0));
		xlsxMeta.setFile(new File(xlsxLocationField.getText()));
		if (!xlsxMeta.getFile().isFile()) {
			throw new IllegalArgumentException("Vybratý súbor neexistuje");
		}
		xlsxMeta.setParentColumnsProperties(parseXlsxColumns(xlsxParentColumnsField));
		xlsxMeta.setPropertyColumnsProperties(parseXlsxColumns(xlsxPropertyColumnsField));
		return xlsxMeta;
	}

	private static List<ColumnProperty> parseXlsxColumns(JTextField field) {
		String[] codes = field.getText().split(",");
		List<ColumnProperty> codesList = new ArrayList<>();
		for (String code : codes) {
			code = code.trim();
			if (!code.isEmpty()) {
				// constructor will process and split percentage mark
				ColumnProperty property = new ColumnProperty(code);
				code = property.getLocationCode();
				if (code.length() > 1) {
					throw new IllegalArgumentException("Stĺpec " + code + " je mimo povoleného rozsahu: [A-Z]");
				}
				codesList.add(property);
			}
		}
		return codesList;
	}

	private NodeStyle readNodeStyle() {
		StyleBuilder style = new NodeStyle.StyleBuilder();
		String width = nodeTextPixelWidthField.getText();
		if (!width.isEmpty()) {
			style.withMaximumLineWidth(Integer.parseInt(width));
		}
		int theme = nodeThemeCombo.getSelectedIndex();
		if (theme == 1) {
			style.withBackgroundColor("lightYellow").withLineColor("crimson");
		}
		return style.withHorizontalAlignment("center").createStyle();
	}

	private int parsePositionOrElse(String text, int orElse) {
		if (!text.isEmpty()) {
			try {
				return Integer.parseInt(text) - 1; // user is never indexing from 0
			} catch (NumberFormatException nfex) {
				return orElse;
			}
		}
		return orElse;
	}

	private boolean missingMandatoryFields() {
		StringBuilder errorMessage = new StringBuilder();
		if (xlsxLocationField.getText().isEmpty()) {
			errorMessage.append("xlsx súbor");
		}
		if (topElementNameField.getText().isEmpty()) {
			if (errorMessage.length() != 0) {
				errorMessage.append(", ");
			}
			errorMessage.append("názov hlavného prvku");
		}
		if (columnHierarchyLabel.getText().isEmpty()) {
			if (errorMessage.length() != 0) {
				errorMessage.append(", ");
			}
			errorMessage.append("hierarchia stĺpcov");
		}
		if (errorMessage.length() != 0) {
			errorMessage.insert(0, "Nie sú vyplnené povinné polia: [");
			errorMessage.append("]");
			showErrorDialog(errorMessage.toString());
			return true;
		}
		return false;
	}

	private boolean wronglyChecked() {
		if (stateColorChecker.isSelected() && boxedChecker.isSelected()) {
			showErrorDialog("Nie je možné označiť obe zaškrtávacie políčka");
			return true;
		}
		return false;
	}

	private boolean badHierarchyDefinitionSyntax() {
		String parentCols = xlsxParentColumnsField.getText().trim(); 
		String propertyCols = xlsxPropertyColumnsField.getText().trim();

		if (parentCols.isEmpty()) {
			showErrorDialog("Minimálne jeden stĺpec v hierarchii musí byť definovaný");
			return true;
		}
		if (!isParsable(parentCols) || (!propertyCols.isEmpty() && !isParsable(propertyCols))) {
			showErrorDialog("Stĺpce musia byť v rozsahu [A-Z] a oddelené čiarkou");
			return true;
		}
		return false;
	}

	private boolean isParsable(String toBeParsed) {
		if (toBeParsed.length() == 1 && Character.isLetter(toBeParsed.charAt(0))) {
			return true;
		}
		boolean first;
		boolean second;
		toBeParsed = toBeParsed.replace(" ", "").replace("%", ""); // [//s//%]*$
		for (int i = 0, j = 1; j < toBeParsed.length(); i++, j++) {
			first = Character.isLetter(toBeParsed.charAt(i));
			second = Character.isLetter(toBeParsed.charAt(j));
			// ak su oba znaky alebo ziaden
			if (first == second) {
				return false;
			}
		}
		return true;
	}

	private Wbs createWbs(Map<String, Object> activities) {
		return new Wbs.WbsBuilder()
			.withStatusBasedTaskColoring(stateColorChecker.isSelected())
			.makeBoxed(!boxedChecker.isSelected())
			.withTopLevelNodeName(topElementNameField.getText())
			.withNodeStyle(readNodeStyle())
			.buildWbs(activities);
	}

	private String validateAndGetOutputFile(String defaultName, FileFormat extension) {
		// open "save file" dialog
		saveDialog.setVisible(true);
		if (saveDialog.getFiles().length == 0) {
			return "";
		}
		String outFileName = saveDialog.getFiles()[0].getAbsolutePath();
		if (saveDialog.getFiles()[0].isDirectory()) {
			return FileUtils.appendFileNameWithExtension(outFileName, defaultName, extension);
		} else {
			return FileUtils.overrideFileExtensionIfDifferent(outFileName, extension);
		}
	}

	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(frame, message, "Chyba", JOptionPane.ERROR_MESSAGE);
	}

	private void showInfoDialog(String title, String message) {
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
