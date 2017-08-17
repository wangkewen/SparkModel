package profile.events;

import profile.metrics.Task_End_Reason;
import profile.metrics.Task_Info;
import profile.metrics.Task_Metrics;

public class SparkListenerTaskEnd implements SparkEvent{
	private String Event;
	private Integer Stage_ID;
	private String Task_Type;
	private Task_Info Task_Info;
	private Task_End_Reason Task_End_Reason;
	private Task_Metrics Task_Metrics;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Integer getStage_ID() {
		return Stage_ID;
	}
	public void setStage_ID(Integer stage_ID) {
		Stage_ID = stage_ID;
	}
	public String getTask_Type() {
		return Task_Type;
	}
	public void setTask_Type(String task_Type) {
		Task_Type = task_Type;
	}
	public Task_Info getTask_Info() {
		return Task_Info;
	}
	public void setTask_Info(Task_Info task_Info) {
		Task_Info = task_Info;
	}
	public Task_End_Reason getTask_End_Reason() {
		return Task_End_Reason;
	}
	public void setTask_End_Reason(Task_End_Reason task_End_Reason) {
		Task_End_Reason = task_End_Reason;
	}
	public Task_Metrics getTask_Metrics() {
		return Task_Metrics;
	}
	public void setTask_Metrics(Task_Metrics task_Metrics) {
		Task_Metrics = task_Metrics;
	}
}
