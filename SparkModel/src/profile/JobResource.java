/*
 * Spark Application resources consumption information
 * JobResource --- Stage0Resource
 *             --- Stage1Resource
 *             ---
 */

package profile;
import java.util.List;

import profile.ResourcesInfo.Res;

import java.util.ArrayList;

public class JobResource{
private String jobname;
private Long jobduration;
private Long jobstart;
private Long jobend;
private ResourcesInfo resinfo;
private List<Long> stagetimeline;
private List<StageResource> stageres;
public JobResource(){
	jobname=null;
	jobduration = null;
	jobstart = null;
	jobend = null;
	resinfo = new ResourcesInfo();
	stagetimeline= new ArrayList<Long>();
	stageres = new ArrayList<StageResource>();
}
public void constructJobResource(JobProfile jobprofile, ResourcesInfo resourceinfo){
	jobname=jobprofile.getAppName();
	jobstart=jobprofile.getStartTime();
	jobend=jobprofile.getEndTime();
	jobduration=jobprofile.getAppDuration();
	int startindex = 0, i = 0;
	if(jobstart<resourceinfo.getBegintime().getTime()) {
		System.out.println("Resource Log Incomplete...");
		return;
	}else if(jobend>resourceinfo.getResList()
			.get(resourceinfo.getResList().size()-1).getTime().getTime()){
		System.out.println("Resource Log error...");
		return;
	}
	startindex = (int) (jobstart/1000 - resourceinfo.getBegintime().getTime()/1000);
	StageResource startupres = new StageResource();
	startupres.setIsjobstage(false);
	startupres.setStageid(-1);
	startupres.setStagename("Startup");
	startupres.setStagebegintime(jobstart);
	startupres.setStageduration(jobprofile.getJobStartupTime());
	for(i=startindex;i<=startindex+jobprofile.getJobStartupTime()/1000;i++)
		startupres.getReslist().add(resourceinfo.getResList().get(i));
	stagetimeline.add(jobstart);
	stageres.add(startupres);
	for(JobProfile.StageProfile stage : jobprofile.getStageProfiles()){
		StageResource stager = new StageResource();
		stager.setIsjobstage(true);
		stager.setStageid(stage.getStageID());
		String stname = stage.getStageName().split("_")[0];
		stager.setStagename(stname);
		stager.setStagebegintime(stage.getSubmissionTime());
		stager.setStageduration(stage.getStageDuration());
		for(i=(int) (startindex + stage.getSubmissionTime()/1000 - jobstart/1000);
				i<=startindex + stage.getCompletionTime()/1000 - jobstart/1000;i++)
			stager.getReslist().add(resourceinfo.getResList().get(i));
		stagetimeline.add(stage.getSubmissionTime());
		stageres.add(stager);
	}
	StageResource cleanupres = new StageResource();
	cleanupres.setIsjobstage(false);
	cleanupres.setStageid(-2);
	cleanupres.setStagename("Cleanup");
	cleanupres.setStagebegintime(jobprofile.getStageProfiles()
			.get(jobprofile.getStageProfiles().size()-1).getCompletionTime());
	cleanupres.setStageduration(jobprofile.getJobCleanupTime());
	for(i=(int) ((jobprofile.getEndTime()-jobprofile.getJobCleanupTime())/1000-jobstart/1000+startindex);
			i<=jobprofile.getEndTime()/1000-jobstart/1000+startindex;i++)
		cleanupres.getReslist().add(resourceinfo.getResList().get(i));
	resinfo=resourceinfo;
	stagetimeline.add(jobprofile.getEndTime()-jobprofile.getJobCleanupTime());
	stagetimeline.add(jobprofile.getEndTime());
	stageres.add(cleanupres);
	for(StageResource sr : stageres){
		Res[] rrs = resinfo.calculateAvgMaxMin(sr.getReslist());
		sr.setAvgres(rrs[0]);
		sr.setMaxres(rrs[1]);
		sr.setMinres(rrs[2]);
	}
}
public String getJobname(){
	return jobname;
}
public void setJobname(String jobname){
	this.jobname=jobname;
}
public Long getJobduration(){
	return jobduration;
}
public void setJobduration(Long jobduration){
	this.jobduration=jobduration;
}
public Long getJobstart(){
	return jobstart;
}
public void setJobstart(Long jobstart){
	this.jobstart=jobstart;
}
public Long getJobend(){
	return jobend;
}
public void setJobend(Long jobend){
	this.jobend=jobend;
}
public ResourcesInfo getResinfo(){
	return resinfo;
}
public void setResinfo(ResourcesInfo resinfo){
	this.resinfo=resinfo;
}
public List<Long> getStagetimeline(){
	return stagetimeline;
}
public void setStagetimeline(List<Long> stagetimeline){
	this.stagetimeline=stagetimeline;
}
public List<StageResource> getStageres(){
	return stageres;
}
public void setStageres(List<StageResource> stageres){
	this.stageres=stageres;
}
public class StageResource implements Comparable<StageResource>{
private Boolean isjobstage;
private Integer stageid;
private String stagename;
private Long stagebegintime;
private Long stageduration;
private List<Res> reslist;
private Res avgres;
private Res maxres;
private Res minres;
public StageResource(){
	isjobstage=true;
	stageid=null;
	stagename=null;
	stagebegintime=null;
	stageduration=null;
	reslist=new ArrayList<Res>();
	avgres=(new ResourcesInfo()).new Res();
	maxres=null;
	minres=null;
}
public StageResource(Boolean isjobstage, Integer stageid, String stagename,
		Long stagebegintime, Long stageduration, Res avgres){
	this.isjobstage=isjobstage;
	this.stageid=stageid;
	this.stagename=stagename;
	this.stagebegintime=stagebegintime;
	this.stageduration=stageduration;
	this.avgres=avgres;
}
public int compareTo(StageResource stageres){
	return Long.compare(this.stagebegintime, stageres.getStagebegintime());
}
public Boolean getIsjobstage(){
	return isjobstage;
}
public void setIsjobstage(Boolean isjobstage){
	this.isjobstage=isjobstage;
}
public Integer getStageid() {
	return stageid;
}
public void setStageid(Integer stageid) {
	this.stageid = stageid;
}
public String getStagename() {
	return stagename;
}
public void setStagename(String stagename) {
	this.stagename = stagename;
}
public Long getStagebegintime() {
	return stagebegintime;
}
public void setStagebegintime(Long stagebegintime) {
	this.stagebegintime = stagebegintime;
}
public Long getStageduration() {
	return stageduration;
}
public void setStageduration(Long stageduration) {
	this.stageduration = stageduration;
}
public List<Res> getReslist(){
	return reslist;
}
public void setReslist(List<Res> reslist){
	this.reslist=reslist;
}
public Res getAvgres(){
	return avgres;
}
public void setAvgres(Res avgres){
	this.avgres=avgres;
}
public Res getMaxres(){
	return maxres;
}
public void setMaxres(Res maxres){
	this.maxres=maxres;
}
public Res getMinres(){
	return minres;
}
public void setMinres(Res minres){
	this.minres=minres;
}
}
}