package profile.metrics;

public class Task_End_Reason implements SparkMetric{
	private String Reason;

	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
}
