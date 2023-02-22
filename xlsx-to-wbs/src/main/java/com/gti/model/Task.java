package com.gti.model;

import java.time.LocalDate;
import java.util.Objects;

import com.gti.enums.TaskStatus;

public class Task extends NamedObject {

	private TaskStatus status; // TODO: one of TaskStatus
	private String solver;
	private int priority; // one of TaskPriority
	private int finishedInPercent;
	private LocalDate from;
	private LocalDate to;
	private Output output; // may be empty

	public Task(String name) {
		super(name);
	}

	public String getName() {
		return this.name;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getSolver() {
		return solver;
	}

	public void setSolver(String solver) {
		this.solver = solver;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getFinishedInPercent() {
		return finishedInPercent;
	}

	public void setFinishedInPercent(int finishedInPercent) {
		this.finishedInPercent = finishedInPercent;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public boolean isEmpty() {
		if (status == null 
				&& solver == null 
				&& from == null 
				&& to == null
				&& priority < 0 
				&& finishedInPercent < 0 
				&& output.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, status, solver, output);
	}

	@Override
	public boolean equals(Object otherTask) {
		if (otherTask == this) {
			return true;
		}
		if (otherTask instanceof Task) {
			Task other = (Task) otherTask;
			if (Objects.equals(other.name, name)
				&& Objects.equals(other.status, status)
				&& Objects.equals(other.solver, solver)
				&& Objects.equals(other.output, output)
				&& Objects.equals(other.from, from)
				&& Objects.equals(other.to, to)
				&& other.finishedInPercent == finishedInPercent
				&& other.priority == priority) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Task [name=" + name + ", "
			+ "status=" + status + ", "
			+ "solver=" + solver + ", "
			+ "priority=" + priority + ", "
			+ "finishedInPercent=" + finishedInPercent + ", "
			+ "from=" + from + ", "
			+ "to=" + to + ", "
			+ "output=" + output + "]";
	}

}
