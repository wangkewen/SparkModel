package profile.metrics;

public class Input_Metrics implements SparkMetric{
	 private Long Bytes_Read;
     private Long Records_Read;
     public Long getBytes_Read(){
             return Bytes_Read;
     }
     public void setBytes_Read(Long bytes_Read){
             Bytes_Read = bytes_Read;
     }
     public Long getRecords_Read(){
             return Records_Read;
     }
     public void setRecords_Read(Long records_Read){
             Records_Read = records_Read;
     }
}
