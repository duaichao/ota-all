package cn.sd.core.config;

import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.sd.core.config.plugin.DefaultLoader;
import cn.sd.core.config.plugin.ILoader;
import cn.sd.core.config.plugin.defaultPlug.Config;
import cn.sd.core.config.plugin.defaultPlug.PlugConfig;
import cn.sd.core.util.PathUtils;
import cn.sd.core.util.filemonitor.FileChangeListener;
import cn.sd.core.util.filemonitor.FileMonitor;



/**
 * 装入系统配置文件，并监视文件是否有变化，有变化会自动重新装入。
 * @author Rex
 */
public class ConfigLoader{
	private static final long serialVersionUID = 5929944342596506692L;
	public static String appPath = "";
	public static Config config = null;
	
	private static String mainConfigFile = "";//主配置文件名
	private static String mainConfigPath = "";//主配置文件所在位置
	
	private static final Log logger = LogFactory.getLog(ConfigLoader.class);
	
	public static void main(String[] args){
		ConfigLoader.init(null,null);
	}
	
	/**
	 * 配置文件的初试化
	 * @param _appPath	
	 * 		  应用程序所在的路径，如果为null则为当前class的ClassPath
	 * @param configName
	 * 		  配置文件的文件名称，如果为null则为SystemConfig.xml
	 */
	public static void init(String _appPath,String configName) {
		String fileseparator=java.io.File.separator;
		if(_appPath==null){
			_appPath = ConfigLoader.class.getClassLoader().getResource("").getPath();
		}
		
        appPath = StringUtils.replace(_appPath,"\\",fileseparator);
        appPath = StringUtils.replace(appPath,"/",fileseparator);
        
        if(appPath.lastIndexOf(fileseparator)+fileseparator.length()==appPath.length()){
        	appPath=appPath.substring(0,appPath.length()-1);
        }
        
        String filename=null;
        if (configName==null || "".equals(configName)){
        	filename=appPath + fileseparator + "SystemConfig.xml";
        } else {
        	if(configName.indexOf(fileseparator)==-1){
        		filename=appPath + fileseparator + configName;
        	} else {
        		filename=appPath + configName;
        	}
        }
        
        if (filename!=null && !filename.equals("")){
        	mainConfigPath = PathUtils.extractFilePath(filename);
        	mainConfigFile = PathUtils.extractFileName(filename);
        	ConfigLoader.loadConfigByFileName(filename);
			ConfigLoader.listenForChanges(filename);
        }
	}
	
	/**
	 * 停止对一个文件的监视
	 * @param filename
	 */
	public static void stopListenForChanges(String filename) {
		FileMonitor.getInstance().removeFileChangeListener(filename);
	}
	
	/**
	 * 停止对全部文件的监视
	 */
	public static void stopListenForChanges() {
		FileMonitor.getInstance().removeFileChangeListener();
	}
	
	/**
	 * 对一个文件启动监视
	 * @param filename
	 */
	public static void listenForChanges(String filename) {
		int fileChangesDelay = 1000;
		
		if (fileChangesDelay > 0) {
			FileMonitor.getInstance().addFileChangeListener(new QueriesFileListener(),filename, fileChangesDelay);
		}
	}
	
	//根据文件使用不同的加载器加载配置文件
	@SuppressWarnings("unchecked")
	public static void loadConfigByFileName(String fileName){
    	String fName = PathUtils.extractFileName(fileName);
    	if(fName.equals(mainConfigFile)){
    		//加载主配置文件前先停止副配置文件的监视器
    		if(ConfigLoader.config!=null){
    			Enumeration elements = ConfigLoader.config.getIncludeConfigs().elements();
    			while (elements.hasMoreElements()) { 
    				PlugConfig pc = (PlugConfig)elements.nextElement();
    				ConfigLoader.stopListenForChanges(mainConfigPath + pc.getPlugFile());
    			}
    		}
    		ILoader dloader = new DefaultLoader();
    		config = (Config)dloader.loadConfig(fileName);
    		if(ConfigLoader.config!=null){
    			//启动副配置文件的监视器
    			Enumeration elements = ConfigLoader.config.getIncludeConfigs().elements();
    			while (elements.hasMoreElements()) { 
    				PlugConfig pc = (PlugConfig)elements.nextElement();
    				if(ConfigLoader.config!=null){
    					Object temppc = ConfigLoader.config.getPluginConfigDetail(pc.getPlugName());
    					if(temppc==null){
    						//载入子配置文件
    						ConfigLoader.loadConfigByFileName(mainConfigPath + pc.getPlugFile());
    					}
    				}
    				ConfigLoader.listenForChanges(mainConfigPath + pc.getPlugFile());
    			}
    		}
    	} else {
    		//副配置文件
    		PlugConfig pc = (PlugConfig)ConfigLoader.config.getIncludeConfigDetail(fName.hashCode() + "");
    		String className = pc.getPlugClass();
    		ILoader handler = null;
    		Object subConfig = null;
    		if(className!=null && !className.equals("")){
    			Class handlerClass = null;
				try {
					handlerClass = Class.forName(className);
					handler = (ILoader)handlerClass.newInstance();
				} catch (Exception e) {
					handler = null;
				}
				if(handler!=null){
					subConfig = handler.loadConfig(fileName);
				}
    		}
    		if(ConfigLoader.config!=null){
    			ConfigLoader.config.delPluginConfigDetail(pc.getPlugName());
    			ConfigLoader.config.addPluginConfigDetail(subConfig, pc.getPlugName());
    		}
    	}
	}
	
	/**
	 * 文件变化事件接收
	 * @author Rex
	 */
	static class QueriesFileListener implements FileChangeListener {
		public void fileChanged(String filename){
			logger.info("reload:" + filename);
			ConfigLoader.loadConfigByFileName(filename);
		}
	}
}
