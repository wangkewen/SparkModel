/*
 * ForcastJobProfile is used to represent the estimated JobProfile
 */

package predict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import profile.Configuration;

public class ForcastJobProfile {
	private String JobName;
	private Long JobDuration;
	private Long StartTime;
	private Long EndTime;
	private Integer SampleBlockNum;
	private List<ForcastStageProfile> ForcastStageProfiles;
	private Long JobStartupTime;
	private Long JobCleanupTime;
	private Configuration Configuration;
	public ForcastJobProfile(){
		JobName=null;
		JobDuration=null;
		StartTime=null;
		EndTime=null;
		SampleBlockNum=null;
		ForcastStageProfiles=new ArrayList<ForcastStageProfile>();
		JobStartupTime=null;
		JobCleanupTime=null;
		Configuration= new Configuration();
	}
	public void computeJob(){
		JobDuration=0l;
		/**
		System.out.println(JobName);
		System.out.println("Sample Block Number:"+SampleBlockNum);
		System.out.println("Block Number:"+Configuration.getBlockNum());
		**/
		for(int k=0;k<ForcastStageProfiles.size();k++){
			JobDuration+=ForcastStageProfiles.get(k).getStageDuration();
			/**
			System.out.printf("%s %3d ","Stage",ForcastStageProfiles.get(k).getStageID());
			System.out.printf("  %s ",ForcastStageProfiles.get(k).getStageName());
			System.out.printf("%s: %6.2fs","Duration",ForcastStageProfiles.get(k).getStageDuration()/1000.0);
			System.out.printf("  %s: %5.1fKB","ShuffleRead",ForcastStageProfiles.get(k).getShuffleReadBytes()/1024.0);
			System.out.printf("  %s: %5.1fKB","ShuffleWrite",ForcastStageProfiles.get(k).getShuffleWriteBytes()/1024.0);
			System.out.println();
			for(String key : Configuration.getHostCores().keySet())
			   System.out.println(ForcastStageProfiles.get(k).getForcastTaskProfiles().get(key).getTimeRatio());
		    **/
		}
		//System.out.println("JobStartupTime:"+JobStartupTime/1000.0+"s");
		//System.out.println("JobCleanupTime:"+JobCleanupTime/1000.0+"s");
		JobDuration+=JobStartupTime+JobCleanupTime;
	}
	public String getJobName() {
		return JobName;
	}
	public void setJobName(String jobName) {
		JobName = jobName;
	}
	public Long getJobDuration() {
		return JobDuration;
	}
	public void setJobDuration(Long jobDuration) {
		JobDuration = jobDuration;
	}
	public Long getStartTime(){
		return StartTime;
	}
	public void setStartTime(Long startTime){
		StartTime = startTime;
	}
	public Long getEndTime(){
		return EndTime;
	}
	public void setEndTime(Long endTime){
		EndTime = endTime;
	}
	public Integer getSampleBlockNum() {
		return SampleBlockNum;
	}
	public void setSampleBlockNum(Integer sampleBlockNum) {
		SampleBlockNum = sampleBlockNum;
	}
	public List<ForcastStageProfile> getForcastStageProfiles() {
		return ForcastStageProfiles;
	}
	public void setForcastStageProfiles(
			List<ForcastStageProfile> forcastStageProfiles) {
		ForcastStageProfiles = forcastStageProfiles;
	}
	public Long getJobStartupTime() {
		return JobStartupTime;
	}
	public void setJobStartupTime(Long jobStartupTime) {
		JobStartupTime = jobStartupTime;
	}
	public Long getJobCleanupTime() {
		return JobCleanupTime;
	}
	public void setJobCleanupTime(Long jobCleanupTime) {
		JobCleanupTime = jobCleanupTime;
	}
	public Configuration getConfiguration() {
		return Configuration;
	}
	public void setConfiguration(Configuration configuration) {
		Configuration = configuration;
	}

