package org.eweb4j.spiderman.plugin;

import java.util.Map;

import org.eweb4j.spiderman.spider.SpiderListener;
import org.eweb4j.spiderman.task.Task;


public interface EndPoint extends Point{

	void init(Task task, Map<String, Object> model, SpiderListener listener) throws Exception;
	
	Map<String, Object> complete(Map<String, Object> dataMap) throws Exception;

}
