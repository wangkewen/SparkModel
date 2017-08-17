/*
 * Entrance to Prediction
 * Print and list the detailed information
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import analyze.LogAnalyze;
import analyze.ResourceAnalyze;
import predict.JobPredict;
import profile.JobProfile;
import profile.JobResource;
import profile.ResourcesInfo;
import profile.JobProfile.StageProfile;
import profile.JobResource.StageResource;
import profile.ResourcesInfo.Res;

public class ShowInfo {
private JobProfile JobProfile = null;
private ResourcesInfo ResourcesInfo = null;
private JobResource JobResource = null;
public JobProfile getJobProfile(){
	return JobProfile;
}
public void setJobProfile(JobProfile JobProfile){
	this.JobProfile=JobProfile;
}
public ResourcesInfo getResourcesInfo(){
	return ResourcesInfo;
}
public void setResourcesInfo(ResourcesInfo ResourcesInfo){
	this.ResourcesInfo=ResourcesInfo;
}
public JobResource getJobResource(){
	return JobResource;
}
public void setJobResource(JobResource JobResource){
	this.JobResource=JobResource;
}
public void showjobinfo(){
	//Display job execution information
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
		System.out.println("Job Name:"+JobProfile.getAppName());
		System.out.println("Start : "+df.format(new Date(JobProfile.getStartTime())));
		System.out.println("End   : "+df.format(new Date(JobProfile.getEndTime())));
		System.out.println("Duration:"+JobProfile.getAppDuration()/1000.0+"s");
		System.out.println("Job Startup:"+JobProfile.getJobStartupTime()+"ms");
		for(int k=0;k<JobProfile.getStageProfiles().size();k++){
			System.out.println(df.format(new Date(JobProfile.getStageProfiles().get(k).getSubmissionTime())));
			System.out.println(df.format(new Date(JobProfile.getStageProfiles().get(k).getCompletionTime())));
			
			System.out.printf("%s %3d ","Stage",JobProfile.getStageProfiles().get(k).getStageID());
			System.out.printf("%s: %6.2fs","Duration",JobProfile.getStageProfiles().get(k).getStageDuration()/1000.0);
			System.out.printf("  %s: %5.1fKB","ShuffleRead",JobProfile.getStageProfiles().get(k).getShuffleReadBytes()/1024.0);
			System.out.printf("  %s: %5.1fKB","ShuffleWrite",JobProfile.getStageProfiles().get(k).getShuffleWriteBytes()/1024.0);
			System.out.println();
			
			
			//System.out.println("  Submission:"+df.format(new Date(JobProfile.getStageProfiles().get(k).getSubmissionTime())));
			//System.out.println("  Completion:"+df.format(new Date(JobProfile.getStageProfiles().get(k).getCompletionTime())));
			//System.out.println("  Name:"+JobProfile.getStageProfiles().get(k).getStageName());
			/*
			for(String key : JobProfile.getStageProfiles().get(k).getAvgTaskProfiles().keySet()){
				System.out.println("  Host:" + key);
				System.out.println("  Average Duration:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getTaskDuration()/1000.0+"s");
				System.out.println("  Average Run Time:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getRunTime()/1000.0+"s");
				System.out.println("  Average Deserialize Time:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getDeserializeTime()/1000.0+"s");
				System.out.println("  Average Serialize Time:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getSerializeTime()/1000.0+"s");
				System.out.println("  Average Cleanup Time:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getCleanupTime()/1000.0+"s");
				System.out.println("  Average Shuffle Read Bytes:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getShuffleReadBytes()/(1024.0*1024.0)+"M");
				System.out.println("  Average Shuffle Write Bytes:"+JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getShuffleWriteBytes()/(1024.0*1024.0)+"M");
			}*/
			/*
			for(int t=0;t<JobProfile.getStageProfiles().get(k).getTaskProfiles().size();t++){
				System.out.println("     Task "+JobProfile.getStageProfiles()
						.get(k).getTaskProfiles().get(t).getTaskID()+" Duration:"
			+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getTaskDuration()/1000.0+"s"
			+" TaskType:"+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getTaskType());
				System.out.println("       Host:"+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getHost());
				System.out.println("       RunTime:"+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getRunTime()/1000.0+"s");
				System.out.println("       DeserializeTime:"+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getDeserializeTime()/1000.0+"s");
				System.out.println("       SerializeTime:"+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getSerializeTime()/1000.0+"s");
				System.out.println("       CleanupTime:"+JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getCleanupTime()/1000.0+"s");
				System.out.println("       Launch:"+df.format(new Date(JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getLaunchTime())));
				System.out.println("       Finish:"+df.format(new Date(JobProfile.getStageProfiles().get(k).getTaskProfiles().get(t).getFinishTime())));
			}
			*/
		}
	}