	class ForcastStageProfile {
		private Integer StageID;
		private String StageName;
		private Long StageDuration;
		private Map<String, ForcastTaskProfile> ForcastTaskProfiles;
		private Long StageStartupTime;
		private Long StageCleanupTime;
		private Long ShuffleReadBytes;
		private Long ShuffleWriteBytes;
		public ForcastStageProfile(){
			StageID=null;
			StageName=null;
			StageDuration=null;
			ForcastTaskProfiles=new HashMap<String, ForcastTaskProfile>();
			StageStartupTime=null;
			StageCleanupTime=null;
			ShuffleReadBytes=0l;
			ShuffleWriteBytes=0l;
		}
		public void computeStage(){
			int taskNum=Configuration.getBlockNum();
			Long maxRunTime=0l;
			Map<String,Long> taskTime=new HashMap<String,Long>();
			Map<String,Integer> count = new HashMap<String,Integer>();
			sortTask();
			for(String key : ForcastTaskProfiles.keySet())
				{taskTime.put(key, 0l);count.put(key, 0);}
			while(taskNum>0){
				Long minTime=Long.MAX_VALUE;
				String minHost=null;
				for(String key: taskTime.keySet()){
					if(taskTime.get(key)<minTime){
						minTime=taskTime.get(key);
						minHost=key; 
					}
				}
				int taskN=count.get(minHost);
				if(taskN>=Configuration.getHostCores().get(minHost))
					minTime+=ForcastTaskProfiles.get(minHost).getLateDuration();
				else
					minTime+=ForcastTaskProfiles.get(minHost).getTaskDuration();
				taskTime.remove(minHost);
				taskTime.put(minHost, minTime);
				count.remove(minHost);
				count.put(minHost, taskN+Configuration.getHostCores().get(minHost));
				//if(StageID==0) System.out.println("kk"+taskTime.get(minHost));
				int runtaskNum=0;
				//System.out.println(Configuration.getHostCores().size());
				if(taskNum<Configuration.getHostCores().get(minHost))
					runtaskNum=taskNum;
				else runtaskNum=Configuration.getHostCores().get(minHost);
				ShuffleReadBytes+=runtaskNum*ForcastTaskProfiles.get(minHost).getShuffleReadBytes();
				ShuffleWriteBytes+=runtaskNum*ForcastTaskProfiles.get(minHost).getShuffleWriteBytes();
				taskNum-=Configuration.getHostCores().get(minHost);
			}
			/*
			while(taskNum>coreSum){
				for(String key: ForcastTaskProfiles.keySet()){
					if(taskTime.isEmpty()||!taskTime.containsKey(key))
						taskTime.put(key, ForcastTaskProfiles.get(key).getTaskDuration());
					else{
						Long time=taskTime.get(key)+ForcastTaskProfiles.get(key).getTaskDuration();
						taskTime.remove(key);
						taskTime.put(key, time);
					}
					ShuffleReadBytes+=Configuration.getHostCores().get(key)
							*ForcastTaskProfiles.get(key).getShuffleReadBytes();
					ShuffleWriteBytes+=Configuration.getHostCores().get(key)
							*ForcastTaskProfiles.get(key).getShuffleWriteBytes();
				}
				taskNum-=coreSum;
			}
			for(String key: ForcastTaskProfiles.keySet()){
				if(taskNum>0){
				Long lastTime=taskTime.get(key)+ForcastTaskProfiles.get(key).getTaskDuration();
				taskTime.remove(key);
				taskTime.put(key, lastTime);
				if(taskNum>Configuration.getHostCores().get(key)){
					ShuffleReadBytes+=Configuration.getHostCores().get(key)
							*ForcastTaskProfiles.get(key).getShuffleReadBytes();
					ShuffleWriteBytes+=Configuration.getHostCores().get(key)
							*ForcastTaskProfiles.get(key).getShuffleWriteBytes();
				}else{
					ShuffleReadBytes+=taskNum*ForcastTaskProfiles.get(key).getShuffleReadBytes();
					ShuffleWriteBytes+=taskNum*ForcastTaskProfiles.get(key).getShuffleWriteBytes();
				}
				taskNum-=Configuration.getHostCores().get(key);
				}else break;
			}*/
			for(String key: taskTime.keySet())
				if(taskTime.get(key)>maxRunTime) maxRunTime=taskTime.get(key);
//			if(StageID==1) {System.out.println("Stage "+StageID+" StartupTime:"+StageStartupTime/1000.0+"s");
//			System.out.println("Stage "+StageID+" RunTime:"+maxRunTime/1000.0+"s");
//			System.out.println("Stage "+StageID+" CleanupTime:"+StageCleanupTime/1000.0+"s");
//			}
			StageDuration=maxRunTime+StageStartupTime+StageCleanupTime;
		}
		public void sortTask(){
			List<ForcastTaskProfile> tasks;
			tasks=new ArrayList<ForcastTaskProfile>();
			//Profiles=new HashMap<String,ForcastTaskProfile>();
			for(String key : ForcastTaskProfiles.keySet())
				tasks.add(ForcastTaskProfiles.get(key));
			
			//System.out.println(tasks.get(0).getRunTime());
			Collections.sort(tasks);
			
			ForcastTaskProfiles=new HashMap<String,ForcastTaskProfile>();
			for(int k=0;k<tasks.size();k++)
				ForcastTaskProfiles.put(tasks.get(k).getHost(), tasks.get(k));
		}
		public Integer getStageID() {
			return StageID;
		}
		public void setStageID(Integer stageID) {
			StageID = stageID;
		}
		public String getStageName() {
			return StageName;
		}
		public void setStageName(String stageName) {
			StageName = stageName;
		}
		public Long getStageDuration() {
			return StageDuration;
		}
		public void setStageDuration(Long stageDuration) {
			StageDuration = stageDuration;
		}
		public Map<String, ForcastTaskProfile> getForcastTaskProfiles() {
			return ForcastTaskProfiles;
		}
		public void setForcastTaskProfiles(Map<String, ForcastTaskProfile> forcastTaskProfiles) {
			ForcastTaskProfiles = forcastTaskProfiles;
		}
		public Long getStageStartupTime() {
			return StageStartupTime;
		}
		public void setStageStartupTime(Long stageStartupTime) {
			StageStartupTime = stageStartupTime;
		}
		public Long getStageCleanupTime() {
			return StageCleanupTime;
		}
		public void setStageCleanupTime(Long stageCleanupTime) {
			StageCleanupTime = stageCleanupTime;
		}
		public Long getShuffleReadBytes() {
			return ShuffleReadBytes;
		}
		public void setShuffleReadBytes(Long shuffleReadBytes) {
			ShuffleReadBytes = shuffleReadBytes;
		}
		public Long getShuffleWriteBytes() {
			return ShuffleWriteBytes;
		}
		public void setShuffleWriteBytes(Long shuffleWriteBytes) {
			ShuffleWriteBytes = shuffleWriteBytes;
		}

