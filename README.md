Spiderman - 又一个Java网络蜘蛛
==========================================
    Spiderman 是一个基于微内核+插件式架构的网络蜘蛛，它的目标是通过简单的方法就能将复杂的目标网页信息抓取并解析为自己所需要的业务数据。

它包含了两部分（二者缺一不可）：

* Spiderman: 内核 [https://github.com/laiweiwei/spiderman](https://github.com/laiweiwei/spiderman) 
* Spiderman-Plugin: 插件 [https://github.com/laiweiwei/spiderman-plugin](https://github.com/laiweiwei/spiderman-plugin)

主要特点是：

* 灵活、可扩展性强，微内核+插件式架构，Spiderman提供了多达 10 个扩展点。横跨蜘蛛线程的整个生命周期。
* 通过简单的配置就可以将复杂的网页内容解析为自己需要的业务数据，无需编写一句代码
* 多线程

不妨先快速试用一下：

* 首先保证你的机器至少可以运行Java程序、也可以执行Maven命令
* 这里有个案例程序[spiderman-sample](https://github.com/laiweiwei/spiderman-sample)将其下载到本地，执行命令 mvn test
* Spiderman程序将会运行10秒钟，这个过程你应该能在控制台看到打印出来的从网易新闻爬回来的新闻数据
* 这是抓取回来解析之后的其中一条新闻截图：[查看高清大图](http://dl.iteye.com/upload/picture/pic/119200/7d63d7fd-66a1-37cb-b079-790d8f73560e.png)
* ![Screenshot](http://dl.iteye.com/upload/picture/pic/119200/7d63d7fd-66a1-37cb-b079-790d8f73560e.png)

这是使用Spiderman的代码：

    public class TestSpider {
        private final static Log log = LogFactory.getConfigLogger(TestSpider.class);
   
    	@Test
    	public void test() throws Exception {
            // 初始化蜘蛛
    		Spiderman.init(new SpiderListener() {
    			public void onNewUrls(Thread thread, Task task, Collection<String> newUrls) {}
    			public void onDupRemoval(Thread currentThread, Task task, Collection<Task> validTasks) {}
    			public void onNewTasks(Thread thread, Task task, Collection<Task> newTasks) {}
    			public void onTargetPage(Thread thread, Task task, Page page) {}
    			public void onParse(Thread thread, Task task, Map<String, Object> model, int count) {
    				log.debug("get target model["+count+"] -> " + CommonUtil.toJson(model));
    			}
    			public void onInfo(Thread thread, String info) {}
    			public void onError(Thread thread, String err, Exception e) {
    				e.printStackTrace();
    			}
    		});
    
    		// 启动蜘蛛
    		Spiderman.start();
    		//运行10s
    		Thread.sleep(CommonUtil.toSeconds("10s").longValue()*1000);
    		// 关闭蜘蛛
    		Spiderman.stop();
    	}
    }

下面详细看看这个sample的配置文件：

首先有一个初始化配置文件spiderman.properties，它就放在#{ClassPath}目录下
 
    #网站配置文件放置目录
    website.xml.folder=#{ClassPath}/WebSites
    #http抓取失败重试次数
    http.fetch.retry=3
    #http连接超时，支持单位 s秒 m分 h时 d天，不写单位则表示s秒
    http.fetch.timeout=5s

然后在#{ClassPath}/WebSites目录下有一份sample.xml（其实应该命名为news.163.xml比较合适的）

    <?xml version="1.0" encoding="UTF-8"?>
    <beans>
        <site name="163news" url="http://news.163.com" enable="1" schedule="10m" thread="2" waitQueue="5s">
    		<queueRules policy="and">
    			<rule type="!regex" value="^.*\.(jpg|png|gif).*$" />
    		</queueRules>
    		<targets>
    			<target name="news">
    				<urls policy="and">
    					<rule type="regex" value="http://news.163.com/\d+/\d+/\d+/\w+\.html" />
    				</urls>
    				<model>
    					<field name="title">
    						<parser xpath="//h1[@id='h1title']/text()"/>
    					</field>
    					<field name="time">
    						<parser xpath="//div[@class='endArea']//div[@class='colL']//div[@class='bg_endPage_blue clearfix']//div[@class='endContent']//span[@class='info']/text()" 
    							regex="\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}"
    						/>
    					</field>
    					<field name="source">
    						<parser xpath="//div[@class='endArea']//div[@class='colL']//div[@class='bg_endPage_blue clearfix']//div[@class='endContent']//span[@class='info']/text()" 
    							regex="[^\x00-\xff]+[(].*[)]" />
    					</field>
    					<field name="images" isArray="1">
    						<parser xpath="//div[@class='endArea']//div[@class='colL']//div[@class='bg_endPage_blue clearfix']//div[@class='endContent']//p[@class='f_center']/img[@src]" attribute="src" />
    					</field>
    					<field name="summary">
    						<parser xpath="//div[@class='endArea']//div[@class='colL']//div[@class='bg_endPage_blue clearfix']//div[@class='endContent']//p[@class='summary']/text()"/>
    					</field>
    					<field name="content">
    						<parser xpath="//div[@class='endArea']//div[@class='colL']//div[@class='bg_endPage_blue clearfix']//div[@class='endContent']//div[@id='endText']/text()"/>
    					</field>
    				</model>
    			</target>
    		</targets>
    		<plugins>
    			<plugin enable="1" name="spider_plugin" version="0.0.1" desc="这是一个官方实现的默认插件，实现了所有扩展点。">
    				<extensions>
    					<extension point="task_poll">
    						<impl type="" value="spiderman.plugin.impl.TaskPollPointImpl" sort="0"/>
    					</extension>
    					<extension point="begin">
    						<impl type="" value="spiderman.plugin.impl.BeginPointImpl" sort="0"/>
    					</extension>
    					<extension point="fetch">
    						<impl type="" value="spiderman.plugin.impl.FetchPointImpl" sort="0"/>
    					</extension>
    					<extension point="dig">
    						<impl type="" value="spiderman.plugin.impl.DigPointImpl" sort="0"/>
    					</extension>
    					<extension point="dup_removal">
    						<impl type="" value="spiderman.plugin.impl.DupRemovalPointImpl" sort="0"/>
    					</extension>
    					<extension point="task_sort">
    						<impl type="" value="spiderman.plugin.impl.TaskSortPointImpl" sort="0"/>
    					</extension>
    					<extension point="task_push">
    						<impl type="" value="spiderman.plugin.impl.TaskPushPointImpl" sort="0"/>
    					</extension>
    					<extension point="target">
    						<impl type="" value="spiderman.plugin.impl.TargetPointImpl" sort="0"/>
    					</extension>
    					<extension point="parse">
    						<impl type="" value="spiderman.plugin.impl.ParsePointImpl" sort="0"/>
    					</extension>
    					<extension point="end">
    						<impl type="" value="spiderman.plugin.impl.EndPointImpl" sort="0"/>
    					</extension>
    				</extensions>
    				<providers>
    					<provider>
    						<orgnization name="深圳优扣科技有限公司" website="" desc="致力于打造一流的社交分享购物社区!">
    							<author name="weiwei" website="http://laiweiweihi.iteye.com" email="l.weiwei@163.com" weibo="http://weibo.com/weiweimiss" desc="一个喜欢自由、音乐、绘画的IT老男孩" />
    						</orgnization>
    					</provider>
    				</providers>
    			</plugin>
    			
    			<plugin enable="1" name="hello_plugin" version="0.0.1" 
    				desc="这是一个样例插件，实现了begin扩展点。实现类有两个，其中一个使用了eweb4j的ioc容器来实例化。注意，任何插件都可以实现多个扩展点，实现相同扩展点的插件按照其实现类给定的sort排序从小到大进行顺序执行">
    				<extensions>
    					<extension point="begin">
    						<impl value="spiderman.plugin.Hello111" sort="1"/>
    						<impl type="ioc" value="hello222" sort="2"/>
    					</extension>
    				</extensions>
    			</plugin>
    		</plugins>
    	</site>
    </beans>

有一个schedule=10m的参数表示每隔十分钟重新从该网站入口处抓取数据，类似这种时间参数的都支持"1s 1m 1h 1d"这样的值分别表示1秒、1分钟、1小时、1天。

更多内容待增加......
----