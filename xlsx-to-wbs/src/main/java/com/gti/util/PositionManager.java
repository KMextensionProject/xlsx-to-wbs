package com.gti.util;

/**
 * Manages the hierarchy position numbers for Activity, Phase, Subactivity and
 * Task objects. When the higher level node position number is incremented all the
 * underlying nodes are reset to its default positions.
 *
 * @author mkrajcovicux
 */
public class PositionManager {

	private PositionHolder activityPositioner;
	private PositionHolder phasePositioner;
	private PositionHolder subactivityPositioner;
	private PositionHolder taskPositioner;

	public PositionManager(int startingPositionNumber) {
		this.activityPositioner = new PositionHolder(startingPositionNumber);
		this.phasePositioner = new PositionHolder(startingPositionNumber);
		this.subactivityPositioner = new PositionHolder(startingPositionNumber);
		this.taskPositioner = new PositionHolder(startingPositionNumber);
	}
	
	public String incrementAndGetActivityPosition() {
		incrementActivityPosition();
		return getActivityPosition();
	}

	public String incrementAndGetPhasePosition() {
		incrementPhasePosition();
		return getPhasePosition();
	}

	public String incrementAndGetSubactivityPosition() {
		incrementSubactivityPosition();
		return getSubactivityPosition();
	}

	public String incrementAndGetTaskPosition() {
		incrementTaskPosition();
		return getTaskPosition();
	}

	public String getActivityPosition() {
		return this.activityPositioner.getPosition() + "";
	}

	public String getPhasePosition() {
		return getActivityPosition() + "." + phasePositioner.getPosition();
	}

	public String getSubactivityPosition() {
		return getPhasePosition() + "." + subactivityPositioner.getPosition();
	}

	public String getTaskPosition() {
		return getSubactivityPosition() + "." + taskPositioner.getPosition();
	}

	public void incrementActivityPosition() {
		activityPositioner.increment();
		phasePositioner.reset();
		subactivityPositioner.reset();
		taskPositioner.reset();
	}

	public void incrementPhasePosition() {
		phasePositioner.increment();
		subactivityPositioner.reset();
		taskPositioner.reset();
	}

	public void incrementSubactivityPosition() {
		subactivityPositioner.increment();
		taskPositioner.reset();
	}

	public void incrementTaskPosition() {
		taskPositioner.increment();
	}

	public static class PositionHolder {
		
		private int resetPosition;
		private int position;

		public PositionHolder(int startingPositionNumber) {
			this.position = startingPositionNumber;
			this.resetPosition = 0;
		}

		public void increment() {
			position++;
		}

		public void reset() {
			this.position = this.resetPosition;
		}

		int getPosition() {
			return this.position;
		}
	}
}
