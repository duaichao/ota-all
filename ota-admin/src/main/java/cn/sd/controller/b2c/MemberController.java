package cn.sd.controller.b2c;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.b2c.IMemberService;

@RestController
@RequestMapping("/b2c/member")
public class MemberController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(MemberController.class);
	
	@Resource
	private IMemberService memberService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start);
			mr.pm.put("end", start+limit);
			List<Map<String, Object>> data = this.memberService.listService(mr.pm);
			int totalSize = this.memberService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询会员信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询会员信息异常");
		}
		return json;
	}
}
