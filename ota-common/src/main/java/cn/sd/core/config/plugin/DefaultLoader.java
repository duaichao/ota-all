package cn.sd.core.config.plugin;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.sd.core.config.plugin.defaultPlug.Config;
import cn.sd.core.config.plugin.defaultPlug.ConfigDetail;
import cn.sd.core.config.plugin.defaultPlug.PlugConfig;

/**
 * 系统默认的配置文件装载器
 * @author Rex
 */
public class DefaultLoader implements ILoader{
	private static final Log logger = LogFactory.getLog(DefaultLoader.class);
	
	public Config loadConfig(String filename){
		Config config = null;
		try {
			Digester digester = new Digester();
            digester.setValidating(false);
            
            digester.addObjectCreate("config", Config.class);
            
            //扩展配置部分
            digester.addObjectCreate("config/include", PlugConfig.class);
            digester.addSetProperties("config/include","file","plugFile");
            digester.addSetProperties("config/include","plugin","plugClass");
            digester.addSetProperties("config/include","key","plugName");
            digester.addSetNext( "config/include", "addIncludeConfigDetail" );
            
            //通用配置部分
            digester.addObjectCreate("config/PublicConfig/detail", ConfigDetail.class);
            digester.addSetProperties("config/PublicConfig/detail","name","configName");
            digester.addBeanPropertySetter("config/PublicConfig/detail","configValue");
            digester.addSetNext( "config/PublicConfig/detail", "addPublicConfigDetail" );
            
            java.io.File input = new java.io.File(filename);
            config = (Config) digester.parse(input);
		} catch (Exception exc) {
			config =new Config();
			logger.error("找不到系统配置文件" + filename + "!");
		}
		return config;
	}
}
