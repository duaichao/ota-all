package cn.sd.service.common;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.core.util.CommonUtils;
import cn.sd.dao.common.ICommonDao;

@Service("commonService")
public class CommonService implements ICommonService{

	@Resource
	private ICommonDao commonDao;
	
	public void saveWebCollect(Map<String, Object> params){
		this.commonDao.saveWebCollect(params);
	}
	
	public void delWebCollect(Map<String, Object> params){
		this.commonDao.delWebCollect(params);
	}
	
	public List<Map<String, Object>> searchWebCollect(Map<String, Object> params){
		return this.commonDao.searchWebCollect(params);
	}
	
	public int countWebCollect(Map<String, Object> params){
		return this.commonDao.countWebCollect(params);
	}
	
	public int expenseSMS(Map<String,Object> params){
		int useCount = (int)Double.parseDouble(String.valueOf("2.5"));
		List<Map<String, Object>> data = this.commonDao.listUserSmsDao(params);
		//不存在充值充值记录,不能发送短信
		if(!CommonUtils.checkString(data) || data.size() != 1){
			return -1;
		}
		
		Map<String, Object> d = data.get(0);
		int enableCount = Integer.parseInt(String.valueOf(d.get("ENABLE_COUNT")));
		//可使用短信条数小于需要使用的条数.不能发送短信
		if(enableCount < useCount){
			return -2;
		}
		params.put("SMS_NUM", useCount);
		this.commonDao.usersmsGroupDao(params);
		return 0;
	}
	
	public List<Map<String, Object>> searchWebCategory(Map<String, Object> params){
		return this.commonDao.searchWebCategory(params);
	}
	
	public List<Map<String, Object>> serachADATTR(Map<String, Object> params){
		return this.commonDao.serachADATTR(params);
	}
	
	public List<Map<String, Object>> searchSiteManager(Map<String, Object> params){
		return this.commonDao.searchSiteManager(params);
	}
	
}