		class ForcastTaskProfile implements Comparable<ForcastTaskProfile>{
			private String TaskType;
			private String Host;
			private Long TaskDuration;
			private Long RunTime;
			private Double TimeRatio; // time_2batch/time_1batch
			private Long LateDuration;
			private Long DeserializeTime;
			private Long SerializeTime;
			private Long CleanupTime;
			private Long ShuffleWriteBytes;
			private Long ShuffleReadBytes;
			public ForcastTaskProfile(){
				TaskType=null;
				Host=null;
				TaskDuration=null;
				RunTime=null;
				TimeRatio=null;
				LateDuration=null;
				DeserializeTime=null;
				SerializeTime=null;
				CleanupTime=null;
				ShuffleWriteBytes=null;
				ShuffleReadBytes=null;
			}
			public int compareTo(ForcastTaskProfile task){
				return Long.compare(this.getTaskDuration(), task.getTaskDuration());
			}
			public void computeTaskDuration(){
				TaskDuration=DeserializeTime+RunTime+SerializeTime+CleanupTime;
				LateDuration=DeserializeTime+(long)(TimeRatio*RunTime)+SerializeTime+CleanupTime;
			}
			public String getTaskType() {
				return TaskType;
			}
			public void setTaskType(String taskType) {
				TaskType = taskType;
			}
			public String getHost() {
				return Host;
			}
			public void setHost(String host) {
				Host = host;
			}
			public Long getTaskDuration() {
				return TaskDuration;
			}
			public void setTaskDuration(Long taskDuration) {
				TaskDuration = taskDuration;
			}
			public Long getRunTime() {
				return RunTime;
			}
			public void setRunTime(Long runTime) {
				RunTime = runTime;
			}
			public Double getTimeRatio() {
				return TimeRatio;
			}
			public void setTimeRatio(Double timeRatio) {
				TimeRatio = timeRatio;
			}
			public Long getLateDuration() {
				return LateDuration;
			}
			public void setLateDuration(Long lateDuration) {
				LateDuration = lateDuration;
			}
			public Long getDeserializeTime() {
				return DeserializeTime;
			}
			public void setDeserializeTime(Long deserializeTime) {
				DeserializeTime = deserializeTime;
			}
			public Long getSerializeTime() {
				return SerializeTime;
			}
			public void setSerializeTime(Long serializeTime) {
				SerializeTime = serializeTime;
			}
			public Long getCleanupTime() {
				return CleanupTime;
			}
			public void setCleanupTime(Long cleanupTime) {
				CleanupTime = cleanupTime;
			}
			public Long getShuffleWriteBytes() {
				return ShuffleWriteBytes;
			}
			public void setShuffleWriteBytes(Long shuffleWriteBytes) {
				ShuffleWriteBytes = shuffleWriteBytes;
			}
			public Long getShuffleReadBytes() {
				return ShuffleReadBytes;
			}
			public void setShuffleReadBytes(Long shuffleReadBytes) {
				ShuffleReadBytes = shuffleReadBytes;
			}
		}
	}
}
