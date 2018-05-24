package cn.sd.service.site;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import cn.sd.core.filter.city.UserCity;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.IP;
import cn.sd.core.util.WebUtils;
import cn.sd.dao.site.ICompanyDao;
import cn.sd.dao.site.ISettingDao;
import java.util.LinkedList;

@Service("settingService")
public class SettingService implements ISettingService{
	
	@Resource
	private ISettingDao settingDao;
	@Resource
	private ICompanyDao companyDao;
	private static List<String> provinces = new LinkedList();
	private static Map<String, String> citys = new HashMap<String, String>();
	static{
		provinces.add("陕西");
		citys.put("陕西", "西安,咸阳");
	}
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.settingDao.listDao(params);
	}
	
	public void saveSiteService(Map<String,Object> params)throws Exception{
		this.settingDao.saveSiteDao(params);
	}
	
	public void editSiteUseStatusService(Map<String,Object> params)throws Exception{
		this.settingDao.editSiteUseStatusDao(params);
	}
	
	public List<Map<String, Object>> usableSitesService(Map<String,Object> params){
		return this.settingDao.usableSitesDao(params);
	}
	
	public UserCity initCityManager(HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		String cityId = "";
		String cityName = "";
		String cityPinYin = "";
		String cookieName = "XSX-SITE-KEY";
		

		UserCity userCity = new UserCity();
		
		/**
		 * 第一步 查询缓存是否存在当前城市
		 */
		if(cityName == null || "".equals(cityName)){
			cityName = WebUtils.getCookieValue(request,cookieName);
		}
		
		/**
		 * 第二步 根据IP判断当前城市
		 */
		if(cityName == null || "".equals(cityName)){
			cityName = "西安";
			String areaInfo =  "";
			if(CommonUtils.checkString(WebUtils.getIpAddress(request))){
				areaInfo = Arrays.toString(IP.find(WebUtils.getIpAddress(request)));
			}
			for (String province : provinces) {
				if(areaInfo.indexOf(province) != -1){
					String[] _citys = citys.get(province).split(",");
					cityName = _citys[0];
					for (String city : _citys) {
						if(areaInfo.indexOf(city) != -1){
							cityName = city;
						}
					}
				}
			}

		}
		
		if(CommonUtils.checkString(cityName)){
			parameters.clear();
			parameters.put("QUERY_SUB_AREA", cityName);
			List<Map<String, Object>> citys = companyDao.listSiteManagerDao(parameters);
			if(CommonUtils.checkList(citys)){
				Map<String, Object> city = citys.get(0);
				cityId = (String)city.get("CITY_ID");
				cityName = (String)city.get("SUB_AREA");
				cityPinYin = (String)city.get("PINYIN");
			}
		}

		if(!CommonUtils.checkString(cityId)){
			cityName = "西安";
			cityPinYin = "xian";
			cityId = "0C9DA31F90BAB047E050007F0100C19F";
		}
		
		/**
		 * 第四步 重新设置Cookie
		 */
		if(!cityName.equals(WebUtils.getCookieValue(request,cookieName))){
			WebUtils.saveCookieValue(response, cookieName, cityName, 0);
		}
		
		/**
		 * 第五步 设置session
		 * **/
		userCity.setLocalCityPinYin(cityPinYin);
		userCity.setLocalCity(cityName.replace("市", ""));
		userCity.setLocalCityID(cityId);
		
		return userCity;
	}
	
	public List<Map<String, Object>> listSiteAttrService(Map<String,Object> params){
		return this.settingDao.listSiteAttrDao(params);
	}
	
	public void saveSiteAttrService(Map<String,Object> params)throws Exception{
		this.settingDao.saveSiteAttrDao(params);
	}
	
	public void updateSiteAttrService(Map<String,Object> params)throws Exception{
		this.settingDao.updateSiteAttrDao(params);
	}
}
