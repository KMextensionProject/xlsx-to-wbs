package com.gti.model;

import static com.gti.util.StringUtils.EMPTY_STRING;

// toto musi byt rozsirene o akceptaciu mirri -> to je ten true alebo false stlpec..inak vystup ma svoje vlastne meno
public class Output extends NamedObject {

	private Boolean value;

	public Output(String outputName) {
		super(outputName);
	}

	public Output(String outputName, Boolean value) {
		this(outputName);
		this.value = value;
	}

	public Boolean getValue() {
		return value;
	}

	public String getValueAsText() {
		if (value == null) {
			return EMPTY_STRING;
		}
		return value ? "Áno" : "Nie";
	}

	public boolean isEmpty() {
		return name == null;
	}

	@Override
	public String toString() {
		return "Output [name=" + name + "value=" + value + "]";
	}

}
