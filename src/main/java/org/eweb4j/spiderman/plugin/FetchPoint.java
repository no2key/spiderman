package org.eweb4j.spiderman.plugin;

import org.eweb4j.spiderman.fetcher.FetchResult;
import org.eweb4j.spiderman.spider.SpiderListener;
import org.eweb4j.spiderman.task.Task;


public interface FetchPoint extends Point{

	void init(Task task, SpiderListener listener) throws Exception;
	
	FetchResult fetch(FetchResult result) throws Exception;

}
