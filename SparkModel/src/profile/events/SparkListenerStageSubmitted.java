package profile.events;

import profile.metrics.Properties;
import profile.metrics.Stage_Info;

public class SparkListenerStageSubmitted implements SparkEvent{
	private String Event;
	private Stage_Info Stage_Info;
	private Properties Properties;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Stage_Info getStage_Info() {
		return Stage_Info;
	}
	public void setStage_Info(Stage_Info stage_Info) {
		Stage_Info = stage_Info;
	}
	public Properties getProperties() {
		return Properties;
	}
	public void setProperties(Properties properties) {
		Properties = properties;
	}
}
