package profile.events;

import profile.metrics.Block_Manager_ID;

public class SparkListenerBlockManagerAdded implements SparkEvent{
	private String Event;
	private Block_Manager_ID Block_Manager_ID;
	private Long Maximum_Memory;
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
	public Long getMaximum_Memory() {
		return Maximum_Memory;
	}
	public void setMaximum_Memory(Long maximum_Memory) {
		Maximum_Memory = maximum_Memory;
	}
}
