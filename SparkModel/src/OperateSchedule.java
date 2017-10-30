/*
 * Class used to operate schedule file
 * 
 */

import java.util.concurrent.TimeUnit;

public class OperateSchedule implements Runnable{
	
	private int optype;
	private ScheduleFile sf;
	private String wfilestr;
	private String cmdstr;
	public OperateSchedule(int optype,ScheduleFile sf){
		this.optype = optype;
		this.sf = sf;
		this.wfilestr = "";
	}
	public OperateSchedule(int optype,ScheduleFile sf,String wfilestr){
		this.optype = optype;
		this.sf = sf;
		this.wfilestr = wfilestr;
	}
	public OperateSchedule(int optype,ScheduleFile sf,String wfilestr,String cmdstr){
		this.optype = optype;
		this.sf = sf;
		this.wfilestr = wfilestr;
		this.cmdstr= cmdstr;
	}
	public void run(){
		if(!Thread.interrupted()){
			switch(optype){
			case -1:  sf.init(cmdstr);
			         break;
			case 0:  sf.readFileString();
			         break;
			case 1:  sf.readFile(); 
			         break;
			case 2:  sf.writeFileString(wfilestr); 
			         break;
			default: System.out.println("No operation..."); 
			         break;
			}	
		}
	}
	public static void main(String[] args){
		//It is for test only.
		String filename = "scfile";
		//ScheduleFile.createNewFile(filename);
		ScheduleFile sf = new ScheduleFile(filename);
		int i=0;
		try{
		Thread ta = new Thread(new OperateSchedule(0,sf));
		ta.start();
		ta.join();
		System.out.println("**********************");
		System.out.println(sf.getFilestr());
		System.out.println("**********************");
		while(i<50){
			Thread ti = new Thread(new OperateSchedule(1,sf));
			ti.start();
			TimeUnit.MILLISECONDS.sleep(1000);
			ti.join();
			System.out.println("id"+i);
			i++;
			if(i==30) {
				ta = new Thread(new OperateSchedule(0,sf));
				ta.start();
				ta.join();
			ti = new Thread(new OperateSchedule(2,sf));
			ti.start();
			ti.join();
			}
		}}catch(InterruptedException e){
			System.err.println("Interrupted...");
		}
	}
}
