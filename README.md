Spiderman - 又一个Java网络蜘蛛
==========================================
    Spiderman 是一个基于微内核+插件式架构的网络蜘蛛，它的目标是通过简单的方法就能将复杂的目标网页信息抓取并解析为自己所需要的业务数据。
    
它包含了两部分（二者缺一不可）：
* [内核https://github.com/laiweiwei/spiderman](https://github.com/laiweiwei/spiderman) 
* [插件https://github.com/laiweiwei/spiderman-plugin](https://github.com/laiweiwei/spiderman-plugin)

主要特点
----------------------
    * 灵活、可扩展性强，微内核+插件式架构，Spiderman提供了多达 10 个扩展点。横跨蜘蛛线程的整个生命周期。
    * 通过简单的配置就可以将复杂的网页内容解析为自己需要的业务数据，无需编写一句代码
    * 多线程

怎么使用？
----------
* 首先，确定好你的目标网站以及目标网页（即某一类你想要获取数据的网页，例如网易新闻的新闻页面）
* 然后，打开目标页面，分析页面的HTML结构，得到你想要数据的XPath，具体XPath怎么获取请看下文。
* 最后，在一个xml配置文件里填写好参数，运行Spiderman吧！

这里有个抓取网易新闻案例
------------------------
* [spiderman-sample](https://github.com/laiweiwei/spiderman-sample)

XPath获取技巧？
--------------
这里只说下Chrome浏览器，其他浏览器估计也差不多，只不过插件不同而已。
* 首先，下载xpathonclick插件，[猛击这里](https://chrome.google.com/webstore/search/xpathonclick)
* 安装完毕之后，打开Chrome浏览器，可以看到右上角有个“X Path” 图标。
* 在浏览器打开你的目标网页，然后点击右上角的那个图片，然后点击网标上你想要获取XPath的地方，例如某个标题
* 这时候按住F12打开JS控制台，拖到底部，可以看到一串XPath内容
* 记住，这个内容不是绝对OK的，你可能还需要做些修改，因此，你最好还是去学习下XPath语法
* 学习XPath语法的地方：[猛击这里](http://www.w3school.com.cn/xpath/index.asp)

更多内容待增加......
----