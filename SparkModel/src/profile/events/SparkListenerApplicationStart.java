package profile.events;

public class SparkListenerApplicationStart implements SparkEvent{
	private String Event;
	private String App_Name;
	private Long Timestamp;
	private String User;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getApp_Name() {
		return App_Name;
	}
	public void setApp_Name(String app_Name) {
		App_Name = app_Name;
	}
	public Long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(Long timestamp) {
		Timestamp = timestamp;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
}
