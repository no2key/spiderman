package org.eweb4j.spiderman.spider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eweb4j.spiderman.infra.SpiderIOC;
import org.eweb4j.spiderman.infra.SpiderIOCs;
import org.eweb4j.spiderman.plugin.BeginPoint;
import org.eweb4j.spiderman.plugin.DigPoint;
import org.eweb4j.spiderman.plugin.DupRemovalPoint;
import org.eweb4j.spiderman.plugin.EndPoint;
import org.eweb4j.spiderman.plugin.ExtensionPoint;
import org.eweb4j.spiderman.plugin.ExtensionPoints;
import org.eweb4j.spiderman.plugin.FetchPoint;
import org.eweb4j.spiderman.plugin.ParsePoint;
import org.eweb4j.spiderman.plugin.PluginManager;
import org.eweb4j.spiderman.plugin.TargetPoint;
import org.eweb4j.spiderman.plugin.TaskPollPoint;
import org.eweb4j.spiderman.plugin.TaskPushPoint;
import org.eweb4j.spiderman.plugin.TaskSortPoint;
import org.eweb4j.spiderman.task.Task;
import org.eweb4j.spiderman.xml.Plugin;
import org.eweb4j.spiderman.xml.Plugins;
import org.eweb4j.spiderman.xml.Site;
import org.eweb4j.spiderman.xml.Target;
import org.eweb4j.util.CommonUtil;
import org.eweb4j.util.FileUtil;
import org.eweb4j.util.xml.BeanXMLUtil;
import org.eweb4j.util.xml.XMLReader;
import org.eweb4j.util.xml.XMLWriter;


public class Spiders {

	public final static SpiderIOC ioc = SpiderIOCs.create();
	public final static Map<String, Counter> counters = new Hashtable<String, Counter>();
	private static ExecutorService pool = null;
	private static Collection<Site> sites = null;
	private static SpiderListener listener = null;
	
	public static void init(SpiderListener _listener) throws Exception{
		listener = _listener;
		
		loadXmls();
		initSites();
		initPool();
	}
	
	public static void start() {
		for (Site site : sites){
			pool.execute(new Spiders._Executor(site));
			listener.onInfo(Thread.currentThread(), "spider tasks of " + site.getName() + " start... ");
		}
	}
	
	public static void stop(){
		pool.shutdownNow();
	}
	
	private static void loadXmls() throws Exception{
		String topPath = FileUtil.getTopClassPath(Spiders.class);
		File siteFolder = new File(topPath + "conf/spider/WebSites");
		if (!siteFolder.exists())
			throw new Exception("can not found WebSites folder -> " + siteFolder.getAbsolutePath());
		
		if (!siteFolder.isDirectory())
			throw new Exception("WebSites -> " + siteFolder.getAbsolutePath() + " must be folder !");
		
		File[] files = siteFolder.listFiles();
		if (files == null || files.length == 0){
			//generate a site.xml file
			File file = new File(siteFolder.getAbsoluteFile()+File.separator+"_site_sample_.xml");
			Site site = new Site();
			
			Plugins plugins = new Plugins();
			plugins.getPlugin().add(PluginManager.createPlugin());
			site.setPlugins(plugins);
			
			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(file, site);
			writer.setBeanName("site");
			writer.setClass("site", Site.class);
			writer.write();
		}
		
		
		sites = new ArrayList<Site>(files.length);
		for (File file : files){
			XMLReader reader = BeanXMLUtil.getBeanXMLReader(file);
			reader.setBeanName("site");
			reader.setClass("site", Site.class);
			Site site = reader.readOne();
			if (site == null)
				throw new Exception("site xml file error -> " + file.getAbsolutePath());
			if ("1".equals(site.getEnable())){
				sites.add(site);
			}
		}
	}
	
