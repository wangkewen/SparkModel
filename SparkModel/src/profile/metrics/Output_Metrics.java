package profile.metrics;

public class Output_Metrics implements SparkMetric{
	private Long Bytes_Written;
    private Long Records_Written;
    public Long getBytes_Written(){
            return Bytes_Written;
    }
    public void setBytes_Written(Long bytes_Written){
            Bytes_Written = bytes_Written;
    }
    public Long getRecords_Written(){
            return Records_Written;
    }
    public void setRecords_Written(Long records_Written){
            Records_Written = records_Written;
    }
}
