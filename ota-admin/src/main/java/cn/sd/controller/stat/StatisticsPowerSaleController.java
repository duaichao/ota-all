package cn.sd.controller.stat;

import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.MapRange;

@RestController
@RequestMapping("/stat/sale")
public class StatisticsPowerSaleController {
	
	public static String firstward(String COMPANY_TYPE, Map<String, Object> user, String isParent, int child_company_cnt){
		if("0".equals(COMPANY_TYPE)){
			return "stat/sale/parent";
		}else if("1".equals(COMPANY_TYPE)){
			return "stat/sale";
		}else{
			String PID = (String)user.get("PID");
			if("-1".equals(PID) && child_company_cnt > 0){
				return "stat/sale/parent";
			}else if(CommonUtils.checkString(isParent) && "3".equals(isParent) && child_company_cnt > 0){
				return "stat/sale/parent";
			}else{
				return "stat/sale";
			}
		}
	}
	
	
	public static void powerCompany(MapRange mr , String COMPANY_TYPE, String cityId, Map<String, Object> params, String isParent, String userName){
		String COMPANY_USER_ID = (String)params.get("COMPANY_USER_ID");
		String COMPANY_ID = (String)params.get("COMPANY_ID");
		String USER_ID = (String)params.get("ID");
		String departIds = (String)params.get("departIds");
		if(userName.equals("admin")){
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}
		}else if("0".equals(COMPANY_TYPE)){
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}
			String USER_TYPE = (String)params.get("USER_TYPE");
			if("0101".equals(USER_TYPE)){
				mr.pm.put("SITE_USER_ID", params.get("COMPANY_USER_ID"));
			}else{
				mr.pm.put("SITE_USER_ID", (String)params.get("ID"));
			}
		}else if("1".equals(COMPANY_TYPE)){
			/**
			 * 总公司看公司数据
			 * 管理员看管理的部门数据
			 * 员工看自己的数据
			 */
			
		    if(CommonUtils.checkString(departIds)){
		    	JSONArray _departIds = JSONArray.fromObject(departIds);
		    	StringBuffer ids = new StringBuffer();
		    	for (Object departId : _departIds) {
		    		ids.append(departId+",");
				}
		    	mr.pm.put("SUPPLY_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
		    }else if(USER_ID.equals(COMPANY_USER_ID)){
		    	mr.pm.put("SUPPLY_COMPANY_ID", (String)params.get("COMPANY_ID"));
			}else{
				String DEPART_IDS = (String)params.get("DEPART_IDS");
				if(CommonUtils.checkString(DEPART_IDS)){
					DEPART_IDS =DEPART_IDS +","+params.get("DEPART_ID");
					mr.pm.put("SUPPLY_DEPART_IDS", DEPART_IDS.split(","));
				}else{
					mr.pm.put("SUPPLY_DEPART_ID", params.get("DEPART_ID"));
				}
			}
		}else{
			
			String companyId = (String)params.get("companyId");
			if(CommonUtils.checkString(companyId) || CommonUtils.checkString(departIds)){
				mr.pm.put("CREATE_COMPANY_ID", companyId);
				if(CommonUtils.checkString(departIds)){
					JSONArray _departIds = JSONArray.fromObject(departIds);
			    	StringBuffer ids = new StringBuffer();
			    	for (Object departId : _departIds) {
			    		ids.append(departId+",");
					}
			    	mr.pm.put("CREATE_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
				}
			}else{
				/**
				 * 总公司公司管理员
				 * 子公司/门市管理员
				 */
				String DEPART_IDS = (String)params.get("DEPART_IDS");
				String PID = (String)params.get("PID");
				
				if(USER_ID.equals(COMPANY_USER_ID) && "-1".equals(PID)){
					mr.pm.put("PID", COMPANY_ID);
				}else if(USER_ID.equals(COMPANY_USER_ID)){
					mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
				}else{

					//部门经理看部门员工的所有订单
					String IS_MANAGER = (String)params.get("IS_MANAGER");
					if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+params.get("DEPART_ID");
							mr.pm.put("CREATE_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							mr.pm.put("CREATE_DEPART_ID", params.get("DEPART_ID"));
						}
					}else{
						if(CommonUtils.checkString(DEPART_IDS)){
							mr.pm.put("CREATE_DEPART_IDS", DEPART_IDS.split(","));
							mr.pm.put("buyUserId", USER_ID);
						}else{
							mr.pm.put("CREATE_USER_ID", USER_ID);
						}
					}
					
				}
				
			}
			/**
			 * 总公司
			 * 子公司
			 */
			/*
			String PID = (String)params.get("PID");
			if(CommonUtils.checkString(isParent) && ("1".equals(isParent) || "2".equals(isParent))){
				mr.pm.put("CREATE_COMPANY_ID", (String)params.get("COMPANY_ID"));
			}else if("-1".equals(PID)){
				mr.pm.put("PID", (String)params.get("COMPANY_ID"));
			}else{
				mr.pm.put("CREATE_COMPANY_ID", (String)params.get("COMPANY_ID"));
			}
			*/
		}
	}
	
	public static void powerLevel(MapRange mr , String COMPANY_TYPE, String cityId, Map<String, Object> params, String isParent, String userName){
		
		String USER_ID = (String)params.get("ID");
		String COMPANY_USER_ID = (String)params.get("COMPANY_USER_ID");
		String IS_MANAGER = (String)params.get("IS_MANAGER");
		String departIds = (String)params.get("departIds");
		String COMPANY_ID = (String)params.get("COMPANY_ID");
		
		if(userName.equals("admin")){
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}
		}else if("0".equals(COMPANY_TYPE)){
			if(CommonUtils.checkString(cityId) && !cityId.equals("all")){
				mr.pm.put("CITY_ID", cityId);
			}
			String USER_TYPE = (String)params.get("USER_TYPE");
			if("0101".equals(USER_TYPE)){
				mr.pm.put("SITE_USER_ID", params.get("COMPANY_USER_ID"));
			}else{
				mr.pm.put("SITE_USER_ID", (String)params.get("ID"));
			}
		}else if("1".equals(COMPANY_TYPE)){
			/**
			 * 公司管理员
			 * 部门经理
			 * 员工
			 */
//			if(CommonUtils.checkString(COMPANY_ID) && !params.get("USER_TYPE").equals("0201")){
//				mr.pm.put("SUPPLY_COMPANY_ID", COMPANY_ID);
//			}else 
			if(CommonUtils.checkString(departIds)){
				JSONArray _departIds = JSONArray.fromObject(departIds);
		    	StringBuffer ids = new StringBuffer();
		    	for (Object departId : _departIds) {
		    		ids.append(departId+",");
				}
		    	mr.pm.put("SUPPLY_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
		    }else if(USER_ID.equals(COMPANY_USER_ID)){
		    	mr.pm.put("SUPPLY_COMPANY_ID", (String)params.get("COMPANY_ID"));
			}else{
				String DEPART_IDS = (String)params.get("DEPART_IDS");
				if(CommonUtils.checkString(DEPART_IDS)){
					DEPART_IDS =DEPART_IDS +","+params.get("DEPART_ID");
					mr.pm.put("SUPPLY_DEPART_IDS", DEPART_IDS.split(","));
				}else{
					mr.pm.put("SUPPLY_DEPART_ID", params.get("DEPART_ID"));
				}
			}
		}else{
			
			String companyId = (String)params.get("companyId");
			if(CommonUtils.checkString(companyId) || CommonUtils.checkString(departIds)){
				mr.pm.put("CREATE_COMPANY_ID", companyId);
				if(CommonUtils.checkString(departIds)){
					JSONArray _departIds = JSONArray.fromObject(departIds);
			    	StringBuffer ids = new StringBuffer();
			    	for (Object departId : _departIds) {
			    		ids.append(departId+",");
					}
			    	mr.pm.put("CREATE_DEPART_IDS", ids.toString().substring(0, ids.length() -1 ).split(","));
				}
			}else{
				/**
				 * 总公司公司管理员
				 * 子公司/门市管理员
				 */
				String DEPART_IDS = (String)params.get("DEPART_IDS");
				String PID = (String)params.get("PID");
				
				if(USER_ID.equals(COMPANY_USER_ID) && "-1".equals(PID)){
					mr.pm.put("PID", COMPANY_ID);
				}else if(USER_ID.equals(COMPANY_USER_ID)){
					mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
				}else{

					//部门经理看部门员工的所有订单
					if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+params.get("DEPART_ID");
							mr.pm.put("CREATE_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							mr.pm.put("CREATE_DEPART_ID", params.get("DEPART_ID"));
						}
					}else{
						if(CommonUtils.checkString(DEPART_IDS)){
							mr.pm.put("CREATE_DEPART_IDS", DEPART_IDS.split(","));
							mr.pm.put("buyUserId", USER_ID);
						}else{
							mr.pm.put("CREATE_USER_ID", USER_ID);
						}
					}
					
				}
				
			}
			
			/**
			 * 总公司
			 * 子公司
			 */
			/*
			String PID = (String)params.get("PID");
			
			if(CommonUtils.checkString(isParent) && ("1".equals(isParent) || "2".equals(isParent))){
				mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
			}else if(CommonUtils.checkString(COMPANY_ID) && !params.get("USER_TYPE").equals("0301")){
				mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
			}else if(USER_ID.equals(COMPANY_USER_ID)){
				if("-1".equals(PID)){
					mr.pm.put("PID", (String)params.get("COMPANY_ID"));
				}else{
					mr.pm.put("CREATE_COMPANY_ID", (String)params.get("COMPANY_ID"));
				}
			}else if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
				mr.pm.put("CREATE_DEPART_ID", (String)params.get("DEPART_ID"));
			}else{
				mr.pm.put("CREATE_USER_ID", USER_ID);
			}
			*/
			
		}
		
	}
	
	
	
}
