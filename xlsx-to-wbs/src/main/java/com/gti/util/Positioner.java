package com.gti.util;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Positioner {

	private final List<String> pos;

	public Positioner() {
		this.pos = new ArrayList<>(Arrays.asList(""));
	}

	public String assignLevelNumber(int depth) {
		if (depth < pos.size()) {
			pos.add(depth, (Integer.parseInt((pos.remove(depth))) + 1) + "");
			for (int i = depth + 1; i < pos.size(); i++) {
				pos.remove(i);
			}
		} else {
			pos.add(1 + "");
		}

		for (int i = depth + 1; i < pos.size(); i++) {
			pos.remove(i);
		}

		return pos.stream().skip(1).collect(joining(".")) + " ";
	}

}
