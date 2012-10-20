package org.eweb4j.spiderman.spider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eweb4j.spiderman.fetcher.FetchResult;
import org.eweb4j.spiderman.fetcher.Page;
import org.eweb4j.spiderman.plugin.BeginPoint;
import org.eweb4j.spiderman.plugin.DigPoint;
import org.eweb4j.spiderman.plugin.DupRemovalPoint;
import org.eweb4j.spiderman.plugin.EndPoint;
import org.eweb4j.spiderman.plugin.FetchPoint;
import org.eweb4j.spiderman.plugin.ParsePoint;
import org.eweb4j.spiderman.plugin.TargetPoint;
import org.eweb4j.spiderman.plugin.TaskPushPoint;
import org.eweb4j.spiderman.plugin.TaskSortPoint;
import org.eweb4j.spiderman.task.Task;
import org.eweb4j.spiderman.xml.Target;


/**
 * 网络蜘蛛
 * @author weiwei
 *
 */
public class Spider implements Runnable{

	private Task task;
	private SpiderListener listener;
	
	public void init(Task task, SpiderListener listener) {
		this.task = task;
		this.listener = listener;
	}
	
	public void run() {
		try {
			//扩展点：begin 蜘蛛开始
			Collection<BeginPoint> beginPoints = task.site.beginPointImpls;
			if (beginPoints != null && !beginPoints.isEmpty()){
				for (BeginPoint point : beginPoints){
					task = point.confirmTask(task);
				}
			}
			
			if (task == null) return ;
			
			//扩展点：fetch 获取HTTP内容
			FetchResult result = null;
			Collection<FetchPoint> fetchPoints = task.site.fetchPointImpls;
			if (fetchPoints != null && !fetchPoints.isEmpty()){
				for (FetchPoint point : fetchPoints){
					point.init(task, listener);
					result = point.fetch(result);
				}
			}
			
			if (result == null) {
				listener.onInfo(Thread.currentThread(), " spider stop cause the fetch result of task["+task+"] is null");
				return ;
			}
			
			listener.onInfo(Thread.currentThread(), "fetch content -> " + result);
			//扩展点：dig new url 发觉新URL
			Collection<String> newUrls = null;
			Collection<DigPoint> digPoints = task.site.digPointImpls;
			if (digPoints != null && !digPoints.isEmpty()){
				for (DigPoint point : digPoints){
					point.init(result, task, listener);
					newUrls = point.digNewUrls(newUrls);
				}
			}
			
			if (newUrls != null && !newUrls.isEmpty())
				this.listener.onNewUrls(Thread.currentThread(), task, newUrls);
			
			//扩展点：dup_removal URL去重,然后变成Task
			Collection<Task> validTasks = null;
			Collection<DupRemovalPoint> dupRemovalPoints = task.site.dupRemovalPointImpls;
			if (dupRemovalPoints != null && !dupRemovalPoints.isEmpty()){
				for (DupRemovalPoint point : dupRemovalPoints){
					point.init(task, newUrls);
					validTasks = point.removeDuplicateTask(validTasks);
				}
			}
			
			if (newUrls != null && !newUrls.isEmpty())
				this.listener.onDupRemoval(Thread.currentThread(), task, validTasks);
			
			//扩展点：task_sort 给任务排序
			Collection<TaskSortPoint> taskSortPoints = task.site.taskSortPointImpls;
			if (taskSortPoints != null && !taskSortPoints.isEmpty()){
				for (TaskSortPoint point : taskSortPoints){
					validTasks = point.sortTasks(validTasks);
				}
			}
			
			//扩展点：task_push 将任务放入队列
			Collection<TaskPushPoint> taskPushPoints = task.site.taskPushPointImpls;
			if (taskPushPoints != null && !taskPushPoints.isEmpty()){
				for (TaskPushPoint point : taskPushPoints){
					validTasks = point.pushTask(validTasks);
				}
			}
			
			if (validTasks != null && !validTasks.isEmpty())
				this.listener.onNewTasks(Thread.currentThread(), task, validTasks);
			
			Page page = result.getPage();
			if (page == null) {
				listener.onInfo(Thread.currentThread(), " spider stop cause the fetch result.page is null");
				return ;
			}
			
			//扩展点：target 确认当前的Task.url符不符合目标期望
			Target target = null;
			Collection<TargetPoint> targetPoints = task.site.targetPointImpls;
			if (targetPoints != null && !targetPoints.isEmpty()){
				for (TargetPoint point : targetPoints){
					point.init(task, listener);
					target = point.confirmTarget(target);
				}
			}
			
			if (target == null) {
				listener.onInfo(Thread.currentThread(), " spider stop cause the task is not the target");
				return ;
			}
			
			this.listener.onTargetPage(Thread.currentThread(), task, page);
			
			//扩展点：parse 把已确认好的目标页面解析成为Map对象
			Map<String, Object> model = null;
			Collection<ParsePoint> parsePoints = task.site.parsePointImpls;
			if (parsePoints != null && !parsePoints.isEmpty()){
				for (ParsePoint point : parsePoints){
					point.init(target, page, listener);
					model = point.parse(model);
				}
			}
			
			if (model == null) {
				listener.onInfo(Thread.currentThread(), " spider stop cause the target model is null");
				return ;
			}
			
			//扩展点：end 蜘蛛完成工作，该收尾了
			Collection<EndPoint> endPoints = task.site.endPointImpls;
			if (endPoints != null && !endPoints.isEmpty()){
				Map<String, Object> dataMap = new HashMap<String,Object>();
				for (EndPoint point : endPoints){
					point.init(task, model, listener);
					dataMap = point.complete(dataMap);
				}
			}
			
		} catch(Exception e){
			this.listener.onError(Thread.currentThread(), e.toString(), e);
		}
	}

}
