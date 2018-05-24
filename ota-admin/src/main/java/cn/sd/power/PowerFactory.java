package cn.sd.power;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PowerFactory {

	private PowerFactory(){}
	
	public static Map<String, Object> getPower(HttpServletRequest request, HttpServletResponse response, String modId, String modName, Map<String, Object> params){
		if(modName.equals("traffic-list") || modName.equals("route-list") || modName.equals("AD-list") || modName.equals("news-list")){
			CascadePower.getPower(request, response, modId, modName, params);
		}else if(modName.equals("site-company")){
			CascadePower.getSiteAndCompanyPower(request, response, modId, modName, params);
		}else if(modName.equals("order-list")){
//			CascadePower.getOrderPower(request, response, modId, modName, params);
		}
		return params;
	}
	
}
