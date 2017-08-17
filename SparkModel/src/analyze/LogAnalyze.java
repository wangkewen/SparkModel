/*
 * Extract Spark Application information from Spark event logs and construct JobProfile
 */

package analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

import profile.JobProfile;
import profile.LogInfo;
import profile.JobProfile.StageProfile;
import profile.JobProfile.StageProfile.TaskProfile;
import profile.events.SparkListenerApplicationEnd;
import profile.events.SparkListenerApplicationStart;
import profile.events.SparkListenerBlockManagerAdded;
import profile.events.SparkListenerBlockManagerRemoved;
import profile.events.SparkListenerEnvironmentUpdate;
import profile.events.SparkListenerJobEnd;
import profile.events.SparkListenerJobStart;
import profile.events.SparkListenerStageCompleted;
import profile.events.SparkListenerStageSubmitted;
import profile.events.SparkListenerTaskEnd;
import profile.events.SparkListenerTaskGettingResult;
import profile.events.SparkListenerTaskStart;
import profile.events.SparkListenerUnpersistRDD;

import com.google.gson.Gson; 

public class LogAnalyze implements Analyze<JobProfile>{
	private String Logfile=null;
	private LogInfo Logs=null;
	private JobProfile JobProfile=null;
	
public LogAnalyze(String Logfile){
		this.Logfile=Logfile;	
}
public JobProfile generate(){
	constructJobProfile();
	return this.JobProfile;
}
public void load(){
	File log=null;
	String logstr="";
	String eventname=null;
	String line=null;
	Gson gs = new Gson();
	BufferedReader br=null;
	Logs = new LogInfo();
	log=new File(Logfile);
	try{
		br = new BufferedReader(new FileReader(log));
	while((line=br.readLine())!=null)
	{
		line=line.replaceAll(" ", "_");
		if(line.contains("{\"Event\":"))
		{
			eventname=line.split("\"")[3];
			logstr=line;
			if(eventname!=null){
				if(eventname.equals("SparkListenerBlockManagerAdded")){
					SparkListenerBlockManagerAdded se = gs.fromJson(logstr, SparkListenerBlockManagerAdded.class);
					Logs.getSparkListenerBlockManagerAddedList().add(se);
                    }
				else if(eventname.equals("SparkListenerBlockManagerRemoved")){
					SparkListenerBlockManagerRemoved se = gs.fromJson(logstr, SparkListenerBlockManagerRemoved.class);
					Logs.getSparkListenerBlockManagerRemovedList().add(se);
					}
				else if(eventname.equals("SparkListenerApplicationEnd")){
					SparkListenerApplicationEnd se = gs.fromJson(logstr, SparkListenerApplicationEnd.class);
					Logs.getSparkListenerApplicationEndList().add(se);
					}
				else if(eventname.equals("SparkListenerApplicationStart")){
					SparkListenerApplicationStart se = gs.fromJson(logstr, SparkListenerApplicationStart.class);
					Logs.getSparkListenerApplicationStartList().add(se);
					}
				else if(eventname.equals("SparkListenerEnvironmentUpdate")){
					SparkListenerEnvironmentUpdate se = gs.fromJson(logstr, SparkListenerEnvironmentUpdate.class);
					Logs.getSparkListenerEnvironmentUpdateList().add(se);
					}
				else if(eventname.equals("SparkListenerJobEnd")){
					SparkListenerJobEnd se = gs.fromJson(logstr, SparkListenerJobEnd.class);
					Logs.getSparkListenerJobEndList().add(se);
					}
				else if(eventname.equals("SparkListenerJobStart")){
					SparkListenerJobStart se = gs.fromJson(logstr, SparkListenerJobStart.class);
					Logs.getSparkListenerJobStartList().add(se);
					}
				else if(eventname.equals("SparkListenerStageCompleted")){
					SparkListenerStageCompleted se = gs.fromJson(logstr, SparkListenerStageCompleted.class);
					Logs.getSparkListenerStageCompletedList().add(se);
					}
				else if(eventname.equals("SparkListenerStageSubmitted")){
					SparkListenerStageSubmitted se = gs.fromJson(logstr, SparkListenerStageSubmitted.class);
					Logs.getSparkListenerStageSubmittedList().add(se);
					}
				else if(eventname.equals("SparkListenerTaskEnd")){
					SparkListenerTaskEnd se = gs.fromJson(logstr, SparkListenerTaskEnd.class);
					Logs.getSparkListenerTaskEndList().add(se);
					}
				else if(eventname.equals("SparkListenerTaskGettingResult")){
					SparkListenerTaskGettingResult se = gs.fromJson(logstr, SparkListenerTaskGettingResult.class);
					Logs.getSparkListenerTaskGettingResultList().add(se);
					}
				else if(eventname.equals("SparkListenerTaskStart")){
					SparkListenerTaskStart se = gs.fromJson(logstr, SparkListenerTaskStart.class);
					Logs.getSparkListenerTaskStartList().add(se);
					}
				else if(eventname.equals("SparkListenerUnpersistRDD")){
					SparkListenerUnpersistRDD se = gs.fromJson(logstr, SparkListenerUnpersistRDD.class);
					Logs.getSparkListenerUnpersistRDDList().add(se);
					}
				}
		}
	}
	}catch(IOException e){
		e.printStackTrace();
	}
}
private void constructJobProfile(){
	if(Logs==null) load();
	JobProfile = new JobProfile();
	if(Logs.getSparkListenerApplicationStartList().size()>0){
	JobProfile.getConfiguration().init(0l);
	JobProfile.setAppName(Logs.getSparkListenerApplicationStartList().get(0).getApp_Name());
	JobProfile.setStartTime(Logs.getSparkListenerApplicationStartList().get(0).getTimestamp());
	JobProfile.setEndTime(Logs.getSparkListenerApplicationEndList().get(0).getTimestamp());
	JobProfile.setAppDuration(JobProfile.getEndTime()-JobProfile.getStartTime());
	for(int k=0;k<Logs.getSparkListenerStageCompletedList().size();k++){
		StageProfile stage=JobProfile.new StageProfile();
		stage.setStageID(Logs.getSparkListenerStageCompletedList()
				.get(k).getStage_Info().getStage_ID());
		stage.setStageName(Logs.getSparkListenerStageCompletedList()
				.get(k).getStage_Info().getStage_Name());
		stage.setSubmissionTime(Logs.getSparkListenerStageCompletedList()
				.get(k).getStage_Info().getSubmission_Time());
		stage.setCompletionTime(Logs.getSparkListenerStageCompletedList()
				.get(k).getStage_Info().getCompletion_Time());
		stage.setStageDuration(stage.getCompletionTime()-stage.getSubmissionTime());
		
		for(int t=0;t<Logs.getSparkListenerTaskEndList().size();t++)
			if(Logs.getSparkListenerTaskEndList().get(t).getStage_ID()==stage.getStageID()){
				TaskProfile task=stage.new TaskProfile();
				task.setTaskType(Logs.getSparkListenerTaskEndList().get(t).getTask_Type());
				task.setTaskID(Logs.getSparkListenerTaskEndList().get(t).getTask_Info().getTask_ID());
				task.setHost(Logs.getSparkListenerTaskEndList().get(t).getTask_Info().getHost());
				task.setLaunchTime(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Info().getLaunch_Time());
				task.setFinishTime(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Info().getFinish_Time());
				task.setTaskDuration(task.getFinishTime()-task.getLaunchTime());
				task.setDeserializeTime(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getExecutor_Deserialize_Time());
				task.setRunTime(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getExecutor_Run_Time());
				task.setSerializeTime(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getResult_Serialization_Time());
				Long cleanup = task.getTaskDuration()-task.getDeserializeTime()-task.getSerializeTime()
						-task.getRunTime();
				task.setCleanupTime(cleanup<0?0:cleanup);
				if(Logs.getSparkListenerTaskEndList().get(t).getTask_Metrics().getShuffle_Write_Metrics()!=null)
				task.setShuffleWriteBytes(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getShuffle_Write_Metrics().getShuffle_Bytes_Written());
				if(Logs.getSparkListenerTaskEndList().get(t).getTask_Metrics().getShuffle_Read_Metrics()!=null)
				task.setShuffleReadBytes(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getShuffle_Read_Metrics().getRemote_Bytes_Read());
				task.setMemoryBytesSpilled(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getMemory_Bytes_Spilled());
				task.setDiskBytesSpilled(Logs.getSparkListenerTaskEndList()
						.get(t).getTask_Metrics().getDisk_Bytes_Spilled());
				stage.getTaskProfiles().add(task);
			}
		Collections.sort(stage.getTaskProfiles()); 
		stage.computeAvgTaskProfiles();
		Long taskstart=Long.MAX_VALUE,taskend=0l;
		for(int x=0;x<stage.getTaskProfiles().size();x++){
			if(stage.getTaskProfiles().get(x).getLaunchTime()<taskstart)
				taskstart=stage.getTaskProfiles().get(x).getLaunchTime();
			if(stage.getTaskProfiles().get(x).getFinishTime()>taskend)
				taskend=stage.getTaskProfiles().get(x).getFinishTime();
			}
		Long stageStartup = taskstart-stage.getSubmissionTime();
		Long stageCleanup = stage.getCompletionTime()-taskend;
		stage.setStageStartupTime(stageStartup<0?0:stageStartup);
		stage.setStageCleanupTime(stageCleanup<0?0:stageCleanup);
		JobProfile.getStageProfiles().add(stage);
	}
    	Collections.sort(JobProfile.getStageProfiles()); //sort by submission time
    	Long stagestart=Long.MAX_VALUE,stageend=0l;
    	for(int y=0;y<JobProfile.getStageProfiles().size();y++){
    		if(JobProfile.getStageProfiles().get(y).getSubmissionTime()<stagestart)
    			stagestart=JobProfile.getStageProfiles().get(y).getSubmissionTime();
    		if(JobProfile.getStageProfiles().get(y).getCompletionTime()>stageend)
    			stageend=JobProfile.getStageProfiles().get(y).getCompletionTime();
    	}
    	Long jobStartup = stagestart-JobProfile.getStartTime();
    	Long jobCleanup = JobProfile.getEndTime()-stageend;
        JobProfile.setJobStartupTime(jobStartup<0?0:jobStartup);
        JobProfile.setJobCleanupTime(jobCleanup<0?0:jobCleanup);
	}
}
}
