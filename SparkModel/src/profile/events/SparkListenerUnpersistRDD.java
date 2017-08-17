package profile.events;

public class SparkListenerUnpersistRDD implements SparkEvent{
	private String Event;
	private Integer RDD_ID;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Integer getRDD_ID() {
		return RDD_ID;
	}
	public void setRDD_ID(Integer rDD_ID) {
		RDD_ID = rDD_ID;
	}
}
