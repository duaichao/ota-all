package cn.sd.service.order;

import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import cn.sd.service.account.IAccountService;

@Repository
@Service("trafficOrderFacadeService")

public class TrafficOrderFacadeService implements ITrafficOrderFacadeService {

	@Resource
	private IAccountService accountService;
	
	/**
	 * 账户余额
	 * */
	public String yeStatService(String DEPART_ID) {
		return accountService.yeStatService(DEPART_ID);
	}
	
	
}
