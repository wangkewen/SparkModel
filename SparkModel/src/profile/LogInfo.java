/*
 * Spark application log information of Spark EVENT 
 */

package profile;

import java.util.ArrayList;
import java.util.List;

import profile.events.SparkListenerApplicationEnd;
import profile.events.SparkListenerApplicationStart;
import profile.events.SparkListenerBlockManagerAdded;
import profile.events.SparkListenerBlockManagerRemoved;
import profile.events.SparkListenerEnvironmentUpdate;
import profile.events.SparkListenerJobEnd;
import profile.events.SparkListenerJobStart;
import profile.events.SparkListenerStageCompleted;
import profile.events.SparkListenerStageSubmitted;
import profile.events.SparkListenerTaskEnd;
import profile.events.SparkListenerTaskGettingResult;
import profile.events.SparkListenerTaskStart;
import profile.events.SparkListenerUnpersistRDD;

public class LogInfo {
	private List<SparkListenerBlockManagerAdded> SparkListenerBlockManagerAddedList;
    private List<SparkListenerBlockManagerRemoved> SparkListenerBlockManagerRemovedList; 
    private List<SparkListenerApplicationEnd> SparkListenerApplicationEndList;
    private List<SparkListenerApplicationStart> SparkListenerApplicationStartList; 
    private List<SparkListenerEnvironmentUpdate> SparkListenerEnvironmentUpdateList; 
    private List<SparkListenerJobEnd> SparkListenerJobEndList;
    private List<SparkListenerJobStart> SparkListenerJobStartList; 
    private List<SparkListenerStageCompleted> SparkListenerStageCompletedList; 
    private List<SparkListenerStageSubmitted> SparkListenerStageSubmittedList;
    private List<SparkListenerTaskEnd> SparkListenerTaskEndList;
    private List<SparkListenerTaskGettingResult> SparkListenerTaskGettingResultList; 
    private List<SparkListenerTaskStart> SparkListenerTaskStartList;
    private List<SparkListenerUnpersistRDD> SparkListenerUnpersistRDDList; 

    public LogInfo() {
    	SparkListenerBlockManagerAddedList = new ArrayList<SparkListenerBlockManagerAdded>();
        SparkListenerBlockManagerRemovedList = new ArrayList<SparkListenerBlockManagerRemoved>();
        SparkListenerApplicationEndList = new ArrayList<SparkListenerApplicationEnd>();
        SparkListenerApplicationStartList = new ArrayList<SparkListenerApplicationStart>();
        SparkListenerEnvironmentUpdateList = new ArrayList<SparkListenerEnvironmentUpdate>();
        SparkListenerJobEndList = new ArrayList<SparkListenerJobEnd>();
        SparkListenerJobStartList = new ArrayList<SparkListenerJobStart>();
        SparkListenerStageCompletedList = new ArrayList<SparkListenerStageCompleted>();
        SparkListenerStageSubmittedList = new ArrayList<SparkListenerStageSubmitted>();
        SparkListenerTaskEndList = new ArrayList<SparkListenerTaskEnd>();
        SparkListenerTaskGettingResultList = new ArrayList<SparkListenerTaskGettingResult>();
        SparkListenerTaskStartList = new ArrayList<SparkListenerTaskStart>();
        SparkListenerUnpersistRDDList = new ArrayList<SparkListenerUnpersistRDD>();
    }

	public List<SparkListenerBlockManagerAdded> getSparkListenerBlockManagerAddedList() {
		return SparkListenerBlockManagerAddedList;
	}

	public void setSparkListenerBlockManagerAddedList(
			List<SparkListenerBlockManagerAdded> sparkListenerBlockManagerAddedList) {
		SparkListenerBlockManagerAddedList = sparkListenerBlockManagerAddedList;
	}

	public List<SparkListenerBlockManagerRemoved> getSparkListenerBlockManagerRemovedList() {
		return SparkListenerBlockManagerRemovedList;
	}

