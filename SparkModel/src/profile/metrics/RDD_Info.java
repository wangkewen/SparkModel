package profile.metrics;

public class RDD_Info implements SparkMetric{
	private Integer RDD_ID;
	private String Name;
	private Storage_Level Storage_Level;
	private Integer Number_of_Partitions;
	private Integer Number_of_Cached_Partitions;
	private Long Memory_Size;
	private Long Tachyon_Size;
	private Long Disk_Size;
	public Integer getRDD_ID() {
		return RDD_ID;
	}
	public void setRDD_ID(Integer rDD_ID) {
		RDD_ID = rDD_ID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Storage_Level getStorage_Level() {
		return Storage_Level;
	}
	public void setStorage_Level(Storage_Level storage_Level) {
		Storage_Level = storage_Level;
	}
	public Integer getNumber_of_Partitions() {
		return Number_of_Partitions;
	}
	public void setNumber_of_Partitions(Integer number_of_Partitions) {
		Number_of_Partitions = number_of_Partitions;
	}
	public Integer getNumber_of_Cached_Partitions() {
		return Number_of_Cached_Partitions;
	}
	public void setNumber_of_Cached_Partitions(Integer number_of_Cached_Partitions) {
		Number_of_Cached_Partitions = number_of_Cached_Partitions;
	}
	public Long getMemory_Size() {
		return Memory_Size;
	}
	public void setMemory_Size(Long memory_Size) {
		Memory_Size = memory_Size;
	}
	public Long getTachyon_Size() {
		return Tachyon_Size;
	}
	public void setTachyon_Size(Long tachyon_Size) {
		Tachyon_Size = tachyon_Size;
	}
	public Long getDisk_Size() {
		return Disk_Size;
	}
	public void setDisk_Size(Long disk_Size) {
		Disk_Size = disk_Size;
	}
}