	private static void initSites() throws Exception{
		for (Site site : sites){
			if (site.getName() == null || site.getName().trim().length() == 0)
				throw new Exception("site name required");
			if (site.getUrl() == null || site.getUrl().trim().length() == 0)
				throw new Exception("site url required");
			if (site.getTargets() == null || site.getTargets().getTarget().isEmpty())
				throw new Exception("site target required");
			
			List<Target> targets = site.getTargets().getTarget();
			if (targets == null || targets.isEmpty())
				throw new Exception("can not get any url target of site -> " + site.getName());
			
			//---------------------插件初始化开始----------------------------
			listener.onInfo(Thread.currentThread(), "plugins loading begin...");
			Collection<Plugin> plugins = site.getPlugins().getPlugin();
			//加载网站插件配置
			try {
				PluginManager.loadPluginConf(plugins, listener);
			
				//加载TaskPoll扩展点实现类
				ExtensionPoint<TaskPollPoint> taskPollPoint = PluginManager.getExtensionPoint(ExtensionPoints.task_poll);
				if (taskPollPoint != null)
					site.taskPollPointImpls = taskPollPoint.getExtensions();
				
				//加载Begin扩展点实现类
				ExtensionPoint<BeginPoint> beginPoint = PluginManager.getExtensionPoint(ExtensionPoints.begin);
				if (beginPoint != null)
					site.beginPointImpls = beginPoint.getExtensions();
				
				//加载Fetch扩展点实现类
				ExtensionPoint<FetchPoint> fetchPoint = PluginManager.getExtensionPoint(ExtensionPoints.fetch);
				if (fetchPoint != null)
					site.fetchPointImpls = fetchPoint.getExtensions();
				
				//加载Dig扩展点实现类
				ExtensionPoint<DigPoint> digPoint = PluginManager.getExtensionPoint(ExtensionPoints.dig);
				if (digPoint != null)
					site.digPointImpls = digPoint.getExtensions();
				
				//加载DupRemoval扩展点实现类
				ExtensionPoint<DupRemovalPoint> dupRemovalPoint = PluginManager.getExtensionPoint(ExtensionPoints.dup_removal);
				if (dupRemovalPoint != null)
					site.dupRemovalPointImpls = dupRemovalPoint.getExtensions();
				
				//加载TaskSort扩展点实现类
				ExtensionPoint<TaskSortPoint> taskSortPoint = PluginManager.getExtensionPoint(ExtensionPoints.task_sort);
				if (taskSortPoint != null)
					site.taskSortPointImpls = taskSortPoint.getExtensions();
				
				//加载TaskPush扩展点实现类
				ExtensionPoint<TaskPushPoint> taskPushPoint = PluginManager.getExtensionPoint(ExtensionPoints.task_push);
				if (taskPushPoint != null)
					site.taskPushPointImpls = taskPushPoint.getExtensions();
				
				//加载Target扩展点实现类
				ExtensionPoint<TargetPoint> targetPoint = PluginManager.getExtensionPoint(ExtensionPoints.target);
				if (targetPoint != null)
					site.targetPointImpls = targetPoint.getExtensions();
				
				//加载Parse扩展点实现类
				ExtensionPoint<ParsePoint> parsePoint = PluginManager.getExtensionPoint(ExtensionPoints.parse);
				if (parsePoint != null)
					site.parsePointImpls = parsePoint.getExtensions();
				
				//加载End扩展点实现类
				ExtensionPoint<EndPoint> endPoint = PluginManager.getExtensionPoint(ExtensionPoints.end);
				if (endPoint != null)
					site.endPointImpls = endPoint.getExtensions();
			//---------------------------插件初始化完毕----------------------------------
			} catch(Exception e){
				throw new Exception("Site["+site.getName()+"] loading plugins fail", e);
			}
			
			//初始化网站目标Model计数器
			counters.put(site.getName(), new Counter());
		}
	}
	
	private static void initPool(){
		if (pool == null){
			int size = sites.size();
			pool = new ThreadPoolExecutor(size, size,
                    60, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>());
			
			listener.onInfo(Thread.currentThread(), "init thread pool size->"+size+" success ");
		}
	}
	
	private static class _Executor implements Runnable{
		private Site site = null;
		private ExecutorService pool = null;
		
		public _Executor(Site site){
			this.site = site;
			String strSize = site.getThread();
			int size = Integer.parseInt(strSize);
			listener.onInfo(Thread.currentThread(), "site thread size -> " + size);
			if (size > 0)
				pool = new ThreadPoolExecutor(size, size,
	                    60, TimeUnit.SECONDS,
	                    new LinkedBlockingQueue<Runnable>());
			else
				pool = Executors.newCachedThreadPool();
		}
		
		public void run() {
			// 运行种子任务
			Task feedTask = new Task(new String(site.getUrl()), site, 10);
			Spider feedSpider = new Spider();
			feedSpider.init(feedTask, listener);
			pool.execute(feedSpider);
			
			final float times = CommonUtil.toSeconds(site.getSchedule()) * 1000;
			long start = System.currentTimeMillis();
			while(true){
				try {
					//扩展点：TaskPoll
					Task task = null;
					Collection<TaskPollPoint> taskPollPoints = site.taskPollPointImpls;
					if (taskPollPoints != null && !taskPollPoints.isEmpty()){
						for (TaskPollPoint point : taskPollPoints){
							task = point.pollTask(site);
						}
					}
					
					if (task == null){
						long wait = CommonUtil.toSeconds(site.getWaitQueue()).longValue();
						listener.onInfo(Thread.currentThread(), "queue empty wait for -> " + wait + " seconds");
						if (wait > 0)
							Thread.sleep(wait * 1000);
						continue;
					}
					
					Spider spider = new Spider();
					spider.init(task, listener);
					pool.execute(spider);
					
				} catch (Exception e) {
					listener.onError(Thread.currentThread(), e.toString(), e);
				}finally{
					long cost = System.currentTimeMillis() - start;
					if (cost >= times){ 
						// 运行种子任务
						pool.execute(feedSpider);
						listener.onInfo(Thread.currentThread(), " shcedule FeedSpider per "+times+", now cost time ->"+cost);
						start = System.currentTimeMillis();//重新计时
					}
				}
			}
		}
	} 
	
}
