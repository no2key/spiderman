package org.eweb4j.spiderman.plugin;

import org.eweb4j.spiderman.spider.SpiderListener;
import org.eweb4j.spiderman.task.Task;
import org.eweb4j.spiderman.xml.Target;


public interface TargetPoint extends Point{

	void init(Task task, SpiderListener listener) throws Exception;
	Target confirmTarget(Target target) throws Exception;

}
