package profile.metrics;

public class Storage_Level implements SparkMetric{
	private Boolean Use_Disk;
	private Boolean Use_Memory;
	private Boolean Use_Tachyon;
	private Boolean Deserialized;
	private Integer Replication;
	public Boolean getUse_Disk() {
		return Use_Disk;
	}
	public void setUse_Disk(Boolean use_Disk) {
		Use_Disk = use_Disk;
	}
	public Boolean getUse_Memory() {
		return Use_Memory;
	}
	public void setUse_Memory(Boolean use_Memory) {
		Use_Memory = use_Memory;
	}
	public Boolean getUse_Tachyon() {
		return Use_Tachyon;
	}
	public void setUse_Tachyon(Boolean use_Tachyon) {
		Use_Tachyon = use_Tachyon;
	}
	public Boolean getDeserialized() {
		return Deserialized;
	}
	public void setDeserialized(Boolean deserialized) {
		Deserialized = deserialized;
	}
	public Integer getReplication() {
		return Replication;
	}
	public void setReplication(Integer replication) {
		Replication = replication;
	}
}