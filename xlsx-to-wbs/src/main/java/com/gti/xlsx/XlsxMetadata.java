package com.gti.xlsx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * this holder describes excel meta data as defined by user..
 */
public class XlsxMetadata {

	private File file;
	private int dataSheetIndex;
	private int titleRowIndex;
	private List<ColumnProperty> parentColumnsProperties;
	private List<ColumnProperty> propertyColumnsProperties;

	public XlsxMetadata() {
		parentColumnsProperties = new ArrayList<>();
		propertyColumnsProperties = new ArrayList<>();
	}

	public XlsxMetadata(File file) {
		this();
		this.file = file;
	}

	public XlsxMetadata (String fileLocation) {
		this();
		this.file = new File(fileLocation);
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	/**
	 * Set sheet index where to look for data to be loaded.<br>
	 * Index positions start with 0.
	 */
	public void setDataSheetIndex(int dataSheetIndex) {
		this.dataSheetIndex = dataSheetIndex;
	}

	public int getDataSheetIndex() {
		return this.dataSheetIndex;
	}

	/**
	 * Set the title row index, to correctly name the data in columns.<br>
	 * Index positions start with 0.
	 */
	public void setTitleRowIndex(int titleRowIndex) {
		this.titleRowIndex = titleRowIndex;
	}

	public int getTitleRowIndex() {
		return this.titleRowIndex;
	}

	public List<ColumnProperty> getParentColumnProperties() {
		return new ArrayList<>(parentColumnsProperties);
	}

	public List<ColumnProperty> getPropertyColumnProperties() {
		return new ArrayList<>(propertyColumnsProperties);
	}

	/**
	 * Sets the parent column code list to define main hierarchy.
	 * This method is null safe.
	 */
	public void setParentColumnsProperties(List<ColumnProperty> parentCols) {
		if (parentColumnsProperties != null) {
			if (this.parentColumnsProperties.isEmpty()) {
				this.parentColumnsProperties.addAll(parentCols);
			} else {
				this.parentColumnsProperties = new ArrayList<>(parentCols);
			}
		}
	}

	/**
	 * Sets the properties column codes of the last parent column in main
	 * hierarchy.<br>
	 * i.e. If the main hierarchy's last element is the column code 'H', then it is
	 * considered that this 'H' element is consisting of all the columns specified
	 * within this list.
	 */
	public void setPropertyColumnsProperties(List<ColumnProperty> propertyCols) {
		if (propertyCols != null) {
			if (this.propertyColumnsProperties.isEmpty()) {
				this.propertyColumnsProperties.addAll(propertyCols);
			} else {
				this.propertyColumnsProperties = new ArrayList<>(propertyCols);
			}
		}
	}
}
