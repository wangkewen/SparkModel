package profile.events;

import java.util.List;

import profile.metrics.Properties;

public class SparkListenerJobStart implements SparkEvent{
	private String Event;
	private Integer Job_ID;
	private List<Integer> Stage_IDs;
	private Properties Properties;
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
	public List<Integer> getStage_IDs() {
		return Stage_IDs;
	}
	public void setStage_IDs(List<Integer> stage_IDs) {
		Stage_IDs = stage_IDs;
	}
	public Properties getProperties() {
		return Properties;
	}
	public void setProperties(Properties properties) {
		Properties = properties;
	}
}
