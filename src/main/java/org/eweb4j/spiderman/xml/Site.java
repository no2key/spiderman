package org.eweb4j.spiderman.xml;

import java.util.Collection;

import org.eweb4j.spiderman.plugin.BeginPoint;
import org.eweb4j.spiderman.plugin.DigPoint;
import org.eweb4j.spiderman.plugin.DupRemovalPoint;
import org.eweb4j.spiderman.plugin.EndPoint;
import org.eweb4j.spiderman.plugin.FetchPoint;
import org.eweb4j.spiderman.plugin.ParsePoint;
import org.eweb4j.spiderman.plugin.TargetPoint;
import org.eweb4j.spiderman.plugin.TaskPollPoint;
import org.eweb4j.spiderman.plugin.TaskPushPoint;
import org.eweb4j.spiderman.plugin.TaskSortPoint;
import org.eweb4j.util.xml.AttrTag;
import org.eweb4j.util.xml.Skip;


public class Site {

	@AttrTag
	private String name;
	
	@AttrTag
	private String url;
	
	@AttrTag
	private String enable = "1";
	
	@AttrTag
	private String schedule = "1h";
	
	@AttrTag
	private String thread = "1";
	
	@AttrTag
	private String waitQueue = "1s";
	
	private Urls queueRules;
	
	private Targets targets ;
	
	private Plugins plugins;
	
	//--------------扩展点-----------------------
	@Skip
	public Collection<TaskPollPoint> taskPollPointImpls;
	@Skip
	public Collection<BeginPoint> beginPointImpls;
	@Skip
	public Collection<FetchPoint> fetchPointImpls;
	@Skip
	public Collection<DigPoint> digPointImpls;
	@Skip
	public Collection<DupRemovalPoint> dupRemovalPointImpls;
	@Skip
	public Collection<TaskSortPoint> taskSortPointImpls;
	@Skip
	public Collection<TaskPushPoint> taskPushPointImpls;
	@Skip
	public Collection<TargetPoint> targetPointImpls;
	@Skip
	public Collection<ParsePoint> parsePointImpls;
	@Skip
	public Collection<EndPoint> endPointImpls;
	//-------------------------------------------
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getWaitQueue() {
		return waitQueue;
	}

	public void setWaitQueue(String waitQueue) {
		this.waitQueue = waitQueue;
	}

	public Targets getTargets() {
		return targets;
	}

	public void setTargets(Targets targets) {
		this.targets = targets;
	}

	public Plugins getPlugins() {
		return plugins;
	}

	public void setPlugins(Plugins plugins) {
		this.plugins = plugins;
	}

	public Urls getQueueRules() {
		return queueRules;
	}

	public void setQueueRules(Urls queueRules) {
		this.queueRules = queueRules;
	}

}
