package cn.sd.service.traffic;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.dao.traffic.ITrafficDao;

@Service("trafficService")
public class TrafficService implements ITrafficService {

	@Resource ITrafficDao trafficDao;
	
	public List<Map<String, Object>> searchTrafficMuster(Map<String,Object> params){
		return this.trafficDao.searchTrafficMuster(params);
	}
	
	public List<Map<String, Object>> searchPriceAttr(Map<String,Object> params){
		return this.trafficDao.searchPriceAttr(params);
	}
}
