package org.eweb4j.spiderman.spider;

import java.util.Collection;
import java.util.Map;

import org.eweb4j.spiderman.fetcher.Page;
import org.eweb4j.spiderman.task.Task;



public interface SpiderListener {

	void onFetchPageOk(Thread thread, Task task, Page page);

	void onError(Thread thread, String err, Exception e);

	void onInfo(Thread thread, String info);

	void onNewTasks(Thread currentThread, String url, Collection<Task> newTasks);

	void onParseOk(Thread currentThread, Task task, Map<String, Object> model, int parseSuccessTargetCount);
	
}
