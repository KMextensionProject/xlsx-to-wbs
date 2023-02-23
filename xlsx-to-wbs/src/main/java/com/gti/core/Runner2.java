package com.gti.core;

import java.io.IOException;
import java.util.List;

import com.gti.wbs.Activity;
import com.gti.wbs.Wbs;
import com.gti.xlsx.XlsxMetadata;

import net.sourceforge.plantuml.FileFormat;

public class Runner2 {
// TODO: take color from excel based on each value.. let the user decide whether to use it or not --colorless
// TODO: when pulling something out from the hierarchy... we need not to save such values that are not present in excel -> check the mapper
// TODO: implement style options + hide configuration construction? -> let user know of sheet where to define column mapping
	public static void main(String[] args) throws IOException {

		XlsxMetadata xlsxConfig = new XlsxMetadata();
		xlsxConfig.setTitleRowIndex(6);
		List<Activity> activities = ActivityLoader.loadFromXlsx("sample.xlsx", xlsxConfig);

		Wbs wbs = new Wbs.WbsBuilder()
			.makeColorless(false)
			.withMaxLineWidth(400)
			.withTopLevelNodeName("MUSP")
			.buildWbs(activities);

//		System.out.println(wbs.getConfigurationString());

		System.out.println(wbs.save("sample.svg", FileFormat.SVG));

	}

}
