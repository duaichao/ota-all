package cn.sd.core.config.plugin.defaultPlug;

/**
 * 对应配置文件的Include标签
 * @author Rex
 */
public class PlugConfig {
	private String plugName = "";
	private String plugFile = "";
	private String plugClass= "";
	
	public String getPlugClass() {
		return plugClass;
	}
	public void setPlugClass(String plugClass) {
		this.plugClass = plugClass;
	}
	public String getPlugFile() {
		return plugFile;
	}
	public void setPlugFile(String plugFile) {
		this.plugFile = plugFile;
	}
	public String getPlugName() {
		return plugName;
	}
	public void setPlugName(String plugName) {
		this.plugName = plugName;
	}
	
}
