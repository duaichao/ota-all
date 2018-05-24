package cn.sd.controller.demo;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.ListRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.demo.IDemoService;
@RestController
@RequestMapping("/demo")
public class DemoController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(DemoController.class);
	@Resource
	private IDemoService demoService;
	
	
	
	/**
	 * 进入业务模块页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/demo/main");
	}
	/**
	 * 列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		params.put("start", start + 1);
		params.put("end", start + limit);
		return json;
	}
	/**
	 * 保存
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/save/{id}")
	public ListRange save(@PathVariable String id,
			HttpServletRequest request,
			HttpServletResponse response){
		ListRange json = new ListRange();
		
		return json;
	}
	/**
	 * 详情
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/detail/{id}")
	public ListRange detail(@PathVariable String id,
			HttpServletRequest request,
			HttpServletResponse response){
		ListRange json = new ListRange();
		
		json.setSuccess(true);
		return json;
	}
	/**
	 * 单条删除
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/delete/{id}")
	public ListRange delete(@PathVariable String id,
			HttpServletRequest request,
			HttpServletResponse response){
		ListRange json = new ListRange();
		//删除业务
		json.setSuccess(true);
		return json;
	}
	/**
	 * 多条删除
	 * @param ids
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/deletes/{ids}")
	public ListRange deletes(@PathVariable String ids,
			HttpServletRequest request,
			HttpServletResponse response){
		ListRange json = new ListRange();
		
		JSONArray jarray = JSONArray.fromObject(request.getParameter("ids"));
		Object[] objArray = jarray.toArray();
		Map<String,Object> params = new HashMap<String,Object>();
		for(int i=0;i<objArray.length;i++){
			JSONObject jobject = JSONObject.fromObject(objArray[i]);
			params.put("ID", jobject.getString("ID"));
			//删除业务
		}
		json.setSuccess(true);
		return json;
	}
}
