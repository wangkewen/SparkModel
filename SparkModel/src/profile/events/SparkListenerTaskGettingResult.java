package profile.events;

import profile.metrics.Task_Info;

public class SparkListenerTaskGettingResult implements SparkEvent{
	private String Event;
	private Task_Info Task_Info;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Task_Info getTask_Info() {
		return Task_Info;
	}
	public void setTask_Info(Task_Info task_Info) {
		Task_Info = task_Info;
	}
}
