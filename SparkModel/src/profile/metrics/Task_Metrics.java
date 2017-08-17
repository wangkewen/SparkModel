package profile.metrics;

import java.util.List;
//org.apache.spark.executor.TaskMetrics
public class Task_Metrics implements SparkMetric{
	private String Host_Name;
	private Long Executor_Deserialize_Time;
	private Long Executor_Run_Time;
	private Long Result_Size;
	private Long JVM_GC_Time;
	private Long Result_Serialization_Time;
	private Long Memory_Bytes_Spilled;
	private Long Disk_Bytes_Spilled;
	private Shuffle_Read_Metrics Shuffle_Read_Metrics;
	private Shuffle_Write_Metrics Shuffle_Write_Metrics;
	private Input_Metrics Input_Metrics;
    private Output_Metrics Output_Metrics;
	private List<Updated_Blocks> Updated_Blocks;
	public String getHost_Name() {
		return Host_Name;
	}
	public void setHost_Name(String host_Name) {
		Host_Name = host_Name;
	}
	public Long getExecutor_Deserialize_Time() {
		return Executor_Deserialize_Time;
	}
	public void setExecutor_Deserialize_Time(Long executor_Deserialize_Time) {
		Executor_Deserialize_Time = executor_Deserialize_Time;
	}
	public Long getExecutor_Run_Time() {
		return Executor_Run_Time;
	}
	public void setExecutor_Run_Time(Long executor_Run_Time) {
		Executor_Run_Time = executor_Run_Time;
	}
	public Long getResult_Size() {
		return Result_Size;
	}
	public void setResult_Size(Long result_Size) {
		Result_Size = result_Size;
	}
	public Long getJVM_GC_Time() {
		return JVM_GC_Time;
	}
	public void setJVM_GC_Time(Long jVM_GC_Time) {
		JVM_GC_Time = jVM_GC_Time;
	}
	public Long getResult_Serialization_Time() {
		return Result_Serialization_Time;
	}
	public void setResult_Serialization_Time(Long result_Serialization_Time) {
		Result_Serialization_Time = result_Serialization_Time;
	}
	public Long getMemory_Bytes_Spilled() {
		return Memory_Bytes_Spilled;
	}
	public void setMemory_Bytes_Spilled(Long memory_Bytes_Spilled) {
		Memory_Bytes_Spilled = memory_Bytes_Spilled;
	}
	public Long getDisk_Bytes_Spilled() {
		return Disk_Bytes_Spilled;
	}
	public void setDisk_Bytes_Spilled(Long disk_Bytes_Spilled) {
		Disk_Bytes_Spilled = disk_Bytes_Spilled;
	}
	public Shuffle_Read_Metrics getShuffle_Read_Metrics() {
		return Shuffle_Read_Metrics;
	}
	public void setShuffle_Read_Metrics(Shuffle_Read_Metrics shuffle_Read_Metrics) {
		Shuffle_Read_Metrics = shuffle_Read_Metrics;
	}
	public Shuffle_Write_Metrics getShuffle_Write_Metrics() {
		return Shuffle_Write_Metrics;
	}
	public void setShuffle_Write_Metrics(Shuffle_Write_Metrics shuffle_Write_Metrics) {
		Shuffle_Write_Metrics = shuffle_Write_Metrics;
	}
	 public Input_Metrics getInput_Metrics(){
         return Input_Metrics;
    }
    public void setInput_Metrics(Input_Metrics input_Metrics){
         Input_Metrics = input_Metrics;
    }
    public Output_Metrics getOutput_Metrics(){
         return Output_Metrics;
    }
    public void setOutput_Metrics(Output_Metrics output_Metrics){
         Output_Metrics = output_Metrics;
    }
	public List<Updated_Blocks> getUpdated_Blocks() {
		return Updated_Blocks;
	}
	public void setUpdated_Blocks(List<Updated_Blocks> updated_Blocks) {
		Updated_Blocks = updated_Blocks;
	}
}
