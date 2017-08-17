package profile.metrics;

public class Task_Info implements SparkMetric{
	private Integer Task_ID;
	private Integer Index;
	private Long Launch_Time;
	private String Executor_ID;
	private String Host;
	private String Locality;
	private Long Getting_Result_Time;
	private Long Finish_Time;
	private Boolean Failed;
	private Long Serialized_Size;
	public Integer getTask_ID() {
		return Task_ID;
	}
	public void setTask_ID(Integer task_ID) {
		Task_ID = task_ID;
	}
	public Integer getIndex() {
		return Index;
	}
	public void setIndex(Integer index) {
		Index = index;
	}
	public Long getLaunch_Time() {
		return Launch_Time;
	}
	public void setLaunch_Time(Long launch_Time) {
		Launch_Time = launch_Time;
	}
	public String getExecutor_ID() {
		return Executor_ID;
	}
	public void setExecutor_ID(String executor_ID) {
		Executor_ID = executor_ID;
	}
	public String getHost() {
		return Host;
	}
	public void setHost(String host) {
		Host = host;
	}
	public String getLocality() {
		return Locality;
	}
	public void setLocality(String locality) {
		Locality = locality;
	}
	public Long getGetting_Result_Time() {
		return Getting_Result_Time;
	}
	public void setGetting_Result_Time(Long getting_Result_Time) {
		Getting_Result_Time = getting_Result_Time;
	}
	public Long getFinish_Time() {
		return Finish_Time;
	}
	public void setFinish_Time(Long finish_Time) {
		Finish_Time = finish_Time;
	}
	public Boolean getFailed() {
		return Failed;
	}
	public void setFailed(Boolean failed) {
		Failed = failed;
	}
	public Long getSerialized_Size() {
		return Serialized_Size;
	}
	public void setSerialized_Size(Long serialized_Size) {
		Serialized_Size = serialized_Size;
	}
}
