/*
 * Spark Application Profile
 * Job --- Stage0 --- Task0
 *                --- Task1
 *                ---
 *     --- Stage1 --- Task0
 *                --- Task1
 *                ---
 *     ---
 */

package profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobProfile {
	private String AppName;
	private Long AppDuration;
	private Long StartTime;
	private Long EndTime;
	private Long JobStartupTime;
	private Long JobCleanupTime;
	private List<StageProfile> StageProfiles;
	private Configuration Configuration;
	public JobProfile() {
		AppName=null;
		AppDuration=null;
		StartTime=null;
		EndTime=null;
		JobStartupTime=null;
		JobCleanupTime=null;
		StageProfiles = new ArrayList<StageProfile>();
		Configuration= new Configuration();
	}
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String appName) {
		AppName = appName;
	}
	public Long getAppDuration() {
		return AppDuration;
	}
	public void setAppDuration(Long appDuration) {
		AppDuration = appDuration;
	}
	public Long getStartTime() {
		return StartTime;
	}
	public void setStartTime(Long startTime) {
		StartTime = startTime;
	}
	public Long getEndTime() {
		return EndTime;
	}
	public void setEndTime(Long endTime) {
		EndTime = endTime;
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
	public List<StageProfile> getStageProfiles() {
		return StageProfiles;
	}
	public void setStageProfiles(List<StageProfile> stageProfiles) {
		StageProfiles = stageProfiles;
	}
	public Configuration getConfiguration() {
		return Configuration;
	}
	public void setConfiguration(Configuration configuration) {
		Configuration = configuration;
	}
	public class StageProfile implements Comparable<StageProfile>{
		private Integer StageID;
		private String StageName;
		private Long StageDuration;
		private Long SubmissionTime;
		private Long CompletionTime;
		private Long StageStartupTime;
		private Long StageCleanupTime;
		private Long ShuffleReadBytes;
		private Long ShuffleWriteBytes;
		private List<TaskProfile> TaskProfiles;
		private Map<String,TaskProfile> avgTaskProfiles;
		public StageProfile() {
			StageID=null;
			StageName=null;
			StageDuration=null;
			SubmissionTime=null;
			CompletionTime=null;
			StageStartupTime=0l;
			StageCleanupTime=0l;
			ShuffleReadBytes=0l;
			ShuffleWriteBytes=0l;
			TaskProfiles = new ArrayList<TaskProfile>();
			avgTaskProfiles = new HashMap<String,TaskProfile>();
		}
		public int compareTo(StageProfile stage){
			return Long.compare(this.SubmissionTime,stage.getSubmissionTime());
		}
		public void computeAvgTaskProfiles(){
			// calculate the average task information of sample to estimate the tasks of predicted job
			Map<String,List<TaskProfile>> tasks;
			Long sumDuration=0l,sumRun=0l,sumDeser=0l,sumSer=0l,sumClean=0l,sumShufR=0l,sumShufW=0l;
			tasks = new HashMap<String, List<TaskProfile>>();
			for(int k=0;k<TaskProfiles.size();k++){
				if(tasks.isEmpty()||!tasks.containsKey(TaskProfiles.get(k).getHost())){
					List<TaskProfile> task = new ArrayList<TaskProfile>();
					task.add(TaskProfiles.get(k));
					tasks.put(TaskProfiles.get(k).getHost(), task);
				}else {
					tasks.get(TaskProfiles.get(k).getHost()).add(TaskProfiles.get(k));
				}	
			}
			Long shuffleReadBytes=0l,shuffleWriteBytes=0l;
			for(String key : tasks.keySet()){
				sumDuration=0l;
				sumRun=0l;
				sumDeser=0l;
				sumSer=0l;
				sumClean=0l;
				sumShufR=0l;
				sumShufW=0l;
				for(int k=0;k<tasks.get(key).size();k++){
					sumDuration+=tasks.get(key).get(k).getTaskDuration();
					sumRun+=tasks.get(key).get(k).getRunTime();
					sumDeser+=tasks.get(key).get(k).getDeserializeTime();
					sumSer+=tasks.get(key).get(k).getSerializeTime();
					sumClean+=tasks.get(key).get(k).getCleanupTime();
					if(tasks.get(key).get(k).getShuffleReadBytes()!=null)
					sumShufR+=tasks.get(key).get(k).getShuffleReadBytes();
					if(tasks.get(key).get(k).getShuffleWriteBytes()!=null)
					sumShufW+=tasks.get(key).get(k).getShuffleWriteBytes();
				}
				shuffleReadBytes+=sumShufR;
				shuffleWriteBytes+=sumShufW;
				TaskProfile avgTask = new TaskProfile();
				avgTask.setHost(key);
				avgTask.setTaskType(tasks.get(key).get(0).getTaskType());
				avgTask.setTaskDuration(sumDuration/tasks.get(key).size());
				avgTask.setRunTime(sumRun/tasks.get(key).size());
				avgTask.setDeserializeTime(sumDeser/tasks.get(key).size());
				avgTask.setSerializeTime(sumSer/tasks.get(key).size());
				avgTask.setCleanupTime(sumClean/tasks.get(key).size());
				avgTask.setShuffleReadBytes(sumShufR/tasks.get(key).size());
				avgTask.setShuffleWriteBytes(sumShufW/tasks.get(key).size());
				int taskNum=tasks.get(key).size();
				int coresNum=Configuration.getHostCores().get(key);
				double firstAvgTime=0.0,secondAvgTime=0.0;
				
				if(taskNum>=2*coresNum){
					for(int i=0;i<coresNum;i++) {
						firstAvgTime+=tasks.get(key).get(i).getRunTime();
						secondAvgTime+=tasks.get(key).get(i+coresNum).getRunTime();
					}
					avgTask.setRunTime((long)firstAvgTime/coresNum);
					avgTask.setTimeRatio(1.0*secondAvgTime/firstAvgTime);
				}else if(taskNum>coresNum){
					for(int i=0;i<coresNum;i++) 
						firstAvgTime+=tasks.get(key).get(i).getRunTime();
					firstAvgTime/=coresNum;
					for(int i=coresNum;i<taskNum;i++) 
						secondAvgTime+=tasks.get(key).get(i).getRunTime();
					secondAvgTime/=(taskNum-coresNum);
					avgTask.setRunTime((long)firstAvgTime);
					avgTask.setTimeRatio(1.0*secondAvgTime/firstAvgTime);
				}else 
				    avgTask.setTimeRatio(1.0);
				avgTaskProfiles.put(key, avgTask);
			}
			ShuffleReadBytes=shuffleReadBytes;
			ShuffleWriteBytes=shuffleWriteBytes;
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
		public Long getSubmissionTime() {
			return SubmissionTime;
		}
		public void setSubmissionTime(Long submissionTime) {
			SubmissionTime = submissionTime;
		}
		public Long getCompletionTime() {
			return CompletionTime;
		}
		public void setCompletionTime(Long completionTime) {
			CompletionTime = completionTime;
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
		public List<TaskProfile> getTaskProfiles() {
			return TaskProfiles;
		}
		public void setTaskProfiles(List<TaskProfile> taskProfiles) {
			TaskProfiles = taskProfiles;
		}
		public Map<String, TaskProfile> getAvgTaskProfiles() {
			return avgTaskProfiles;
		}
		public void setAvgTaskProfiles(Map<String, TaskProfile> avgTaskProfiles) {
			this.avgTaskProfiles = avgTaskProfiles;
		}

		public class TaskProfile implements Comparable<TaskProfile>{
			private Integer TaskID;
			private String Host;
			private String TaskType;
			private Long TaskDuration;
			private Long LaunchTime;
			private Long FinishTime;
			private Long RunTime;
			private Double TimeRatio; // time_2batch/time_1batch
			private Long LateDuration;
			private Long DeserializeTime;
			private Long SerializeTime;
			private Long CleanupTime;
			private Long ShuffleWriteBytes;
			private Long ShuffleReadBytes;
			private Long MemoryBytesSpilled;
			private Long DiskBytesSpilled;
			public TaskProfile() {
				TaskID=null;
				Host=null;
				TaskType=null;
				TaskDuration=null;
				LaunchTime=null;
				FinishTime=null;
				RunTime=null;
				TimeRatio=null;
				LateDuration=null;
				DeserializeTime=null;
				SerializeTime=null;
				CleanupTime=null;
				ShuffleWriteBytes=null;
				ShuffleReadBytes=null;
				MemoryBytesSpilled=null;
				DiskBytesSpilled=null;
			}
			public int compareTo(TaskProfile task){
				return Long.compare(this.getLaunchTime(), task.getLaunchTime());
			}
			public Integer getTaskID() {
				return TaskID;
			}
			public void setTaskID(Integer taskID) {
				TaskID = taskID;
			}
			public String getHost() {
				return Host;
			}
			public void setHost(String host) {
				Host = host;
			}
			public String getTaskType() {
				return TaskType;
			}
			public void setTaskType(String taskType) {
				TaskType = taskType;
			}
			public Long getTaskDuration() {
				return TaskDuration;
			}
			public void setTaskDuration(Long taskDuration) {
				TaskDuration = taskDuration;
			}
			public Long getLaunchTime() {
				return LaunchTime;
			}
			public void setLaunchTime(Long launchTime) {
				LaunchTime = launchTime;
			}
			public Long getFinishTime() {
				return FinishTime;
			}
			public void setFinishTime(Long finishTime) {
				FinishTime = finishTime;
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
			public Long getMemoryBytesSpilled() {
				return MemoryBytesSpilled;
			}
			public void setMemoryBytesSpilled(Long memoryBytesSpilled) {
				MemoryBytesSpilled = memoryBytesSpilled;
			}
			public Long getDiskBytesSpilled() {
				return DiskBytesSpilled;
			}
			public void setDiskBytesSpilled(Long diskBytesSpilled) {
				DiskBytesSpilled = diskBytesSpilled;
			}
		}
	}
}
