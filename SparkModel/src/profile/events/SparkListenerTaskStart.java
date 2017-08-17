package profile.events;

import profile.metrics.Task_Info;

public class SparkListenerTaskStart implements SparkEvent{
	private String Event;
	private Integer Stage_ID;
	private Task_Info Task_Info;
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
	public Task_Info getTask_Info() {
		return Task_Info;
	}
	public void setTask_Info(Task_Info task_Info) {
		Task_Info = task_Info;
	}
}
