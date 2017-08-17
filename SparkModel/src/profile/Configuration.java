/*
 * Cluster Configurations
 */

package profile;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
	private Map<String,Integer> HostCores;  // CPU cores of each Host machine
	private Long BlockSize;  // Block size of HDFS
	private Long InputDataSize; // InputData Size of this Spark Application
	private Integer BlockNum;
	public Configuration() {
		HostCores = new HashMap<String,Integer>();
		BlockSize=null;
		InputDataSize=null;
		BlockNum=null;
	}
	public void init(Long InputSize) {
		for(int i=1; i<=4;i++)
			for(int j=6; j>=1; j--)
			HostCores.put("xen"+j+""+i, 1);
		
		BlockSize=64*1024*1024l;
		InputDataSize=InputSize;
		BlockNum= (int) Math.ceil((InputDataSize*1.0/BlockSize));
	}
	public Map<String, Integer> getHostCores() {
		return HostCores;
	}
	public void setHostCores(Map<String, Integer> hostCores) {
		HostCores = hostCores;
	}
	public Long getBlockSize() {
		return BlockSize;
	}
	public void setBlockSize(Long blockSize) {
		BlockSize = blockSize;
	}
	public Long getInputDataSize() {
		return InputDataSize;
	}
	public void setInputDataSize(Long inputDataSize) {
		InputDataSize = inputDataSize;
	}
	public Integer getBlockNum() {
		return BlockNum;
	}
	public void setBlockNum(Integer blockNum) {
		BlockNum = blockNum;
	}
}
