package profile.metrics;

import java.util.List;

public class Stage_Info implements SparkMetric{
	private Integer Stage_ID;
	private String Stage_Name;
	private Integer Number_of_Tasks;
	private List<RDD_Info> RDD_Info;
	private Long Submission_Time;
	private Long Completion_Time;
	private String Failure_Reason;
	private Boolean Emitted_Task_Size_Warning;
	public Integer getStage_ID() {
		return Stage_ID;
	}
	public void setStage_ID(Integer stage_ID) {
		Stage_ID = stage_ID;
	}
	public String getStage_Name() {
		return Stage_Name;
	}
	public void setStage_Name(String stage_Name) {
		Stage_Name = stage_Name;
	}
	public Integer getNumber_of_Tasks() {
		return Number_of_Tasks;
	}
	public void setNumber_of_Tasks(Integer number_of_Tasks) {
		Number_of_Tasks = number_of_Tasks;
	}
	public List<RDD_Info> getRDD_Info() {
		return RDD_Info;
	}
	public void setRDD_Info(List<RDD_Info> rDD_Info) {
		RDD_Info = rDD_Info;
	}
	public Long getSubmission_Time() {
		return Submission_Time;
	}
	public void setSubmission_Time(Long submission_Time) {
		Submission_Time = submission_Time;
	}
	public Long getCompletion_Time() {
		return Completion_Time;
	}
	public void setCompletion_Time(Long completion_Time) {
		Completion_Time = completion_Time;
	}
	public String getFailure_Reason() {
		return Failure_Reason;
	}
	public void setFailure_Reason(String failure_Reason) {
		Failure_Reason = failure_Reason;
	}
	public Boolean getEmitted_Task_Size_Warning() {
		return Emitted_Task_Size_Warning;
	}
	public void setEmitted_Task_Size_Warning(Boolean emitted_Task_Size_Warning) {
		Emitted_Task_Size_Warning = emitted_Task_Size_Warning;
	}
}
