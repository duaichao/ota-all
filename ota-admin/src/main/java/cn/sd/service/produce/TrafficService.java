package cn.sd.service.produce;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.lock.LockContainer;
import cn.sd.dao.produce.ITrafficDao;

@Repository
@Service("produceTrafficService")
@SuppressWarnings("all")
public class TrafficService extends BaseDaoImpl implements ITrafficService{
	
	@Resource
	private ITrafficDao produceTrafficDao;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.produceTrafficDao.listDao(params);
	}

	public int countService(Map<String,Object> params){
		return this.produceTrafficDao.countDao(params);
	}
	
}
