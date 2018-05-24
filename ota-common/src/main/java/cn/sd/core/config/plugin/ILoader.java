package cn.sd.core.config.plugin;
/**
 * 系统配置文件装载器接口
 * @author Rex
 */
public interface ILoader {
	public Object loadConfig(String filename);
}
