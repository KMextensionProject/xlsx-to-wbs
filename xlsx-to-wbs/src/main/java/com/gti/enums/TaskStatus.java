package com.gti.enums;

import static com.gti.util.StringUtils.EMPTY_STRING;

public enum TaskStatus {

	COMPLETED("Dokončené", "[#lightgreen]"), 
	ACCORDING_TO_PLAN("Podľa plánu", "[#SkyBlue]"), 
	FUTURE_TASK("Budúca úloha"), 
	DELAYED("Oneskorené", "[#orange]"), 
	CANCELED("Zrušené", "[#salmon]"),
	UNDEFINED(EMPTY_STRING);

	private String value;
	private String colorCode;

	private TaskStatus(String status) {
		this(status, EMPTY_STRING);
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
		case "Budúca úloha":
			return TaskStatus.FUTURE_TASK;
		default:
			return TaskStatus.UNDEFINED;
		}
	}
}
