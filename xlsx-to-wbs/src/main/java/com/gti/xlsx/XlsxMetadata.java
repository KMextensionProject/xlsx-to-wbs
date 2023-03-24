package com.gti.xlsx;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

/**
 * this holder describes excel meta data as defined by user..
 * TODO: perform validations on BL as well
 * 
 * index constraints, file location/existence, parentColumns size and so on..
 */
public class XlsxMetadata {

	private File file;
	private int dataSheetIndex;
	private int titleRowIndex;
	// TODO: list of more complex object with resolving % and sheet # if the child elements or other elements in hierarchy resides in different sheet ? (relational db)
	// these must not be empty and at least one must be present
	List<String> parentColumns; // A, B, C, E [names will not be displayed]
	// these may be empty, but they represent properties of the last column in the hierarchy
	List<String> propertyColumns; // D, F, M, AB, AS // TODO: I need to have the title names because they will be displayed

	public XlsxMetadata() {
		// zeroes are default
		parentColumns = new ArrayList<>();
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

	public void setDataSheetIndex(int dataSheetIndex) {
		this.dataSheetIndex = dataSheetIndex;
	}

	public int getDataSheetIndex() {
		return this.dataSheetIndex;
	}

	public void setTitleRowIndex(int titleRowIndex) {
		this.titleRowIndex = titleRowIndex;
	}

	public int getTitleRowIndex() {
		return this.titleRowIndex;
	}

	public void addPropertyColumn(String location) {
		this.propertyColumns.add(location);
	}

	public List<String> getPropertyColumns() {
		return new ArrayList<>(propertyColumns);
	}

	public void addParentColumn(String location) {
		this.parentColumns.add(location);
	}

	public List<String> getParentColumns() {
		return new ArrayList<>(parentColumns);
	}

	public void setParentColumns(List<String> parentCols) {
		if (parentCols != null) {
			if (this.parentColumns.isEmpty()) {
				this.parentColumns.addAll(parentCols);
			} else {
				this.parentColumns = new ArrayList<>(parentCols);
			}
		}
	}

	public void setPropertyColumns(List<String> propertyCols) {
		if (propertyCols != null) {
			if (this.propertyColumns.isEmpty()) {
				this.propertyColumns.addAll(propertyCols);
			} else {
				this.propertyColumns = new ArrayList<>(propertyCols);
			}
		}
	}
}
