package profile.events;

import profile.metrics.Block_Manager_ID;

public class SparkListenerBlockManagerRemoved implements SparkEvent{
	private String Event;
	private Block_Manager_ID Block_Manager_ID;
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public Block_Manager_ID getBlock_Manager_ID() {
		return Block_Manager_ID;
	}
	public void setBlock_Manager_ID(Block_Manager_ID block_Manager_ID) {
		Block_Manager_ID = block_Manager_ID;
	}
}
