/*
 * Resources Consumption collected by Dstat (http://dag.wiee.rs/home-made/dstat/) per 1000 ms
 */

package profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResourcesInfo {
private String filename;
private String cmdline;
private String host;
private Date begintime;
private ArrayList<Res> ResList;
public ResourcesInfo mergeResourcesInfo(ResourcesInfo resinfo){
	//Calculate the combined resources consumption of different host
	if(this.begintime.getTime() > resinfo.getBegintime().getTime() + 1000*(resinfo.getResList().size()-1)
			|| resinfo.getBegintime().getTime() > this.begintime.getTime() + 1000*(this.ResList.size()-1)){
		System.out.println("Resource files not merge...");
		return null;
	}
	ResourcesInfo resi = new ResourcesInfo();
	resi.setFilename(this.filename);
	resi.setCmdline(this.cmdline);
	resi.setHost(this.host);
	resi.setBegintime(this.begintime);
	int startindex = 0, startindex2 = 0, i = 0;
	if(this.begintime.getTime()<resinfo.getBegintime().getTime()){ 
		startindex = (int) (resinfo.getBegintime().getTime() - this.begintime.getTime()) / 1000;
		for(i=0;i<startindex;i++) 
			resi.getResList().add(this.ResList.get(i));
	}else startindex2 = (int) (this.begintime.getTime() - resinfo.getBegintime().getTime()) / 1000;
	while(startindex < this.ResList.size() && startindex2 < resinfo.getResList().size()){
		Res mergeRes = new Res();
		mergeRes.setCpu_usr(this.ResList.get(startindex).getCpu_usr()
				+resinfo.getResList().get(startindex2).getCpu_usr());
		mergeRes.setCpu_sys(this.ResList.get(startindex).getCpu_sys()
				+resinfo.getResList().get(startindex2).getCpu_sys());
		mergeRes.setCpu_idl(this.ResList.get(startindex).getCpu_idl()
				+resinfo.getResList().get(startindex2).getCpu_idl());
		mergeRes.setCpu_wai(this.ResList.get(startindex).getCpu_wai()
				+resinfo.getResList().get(startindex2).getCpu_wai());
		mergeRes.setDisk_read(this.ResList.get(startindex).getDisk_read()
				+resinfo.getResList().get(startindex2).getDisk_read());
		mergeRes.setDisk_writ(this.ResList.get(startindex).getDisk_writ()
				+resinfo.getResList().get(startindex2).getDisk_writ());
		mergeRes.setNet_recv(this.ResList.get(startindex).getNet_recv()
				+resinfo.getResList().get(startindex2).getNet_recv());
		mergeRes.setNet_send(this.ResList.get(startindex).getNet_send()
				+resinfo.getResList().get(startindex2).getNet_send());
		mergeRes.setSys_int(this.ResList.get(startindex).getSys_int()
				+resinfo.getResList().get(startindex2).getSys_int());
		mergeRes.setSys_csw(this.ResList.get(startindex).getSys_csw()
				+resinfo.getResList().get(startindex2).getSys_csw());
		mergeRes.setTime(this.ResList.get(startindex).getTime());
		resi.getResList().add(mergeRes);
		startindex++;
		startindex2++;
	}
	if(startindex2 > resinfo.getResList().size())
		while(startindex < this.ResList.size())
			resi.getResList().add(this.ResList.get(startindex2++));
	return resi;
	
}
public Res[] calculateAvgMaxMin(List<Res> reslist){
	if(reslist==null || reslist.size()==0){
		System.out.println("List Null...");
		return null;
	}
	Res[] amm = new Res[3];
	Res avg = new Res();
	Res max = new Res();
	Res min = new Res();
	Double cpu_usr=0.0,cpu_usr_max=0.0,cpu_usr_min=0.0;
	Double cpu_sys=0.0,cpu_sys_max=0.0,cpu_sys_min=0.0;
	Double cpu_idl=0.0,cpu_idl_max=0.0,cpu_idl_min=0.0;
	Double cpu_wai=0.0,cpu_wai_max=0.0,cpu_wai_min=0.0;
	Double disk_read=0.0,disk_read_max=0.0,disk_read_min=0.0;
	Double disk_writ=0.0,disk_writ_max=0.0,disk_writ_min=0.0;
	Double net_recv=0.0,net_recv_max=0.0,net_recv_min=0.0;
	Double net_send=0.0,net_send_max=0.0,net_send_min=0.0;
	Double sys_int=0.0,sys_int_max=0.0,sys_int_min=0.0;
	Double sys_csw=0.0,sys_csw_max=0.0,sys_csw_min=0.0;
	for(Res re : reslist){
		cpu_usr += re.getCpu_usr();
		if(re.getCpu_usr()>cpu_usr_max) cpu_usr_max=re.getCpu_usr();
		if(re.getCpu_usr()<cpu_usr_min) cpu_usr_min=re.getCpu_usr();
		cpu_sys += re.getCpu_sys();
		if(re.getCpu_sys()>cpu_sys_max) cpu_sys_max=re.getCpu_sys();
		if(re.getCpu_sys()<cpu_sys_min) cpu_sys_min=re.getCpu_sys();
		cpu_idl += re.getCpu_idl();
		if(re.getCpu_idl()>cpu_idl_max) cpu_idl_max=re.getCpu_idl();
		if(re.getCpu_idl()<cpu_idl_min) cpu_idl_min=re.getCpu_idl();
		cpu_wai += re.getCpu_wai();
		if(re.getCpu_wai()>cpu_wai_max) cpu_wai_max=re.getCpu_wai();
		if(re.getCpu_wai()<cpu_wai_min) cpu_wai_min=re.getCpu_wai();
		disk_read += re.getDisk_read();
		if(re.getDisk_read()>disk_read_max) disk_read_max=re.getDisk_read();
		if(re.getDisk_read()<disk_read_min) disk_read_min=re.getDisk_read();
		disk_writ += re.getDisk_writ();
		if(re.getDisk_writ()>disk_writ_max) disk_writ_max=re.getDisk_writ();
		if(re.getDisk_writ()<disk_writ_min) disk_writ_min=re.getDisk_writ();
		net_recv += re.getNet_recv();
		if(re.getNet_recv()>net_recv_max) net_recv_max=re.getNet_recv();
		if(re.getNet_recv()<net_recv_min) net_recv_min=re.getNet_recv();
		net_send += re.getNet_send();
		if(re.getNet_send()>net_send_max) net_send_max=re.getNet_send();
		if(re.getNet_send()<net_send_min) net_send_min=re.getNet_send();
		sys_int += re.getSys_int();
		if(re.getSys_int()>sys_int_max) sys_int_max=re.getSys_int();
		if(re.getSys_int()<sys_int_min) sys_int_min=re.getSys_int();
		sys_csw += re.getSys_csw();
		if(re.getSys_csw()>sys_csw_max) sys_csw_max=re.getSys_csw();
		if(re.getSys_csw()<sys_csw_min) sys_csw_min=re.getSys_csw();
	}
	avg.setCpu_usr(cpu_usr/reslist.size());
	avg.setCpu_sys(cpu_sys/reslist.size());
	avg.setCpu_idl(cpu_idl/reslist.size());
	avg.setCpu_wai(cpu_wai/reslist.size());
	avg.setDisk_read(disk_read/reslist.size());
	avg.setDisk_writ(disk_writ/reslist.size());
	avg.setNet_recv(net_recv/reslist.size());
	avg.setNet_send(net_send/reslist.size());
	avg.setSys_int(sys_int/reslist.size());
	avg.setSys_csw(sys_csw/reslist.size());
	
	max.setCpu_usr(cpu_usr_max);
	max.setCpu_sys(cpu_sys_max);
	max.setCpu_idl(cpu_idl_max);
	max.setCpu_wai(cpu_wai_max);
	max.setDisk_read(disk_read_max);
	max.setDisk_writ(disk_writ_max);
	max.setNet_recv(net_recv_max);
	max.setNet_send(net_send_max);
	max.setSys_int(sys_int_max);
	max.setSys_csw(sys_csw_max);
	
	min.setCpu_usr(cpu_usr_min);
	min.setCpu_sys(cpu_sys_min);
	min.setCpu_idl(cpu_idl_min);
	min.setCpu_wai(cpu_wai_min);
	min.setDisk_read(disk_read_min);
	min.setDisk_writ(disk_writ_min);
	min.setNet_recv(net_recv_min);
	min.setNet_send(net_send_min);
	min.setSys_int(sys_int_min);
	min.setSys_csw(sys_csw_min);
	amm[0]=avg;
	amm[1]=max;
	amm[2]=min;
	return amm;
}
public ResourcesInfo(){
	filename=null;
	cmdline=null;
	host=null;
	begintime=null;
	ResList = new ArrayList<Res>();
}
public String getFilename() {
	return filename;
}
public void setFilename(String filename) {
	this.filename = filename;
}
public String getCmdline() {
	return cmdline;
}
public void setCmdline(String cmdline) {
	this.cmdline = cmdline;
}
public String getHost() {
	return host;
}
public void setHost(String host) {
	this.host = host;
}
public Date getBegintime() {
	return begintime;
}
public void setBegintime(Date begintime) {
	this.begintime = begintime;
}
public ArrayList<Res> getResList() {
	return ResList;
}
public void setResList(ArrayList<Res> resList) {
	ResList = resList;
}

public class Res {
	// resources consumption metrics
	// paranum:11
private Double cpu_usr;
private Double cpu_sys;
private Double cpu_idl;
private Double cpu_wai;
private Double disk_read;
private Double disk_writ;
private Double net_recv;
private Double net_send;
private Double sys_int;
private Double sys_csw;
private Date time;

public Res(){
	cpu_usr=null;
	cpu_sys=null;
	cpu_idl=null;
	cpu_wai=null;
	disk_read=null;
	disk_writ=null;
	net_recv=null;
	net_send=null;
	sys_int=null;
	sys_csw=null;
	time=null;
}
public void setAll(Double cpu_usr, Double cpu_sys, Double cpu_idl,
		Double cpu_wai, Double disk_read, Double disk_writ,
		Double net_recv, Double net_send, Double sys_int,
		Double sys_csw, Date time){
	setCpu_usr(cpu_usr);
	setCpu_sys(cpu_sys);
	setCpu_idl(cpu_idl);
	setCpu_wai(cpu_wai);
	setDisk_read(disk_read);
	setDisk_writ(disk_writ);
	setNet_recv(net_recv);
	setNet_send(net_send);
	setSys_int(sys_int);
	setSys_csw(sys_csw);
	setTime(time);
}
public Double getCpu_usr() {
	return cpu_usr;
}
public void setCpu_usr(Double cpu_usr) {
	this.cpu_usr = cpu_usr;
}
public Double getCpu_sys() {
	return cpu_sys;
}
public void setCpu_sys(Double cpu_sys) {
	this.cpu_sys = cpu_sys;
}
public Double getCpu_idl() {
	return cpu_idl;
}
public void setCpu_idl(Double cpu_idl) {
	this.cpu_idl = cpu_idl;
}
public Double getCpu_wai() {
	return cpu_wai;
}
public void setCpu_wai(Double cpu_wai) {
	this.cpu_wai = cpu_wai;
}
public Double getDisk_read() {
	return disk_read;
}
public void setDisk_read(Double disk_read) {
	this.disk_read = disk_read;
}
public Double getDisk_writ() {
	return disk_writ;
}
public void setDisk_writ(Double disk_writ) {
	this.disk_writ = disk_writ;
}
public Double getNet_recv() {
	return net_recv;
}
public void setNet_recv(Double net_recv) {
	this.net_recv = net_recv;
}
public Double getNet_send() {
	return net_send;
}
public void setNet_send(Double net_send) {
	this.net_send = net_send;
}
public Double getSys_int() {
	return sys_int;
}
public void setSys_int(Double sys_int) {
	this.sys_int = sys_int;
}
public Double getSys_csw() {
	return sys_csw;
}
public void setSys_csw(Double sys_csw) {
	this.sys_csw = sys_csw;
}
public Date getTime() {
	return time;
}
public void setTime(Date time) {
	this.time = time;
}
}
}