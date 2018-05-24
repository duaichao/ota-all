package cn.sd.core.config.plugin.defaultPlug;

import java.util.Hashtable;

/**
 * 用于 系统默认配置装载器 的配置存放
 * @author Rex
 */
public class Config {
	//公共参数
	private Hashtable<String, ConfigDetail> publicConfigs = new Hashtable<String,ConfigDetail>();
	
	//外接配置,Object是配置对象
	private Hashtable<String, Object> pluginConfigs = new Hashtable<String,Object>();
	
	//外接配置的配置<include key="SystemConfig1" plugin="">SystemConfig1.xml</include>只是用于解析这个
	private Hashtable<String, PlugConfig> includeConfigs = new Hashtable<String,PlugConfig>();
	
	/**
	 * 外接配置操作
	 * @param c
	 */
	public void addIncludeConfigDetail(PlugConfig c) {
		includeConfigs.put(c.getPlugFile().hashCode() + "", c);
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable getIncludeConfigs() {
		return includeConfigs;
	}
	
	public PlugConfig getIncludeConfigDetail(String name) {
		return (PlugConfig)includeConfigs.get(name);
	}
	
	/**
	 * 外接配置操作
	 * @param c
	 */
	public void addPluginConfigDetail(Object c,String key) {
		pluginConfigs.put(key, c);
	}
	public void delPluginConfigDetail(String key) {
		pluginConfigs.remove(key);
	}
	@SuppressWarnings("unchecked")
	public Hashtable getPluginConfigs() {
		return pluginConfigs;
	}
	
	public Object getPluginConfigDetail(String name) {
		return pluginConfigs.get(name);
	}
	
	
	/**
	 * 公共参数操作
	 * @param c
	 */
	public void addPublicConfigDetail(ConfigDetail c) {
		publicConfigs.put(c.getConfigName(), c);
	}
	
	@SuppressWarnings("unchecked")
	public Hashtable getPublicConfigs() {
		return publicConfigs;
	}
	
	public ConfigDetail getPublicConfigDetail(String name) {
		return (ConfigDetail) publicConfigs.get(name);
	}
	
	public String getStringConfigDetail(String name) {
		String v = null;
		ConfigDetail detail = getPublicConfigDetail(name);
		if (detail != null) {
			v = detail.getConfigValue();
		}
		return v;
	}
	
	public boolean getBooleanConfigDetail(String name) {
		boolean v = false;
		ConfigDetail detail = getPublicConfigDetail(name);
		if (detail != null) {
			v = detail.getBooleanValue();
		}
		return v;
	}
	
	public int getIntConfigDetail(String name) {
		int v = 0;
		ConfigDetail detail = getPublicConfigDetail(name);
		if (detail != null) {
			v = detail.getIntValue();
		}
		return v;
	}
	
	public double getDoubleConfigDetail(String name) {
		double v = 0d;
		ConfigDetail detail = getPublicConfigDetail(name);
		if (detail != null) {
			v = detail.getDoubleValue();
		}
		return v;
	}
	
}
