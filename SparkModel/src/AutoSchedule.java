/*
 * Entrance to Job Scheduling
 * Update schedule file when new job is submitted
 * Run scheduled job
 */

import profile.JobProfile;
import profile.JobResource;
import predict.JobPredict;
import predict.JobPredict.Two;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;



public class AutoSchedule {
public static Long parseInputSize(String s){
	Long inputsize=0l;
	if(s.matches(".*(g|G).*"))
		inputsize = 1024*1024*1024*Long.valueOf(s.split("g|G")[0]);
	else if(s.matches(".*(m|M).*"))
		inputsize = 1024*1024*Long.valueOf(s.split("m|M")[0]);
	else if(s.matches(".*(k|K).*"))
		inputsize = 1024*Long.valueOf(s.split("k|K")[0]);
	else inputsize = Long.valueOf(s);
	return inputsize;
}
public static Integer nameTono(String jobname){
	Integer jobno = 0;
	if(jobname.matches(".*(p|P)(age)?(r|R)(ank)?.*"))
		jobno = 0;
	else if(jobname.matches(".*(k|K)(m|M)(eans)?.*"))
		jobno = 1;
	else if(jobname.matches(".*((h|H)dfs)?(l|L)(r|R).*"))
		jobno = 2;
	else if(jobname.matches(".*(w|W)(ord)?(c|C)(ount)?.*"))
		jobno = 3;
	return jobno;
}
public static void runSingleJob(String jobname,String jobinput,String runserver){
	String jobrun=null;
	String runjobname = null;
	String runjobconfig = null;
	String[] jobsname = {"SparkPageRank","SparkKMeans","SparkHdfsLR","WordCount"};
	StringBuffer runjobinput = new StringBuffer();
	// 0 pagerank->SparkPageRank; 1 kmeans->SparkKMeans; 2 hdfslr->SparkHdfsLR; 3 wordcount->WordCount;
	int runjobno = nameTono(jobname);
	long currentTime = System.currentTimeMillis();
	switch(runjobno){
		case 0: {runjobname = jobsname[runjobno];
		         runjobconfig = "output"+currentTime+" 3"; 
		         runjobinput.append("PR");} break;
		case 1: {runjobname = jobsname[runjobno];
		         runjobconfig = "10 5";
		         runjobinput.append("LR");} break;
		case 2: {runjobname = jobsname[runjobno];
		         runjobconfig = "10";
		         runjobinput.append("LR");} break;
		case 3: {runjobname = jobsname[runjobno];
		         runjobconfig = "output"+currentTime;
		         runjobinput.append("WC");} break;
		}
	//(PR|LR|WC)(1|2|3|4|5|20)g;
	//
	if(jobinput.matches("(1|2|3|4|5|10|15|20)(g|G)*."))
		runjobinput.append(jobinput.split("(g|G)")[0]+"g");
		//System.out.println(runjobstart+" "+runjobname+" "+runjobinput+" "+runjobconfig+" "+runserver);
	jobrun = " ./spark-1.0.2-ha1/bin/spark-submit --class "+runjobname+" ./spark-1.0.2-ha1/Test"
		    +runserver.charAt(runserver.length()-1)+".jar "+runjobinput+"/"
			+runjobinput+" "+runjobconfig +" >/dev/null 2>\\&1";
	String newcmds = "sed -i '5s%.*%"+jobrun+"%' runjob.sh";
	newcmds = "nohup ssh "+runserver + " \" " + newcmds + "; bash ./runjob.sh" +" \" " + " >/dev/null 2>&1 &";
	String[] cmd = {"/bin/sh","-c",newcmds};
	System.out.println("#"+cmd[2]);
	try{
	Process process = Runtime.getRuntime().exec(cmd);
	process.waitFor();
	}catch(IOException e){
		e.printStackTrace();
	}catch(InterruptedException ei){
		ei.printStackTrace();
	}
}
public static void main(String[] args){	
	if(args.length < 3){
		System.err.println("Usage: java -jar SparkModel.jar Jobname Inputsize Schedulefile");
		System.exit(0);
	}
	if(args[2].contains("xxx")){
	runSingleJob(args[0],args[1],args[2]);
	System.exit(0);
	}
	List<String> logfiles = new ArrayList<String>();
	List<String> resfiles = new ArrayList<String>();
	File dirk = new File("");
	if(!dirk.exists())
	{
		
	logfiles.add("logfile");
	}
	else{
	logfiles.add("anotherlogfile");
	}
	List<Long> fixstart = new ArrayList<Long>();
	String line = null;
	List<String> jobnames = new ArrayList<String>();
	List<String> inputsStr = new ArrayList<String>();
	List<Long> inputs = new ArrayList<Long>();
	List<Long> starts = new ArrayList<Long>();
	List<String> runningservers = new ArrayList<String>();
	List<Long> jobexetime = new ArrayList<Long>();
	List<Long> jobwaittime = new ArrayList<Long>();
	List<String> jobcmdstrs = new ArrayList<String>();
	List<JobResource> jobresources = new ArrayList<JobResource>();
	List<JobProfile> jobprofiles = new ArrayList<JobProfile>();	
	DateFormat startFormat = new SimpleDateFormat("HH:mm:ss-yyyy-MM-dd");
	String[] mastersname = {"",""};
	SortedSet<String> waitingserver = new TreeSet<String>();
	String runserver = null;
    waitingserver.addAll(Arrays.asList(mastersname));
    Long jobdely=0l;
	try{
		//time interval between each job submission: 10s
		//schedule file
		String schedulefile = args[2];
		ScheduleFile sf = new ScheduleFile(schedulefile);
		String jobname = args[0];
		String jobinput = args[1];
		Long inputsize = 1l;
		inputsize=parseInputSize(jobinput);
		Integer jobno = nameTono(jobname);
		ShowInfo sw = new ShowInfo();
		sw.onefile(logfiles.get(jobno), resfiles.get(jobno));
			
		Long currentTime = System.currentTimeMillis();
		//read schedule file content
		Thread ta = new Thread(new OperateSchedule(0,sf));
		ta.start();
		ta.join();
		boolean nullschedule = true;
		String filestring = sf.getFilestr();
		if(filestring != null){
		nullschedule = false;
		String[] filestrings = filestring.split("\n");
		System.out.println(filestrings.length);
		int stri=0;
		while(stri < filestrings.length && (!filestrings[stri].equals("")) && filestrings[stri].length()>0){
			line=filestrings[stri];
			String jname = line.split("\\s+")[0];
			ShowInfo swt = new ShowInfo();
			//generate jobprofile and jobresource for sample job
			swt.onefile(logfiles.get(nameTono(jname)),resfiles.get(nameTono(jname)));
			JobPredict jps = new JobPredict("",1l);
			Long onejobstarttime = Long.valueOf(line.split("\\s+")[4]);
			Long onejobinputsize = parseInputSize(line.split("\\s+")[1]);
			String inputstr = line.split("\\s+")[1];
			jobdely = currentTime - onejobstarttime;
			String servername = line.split("\\s+")[3];
			Long exetime = Long.valueOf(line.split("\\s+")[5]);
			Long waittime = Long.valueOf(line.split("\\s+")[6]);
			String cmdstrs = line.split("#")[1];

			if(exetime<jps.predictJob(0l,swt.getJobProfile(),onejobinputsize).getJobDuration())
			// Is job not finished ?
			{jobnames.add(jname);
			inputs.add(onejobinputsize);
			inputsStr.add(inputstr);
			starts.add(onejobstarttime);
			runningservers.add(servername);
			jobexetime.add(exetime);
			jobwaittime.add(waittime);
			jobcmdstrs.add(cmdstrs);
			fixstart.add(jobdely);
			jobresources.add(swt.getJobResource());
			jobprofiles.add(swt.getJobProfile());
			waitingserver.remove(servername);
			}
			stri++;
		}
		}
		if(waitingserver.size()>0){
		//there is available server
		Long interval = jobdely;
		System.out.println("interval:"+interval/1000.0);
		runserver=waitingserver.iterator().next();
		ShowInfo swc = new ShowInfo();
		swc.onefile(logfiles.get(jobno), resfiles.get(jobno));
		jobnames.add(jobname);
		inputs.add(inputsize);
		inputsStr.add(jobinput);
		starts.add(currentTime);
		runningservers.add(runserver);
		jobexetime.add(0l);
		jobwaittime.add(0l);
		jobcmdstrs.add("");
		fixstart.add(0l);
		jobresources.add(swc.getJobResource());
		jobprofiles.add(swc.getJobProfile());
		
		
		//prepare to run a new job
		JobPredict jp = new JobPredict("",1l);	
	    Two<Map<Integer,Long>,Map<Integer,Long>> jobexestart;
	    fixstart=null;
	    ///
	    //Entrance to time prediction for jobs interfered with others
	    synchronized(sf){
	    jobexestart=jp.schedulingJobsWithInterference(interval,jobexetime,jobwaittime,inputs.size(), null, jobprofiles, jobresources, inputs);
	    }
	   
		//calculate scheduling of these jobs	
		Long runjobstart = jp.getFinalStartSec().get(jp.getFinalStartSec().size()-1);
		String runjobname = null;
		String runjobconfig = null;
		String[] jobsname = {"SparkPageRank","SparkKMeans","SparkHdfsLR","WordCount"};
		StringBuffer runjobinput = new StringBuffer();
		// 0 pagerank->SparkPageRank; 1 kmeans->SparkKMeans; 2 hdfslr->SparkHdfsLR; 3 wordcount->WordCount;
		int runjobno = nameTono(jobname);
		currentTime = System.currentTimeMillis();
		switch(runjobno){
			case 0: {runjobname = jobsname[runjobno];
			         runjobconfig = "output"+currentTime+" 3"; 
			         runjobinput.append("PR");} break;
			case 1: {runjobname = jobsname[runjobno];
			         runjobconfig = "10 5";
			         runjobinput.append("LR");} break;
			case 2: {runjobname = jobsname[runjobno];
			         runjobconfig = "10";
			         runjobinput.append("LR");} break;
			case 3: {runjobname = jobsname[runjobno];
			         runjobconfig = "output"+currentTime;
			         runjobinput.append("WC");} break;
			}
		//(PR|LR|WC)(1|2|3|4|5|20)g;
		if(jobinput.matches("(1|2|3|4|5|10|15|20)(g|G)*.")){
			runjobinput.append(jobinput.split("(g|G)")[0]+"g");
			System.out.println(runjobstart+" "+runjobname+" "+runjobinput+" "+runjobconfig+" "+runserver);
		File runshell = new File("runjob.sh");
		BufferedWriter bwshell = new BufferedWriter(new FileWriter(runshell));
		bwshell.write("#! /bin/bash\n");
		bwshell.write("nohup ssh "+runserver+" \" ./spark-1.0.2-ha1/bin/spark-submit --class "
				+runjobname+" ./spark-1.0.2-ha1/Test"+runserver.charAt(runserver.length()-1)+".jar "+runjobinput+"/"+runjobinput+" "+runjobconfig 
				+"\" >/dev/null 2>&1 &");
		bwshell.close();
		String cmdString = "nohup ssh "+runserver+" \" ./spark-1.0.2-ha1/bin/spark-submit --class "
				+runjobname+" ./spark-1.0.2-ha1/Test"+runserver.charAt(runserver.length()-1)+".jar "+runjobinput+"/"+runjobinput+" "+runjobconfig 
				+"\" >/dev/null 2>&1 &";
		
		currentTime = System.currentTimeMillis();
		starts.remove(starts.size()-1);
		starts.add(currentTime);
		jobcmdstrs.remove(jobcmdstrs.size()-1);
		jobcmdstrs.add(cmdString);
		//update job info in scheduling file and write this new job to scheduling file
		StringBuffer writesb = new StringBuffer("");
		Long baseTime = currentTime;
		for(int i=0;i<jobnames.size();i++){
			writesb.append(jobnames.get(i)+"  "+inputsStr.get(i)+"  "
		               +startFormat.format(starts.get(i))+"  "+runningservers.get(i)
		               +"  "+ starts.get(i) +"  "+jobexestart.getExetime().get(i)
		               +"  "+jobexestart.getWaittime().get(i)
		               +"  "+baseTime+"  "+"#"+jobcmdstrs.get(i)+"\n");
		}
	    String writestr = new String(writesb);	
		
		Thread tw = new Thread(new OperateSchedule(2,sf,writestr));
		tw.start();
		tw.join();
		
		int threadi=0;
		//TimeUnit.MILLISECONDS.sleep(5000);
		// read schedule file to run the job
		if(nullschedule==true)
		while(true){
			Thread ti = new Thread(new OperateSchedule(1,sf,""));
			ti.start();
			TimeUnit.MILLISECONDS.sleep(1000);
			ti.join();
			System.out.println("threadi:"+threadi);
			threadi++;
		}
		}else System.out.println("Specified input not exist...");
		}else System.out.println("No available server...");
	
    //time interval between each job submission
	}catch(IOException e){
		e.printStackTrace();
	}catch(InterruptedException e1){
		e1.printStackTrace();
	}
}
}