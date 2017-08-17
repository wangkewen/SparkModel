package profile.metrics;

public class Shuffle_Read_Metrics implements SparkMetric{
	private Long Shuffle_Finish_Time;
	private Long Total_Blocks_Fetched;
	private Long Remote_Blocks_Fetched;
	private Long Local_Blocks_Fetched;
	private Long Fetch_Wait_Time;
	private Long Remote_Bytes_Read;
	private Long Local_Bytes_Read;
    private Long Shuffle_Records_Read;
    private Long Total_Records_Read;
	public Long getShuffle_Finish_Time() {
		return Shuffle_Finish_Time;
	}
	public void setShuffle_Finish_Time(Long shuffle_Finish_Time) {
		Shuffle_Finish_Time = shuffle_Finish_Time;
	}
	public Long getTotal_Blocks_Fetched() {
		return Total_Blocks_Fetched;
	}
	public void setTotal_Blocks_Fetched(Long total_Blocks_Fetched) {
		Total_Blocks_Fetched = total_Blocks_Fetched;
	}
	public Long getRemote_Blocks_Fetched() {
		return Remote_Blocks_Fetched;
	}
	public void setRemote_Blocks_Fetched(Long remote_Blocks_Fetched) {
		Remote_Blocks_Fetched = remote_Blocks_Fetched;
	}
	public Long getLocal_Blocks_Fetched() {
		return Local_Blocks_Fetched;
	}
	public void setLocal_Blocks_Fetched(Long local_Blocks_Fetched) {
		Local_Blocks_Fetched = local_Blocks_Fetched;
	}
	public Long getFetch_Wait_Time() {
		return Fetch_Wait_Time;
	}
	public void setFetch_Wait_Time(Long fetch_Wait_Time) {
		Fetch_Wait_Time = fetch_Wait_Time;
	}
	public Long getRemote_Bytes_Read() {
		return Remote_Bytes_Read;
	}
	public void setRemote_Bytes_Read(Long remote_Bytes_Read) {
		Remote_Bytes_Read = remote_Bytes_Read;
	}
	public Long getLocal_Bytes_Read(){
        return Local_Bytes_Read;
    }
    public void setLocal_Bytes_Read(Long local_Bytes_Read){
        Local_Bytes_Read = local_Bytes_Read;
    }
    public Long getShuffle_Records_Read(){
        return Shuffle_Records_Read;
    }
    public void setShuffle_Records_Read(Long shuffle_Records_Read){
        Shuffle_Records_Read=shuffle_Records_Read;
    }
    public Long getTotal_Records_Read(){
        return Total_Records_Read;
    }
    public void setTotal_Records_Read(Long total_Records_Read){
        Total_Records_Read=total_Records_Read;
    }

}
