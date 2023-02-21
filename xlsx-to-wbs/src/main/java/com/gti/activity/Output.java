package com.gti.activity;

public class Output {

	private String name;
	// co ak nie vsetky scenare/projekty budu mat boolean stav?
	private boolean value;

	public String getName() {
		return name;
	}

	public boolean getValue() {
		return value;
	}

	public String getValueAsText() {
		return value ? "Áno" : "Nie";
	}

	public boolean isEmpty() {
		return name == null;
	}

}
