package org.eweb4j.spiderman.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eweb4j.spiderman.infra.SpiderIOC;
import org.eweb4j.spiderman.infra.SpiderIOCs;
import org.eweb4j.spiderman.spider.SpiderListener;
import org.eweb4j.spiderman.xml.Extension;
import org.eweb4j.spiderman.xml.Extensions;
import org.eweb4j.spiderman.xml.Impl;
import org.eweb4j.spiderman.xml.Plugin;


public class PluginManager {

	private static Map<String, Collection<Impl>> impls = new HashMap<String, Collection<Impl>>();
	private final static SpiderIOC ioc = SpiderIOCs.create();
	
	public static Plugin createPlugin(){
		Plugin plugin = new Plugin();
		Extensions extensions = new Extensions();
		for (String point : ExtensionPoints.toArray()){
			Extension e = new Extension();
			Impl impl = new Impl();
			impl.setSort("0");
			impl.setType(null);
			impl.setValue(ExtensionPoints.getPointImplClassName(point));
			e.getImpl().add(impl);
			e.setPoint(point);
			extensions.getExtension().add(e);
		}
		plugin.setExtensions(extensions);
		
		return plugin;
	}
	
	public static void loadPluginConf(Collection<Plugin> plugins, SpiderListener listener) throws Exception{
		if (plugins == null || plugins.isEmpty())
			listener.onInfo(Thread.currentThread(), "there is no plugins to load");
		
		Comparator<Impl> implComp = new Comparator<Impl>() {
			public int compare(Impl o1, Impl o2) {
				//注意这里是优先级，1 < 0 < -1 
				int sort1 = Integer.parseInt(o1.getSort());
				int sort2 = Integer.parseInt(o2.getSort());
				if (sort1 == sort2) return 0;
				return sort1 > sort2 ? 1 : -1;
			}
		};
		
		for (Plugin plugin : plugins){
			if (!"1".equals(plugin.getEnable())){ 
				listener.onInfo(Thread.currentThread(), "skip plugin["+plugin.getName()+"] cause it is not enable");
				continue;
			}
			
			Collection<Extension> extensions = plugin.getExtensions().getExtension();
			if (extensions == null || extensions.isEmpty()){
				listener.onInfo(Thread.currentThread(), "plugin["+plugin.getName()+"] has no extentions to load");
				continue;
			}
			
			listener.onInfo(Thread.currentThread(), "plugin info -> \n"+ plugin);
			
			for (Extension ext : extensions){
				String point = ext.getPoint();
				if (!ExtensionPoints.contains(point)){
					String err = "plugin["+plugin.getName()+"] extension-point["+point+"] not found please confim the point value must be in "+ExtensionPoints.string();
					Exception e = new Exception(err);
					listener.onError(Thread.currentThread(), err, e);
					throw e;
				}
				
				List<Impl> impls =  ext.getImpl();
				if (impls == null || impls.isEmpty()){
					listener.onInfo(Thread.currentThread(), "skip plugin["+plugin.getName()+"] extension-point["+point+"] cause it has no impls to load");
					continue;
				}
				
				//按照排序
				Collections.sort(impls, implComp);
				
				PluginManager.impls.put(point, impls);
				
				listener.onInfo(Thread.currentThread(), "plugin["+plugin.getName()+"] extension-point["+point+"] loading ok ");
			}
		}
	}
	
	public static <T> ExtensionPoint<T> getExtensionPoint(final String name){
		if (!PluginManager.impls.containsKey(name))
			return null;
		
		final Collection<Impl> _impls = PluginManager.impls.get(name);
		return new ExtensionPoint<T>() {
			public Collection<T> getExtensions() {
				Collection<T> result = new ArrayList<T>();
				for (Impl impl : _impls){
					T t = null;
					String type = impl.getType();
					String value = impl.getValue();
					if ("ioc".equals(type)){
						t = ioc.createExtensionInstance(value);
					}else{
						try {
							Class<T> cls = (Class<T>) Class.forName(value);
							t = cls.newInstance();
						} catch (ClassNotFoundException e) {
							throw new RuntimeException("Impl class -> " + value + " of ExtensionPoint["+name+"] not found !", e);
						} catch (InstantiationException e) {
							throw new RuntimeException("Impl class -> " + value + " of ExtensionPoint["+name+"] instaniation fail !", e);
						} catch (IllegalAccessException e) {
							throw new RuntimeException("Impl class -> " + value + " of ExtensionPoint["+name+"] illegal access !", e);
						}
					}
					
					result.add(t);
				}
				
				return result;
			}
		};
	}
	
}
