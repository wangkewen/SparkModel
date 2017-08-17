package profile.events;

public class SparkListenerEnvironmentUpdate implements SparkEvent{
	private String Event;
	private JVM_Information JVM_Information;
	private Spark_Properties Spark_Properties;
	private System_Properties System_Properties;
	private Classpath_Entries Classpath_Entries;
	
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public JVM_Information getJVM_Information() {
		return JVM_Information;
	}
	public void setJVM_Information(JVM_Information jVM_Information) {
		JVM_Information = jVM_Information;
	}
	public Spark_Properties getSpark_Properties() {
		return Spark_Properties;
	}
	public void setSpark_Properties(Spark_Properties spark_Properties) {
		Spark_Properties = spark_Properties;
	}
	public System_Properties getSystem_Properties() {
		return System_Properties;
	}
	public void setSystem_Properties(System_Properties system_Properties) {
		System_Properties = system_Properties;
	}
	public Classpath_Entries getClasspath_Entries() {
		return Classpath_Entries;
	}
	public void setClasspath_Entries(Classpath_Entries classpath_Entries) {
		Classpath_Entries = classpath_Entries;
	}
	class JVM_Information {}
	class Spark_Properties {}
	class System_Properties {}
	class Classpath_Entries {}
}
