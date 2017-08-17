package profile.metrics;

public class Block_Manager_ID implements SparkMetric{
	private String Executor_ID;
	private String Host;
	private Integer Port;
	private Integer Netty_Port;
	public String getExecutor_ID() {
		return Executor_ID;
	}
	public void setExecutor_ID(String executor_ID) {
		Executor_ID = executor_ID;
	}
	public String getHost() {
		return Host;
	}
	public void setHost(String host) {
		Host = host;
	}
	public Integer getPort() {
		return Port;
	}
	public void setPort(Integer port) {
		Port = port;
	}
	public Integer getNetty_Port() {
		return Netty_Port;
	}
	public void setNetty_Port(Integer netty_Port) {
		Netty_Port = netty_Port;
	}
}
