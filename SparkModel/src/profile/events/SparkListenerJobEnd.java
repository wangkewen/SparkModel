package profile.events;

import profile.metrics.Job_Result;

public class SparkListenerJobEnd implements SparkEvent{
	private String Event;
	private Integer Job_ID;
	private Job_Result Job_Result;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Integer getJob_ID() {
		return Job_ID;
	}
	public void setJob_ID(Integer job_ID) {
		Job_ID = job_ID;
	}
	public Job_Result getJob_Result() {
		return Job_Result;
	}
	public void setJob_Result(Job_Result job_Result) {
		Job_Result = job_Result;
	}
}
