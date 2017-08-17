package profile.metrics;

public class Shuffle_Write_Metrics implements SparkMetric{
	private Long Shuffle_Bytes_Written;
	private Long Shuffle_Records_Written;
	private Long Shuffle_Write_Time;
	public Long getShuffle_Bytes_Written() {
		return Shuffle_Bytes_Written;
	}
	public void setShuffle_Bytes_Written(Long shuffle_Bytes_Written) {
		Shuffle_Bytes_Written = shuffle_Bytes_Written;
	}
	public Long getShuffle_Records_Written() {
        return Shuffle_Records_Written;
    }
    public void setShuffle_Records_Written(Long shuffle_Records_Written){
        Shuffle_Records_Written = shuffle_Records_Written;
    }
	public Long getShuffle_Write_Time() {
		return Shuffle_Write_Time;
	}
	public void setShuffle_Write_Time(Long shuffle_Write_Time) {
		Shuffle_Write_Time = shuffle_Write_Time;
	}
}
