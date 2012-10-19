package org.eweb4j.spiderman.plugin;

import java.util.Map;

import org.eweb4j.spiderman.fetcher.Page;
import org.eweb4j.spiderman.spider.SpiderListener;
import org.eweb4j.spiderman.xml.Target;


public interface ParsePoint extends Point{

	void init(Target target, Page page, SpiderListener listener) throws Exception;
	
	Map<String, Object> parse(Map<String, Object> model) throws Exception;

}
