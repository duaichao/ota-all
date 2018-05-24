package cn.sd.service.produce;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.dao.produce.IRouteDao;

@Repository
@Service("produceRouteService")
public class RouteService extends BaseDaoImpl implements IRouteService{
	
	@Resource
	private IRouteDao produceRouteDao;
	
	public List<Map<String, Object>> listRenewService(Map<String, Object> params){
		return this.produceRouteDao.listRenewDao(params);
	}
	
	public int countRenewService(Map<String, Object> params){
		return this.produceRouteDao.countRenewDao(params);
	}
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.produceRouteDao.listDao(params);
	}

	public int countService(Map<String,Object> params){
		return this.produceRouteDao.countDao(params);
	}
	
	public List<Map<String, Object>> listCompanyService(Map<String,Object> params){
		return this.produceRouteDao.listCompanyDao(params);
	}
	
	public List<Map<String, Object>> listLabelService(Map<String,Object> params){
		return this.produceRouteDao.listLabelDao(params);
	}
	
	public List<Map<String, Object>> listRouteTrafficMusterService(Map<String,Object> params){
		return this.produceRouteDao.listRouteTrafficMusterDao(params);
	}
	
	public List<Map<String, Object>> listSingleRouteService(Map<String,Object> params){
		return this.produceRouteDao.listSingleRouteDao(params);
	}

	public int countSingleRouteService(Map<String,Object> params){
		return this.produceRouteDao.countSingleRouteDao(params);
	}
	
	public void saveWapPriceService(Map<String,Object> params) throws Exception{
		this.produceRouteDao.saveWapPriceDao(params);
	}
	public void delWapPriceService(Map<String,Object> params)throws Exception{
		this.produceRouteDao.delWapPriceDao(params);
	}
	public List<Map<String, Object>> listWapPriceService(Map<String,Object> params){
		return this.produceRouteDao.listWapPriceDao(params);
	}
}
