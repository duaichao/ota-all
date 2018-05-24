package cn.sd.core.filter.city;

import javax.servlet.http.HttpSession;

public interface IUserCityReader {
	/**
	 * 从Session中得到登录用户的城市信息
	 * @param session
	 * @return
	 */
	public String getCityInSession(HttpSession session);
	
	/**
	 * 根据IP地址得到用户的城市
	 * @param ip
	 * @return
	 */
	public String getCityByIpAddress(String ip);
	
	/**
	 * 得到城市的拼音
	 * @param city
	 * @return
	 */
	public String getCityPinYin(String city);
	
	/**
	 * 拼音得到城市
	 * @param city
	 * @return
	 */
	public String getCityByPinYin(String cityPY);
}