	public void setSparkListenerBlockManagerRemovedList(
			List<SparkListenerBlockManagerRemoved> sparkListenerBlockManagerRemovedList) {
		SparkListenerBlockManagerRemovedList = sparkListenerBlockManagerRemovedList;
	}

	public List<SparkListenerApplicationEnd> getSparkListenerApplicationEndList() {
		return SparkListenerApplicationEndList;
	}

	public void setSparkListenerApplicationEndList(
			List<SparkListenerApplicationEnd> sparkListenerApplicationEndList) {
		SparkListenerApplicationEndList = sparkListenerApplicationEndList;
	}

	public List<SparkListenerApplicationStart> getSparkListenerApplicationStartList() {
		return SparkListenerApplicationStartList;
	}

	public void setSparkListenerApplicationStartList(
			List<SparkListenerApplicationStart> sparkListenerApplicationStartList) {
		SparkListenerApplicationStartList = sparkListenerApplicationStartList;
	}

	public List<SparkListenerEnvironmentUpdate> getSparkListenerEnvironmentUpdateList() {
		return SparkListenerEnvironmentUpdateList;
	}

	public void setSparkListenerEnvironmentUpdateList(
			List<SparkListenerEnvironmentUpdate> sparkListenerEnvironmentUpdateList) {
		SparkListenerEnvironmentUpdateList = sparkListenerEnvironmentUpdateList;
	}

	public List<SparkListenerJobEnd> getSparkListenerJobEndList() {
		return SparkListenerJobEndList;
	}

	public void setSparkListenerJobEndList(
			List<SparkListenerJobEnd> sparkListenerJobEndList) {
		SparkListenerJobEndList = sparkListenerJobEndList;
	}

	public List<SparkListenerJobStart> getSparkListenerJobStartList() {
		return SparkListenerJobStartList;
	}

	public void setSparkListenerJobStartList(
			List<SparkListenerJobStart> sparkListenerJobStartList) {
		SparkListenerJobStartList = sparkListenerJobStartList;
	}

	public List<SparkListenerStageCompleted> getSparkListenerStageCompletedList() {
		return SparkListenerStageCompletedList;
	}

	public void setSparkListenerStageCompletedList(
			List<SparkListenerStageCompleted> sparkListenerStageCompletedList) {
		SparkListenerStageCompletedList = sparkListenerStageCompletedList;
	}

	public List<SparkListenerStageSubmitted> getSparkListenerStageSubmittedList() {
		return SparkListenerStageSubmittedList;
	}

	public void setSparkListenerStageSubmittedList(
			List<SparkListenerStageSubmitted> sparkListenerStageSubmittedList) {
		SparkListenerStageSubmittedList = sparkListenerStageSubmittedList;
	}

	public List<SparkListenerTaskEnd> getSparkListenerTaskEndList() {
		return SparkListenerTaskEndList;
	}

	public void setSparkListenerTaskEndList(
			List<SparkListenerTaskEnd> sparkListenerTaskEndList) {
		SparkListenerTaskEndList = sparkListenerTaskEndList;
	}

	public List<SparkListenerTaskGettingResult> getSparkListenerTaskGettingResultList() {
		return SparkListenerTaskGettingResultList;
	}

	public void setSparkListenerTaskGettingResultList(
			List<SparkListenerTaskGettingResult> sparkListenerTaskGettingResultList) {
		SparkListenerTaskGettingResultList = sparkListenerTaskGettingResultList;
	}

	public List<SparkListenerTaskStart> getSparkListenerTaskStartList() {
		return SparkListenerTaskStartList;
	}

	public void setSparkListenerTaskStartList(
			List<SparkListenerTaskStart> sparkListenerTaskStartList) {
		SparkListenerTaskStartList = sparkListenerTaskStartList;
	}

	public List<SparkListenerUnpersistRDD> getSparkListenerUnpersistRDDList() {
		return SparkListenerUnpersistRDDList;
	}

	public void setSparkListenerUnpersistRDDList(
			List<SparkListenerUnpersistRDD> sparkListenerUnpersistRDDList) {
		SparkListenerUnpersistRDDList = sparkListenerUnpersistRDDList;
	}
}
