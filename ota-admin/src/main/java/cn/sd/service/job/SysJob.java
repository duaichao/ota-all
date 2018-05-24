package cn.sd.service.job;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.sd.rmi.RemoteServiceImpl;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.service.b2b.ISMSService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.site.ICompanyService;

public class SysJob {
	
	@Resource
	private ISMSService smsService;
	
	@Resource
	private IOrderService orderService;
	
	@Resource
	private ICompanyService companyService;
	
	public void sendSmsMain(){
		smsService.sendGroupSmsService();	
	}
	
	public void reSeat(){
		//所有在线支付/付款中/未掉单的所有订单/定金付款中
		try {
//			ServiceProxyFactory.getProxy().setReSeatlock(1);
			orderService.reSeatService();
			orderService.reSeatYYService();
			orderService.cancelRenewOrderService();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
//			ServiceProxyFactory.getProxy().setReSeatlock(0);
		}
//		
	}
	
	/**
	 * 系统自动退团
	 */
	public void resetEarnestType(){
		/**
		 * 1.已付定金
		 * 2.超过时间未付尾款或未确认的订单
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			this.orderService.batchResetEarnestTypeService(params);
			this.orderService.autoRefundEarnestService(params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		}
	}
	
	/**
	 * 系统重置支付密码错误次数
	 * 重置远程服务器用户登录信息
	 */
	public void resetPayErrorCnt(){
		/**
		 * 1.已付定金
		 * 2.超过时间未付尾款或未确认的订单
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			this.companyService.resetPayErrorCntService(params);
			
			/**
			 * 远程服务器充值用户登录信息
			 */
			if(ServiceProxyFactory.getFlag() == 0){
				RemoteServiceImpl.clearLoginInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		}
	}
}
