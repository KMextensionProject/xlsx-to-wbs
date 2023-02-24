package com.gti.core;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import com.gti.wbs.Activity;
import com.gti.wbs.NodeStyle;
import com.gti.wbs.Wbs;
import com.gti.xlsx.XlsxMetadata;

import net.sourceforge.plantuml.FileFormat;

public class Runner2 {

	public static void main(String[] args) throws IOException {

		XlsxMetadata xlsxConfig = new XlsxMetadata();
		xlsxConfig.setTitleRowIndex(6);

		List<Activity> activities = ActivityLoader.loadFromXlsx("sample.xlsx", xlsxConfig);
		activities.sort(Comparator.comparing(Activity::getDescription));

		NodeStyle style = new NodeStyle.StyleBuilder()
			.withHorizontalAlignment("center")
			.withMaximumLineWidth(400)
			.withBackgroundColor("lightYellow")
			.withLineColor("crimson")
			.createStyle();

		Wbs wbs = new Wbs.WbsBuilder()
			.withNodeStyle(style)
			.withTopLevelNodeName("MUSP")
			.withStatusBasedTaskColoring(true)
			.buildWbs(activities);

		System.out.println(wbs.getConfigurationString());
		System.out.println(wbs.save("sample.svg", FileFormat.SVG));

	}

}
