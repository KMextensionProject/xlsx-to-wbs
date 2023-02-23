package com.gti.enums;

import com.gti.util.StringUtils;

public enum TaskStatus {

	COMPLETED("Dokončené", "[#lightgreen]"), 
	ACCORDING_TO_PLAN("Podľa plánu", "[#SkyBlue]"), 
	FUTURE_TASK("Budúca úloha", StringUtils.EMPTY_STRING), 
	DELAYED("Oneskorené", "[#orange]"), 
	CANCELED("Zrušené", "[#salmon]");

	private String value;
	private String colorCode;

	private TaskStatus(String value) {
		this(value, StringUtils.EMPTY_STRING);
	}

	private TaskStatus(String value, String colorCode) {
		this.value = value;
		this.colorCode = colorCode;
	}

	public String getValue() {
		return this.value;
	}

	public String getColorCode() {
		return this.colorCode;
	}

	public static TaskStatus getStatusByValue(String taskStatusValue) {
		switch (taskStatusValue) {
		case "Dokončené":
			return TaskStatus.COMPLETED;
		case "Zrušené":
			return TaskStatus.CANCELED;
		case "Oneskorené":
			return TaskStatus.DELAYED;
		case "Podľa plánu":
			return TaskStatus.ACCORDING_TO_PLAN;
		default:
			return TaskStatus.FUTURE_TASK;	
		}
	}
}
