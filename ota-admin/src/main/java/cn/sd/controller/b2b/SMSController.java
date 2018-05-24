package cn.sd.controller.b2b;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.core.util.wordfilter.FilteredResult;
import cn.sd.core.util.wordfilter.WordFilterUtil;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.b2b.ISMSService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2b/sms")
public class SMSController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(SMSController.class);
	
	@Resource
	private ISMSService smsService;
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request,HttpServletResponse response,MapRange mr, String type){
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		String PID = (String)user.get("PID");
		if(!user.get("USER_NAME").equals("admin")){
			
			if(COMPANY_TYPE.equals("0")){
				params.put("T_USER_ID", (String)user.get("ID"));
				params.put("T_COMPANY_ID", (String)user.get("ID"));
				params.put("HAS_CITY", "YES");
				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
				if(CommonUtils.checkList(siteManagers)){
					StringBuffer siteIds = new StringBuffer();
					for (Map<String, Object> siteManager : siteManagers) {
						String id = (String)siteManager.get("ID");
						siteIds.append("'"+id+"',");
					}
					mr.pm.put("SITE_ID", siteIds.substring(0, siteIds.toString().length()-1));
				}
			}else{
				if(PID.equals("0")){
					mr.pm.put("COMPANY_PID", user.get("COMPANY_ID"));
				}else{
					mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
				}
			}
		}
		
		String query = toString(request.getParameter("query"));
		try {
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("QUERY", query);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ListRange json = new ListRange();
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		
		mr.pm.put("start", start + 1);
		mr.pm.put("end", start + limit);
		mr.pm.put("TYPE", type);
		List<Map<String,Object>> data = smsService.listService(mr.pm);
		int totalSize = smsService.countService(mr.pm);
		json.setData(data);
		json.setTotalSize(totalSize);
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("/batchSend")
	public ListRange batchSend(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String, Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String, Object> site = (Map<String, Object>)request.getSession().getAttribute("S_SITE_SESSION_KEY");
		ListRange json = new ListRange();
		String CONTENT = (String)mr.pm.get("CONTENT");
		FilteredResult result =  WordFilterUtil.filterText(CONTENT, '*');//------------------------
		int error = result.getFilteredContent().indexOf("*");
		if(error!=-1){
			json.setSuccess(false);
			json.setStatusCode("-1");
			json.setMessage("非法字符:"+result.getBadWords());
			return json;
		}
		mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
		int flag = this.smsService.smsSendStatusService(mr.pm);
		if(flag==0){
			json.setSuccess(false);
			json.setStatusCode("-2");
			json.setMessage("群发短信没有开通或没有足够条数");
			return json;
		}
		
		Set<String> send = new HashSet<String>();
		String[] mobiles = ((String)mr.pm.get("MOBILES")).split(",");
		for(String mobile : mobiles){
			if(mobile != null && !"".equals(mobile) && send.contains(mobile) == false){
				send.add(mobile);
			}
		}
		double c = 50.00;
		double cnt = Math.ceil(Double.parseDouble(String.valueOf(CONTENT.length()))/c);
		mr.pm.put("SMS_NUM", cnt * mobiles.length);
		
		flag = this.smsService.smsSendStatusGroupService(mr.pm);
		if(flag==0){
			json.setSuccess(false);
			json.setStatusCode("-3");
			json.setMessage("剩余条数不足");
			return json;
		}
		
		boolean b = this.smsService.voild(((String)mr.pm.get("MOBILES")), CONTENT, "4");
		if(b){
			this.smsService.usersmsGroupService(mr.pm);
			
			this.smsService.iniTask(send, CONTENT, (String)user.get("ID"), "7", "4", (String)site.get("ID"), (String)user.get("USER_NAME"));
		}else{
			json.setSuccess(false);
			json.setStatusCode("-4");//短信验证码超过5次，请于一小时后重新发送
			return json;
		}
		
		json.setSuccess(true);
		return json;
	}
}
