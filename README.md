Spiderman - 又一个Java网络蜘蛛
==========================================
    Spiderman 是一个基于微内核+插件式架构的网络蜘蛛，它的目标是通过简单的方法就能将复杂的目标网页信息抓取并解析为自己所需要的业务数据。

它包含了两部分（二者缺一不可）：

* Spiderman: 内核 [https://github.com/laiweiwei/spiderman](https://github.com/laiweiwei/spiderman) 
* Spiderman-Plugin: 插件 [https://github.com/laiweiwei/spiderman-plugin](https://github.com/laiweiwei/spiderman-plugin)

怎么使用？这里有个抓取网易新闻案例
----------------------------------
* [spiderman-sample](https://github.com/laiweiwei/spiderman-sample)

Spiderman的主要特点是
----------------------
    * 灵活、可扩展性强，微内核+插件式架构，Spiderman提供了多达 10 个扩展点。横跨蜘蛛线程的整个生命周期。
    * 通过简单的配置就可以将复杂的网页内容解析为自己需要的业务数据，无需编写一句代码
    * 多线程

更多内容待增加......
----