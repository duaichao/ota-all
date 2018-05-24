package cn.sd.core.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import cn.sd.core.util.json.DateJsonValueProcessor;
@Controller
public class BaseController {
	private static Log logger = LogFactory.getLog(BaseController.class);
	private HttpServletResponse response;
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	/**
	 * 手动获取主键
	 * @return
	 */
	public String getUuid(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-","");	
	}
	/**
	 * 参数转换 非空 String
	 * @param obj
	 * @return
	 */
	public String toString(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	/**
	 * 参数转换 非空 int
	 * @param obj
	 * @return
	 */
	public int toInt(Object obj){
		if(obj==null){
			return 0;
		}
		return Integer.parseInt(obj.toString());
	}
	public String toUtf8(String str){
    	if(!toString(str).equals("")){
    		try {
				str = new String(str.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("转换utf8异常",e);
			}
    	}else{
    		str = "";
    	}
    	return str;
    }
	public void outString(String str) {
		try {
			PrintWriter out = getResponse().getWriter();
			out.write(str);
		} catch (IOException e) {
		}
	}
	public void outXMLString(String xmlStr) {
		getResponse().setContentType("application/xml;charset=UTF-8");
		outString(xmlStr);
	}
	/**
	 * 绕过Template,直接输出内容的简便函数. 
	 */
	public String render(String text, String contentType) {
		try {
			HttpServletResponse response = this.getResponse();
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 直接输出字符串.
	 */
	public String renderText(String text) {
		return render(text, "text/plain;charset=UTF-8");
	}

	/**
	 * 直接输出HTML.
	 */
	public String renderHtml(String html) {
		return render(html, "text/html;charset=UTF-8");
	}

	/**
	 * 直接输出XML.
	 */
	public String renderXML(String xml) {
		return render(xml, "text/xml;charset=UTF-8");
	}
	
	public boolean isNullOrEmptyString(Object o) {
		if(o == null)
			return true;
		if(o instanceof String) {
			String str = (String)o;
			if(str.length() == 0)
				return true;
		}
		return false;
	}
	
    private JsonConfig getJsonConfig(String[] excludes){
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,new DateJsonValueProcessor());
        jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,new DateJsonValueProcessor());  
        if(excludes!=null){
            jsonConfig.setExcludes( excludes );
        }
        return jsonConfig;
    }
    
    public String getJsonArray(Object array,String[] excludes){
        JsonConfig jsonConfig = getJsonConfig(excludes);
        return JSONArray.fromObject(array,jsonConfig).toString();
    }
    public String getJsonArray(Object array){
        return getJsonArray(array,null);
    }
    
    public String getJsonString(Object obj,String[] excludes){
        JsonConfig jsonConfig = getJsonConfig(excludes);
        return JSONObject.fromObject(obj,jsonConfig).toString();
    }
    
    public String getJsonString(Object obj){
        return getJsonString(obj,null);
    }
    
    public void outJsonString(String str) {
        getResponse().setContentType("text/javascript;charset=UTF-8");
        outString(str);
    }

    public void outJson(Object obj) {
        outJson(obj,null);
    }
    
    
    /**
     * 输出JSON格式的数据。
     * @param obj
     * @param excludes 排除的属性列表
     */
    public void outJson(Object obj,String[] excludes) {
        outJsonString(getJsonString(obj,excludes));
    }
    
    public void outJsonArray(Object array) {
        outJsonArray(array,null);
    }
    
    /**
     * 输出JSON格式的数据。
     * @param array
     * @param excludes 排除的属性列表
     */
    public void outJsonArray(Object array,String[] excludes) {
        outJsonString(getJsonArray(array,excludes));
    }
}
