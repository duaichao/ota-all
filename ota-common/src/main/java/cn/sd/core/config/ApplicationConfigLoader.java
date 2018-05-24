package cn.sd.core.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.StringUtils;

/**
 * 装入系统配置文件，用于Web项目
 * @author Rex
 */
public class ApplicationConfigLoader extends HttpServlet {
	private static final long serialVersionUID = 5929944342596506691L;
	
	public void init(ServletConfig sconfig) throws ServletException {
		String configName = sconfig.getInitParameter("configFile");
		String appPath = sconfig.getServletContext().getRealPath("");
		
		String filename = null;
		
		if (configName != null && !"".equals(configName)) {
			filename = StringUtils.replace(configName, "\\", java.io.File.separator);
			filename = StringUtils.replace(filename, "/", java.io.File.separator);
		}
		if("".equals(filename)){
			filename=null;
		}
		ConfigLoader.init(appPath,filename);
	}

	public void destroy() {
		ConfigLoader.stopListenForChanges();
	}
}