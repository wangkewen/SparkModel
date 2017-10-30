/*
 * Schedule File class used to read and write jobs scheduling information.
 * File structure: job_name input_size start_time server long(start_time) executed_time wait_time submitted_time running_script
 * An example file:
 * "
 * wc  20G  09:29:15-2016-11-23  server1  1479911355891  0  0  1479911355891 #nohup ssh server1 "runcomd" >/dev/null 2>&1 &
 * "
 */
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ScheduleFile implements Serializable {
	private static final long serialVersionUID = 20161028001L;
	private String Filename;
	private File sfile;
	private String filestr = null;
	public ScheduleFile(String Filename){
		this.Filename = Filename;
		this.sfile = new File(Filename);
	}
	@Override
	public String toString(){
		return "File:"+Filename;
	}
	public synchronized String getFilestr(){
		return filestr;
	}
	public synchronized void init(String cmdstr){
		//-1 not used anymore
		Process pros;
		String[] cmdStrings = {"/bin/sh","-c",cmdstr};
		DateFormat df = new SimpleDateFormat("hh:mm:ss MM-dd-yyyy");
		try {
			pros = Runtime.getRuntime().exec(cmdStrings);
			System.out.println("#"+cmdStrings[2]);
			pros.waitFor();
			TimeUnit.MILLISECONDS.sleep(6000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(df.format(new Date(System.currentTimeMillis())));
	}
	public synchronized void readFileString(){
		//0
		// read schedule file content to filestr
		StringBuffer strb =  new StringBuffer("");
		if(sfile.exists()){
			try{
			BufferedReader br = new BufferedReader(new FileReader(sfile));
			String line = null;
			while((line=br.readLine()) != null){
				strb.append(line+"\n");
			}
			//TimeUnit.MILLISECONDS.sleep(1000);
			filestr=new String(strb);
			br.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}else System.out.println("File not found...");
	}
	public synchronized void writeFileString(String wfilestr){
		//2
		// write new schedule information into schedule file
		RandomAccessFile rf = null;
		FileChannel fc = null;
		FileLock flock = null;
		try{
		    rf = new RandomAccessFile(sfile,"rw");
		    fc = rf.getChannel();
		    flock = fc.lock();
		    BufferedWriter sbw = new BufferedWriter(new FileWriter(sfile));
		    sbw.write(wfilestr);
            sbw.flush();
            sbw.close();
            System.out.println("Write done...");
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
			if(flock != null){
			flock.release();
			flock = null;
			}
			if(fc != null){
				fc.close();
				fc = null;
			}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		notifyAll();
	}
	public synchronized void readFile(){
		//1
		//run scheduled job using script written in schedule file
		long currenttime = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("hh:mm:ss MM-dd-yyyy");
		System.out.println("currentTime:"+currenttime);
		try{
			while(!sfile.exists()) wait();
			BufferedReader sbr = new BufferedReader(new FileReader(sfile));
			String line = null;
			while((line = sbr.readLine()) != null){
				long startpoint = Long.valueOf(line.split("\\s+")[6]) 
						+ Long.valueOf(line.split("\\s+")[7]);
				long baseT = Long.valueOf(line.split("\\s+")[7]);
				long exetime = Long.valueOf(line.split("\\s+")[5]);
				System.out.println("baseTime   :"+ baseT);
				//reach the scheduled time of a job, run this job
				if(exetime == 0l && currenttime >= startpoint && (currenttime - startpoint) < 1000){
					String cmdString = line.split("#")[1];
					String[] cmdStringsplit = cmdString.split("\"");
					String newcmd = cmdStringsplit[1] 
							+ cmdStringsplit[2].substring(0,cmdStringsplit[2].indexOf("&"));
					newcmd = newcmd +"\\&1";
					Process pros;
					String newcmds = "sed -i '5s%.*%"+newcmd+"%' runjob.sh";
					newcmds = cmdStringsplit[0] + " \" " + newcmds + "; bash ./runjob.sh" +" \" " + cmdStringsplit[2];
					String[] cmdStrings = {"/bin/sh","-c",newcmds};
					pros = Runtime.getRuntime().exec(cmdStrings);
					System.out.println("#"+cmdStrings[2]);
					pros.waitFor();
					// unpause this server to start the job, but wait 16~27s to actually launch this job after unpause
					System.out.println(df.format(System.currentTimeMillis()));
					System.out.println("shiftT:"+(currenttime - startpoint));
				}
			}
			sbr.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			catch(InterruptedException e1){
				e1.printStackTrace();
			}
	}
	
	public static void createNewFile(String filename){ 
		//for test only
		BufferedWriter bw = null;
		try{
			File f = new File(filename);
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(System.currentTimeMillis()+"\n");
            bw.flush();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
			bw.close();
			}catch(IOException e1){
				e1.printStackTrace();
			}
		}
	}
	public static void main(String[] args){
		String filename = "testfile.txt";
		//createNewFile(filename);
		long begintime = System.currentTimeMillis();
		ScheduleFile sf = new ScheduleFile(filename);
		sf.readFile();
		System.out.println("Time:"+(System.currentTimeMillis()-begintime));
	}
}
