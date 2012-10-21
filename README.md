Spiderman - 又一个Java网络蜘蛛
==========================================
Spiderman 是一个基于微内核+插件式架构的网络蜘蛛，它的目标是通过简单的方法就能将复杂的目标网页信息抓取并解析为自己所需要的业务数据。

它包含了两部分：

* Spiderman: 内核程序
* Spiderman-Plugin: 插件

注意：以上两部分缺一不可，本项目是内核部分，另外一个项目实现了一个默认的插件（你可以认为它就是官方默认的插件），该插件实现了内核所有的十个扩展点。只有内核+插件才能让Spiderman工作起来。

下面通过一个抓取网易新闻的例子来了解下如何使用Spiderman：

*首先我们会有一个初始化配置文件spiderman.properties，它就放在${ClassPath}目录下
<blockquote>
#网站配置文件放置目录
website.xml.folder=${ClassPath}/WebSites
#http抓取失败重试次数
http.fetch.retry=3
#http连接超时，支持单位 s秒 m分 h时 d天，不写单位则表示s秒
http.fetch.timeout=5s
</blockquote>


