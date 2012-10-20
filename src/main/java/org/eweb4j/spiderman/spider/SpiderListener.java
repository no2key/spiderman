package org.eweb4j.spiderman.spider;

import java.util.Collection;
import java.util.Map;

import org.eweb4j.spiderman.fetcher.Page;
import org.eweb4j.spiderman.task.Task;



public interface SpiderListener {

    void onNewUrls(Thread thread, Task task, Collection<String> newUrls);
	
    void onDupRemoval(Thread currentThread, Task task, Collection<Task> validTasks);
    
	void onNewTasks(Thread thread, Task task, Collection<Task> newTasks);
	
	void onTargetPage(Thread thread, Task task, Page page);
	
	void onParse(Thread thread, Task task, Map<String, Object> model, int count);

	void onInfo(Thread thread, String info);
	
	void onError(Thread thread, String err, Exception e);
}
