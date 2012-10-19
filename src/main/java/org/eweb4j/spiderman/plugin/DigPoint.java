package org.eweb4j.spiderman.plugin;

import java.util.Collection;

import org.eweb4j.spiderman.fetcher.FetchResult;
import org.eweb4j.spiderman.spider.SpiderListener;
import org.eweb4j.spiderman.task.Task;


public interface DigPoint extends Point{

	void init(FetchResult result, Task task, SpiderListener listener) throws Exception;
	
	Collection<String> digNewUrls(Collection<String> urls) throws Exception;

}
