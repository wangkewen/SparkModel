package profile.events;

public class SparkListenerApplicationEnd implements SparkEvent{
	private String Event;
	private Long Timestamp;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Long getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(Long timestamp) {
		Timestamp = timestamp;
	}
}
