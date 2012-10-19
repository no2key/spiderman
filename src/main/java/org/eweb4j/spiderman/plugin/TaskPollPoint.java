package org.eweb4j.spiderman.plugin;

import org.eweb4j.spiderman.task.Task;
import org.eweb4j.spiderman.xml.Site;


public interface TaskPollPoint extends Point{

	Task pollTask(Site site) throws Exception;

}