public void showresinfo(){
	//Display resources consumption infomation
	System.out.println("JobName: "+JobResource.getJobname()+ "  Duration(s): "+JobResource.getJobduration()/1000.0+"s");
	System.out.print("Stage:         ");
	for(StageResource sr : JobResource.getStageres())
	     System.out.printf("%12s ",sr.getStagename());	
	System.out.println();
	System.out.print("Stage ID:      ");
	for(StageResource s : JobResource.getStageres())
		System.out.printf("%12s ",s.getStageid());
	System.out.println();
	System.out.println("Time(s):       ");
	for(StageResource s : JobResource.getStageres()){
		System.out.printf("%12s ",s.getStageduration()/1000.0);
		System.out.println();
		}
	System.out.println();
	System.out.println("CPU(%)         ");
	System.out.print("Avg:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.1f ",s.getAvgres().getCpu_sys()+s.getAvgres().getCpu_usr());
	System.out.println();
	System.out.print("Max:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.1f ",s.getMaxres().getCpu_sys()+s.getMaxres().getCpu_usr());
	System.out.println();
	System.out.print("Min:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.1f ",s.getMinres().getCpu_sys()+s.getMinres().getCpu_usr());
	System.out.println();
	System.out.println("Disk Read(KB)  ");
	System.out.print("Avg:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getAvgres().getDisk_read()/1024.0);
	System.out.println();
	System.out.print("Max:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMaxres().getDisk_read()/1024.0);
	System.out.println();
	System.out.print("Min:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMinres().getDisk_read()/1024.0);
	System.out.println();
	System.out.println("Disk Write(KB) ");
	System.out.print("Avg:           ");   
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getAvgres().getDisk_writ()/1024.0);
	System.out.println();
	System.out.print("Max:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMaxres().getDisk_writ()/1024.0);
	System.out.println();
	System.out.print("Min:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMinres().getDisk_writ()/1024.0);
	System.out.println();
	System.out.println("Net Recv(KB)   ");
	System.out.print("Avg:           "); 
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getAvgres().getNet_recv()/1024.0);
	System.out.println();
	System.out.print("Max:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMaxres().getNet_recv()/1024.0);
	System.out.println();
	System.out.print("Min:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMinres().getNet_recv()/1024.0);
	System.out.println();
	System.out.println("Net Send(KB)   ");
	System.out.print("Avg:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getAvgres().getNet_send()/1024.0);
	System.out.println();
	System.out.print("Max:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMaxres().getNet_send()/1024.0);
	System.out.println();
	System.out.print("Min:           ");
	for(StageResource s: JobResource.getStageres())
		System.out.printf("%12.0f ",s.getMinres().getNet_send()/1024.0);
	System.out.println();
}
public void twofiles(String logfile,String resfile1, String resfile2){
	//Merge the resource consumption of different hosts
	LogAnalyze la = new LogAnalyze(logfile);
	ResourceAnalyze ra = new ResourceAnalyze(resfile1);
	ResourceAnalyze ra2 = new ResourceAnalyze(resfile2);
	JobResource jr = new JobResource();
	setJobProfile(la.generate());
	setResourcesInfo(ra.generate());
	ResourcesInfo resinfo2 = ra2.generate();
	ResourcesInfo newres = getResourcesInfo().mergeResourcesInfo(resinfo2);
	jr.constructJobResource(getJobProfile(), newres);
	setJobResource(jr);
	showjobinfo();
	showresinfo();
}
public void onefile(String logfile,String resfile){
	LogAnalyze la = new LogAnalyze(logfile);
	ResourceAnalyze ra = new ResourceAnalyze(resfile);
	JobResource jr = new JobResource();
	setJobProfile(la.generate());
	setResourcesInfo(ra.generate());
	jr.constructJobResource(getJobProfile(), getResourcesInfo());
	setJobResource(jr);
	//showjobinfo();
	//showresinfo();
}


public static void main(String[] args){
	//Main function
	List<Long> fixstart = new ArrayList<Long>();
	int N;
	int maxstart = 190;
	int start=0;
	Random rd = new Random();
	N = 4;
	fixstart.add(0l);
	for(int i=0;i<N-1;i++){
	    start = rd.nextInt(maxstart/2-maxstart/10)+maxstart/10+1;
		fixstart.add(start*100l);
	}
	JobPredict jp = new JobPredict("",1l);
	List<JobResource> jobresources = new ArrayList<JobResource>();
	List<JobProfile> jobprofileslist = new ArrayList<JobProfile>();
	List<Long> inputdatasizes = new ArrayList<Long>();
	
    for(int t=0;t<N;t++){
    	String log = args[1+t].split("&&")[0];
    	String res = args[1+t].split("&&")[1];
    	ShowInfo sw = new ShowInfo();
    	sw.onefile(log, res);
    	jobresources.add(sw.getJobResource());
    	jobprofileslist.add(sw.getJobProfile());
    	inputdatasizes.add((20*1024)*1024*1024l);
    }
	jp.predictJobWithInterference(4,fixstart,jobprofileslist, jobresources,inputdatasizes);
}
}
