package predict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import predict.ForcastJobProfile.ForcastStageProfile;
import predict.ForcastJobProfile.ForcastStageProfile.ForcastTaskProfile;
import profile.JobProfile;
import profile.JobResource;
import profile.JobProfile.StageProfile;
import profile.JobProfile.StageProfile.TaskProfile;
import profile.JobResource.StageResource;
import profile.ResourcesInfo.Res;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

import analyze.LogAnalyze;

public class JobPredict {
	private String Logfile = null;
	private Long InputSize = null;
	private ForcastJobProfile ForcastJobProfile=null;
	private JobProfile JobProfile=null;
	private List<Long> finalStartSec;
	
public JobPredict(String Logfile, Long InputSize){
	this.Logfile=Logfile;
	this.InputSize=InputSize;
	this.finalStartSec = new ArrayList<Long>();
}
public List<Long> getFinalStartSec(){
	return finalStartSec;
}
public void setFinalStartSec(List<Long> finalStartSec){
	this.finalStartSec=finalStartSec;
}
private void setJobProfile(){
	LogAnalyze la = new LogAnalyze(Logfile);
	JobProfile = la.generate();
}
public ForcastJobProfile predictJob(Long starttimediff,JobProfile jobProfile, Long InputSize){
	    // Predict Job without interference
	    // starttimediff = newjobsubmittime - samplejobstarttime
	    if(jobProfile==null) setJobProfile();
	    else JobProfile=jobProfile;
		ForcastJobProfile = new ForcastJobProfile();
		ForcastJobProfile.getConfiguration().init(InputSize);
		ForcastJobProfile.setJobName(JobProfile.getAppName());
		ForcastJobProfile.setStartTime(JobProfile.getStartTime()+starttimediff);
		if(JobProfile.getStageProfiles().get(0).getTaskProfiles()!=null)
		ForcastJobProfile.setSampleBlockNum(JobProfile.getStageProfiles().get(0).getTaskProfiles().size());
		ForcastJobProfile.setJobStartupTime(JobProfile.getJobStartupTime());
		ForcastJobProfile.setJobCleanupTime(JobProfile.getJobCleanupTime());
		for(int k=0;k<JobProfile.getStageProfiles().size();k++){
			ForcastStageProfile stageProfile = ForcastJobProfile.new ForcastStageProfile();
			stageProfile.setStageID(JobProfile.getStageProfiles().get(k).getStageID());
			stageProfile.setStageName(JobProfile.getStageProfiles().get(k).getStageName());
			stageProfile.setStageStartupTime(JobProfile.getStageProfiles().get(k).getStageStartupTime());
			stageProfile.setStageCleanupTime(JobProfile.getStageProfiles().get(k).getStageCleanupTime());
			for(String key : JobProfile.getStageProfiles().get(k).getAvgTaskProfiles().keySet()){
				ForcastTaskProfile taskProfile = stageProfile.new ForcastTaskProfile();
				taskProfile.setTaskType(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getTaskType());
				taskProfile.setHost(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getHost());
				taskProfile.setDeserializeTime(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getDeserializeTime());
				taskProfile.setRunTime(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getRunTime());
				taskProfile.setTimeRatio(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getTimeRatio());
				taskProfile.setSerializeTime(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getSerializeTime());
				taskProfile.setCleanupTime(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getCleanupTime());
				taskProfile.computeTaskDuration();
				taskProfile.setShuffleReadBytes(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getShuffleReadBytes());
				taskProfile.setShuffleWriteBytes(JobProfile.getStageProfiles()
						.get(k).getAvgTaskProfiles().get(key).getShuffleWriteBytes());
				stageProfile.getForcastTaskProfiles().put(key, taskProfile);
			}
			stageProfile.computeStage();
			ForcastJobProfile.getForcastStageProfiles().add(stageProfile);
		}
		    ForcastJobProfile.computeJob();
		    ForcastJobProfile.setEndTime(ForcastJobProfile.getStartTime()+ForcastJobProfile.getJobDuration());
		    return ForcastJobProfile;
}
public void calculateStartTime(int level,int parallelism, int startN, Map<Integer,List<Long>> intervals, List<List<Long>> schedulestarts, List<Long> tmpstart){
	// calculate different starting time for each job and save in "schedulestarts", "intervals" contains starting time collection for each job 
	for(Long startInterval : intervals.get(level)){
		tmpstart.add(startInterval);
		if(level < parallelism - 1) calculateStartTime(level+1, parallelism, startN,intervals,schedulestarts,tmpstart);
		else { List<Long> tps = new ArrayList<Long>();
			   tps.addAll(tmpstart);
			   schedulestarts.add(tps);
		}
		tmpstart.remove(startInterval);
	}
}
public int quicksort(long[] jobtimepoint,long[] jobtimeids,int low,int high){
	int start = low, end = high;
	long pivot = jobtimepoint[low];
	long pivotid = jobtimeids[low];
	while(low < high){
		while(low < high && jobtimepoint[high] >= pivot) high--;
		if(low<high) { 
			jobtimepoint[low]=jobtimepoint[high]; jobtimeids[low]=jobtimepoint[high];
		}
		while(low < high && jobtimepoint[low] <= pivot) low++;
		if(low<high) {
			jobtimepoint[high]=jobtimepoint[low]; jobtimeids[high]=jobtimepoint[low];
		}
	}
	jobtimepoint[low] = pivot;
	jobtimeids[low] = pivotid;
	if(low-1>start) quicksort(jobtimepoint,jobtimeids,start,low-1);
	if(low+1<end) quicksort(jobtimepoint,jobtimeids,low+1,end);
	return low;
}
public List<JobProfile> predictJobWithInterference(Integer parallelism,List<Long> fixstart, List<JobProfile> samplejobprofiles,List<JobResource> samplejobresources,List<Long> inputsizes){
	// call function predictJobExecution to predict job execution with interference
	// Starting times of each job are stored in fixstart<Long>
	List<JobProfile> jobprofiles = new ArrayList<JobProfile>();
	List<ForcastJobProfile> forcastjobprofiles = new ArrayList<ForcastJobProfile>();
	List<JobResource> jobresources = new ArrayList<JobResource>();
	int indexf=0;
	for(JobProfile sample : samplejobprofiles){
		//in this function, starting time shift is not considered, so use starttimediff=0l
		forcastjobprofiles.add(this.predictJob(0l,sample,inputsizes.get(indexf)));
		//predict execution time (Unit: 1ms = 0.001s) for single job without interference
		indexf++;
	}
	int resindex=0;
	for(ForcastJobProfile forcastjob : forcastjobprofiles){
		//transfer ForcastJobProfile into JobProfile
		Integer sampleBlockNum = samplejobprofiles.get(resindex)
				.getStageProfiles().get(0).getTaskProfiles().size();
		Integer forcastBlockNum = forcastjob.getConfiguration().getBlockNum();
		JobProfile jobprofile = new JobProfile();
		jobprofile.setAppName(forcastjob.getJobName());
		jobprofile.setAppDuration(forcastjob.getJobDuration());
		jobprofile.setStartTime(forcastjob.getStartTime());
		jobprofile.setEndTime(forcastjob.getEndTime());
		jobprofile.setJobStartupTime(forcastjob.getJobStartupTime());
		
		jobprofile.setJobCleanupTime(forcastjob.getJobCleanupTime());
		jobprofile.setConfiguration(forcastjob.getConfiguration());
		JobResource jobresource = new JobResource();
		jobresource.setJobname(samplejobresources.get(resindex).getJobname());
		jobresource.setJobduration(forcastjob.getJobDuration());
		jobresource.setJobstart(forcastjob.getStartTime());
		jobresource.setJobend(forcastjob.getEndTime());
		jobresource.getStagetimeline().add(forcastjob.getStartTime());
		List<Long> sampletimeline = new ArrayList<Long>();
		sampletimeline.add(forcastjob.getStartTime());
	    sampletimeline.add(forcastjob.getStartTime()+samplejobprofiles.get(resindex).getJobStartupTime());
	    for(StageProfile sp : samplejobprofiles.get(resindex).getStageProfiles())
	    	sampletimeline.add(sampletimeline.get(sampletimeline.size()-1)+sp.getStageDuration());
	    sampletimeline.add(samplejobprofiles.get(resindex).getEndTime());
		Long timepoint=0l;
		Boolean isFirst = true;
		for(ForcastStageProfile forcastStage : forcastjob.getForcastStageProfiles()){
			StageProfile stageprofile = jobprofile.new StageProfile();
			if(isFirst == true) {timepoint = forcastjob.getStartTime()+forcastjob.getJobStartupTime();isFirst = false;}
			jobresource.getStagetimeline().add(timepoint);
			stageprofile.setStageID(forcastStage.getStageID());
			stageprofile.setStageName(forcastStage.getStageName());
			stageprofile.setStageDuration(forcastStage.getStageDuration());
			stageprofile.setStageStartupTime(forcastStage.getStageStartupTime());
			stageprofile.setStageCleanupTime(forcastStage.getStageCleanupTime());
			stageprofile.setShuffleReadBytes(forcastStage.getShuffleReadBytes());
			stageprofile.setShuffleWriteBytes(forcastStage.getShuffleWriteBytes());
			stageprofile.setSubmissionTime(timepoint);
			stageprofile.setCompletionTime(timepoint+forcastStage.getStageDuration());
			timepoint += forcastStage.getStageDuration();
			for(String key : forcastStage.getForcastTaskProfiles().keySet()){
				TaskProfile taskprofile = stageprofile.new TaskProfile();
				taskprofile.setHost(forcastStage.getForcastTaskProfiles().get(key).getHost());
				taskprofile.setTaskType(forcastStage.getForcastTaskProfiles().get(key).getTaskType());
				taskprofile.setTaskDuration(forcastStage.getForcastTaskProfiles().get(key).getTaskDuration());
				taskprofile.setRunTime(forcastStage.getForcastTaskProfiles().get(key).getRunTime());
				taskprofile.setTimeRatio(forcastStage.getForcastTaskProfiles().get(key).getTimeRatio());
				taskprofile.setLateDuration(forcastStage.getForcastTaskProfiles().get(key).getLateDuration());
				taskprofile.setDeserializeTime(forcastStage.getForcastTaskProfiles().get(key).getDeserializeTime());
				taskprofile.setSerializeTime(forcastStage.getForcastTaskProfiles().get(key).getSerializeTime());
				taskprofile.setCleanupTime(forcastStage.getForcastTaskProfiles().get(key).getCleanupTime());
				taskprofile.setShuffleReadBytes(forcastStage.getForcastTaskProfiles().get(key).getShuffleReadBytes());
				taskprofile.setShuffleWriteBytes(forcastStage.getForcastTaskProfiles().get(key).getShuffleWriteBytes());
				stageprofile.getAvgTaskProfiles().put(key, taskprofile);
			}
			jobprofile.getStageProfiles().add(stageprofile);
		}
		jobresource.getStagetimeline().add(timepoint);
		jobresource.getStagetimeline().add(forcastjob.getEndTime());
		int timelineIndex = 0;
		Double dataratio=1.0;
		for(StageResource sr : samplejobresources.get(resindex).getStageres()){
			Double timeratio=1.0;
			StageResource stageres = jobresource.new StageResource();
			stageres.setIsjobstage(sr.getIsjobstage());
			stageres.setStageid(sr.getStageid());
			stageres.setStagename(sr.getStagename());
			dataratio = 1.0 * forcastBlockNum / sampleBlockNum;
			stageres.setStagebegintime(jobresource.getStagetimeline().get(timelineIndex));
			stageres.setStageduration(jobresource.getStagetimeline().get(timelineIndex+1)
					-jobresource.getStagetimeline().get(timelineIndex));
			Long realtime = jobresource.getStagetimeline().get(timelineIndex+1)
			           - jobresource.getStagetimeline().get(timelineIndex);
			Long sampletime = sampletimeline.get(timelineIndex+1)
			           - sampletimeline.get(timelineIndex);
			if(realtime<1) timeratio = 1.0;
			else if(sampletime<1) timeratio = realtime/1.0;
			else timeratio = 1.0 * realtime / sampletime;
			//calculate avgres
			stageres.getAvgres().setCpu_sys(sr.getAvgres().getCpu_sys());
			stageres.getAvgres().setCpu_idl(sr.getAvgres().getCpu_idl());
			stageres.getAvgres().setCpu_wai(sr.getAvgres().getCpu_wai());
			stageres.getAvgres().setSys_int(sr.getAvgres().getSys_int());
			stageres.getAvgres().setSys_csw(sr.getAvgres().getSys_csw());
			stageres.getAvgres().setDisk_read(sr.getAvgres().getDisk_read()*dataratio/timeratio);
			stageres.getAvgres().setDisk_writ(sr.getAvgres().getDisk_writ()*dataratio/timeratio);
			stageres.getAvgres().setNet_recv(sr.getAvgres().getNet_recv()*dataratio/timeratio);
			stageres.getAvgres().setNet_send(sr.getAvgres().getNet_send()*dataratio/timeratio);		
		    timelineIndex++; 	
			jobresource.getStageres().add(stageres);
		}	
		jobprofiles.add(jobprofile);
		jobresources.add(jobresource);
		resindex++;
	}
	//print out predicted job execution information without interference
	for(JobProfile job : jobprofiles){
		
		System.out.println("*****************************");
		System.out.println("JobName: "+job.getAppName()+ "  Duration(s): "+job.getAppDuration()/1000.0+"s");
		job.setJobStartupTime(job.getJobStartupTime());
		System.out.print("Stage:         ");
		for(StageProfile sr : job.getStageProfiles())
		     System.out.printf("%12s ",sr.getStageName());	
		System.out.println();
		System.out.print("Stage ID:      ");
		for(StageProfile s : job.getStageProfiles())
			System.out.printf("%12s ",s.getStageID());
		System.out.println();
		System.out.println("Time(s):       ");
		for(StageProfile s : job.getStageProfiles())
			System.out.printf("%12s \n",s.getStageDuration()/1000.0);
		System.out.println();
		System.out.println("*****************************");

	}
	//add the fixstart<Long> as the starting time for different jobs
	int jm=0;
	for(JobProfile jp : jobprofiles){
		jp.setJobStartupTime(jp.getJobStartupTime()+fixstart.get(jm));
		jp.setEndTime(jp.getEndTime()+fixstart.get(jm));
		for(StageProfile sp : jp.getStageProfiles()){
			sp.setSubmissionTime(sp.getSubmissionTime()+fixstart.get(jm));
			sp.setCompletionTime(sp.getCompletionTime()+fixstart.get(jm));
		}
		jm++;
	}
	jm=0;
	for(JobResource jr : jobresources){
		jr.setJobend(jr.getJobend()+fixstart.get(jm));
		List<Long> newtimeline = new ArrayList<Long>();
		for(int t=1;t<jr.getStagetimeline().size();t++)
			newtimeline.add(jr.getStagetimeline().get(t)+fixstart.get(jm));
		jr.setStagetimeline(newtimeline);
		for(StageResource sr : jr.getStageres())
			if(sr.getStagename().equals("Startup"))
				sr.setStageduration(sr.getStageduration()+fixstart.get(jm));
			else
				sr.setStagebegintime(sr.getStagebegintime()+fixstart.get(jm));
	    jm++;
	}
	Long maxjobtime = 0l;
	//find out the longest job execution time when starting at the same time
	jm=0;
	for(JobProfile jp : this.predictJobExecution(jobresources, jobprofiles)){
		if(jp.getAppDuration()>maxjobtime) maxjobtime = jp.getAppDuration();
	
	//print out predicted job execution information with interference
	System.out.println("##############################");
	System.out.println("JobName: "+jp.getAppName()+ "  Duration(s): "+jp.getAppDuration()/1000.0+"s");
	System.out.print("Stage:         ");
	for(StageProfile sr : jp.getStageProfiles())
	     System.out.printf("%12s ",sr.getStageName());	
	System.out.println();
	System.out.print("Stage ID:      ");
	for(StageProfile s : jp.getStageProfiles())
		System.out.printf("%12s ",s.getStageID());
	System.out.println();
	System.out.println("Time(s):       ");
	int jms=0;
	for(StageProfile s : jp.getStageProfiles()){
		if(jms==0)
			System.out.printf("%12s \n",(s.getStageDuration()-fixstart.get(jm))/1000.0);
		else
		System.out.printf("%12s \n",s.getStageDuration()/1000.0);
		jms++;
		}
	System.out.println();
	System.out.println("##############################");
	jm++;
	}
	return jobprofiles;
}
public List<JobProfile> predictJobExecution(List<JobResource> jobresources,List<JobProfile> jobprofileslist){
	//calculate execution time for each job when interfered with each other
	List<JobResource> joblists = new ArrayList<JobResource>();
	List<JobProfile> jobprofiles = new ArrayList<JobProfile>();
	Map<Integer,JobResource> jobs = new HashMap<Integer,JobResource>();
	List<phase> phaselist = new ArrayList<phase>();
	int i = 0;
	for(i=0;i<jobresources.size();i++){
		joblists.add(new JobResource());
		jobprofiles.add(new JobProfile());
		jobs.put(i, jobresources.get(i));
		//init phaselist
		StageResource sr = jobresources.get(i).getStageres().get(0);
		phaselist.add(new phase(i,false,0,sr.getStageid(),sr.getStagename(),
				false,0.0,1.0,sr.getStagebegintime(),sr.getStageduration(),sr));
	}
	phase shortp = null;
	List<phase> otherphases;
	int stageSeqNo = 0;
	// analyze each stage of jobs
	while(phaselist.size()>0){
		Double min = Double.MAX_VALUE;
		otherphases = new ArrayList<phase>();
		otherphases.addAll(phaselist);
		// find the shortest phase prolonged
		for(phase ps : phaselist){
			otherphases.remove(ps);
			//calculate the phase time when interfered with others
		    Double ptime = ps.getPhaseduration()/ps.calculateSlowdownratio(otherphases);
		    if(ptime<min) {
		    	min = ptime;
		    	shortp = ps;
		    	stageSeqNo = shortp.getStageSeqNo();
		    }
		    otherphases.add(ps);
		 }
	    Double phduration = 0.0;
	    Integer jid=0;
	    List<phase> delphs = new ArrayList<phase>();
		for(phase ph : phaselist){
			if(ph.getPhaseduration()/ph.getSlowdownratio() != min){
				//update phase of non-shortest job
			    ph.setIsPartialStage(true);
			    ph.setPartialduration(ph.getPartialduration()+min);
			    ph.setPhaseduration(ph.getPhaseduration()-min*ph.getSlowdownratio());
		    }else{ //update following phase for shortest job 
		    	jid = ph.getJobid();
		    	stageSeqNo = ph.getStageSeqNo();
		    	if(ph.getIsPartialStage() != true)
	    			phduration = min;
	    		else phduration = ph.getPartialduration() + min;
		    	if(ph.getIsjobstage() == false && ph.getStagename().equals("Cleanup")){
		    		jobprofiles.get(jid).setJobCleanupTime(phduration.longValue());
		    		jobprofiles.get(jid).setEndTime(ph.getPhasebegintime()+phduration.longValue());
		    		jobprofiles.get(jid).setAppDuration(jobprofiles.get(jid).getEndTime()
		    				-jobprofiles.get(jid).getStartTime());
		    		delphs.add(ph);
		    	}else{
		    		if(ph.getIsjobstage() == false && ph.getStagename().equals("Startup")){
		    			  jobprofiles.get(jid).setAppName(jobs.get(ph.getJobid()).getJobname());
				    	  jobprofiles.get(jid).setStartTime(jobs.get(ph.getJobid()).getJobstart());
			    	      jobprofiles.get(jid).setJobStartupTime(phduration.longValue());
			    	      }
		    		else{
		    		StageProfile sp = jobprofiles.get(jid).new StageProfile();
		    		sp.setStageID(ph.getStageid());
		    		sp.setStageName(ph.getStagename());
		    		sp.setStageDuration(phduration.longValue());
		    		sp.setSubmissionTime(ph.getPhasebegintime());
		    		sp.setCompletionTime(ph.getPhasebegintime() + phduration.longValue());
		    		Map<String,TaskProfile> avgtaskprofiles = jobprofileslist.get(jid)
		    				.getStageProfiles().get(stageSeqNo-1).getAvgTaskProfiles();
		    		Map<String,TaskProfile> avgprofile = new HashMap<String,TaskProfile>();
		    		Double stagetimeRatio = phduration / jobprofileslist.get(jid)
		    				.getStageProfiles().get(stageSeqNo-1).getStageDuration();
		    		for(String host : avgtaskprofiles.keySet()){
		    			TaskProfile tp = sp.new TaskProfile();
		    			tp.setTaskType(avgtaskprofiles.get(host).getTaskType());
		    			tp.setHost(host);
		    			tp.setDeserializeTime((long)(avgtaskprofiles.get(host).getDeserializeTime()
		    					*stagetimeRatio));
		    			tp.setRunTime((long) (avgtaskprofiles.get(host).getRunTime()*stagetimeRatio));
		    			tp.setTimeRatio(avgtaskprofiles.get(host).getTimeRatio());
		    			tp.setSerializeTime((long) (avgtaskprofiles.get(host).getSerializeTime()
		    					*stagetimeRatio));
		    			tp.setCleanupTime((long) (avgtaskprofiles.get(host).getCleanupTime()
		    					*stagetimeRatio));
		    			tp.setShuffleReadBytes(1l);
		    			tp.setShuffleWriteBytes(1l);
		    			avgprofile.put(host, tp);
		    		}
		    		sp.setAvgTaskProfiles(avgprofile);
		    		jobprofiles.get(jid).getStageProfiles().add(sp);
		    		}
		    		if(ph.getIsPartialStage() != true)
				         ph.setPhasebegintime((long)(ph.getPhasebegintime()+phduration));
			        else{
			        	 ph.setPhasebegintime((long) (ph.getPhasebegintime()+phduration));
				         ph.setIsPartialStage(false); 
			             }
		    	    ph.setIsjobstage(jobs.get(jid).getStageres().get(stageSeqNo+1).getIsjobstage());
		            ph.setStageSeqNo(stageSeqNo+1);
		            ph.setStageid(jobs.get(jid).getStageres().get(stageSeqNo+1).getStageid());
		            ph.setStagename(jobs.get(jid).getStageres().get(stageSeqNo+1).getStagename());
		            ph.setPhaseduration((double)jobs.get(jid).getStageres()
		        		        .get(stageSeqNo+1).getStageduration());
		            ph.setStageres(jobs.get(jid).getStageres().get(stageSeqNo+1));
		            ph.setPartialduration(0.0);
		    	}
		     }
		}
		for(phase delph : delphs)
			phaselist.remove(delph);
		}
	return jobprofiles;
}
public class phase {
	// phase is used to specify the time slice when interference is calculated
	private Integer jobid;
	private Boolean isjobstage;
	private Integer stageSeqNo;
	private Integer stageid;
	private String stagename;
	private Boolean isPartialStage;
	private Double partialduration;
	private Double slowdownratio;
	private Long phasebegintime;
	private Double phaseduration;
	private StageResource stageres;
	public phase(){
		
	}
	public phase(Integer jobid,Boolean isjobstage, Integer stageSeqNo,Integer stageid,String stagename,
			Boolean isPartialStage,Double partialduration, Double slowdownratio, Long phasebegintime, 
			Long phaseduration, StageResource stageres){
		this.jobid=jobid;
		this.isjobstage=isjobstage;
		this.stageSeqNo=stageSeqNo;
		this.stageid=stageid;
		this.stagename=stagename;
		this.isPartialStage=isPartialStage;
		this.partialduration=partialduration;
		this.slowdownratio=slowdownratio;
		this.phasebegintime=phasebegintime;
		this.phaseduration=(double) phaseduration;
		this.stageres=stageres;
	}
	public Double calculateSlowdownratio(List<phase> phases){
		Double BasicDiskread = 13815.0*1024;
		// Disk Read Rate of first stage for single Sample Job
		Double[] BasicRatio = {1.302436502,1.82757729,2.538213998};
		// Slowdown ratio of simulate job for 2,3,4 jobs running in parallel
		Double ratio =1.0;
	    Double slowdownratio=1.0;
	    Double thisDiskread = 0.0;
	    Integer parallelism = 0;
	    this.slowdownratio=slowdownratio;
	    if(phases==null || phases.size()==0) return 1.0;
	    if(this.isjobstage==false) return 1.0;
        if(this.stageSeqNo == 1){
        	for(phase ph: phases)
        		if(ph.getIsjobstage()==true && ph.getStageSeqNo() ==1)
        			parallelism++;
        	
            if(parallelism > 0){
        		    thisDiskread = this.getStageres().getAvgres().getDisk_read();
        		    ratio = Math.floor(BasicRatio[parallelism-1])+(thisDiskread/BasicDiskread)*(BasicRatio[parallelism-1]-Math.floor(BasicRatio[parallelism-1]));
        		    slowdownratio = 1.0/ratio;
        	}
           else slowdownratio = 1.0;
            
        }
        this.slowdownratio=slowdownratio;
		return slowdownratio;
	}
	public Integer getJobid() {
		return jobid;
	}
	public void setJobid(Integer jobid) {
		this.jobid = jobid;
	}
	public Boolean getIsjobstage(){
		return isjobstage;
	}
	public void setIsjobstage(Boolean isjobstage){
		this.isjobstage=isjobstage;
	}
	public String getStagename(){
		return stagename;
	}
	public void setStagename(String stagename){
		this.stagename=stagename;
	}
	public Integer getStageSeqNo() {
		return stageSeqNo;
	}
	public void setStageSeqNo(Integer stageSeqNo) {
		this.stageSeqNo = stageSeqNo;
	}
	public Integer getStageid() {
		return stageid;
	}
	public void setStageid(Integer stageid) {
		this.stageid = stageid;
	}
	public Boolean getIsPartialStage() {
		return isPartialStage;
	}
	public void setIsPartialStage(Boolean isPartialStage) {
		this.isPartialStage = isPartialStage;
	}
	public Double getPartialduration() {
		return partialduration;
	}
	public void setPartialduration(Double partialduration) {
		this.partialduration = partialduration;
	}
	public Double getSlowdownratio() {
		return slowdownratio;
	}
	public void setSlowdownratio(Double slowdownratio) {
		this.slowdownratio = slowdownratio;
	}
	public Long getPhasebegintime() {
		return phasebegintime;
	}
	public void setPhasebegintime(Long phasebegintime) {
		this.phasebegintime = phasebegintime;
	}
	public Double getPhaseduration() {
		return phaseduration;
	}
	public void setPhaseduration(Double phaseduration) {
		this.phaseduration = phaseduration;
	}
	public StageResource getStageres() {
		return stageres;
	}
	public void setStageres(StageResource stageres) {
		this.stageres = stageres;
	}
}
public static void main(String[] args){
	
}
}
