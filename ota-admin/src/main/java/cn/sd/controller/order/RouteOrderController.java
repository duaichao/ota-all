package cn.sd.controller.order;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.core.util.word.XXWPFDocument;
import cn.sd.core.web.BaseController;
import cn.sd.service.b2b.ISMSService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.order.IVisitorOrderService;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/order/route")
public class RouteOrderController extends BaseController{

	private static Log log = LogFactory.getLog(RouteOrderController.class);
	
	@Resource
	private IOrderService orderService;
	@Resource
	private ICompanyService companyService;
	@Resource
	private IRouteService routeService;
	@Resource
	private IVisitorOrderService visitorOrderService;
	@Resource
	private ISMSService smsService;
	
	@RequestMapping("/edit/visitor")
	public ListRange editVisitor(HttpServletRequest request,HttpServletResponse response, MapRange mr, String visitors){
		ListRange json = new ListRange();
		try {
			
		    JSONArray jarray = JSONArray.fromObject(visitors);
			Object[] objArray = jarray.toArray();
			String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT", "ID"};
		    List<Map<String, Object>> datas = CommonUtils.jsonArrayToList(objArray, keyNames);
		    for (Map<String, Object> data : datas) {
				mr.pm.clear();
				mr.pm.put("ID", (String)data.get("ID"));
				mr.pm.put("NAME", (String)data.get("NAME"));
				mr.pm.put("SEX", (String)data.get("SEX"));
				mr.pm.put("MOBILE", (String)data.get("MOBILE"));
				mr.pm.put("CARD_TYPE", (String)data.get("CARD_TYPE"));
				mr.pm.put("CARD_NO", (String)data.get("CARD_NO"));
				this.visitorOrderService.updateOrderVisitorService(mr.pm);
				
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("编辑游客异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/del")
	public ListRange delOrder(HttpServletRequest request,HttpServletResponse response, MapRange mr, String models){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("DEL_USER", user.get("ID"));
				params.put("ORDER_ID", jobject.getString("ID"));
				this.orderService.delOrderService(params);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除订单异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/save/contact")
	public ListRange saveContact(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("models", models);
			this.orderService.saveOrderContactService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存订单联系人异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存订单联系人异常");
		}
		return json;
	}
	
	@RequestMapping("/list/contact")
	public ListRange listConatct(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ORDER_ID", orderId);
			List<Map<String, Object>> data = this.orderService.listOrderContactService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询订单联系人异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询订单联系人异常");
		}
		return json;
	}
	
	@RequestMapping("/comfirm/instead/pay")
	public ListRange confirmInsteadPay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String id){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			/**
			 * 供应商确认代收付款
			 * 定金同行/外卖 覆盖原 同行/外面价格
			 * 记录原同行/外卖价格
			 * 修改订单状态（付款完成）、定金状态（确认代收）
			 * 备注
			 */
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			this.orderService.confirmInsteadPayService(mr.pm);
		} catch (Exception e) {
			log.error("保存订单备注异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存订单备注异常");
		}
		return json;
	}
	
	@RequestMapping("/save/remark")
	public ListRange startRemark(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			this.orderService.updateOrderBaseRemarkService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存订单备注异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存订单备注异常");
		}
		return json;
	}
	
	@RequestMapping("/export/start/confirm")
	public void export(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		try {
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			mr.pm.put("ID", orderId);
			Map<String, Object> order = this.orderService.listRouteOrderService(mr.pm).get(0);
			
			String file_path = ConfigLoader.config.getStringConfigDetail("system.file.path")+"/start_confirm.docx";
			FileInputStream is = new FileInputStream(new File(file_path));
			XXWPFDocument xdoc = new XXWPFDocument(is);
		    XWPFTable table = xdoc.getTables().get(0);  
		    List<XWPFTableRow> rows = table.getRows(); 
		    
	        rows.get(0).getTableCells().get(1).setText(order.get("BUY_COMPANY").toString());
	        rows.get(0).getTableCells().get(3).setText(order.get("SALE_COMPANY").toString());
	        
	        rows.get(1).getTableCells().get(1).setText((String)order.get("BUY_USER_NAME"));
	        rows.get(1).getTableCells().get(3).setText((String)order.get("SALE_CHINA_NAME"));
	        if(CommonUtils.checkString(order.get("BUY_PHONE"))){
	        	rows.get(2).getTableCells().get(1).setText((String)order.get("BUY_PHONE"));
	        }
	        if(CommonUtils.checkString(order.get("SALE_PHONE"))){
	        	rows.get(2).getTableCells().get(3).setText((String)order.get("SALE_PHONE"));
	        }
	        
	        rows.get(3).getTableCells().get(1).setText(order.get("PRODUCE_NO").toString());
	        rows.get(3).getTableCells().get(3).setText(order.get("START_DATE").toString());
		    
	        rows.get(4).getTableCells().get(1).setText(order.get("PRODUCE_NAME").toString());
	        
	        int MAN_COUNT = Integer.parseInt(String.valueOf(order.get("MAN_COUNT")));
	        int CHILD_COUNT = Integer.parseInt(String.valueOf(order.get("CHILD_COUNT")));
	        int PERSON_COUNT = MAN_COUNT + CHILD_COUNT;
	        rows.get(5).getTableCells().get(1).setText(String.valueOf(PERSON_COUNT));
	        
	        mr.pm.clear();
	        mr.pm.put("ORDER_ID", orderId);
	        mr.pm.put("start", 1);
	        mr.pm.put("end", 1000);
	        List<Map<String, Object>> visitors = this.visitorOrderService.listOrderVisitorService(mr.pm);
	        StringBuffer v = new StringBuffer();
	        String VISITOR_CONCAT = (String)order.get("VISITOR_CONCAT");
	        for (Map<String, Object> visitor : visitors) {
				v.append(visitor.get("NAME") + "   " + visitor.get("CARD_NO"));
				if(CommonUtils.checkString(visitor.get("MOBILE"))){
					v.append("    联系方式");
					if(VISITOR_CONCAT.equals(visitor.get("NAME").toString())){
						v.append("(联系人)");
					}
					v.append(":"+visitor.get("MOBILE"));
				}
				v.append(" \r\n ");
			}
	        rows.get(6).getTableCells().get(1).setText(v.toString());//客人名单
	        
	        //地接单价+交通单价
	        List<Map<String, Object>> orderPrices = this.orderService.searchOrderPriceService(mr.pm);
	        List<Map<String, Object>> orderPriceMains = this.orderService.searchOrderPriceMainService(mr.pm);
	        
	        double dj_man_price = 0.0, dj_child_price = 0.0, jt_man_price = 0.0, jt_child_price = 0.0;
	        if(CommonUtils.checkList(orderPrices)){
	        	for (Map<String, Object> orderPrice : orderPrices) {
	        		if(orderPrice.get("TYPE_ID").toString().equals("0FA5123749D38C87E050007F0100BCAD") && orderPrice.get("ATTR_ID").toString().equals("0FA5123749CF8C87E050007F0100BCAD")){
	        			dj_man_price = Double.parseDouble(String.valueOf(orderPrice.get("PRICE")));
		        	}else if(orderPrice.get("TYPE_ID").toString().equals("0FA5123749D38C87E050007F0100BCAD") && orderPrice.get("ATTR_ID").toString().equals("2D80E611DBA35261E050007F0100ED30")){
		        		dj_child_price = Double.parseDouble(String.valueOf(orderPrice.get("PRICE")));
		        	}
				}
	        	
	        }
	        if(CommonUtils.checkList(orderPriceMains)){
	        	for (Map<String, Object> orderPriceMain : orderPriceMains) {
	        		if(orderPriceMain.get("TYPE_ID").toString().equals("0FA5123749D38C87E050007F0100BCAD") && orderPriceMain.get("ATTR_ID").toString().equals("0FA5123749CF8C87E050007F0100BCAD")){
	        			jt_man_price = Double.parseDouble(String.valueOf(orderPriceMain.get("PRICE")));
		        	}else if(orderPriceMain.get("TYPE_ID").toString().equals("0FA5123749D38C87E050007F0100BCAD") && orderPriceMain.get("ATTR_ID").toString().equals("2D80E611DBA35261E050007F0100ED30")){
		        		jt_child_price = Double.parseDouble(String.valueOf(orderPriceMain.get("PRICE")));
		        	}
				}
	        }
	        String man_price_content = String.valueOf((dj_man_price+jt_man_price)) + "*" +  MAN_COUNT+"(成人)";
	        String child_price_content = String.valueOf((dj_child_price+jt_child_price)) + "*" +  CHILD_COUNT+"(儿童)";
	        rows.get(7).getTableCells().get(1).setText(man_price_content + " + " + child_price_content + " = " + (((dj_man_price+jt_man_price)*MAN_COUNT)+((dj_child_price+jt_child_price)* CHILD_COUNT)));
	        
	        mr.pm.clear();
	        mr.pm.put("COMPANY_ID", order.get("COMPANY_ID"));
	        List<Map<String, Object>> companyBanks = this.companyService.searchCompanyBankService(mr.pm);
	        if(CommonUtils.checkList(companyBanks) && companyBanks.size() == 1){
	        	if(CommonUtils.checkString(companyBanks.get(0).get("OPEN_ADDRESS"))){
	        		rows.get(8).getTableCells().get(1).setText((String)companyBanks.get(0).get("OPEN_ADDRESS"));//账户信息
	        	}
	        	if(CommonUtils.checkString(companyBanks.get(0).get("BANK_NO"))){
	        		rows.get(8).getTableCells().get(2).setText((String)companyBanks.get(0).get("BANK_NO"));	
	        	}
	        }
	        
	        if(CommonUtils.checkString(order.get("REMARK"))){
	        	rows.get(9).getTableCells().get(1).setText((String)order.get("REMARK"));//备注
	        }
		    
	        XWPFParagraph p = xdoc.createParagraph();
		    String BUY_COMPANY_SIGN = (String)order.get("BUY_COMPANY_SIGN");
		    
		    
		    String pic_path = "";
		    if(CommonUtils.checkString(BUY_COMPANY_SIGN)){
		    	pic_path = ConfigLoader.config.getStringConfigDetail("upload.path")+"/"+BUY_COMPANY_SIGN;
		    }else{
		    	pic_path = ConfigLoader.config.getStringConfigDetail("system.file.path")+"/default_sign.png";
		    }
		    
		    FileInputStream sale_fis = new FileInputStream(pic_path);
		    FileInputStream _sale_fis = new FileInputStream(pic_path);
	    	BufferedImage sale_bufferedImg = ImageIO.read(_sale_fis);
	    	int sale_imgWidth = sale_bufferedImg.getWidth(), sale_imgHeight = sale_bufferedImg.getHeight();
	    	byte [] sale_picbytes = IOUtils.toByteArray(sale_fis);
	    	xdoc.addPictureData(sale_picbytes, XWPFDocument.PICTURE_TYPE_PNG);
	    	xdoc.createPicture(p,xdoc.getAllPictures().size()-1, sale_imgWidth, sale_imgHeight,""); 
	    	
	    	sale_bufferedImg.flush();
	    	_sale_fis.close();
	    	sale_fis.close();
	    	
		    String START_CONFIRM = String.valueOf(order.get("START_CONFIRM"));
		    if(START_CONFIRM.equals("1")){
		    	String SALE_COMPANY_SIGN = (String)order.get("SALE_COMPANY_SIGN");
		    	pic_path = ConfigLoader.config.getStringConfigDetail("upload.path")+"/"+SALE_COMPANY_SIGN;
		    	
		    	FileInputStream buy_fis = new FileInputStream(pic_path);
			    FileInputStream _buy_fis = new FileInputStream(pic_path);
		    	BufferedImage buy_bufferedImg = ImageIO.read(_buy_fis);
		    	int buy_imgWidth = buy_bufferedImg.getWidth(), buy_imgHeight = buy_bufferedImg.getHeight();
		    	byte [] buy_picbytes = IOUtils.toByteArray(buy_fis);
		    	xdoc.addPictureData(buy_picbytes, XWPFDocument.PICTURE_TYPE_PNG);
		    	xdoc.createPicture(p,xdoc.getAllPictures().size()-1, buy_imgWidth, buy_imgHeight,""); 
		    	
		    	buy_bufferedImg.flush();
		    	_buy_fis.close();
		    	buy_fis.close();
		    }
		    
		    response.setContentType("application/vnd.ms-word");
	        response.setHeader("Content-disposition", "attachment;filename="+DateUtil.getNowDate()+".docx");    
		    ServletOutputStream sos = response.getOutputStream();
		    xdoc.write(sos);
		    sos.flush();    
		    sos.close();
	    	
		} catch (Exception e) {
			log.error("查询线路详情异常", e);
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/start/confirm")
	public ListRange startConfirm(HttpServletRequest request, HttpServletResponse response, MapRange mr, String models){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			JSONArray jarray = JSONArray.fromObject(models);
			Object[] objArray = jarray.toArray();
			for(int i=0;i<objArray.length;i++){
				JSONObject jobject = JSONObject.fromObject(objArray[i]);
				mr.pm.put("orderId", jobject.get("ID"));
				this.orderService.updateStartConfirmByIdService(mr.pm);
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("确认出团异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("确认出团异常");
		}
		return json;
	}
	
	@RequestMapping("/audit/inter/float")
	public ListRange auditInterFloat(HttpServletRequest request, HttpServletResponse response, MapRange mr, String auditStatus){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			Map<String, Object> detailOrder = this.orderService.detailOrderService(mr.pm);
			double INTER_AMOUNT = Double.parseDouble(String.valueOf(detailOrder.get("INTER_AMOUNT")));
			double INTER_FLOAT = Double.parseDouble(String.valueOf(detailOrder.get("INTER_FLOAT")));
			double INTER_FLOAT_TEMP = Double.parseDouble(String.valueOf(detailOrder.get("INTER_FLOAT_TEMP")));
			
			mr.pm.put("INTER_AMOUNT", INTER_AMOUNT+INTER_FLOAT_TEMP);
			mr.pm.put("INTER_FLOAT", INTER_FLOAT+INTER_FLOAT_TEMP);
			mr.pm.put("auditStatus", auditStatus);
			String content = "";
			if(auditStatus.equals("2")){
				mr.pm.put("AUDIT_INTER_FLOAT", "2");
				content += "审核通过<br />";
			}else{
				mr.pm.put("AUDIT_INTER_FLOAT", "3");
				content += "审核不通过<br />";
			}
			content += "同行金额&nbsp;&nbsp;&nbsp;&nbsp;:  "+INTER_AMOUNT+" <br />议价金额&nbsp;&nbsp;&nbsp;&nbsp;:  "+INTER_FLOAT_TEMP+" <br />议价后金额  :  "+(INTER_AMOUNT+INTER_FLOAT_TEMP);
			if(mr.pm.get("REMARK") != null){
				content += " <br />"+(String)mr.pm.get("REMARK");
			}
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("CONTENT", content);
			this.orderService.updateOrderInterFloatService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("同行议价申请异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("同行议价申请异常");
		}
		return json;
	}
	
	@RequestMapping("/inter/float")
	public ListRange interFloat(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			Map<String, Object> detailOrder = this.orderService.detailOrderService(mr.pm);
			double SALE_AMOUNT = Double.parseDouble(String.valueOf(detailOrder.get("INTER_AMOUNT")));
			
			double _INTER_FLOAT = Double.parseDouble(String.valueOf(mr.pm.get("INTER_FLOAT")));
			
			mr.pm.put("INTER_FLOAT_TEMP", _INTER_FLOAT);
			String content = "同行金额&nbsp;&nbsp;&nbsp;&nbsp;:  "+SALE_AMOUNT+" <br />议价金额&nbsp;&nbsp;&nbsp;&nbsp;:  "+_INTER_FLOAT+" <br />议价后金额  :  "+(SALE_AMOUNT+_INTER_FLOAT);
			if(mr.pm.get("REMARK") != null){
				content += " <br />"+(String)mr.pm.get("REMARK");
			}
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("CONTENT", content);
			this.orderService.updateOrderInterFloatTempService(mr.pm);
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("同行议价申请异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("同行议价申请异常");
		}
		return json;
	}
	
	
	@RequestMapping("/sale/float")
	public ListRange saleFloat(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			Map<String, Object> detailOrder = this.orderService.detailOrderService(mr.pm);
			double SALE_AMOUNT = Double.parseDouble(String.valueOf(detailOrder.get("SALE_AMOUNT")));
			double SALE_FLOAT = Double.parseDouble(String.valueOf(detailOrder.get("SALE_FLOAT")));
			
			double _SALE_FLOAT = Double.parseDouble(String.valueOf(mr.pm.get("SALE_FLOAT")));
			
			mr.pm.put("SALE_AMOUNT", SALE_AMOUNT+_SALE_FLOAT);
			mr.pm.put("SALE_FLOAT", SALE_FLOAT+_SALE_FLOAT);
			String content = "外卖金额&nbsp;&nbsp;&nbsp;&nbsp;:  "+SALE_AMOUNT+" <br />议价金额&nbsp;&nbsp;&nbsp;&nbsp;:  "+_SALE_FLOAT+" <br />议价后金额  :  "+(SALE_AMOUNT+_SALE_FLOAT);
			if(mr.pm.get("REMARK") != null){
				content += " <br />"+(String)mr.pm.get("REMARK");
			}
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("CONTENT", content);
			this.orderService.updateOrderSaleFloatService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("外卖议价异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("外卖议价异常");
		}
		return json;
	}
	
	@RequestMapping("/other/fee/logs")
	public ListRange otherFee(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ORDER_ID", orderId);
			List<Map<String, Object>> data = this.orderService.listOrderOtherPriceService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("审核附加费用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("审核附加费用异常");
		}
		return json;
	}
	
	@RequestMapping("/other/fee/audit")
	public ListRange otherFeeAudit(HttpServletRequest request, HttpServletResponse response, MapRange mr, String auditStatus){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		try {
			String orderId = String.valueOf(mr.pm.get("ORDER_ID"));
			mr.pm.put("ID", orderId);
			Map<String, Object> order = this.orderService.detailOrderService(mr.pm);
			
			String content = "", phone = (String)order.get("SALE_PHONE"), mobile = (String)order.get("BUY_MOBILE");
			
			Map<String, Object> p = new HashMap<String, Object>();
			
			if(CommonUtils.checkString(mobile)){

				if(!CommonUtils.checkString(phone)){
					phone = (String)order.get("SALE_MOBILE");
				}
				
				if(auditStatus.equals("2")){
					content = order.get("BUY_COMPANY")+"：您预订的订单号为"+order.get("NO")+"的产品已审核通过，请尽快付款。如有疑问，请联系"+order.get("SALE_COMPANY_NAME")+"客服"+phone+"咨询";
				}else{
					content = order.get("BUY_COMPANY")+"：您预订的订单号为"+order.get("NO")+"的产品审核未通过。如有疑问，请联系"+order.get("SALE_COMPANY_NAME")+"客服"+phone+"咨询";
				}
				/**
				 * 扣供应商短信条数
				 */
				p.clear();
				p.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				double c = 50.00;
				double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
				p.put("useCount", cnt);
				int status = this.smsService.expenseSMS(p);
				if(status == 0){
					SMSSender.sendSMS(mobile, content, (String)user.get("USER_NAME"), "", "8", "", "2", "0", orderId, (String)user.get("COMPANY_ID"), cnt);
				}
			}
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", mr.pm.get("ORDER_ID"));
			p.put("TITLE", "其他费用审核");//-----------------------------
			p.put("STEP_USER", (String)user.get("user_name"));
			p.put("STEP_USER_ID", (String)user.get("ID"));
			p.put("REMARK", mr.pm.get("OTHER_SUPPLY_CONTENT"));//-----------------------------;
			
			this.orderService.saveOrderStepService(p);
			
			mr.pm.put("OTHER_SUPPLY_AUDIT", auditStatus);
			this.orderService.updateOrderOtherAuditService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("审核附加费用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("审核附加费用异常");
		}
		return json;
	}
	
	@RequestMapping("/contract/status")
	public ListRange contract(HttpServletRequest request, HttpServletResponse response, MapRange mr, String no){
		ListRange json = new ListRange();
		try {
			mr.pm.put("NO", no);
			Map<String, Object> d = this.orderService.detailContractAgencyByNoService(mr.pm);
			json.setMessage(String.valueOf(d.get("STATUS")));
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询合同状态异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询合同状态异常");
		}
		return json;
	}
	
	@RequestMapping("/traffics")
	public ListRange traffics(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ORDER_ID", orderId);
			List<Map<String, Object>> data = this.orderService.searchOrderRouteTraffic(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路订单交通信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路订单交通信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save/contract")
	public ListRange saveContract(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			this.orderService.saveContractService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路订单信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路订单信息异常");
		}
		return json;
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderTrade, 
			String SALER, String BUYER, String CREATE_BEGIN_TIME, String CREATE_END_TIME, String PAY_BEGIN_TIME, String PAY_END_TIME, 
			String query, String BEGIN_CITY, String END_CITY, String STATUS, String ACCOUNT_STATUS, String orderStatus, String IS_CANCEL
			,String PAY_WAY, String GO_BEGIN_TIME, String GO_END_TIME){
		Map<String, Object> params = new HashMap<String, Object>();
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
				
			String USER_ID = (String)user.get("ID"),
			COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID"),
			COMPANY_ID = (String)user.get("COMPANY_ID"),
			COMPANY_TYPE = (String)user.get("COMPANY_TYPE"),
			USER_NAME = (String)user.get("USER_NAME"),
			DEPART_IDS = (String)user.get("DEPART_IDS"),
			COMPANY_PID = (String)user.get("PID");
			if("0".equals(COMPANY_TYPE)){
				params.put("T_USER_ID", USER_ID);
				params.put("T_COMPANY_ID", COMPANY_ID);
				params.put("HAS_CITY", "YES");
				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
				if(CommonUtils.checkList(siteManagers)){
					StringBuffer siteIds = new StringBuffer();
					for (Map<String, Object> siteManager : siteManagers) {
						String id = (String)siteManager.get("ID");
						siteIds.append("'"+id+"',");
					}
					mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
				}
			}else if(!USER_NAME.equals("admin")){
				/**
				 * 默认0 平台管理公司 1供应商公司2分销商公司3门市部4子公司5同行6商务中心
				 * 公司管理员看公司订单
				 * 	旅行社员工查询自己下的订单
				 * 	供应商员工查询自己发的产品订单
				 */
				if("1".equals(COMPANY_TYPE)){
					if(USER_ID.equals(COMPANY_USER_ID)){
						mr.pm.put("COMPANY_ID", COMPANY_ID);
					}else{
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
							mr.pm.put("SALE_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
						}
						
						//部门经理看部门员工的所有订单
//						String IS_MANAGER = (String)user.get("IS_MANAGER");
//						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
//							mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
//						}else{
//							mr.pm.put("PUB_USER_ID", USER_ID);
//						}
						
					}
				}else{
					if(USER_ID.equals(COMPANY_USER_ID)){
						if(COMPANY_PID.equals("-1")){
							mr.pm.put("COMPANY_PID", COMPANY_ID);
						}else{
							mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
						}
					}else{
						
						//部门经理看部门员工的所有订单
						String IS_MANAGER = (String)user.get("IS_MANAGER");
						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
							if(CommonUtils.checkString(DEPART_IDS)){
								DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
								mr.pm.put("BUY_DEPART_IDS", DEPART_IDS.split(","));
							}else{
								mr.pm.put("BUY_DEPART_ID", user.get("DEPART_ID"));
							}
						}else{
							if(CommonUtils.checkString(DEPART_IDS)){
								mr.pm.put("BUY_DEPART_IDS", DEPART_IDS.split(","));
								mr.pm.put("buyUserId", USER_ID);
							}else{
								mr.pm.put("CREATE_USER_ID", USER_ID);
							}
						}
					}
				}
			}
			
			mr.pm.put("STATUS", STATUS);
			mr.pm.put("ACCOUNT_STATUS", ACCOUNT_STATUS);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}

			if(CommonUtils.checkString(CREATE_BEGIN_TIME)){
				mr.pm.put("CREATE_BEGIN_TIME", DateUtil.formatString(CREATE_BEGIN_TIME, "") + " 00:00:00");
			}
			if(CommonUtils.checkString(CREATE_END_TIME)){
				mr.pm.put("CREATE_END_TIME", DateUtil.formatString(CREATE_END_TIME, "") + " 23:59:59");
			}
			if(CommonUtils.checkString(PAY_BEGIN_TIME)){
				mr.pm.put("PAY_BEGIN_TIME", DateUtil.formatString(PAY_BEGIN_TIME, "") + " 00:00:00");
			}
			if(CommonUtils.checkString(PAY_END_TIME)){
				mr.pm.put("PAY_END_TIME", DateUtil.formatString(PAY_END_TIME, "") + " 23:59:59");
			}
			if(CommonUtils.checkString(SALER)){
				SALER = new String(SALER.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("SALER", SALER);
			}
			if(CommonUtils.checkString(BUYER)){
				BUYER = new String(BUYER.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("BUYER", BUYER);
			}
			if(CommonUtils.checkString(orderStatus) && (orderStatus.equals("0")||orderStatus.equals("1")||orderStatus.equals("3")||orderStatus.equals("4"))){
				mr.pm.put("STATUSES", orderStatus);
			}else if(CommonUtils.checkString(orderStatus) && orderStatus.equals("refund")){
				mr.pm.put("STATUSES", "3,4");
			}
			mr.pm.put("PRODUCE_TYPES", "'2', '3'");
			mr.pm.put("IS_RENEW", "0");
			mr.pm.put("PAY_WAY", PAY_WAY);
			if(CommonUtils.checkString(GO_BEGIN_TIME)){
				mr.pm.put("GO_BEGIN_TIME", DateUtil.formatString(GO_BEGIN_TIME, ""));
			}
			if(CommonUtils.checkString(GO_END_TIME)){
				mr.pm.put("GO_END_TIME", DateUtil.formatString(GO_END_TIME, ""));
			}
			if(CommonUtils.checkString(IS_CANCEL) && IS_CANCEL.equals("true")){
				mr.pm.put("IS_CANCEL", IS_CANCEL);
			}
			
			List<Map<String, Object>> data = this.orderService.listRouteOrderService(mr.pm);
			int totalSize = this.orderService.countRouteOrderService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路订单信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路订单信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String visitors, String detail){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			Map<String,Object> site = (Map<String,Object>)request.getSession().getAttribute("S_SITE_SESSION_KEY");
			Map<String, Object> params = new HashMap<String, Object>();
			String ORDER_ID = (String)mr.pm.get("ID");

			String COMPANY_TYPE = String.valueOf(user.get("COMPANY_TYPE"));
			String IS_ALONE = String.valueOf(user.get("IS_ALONE"));
			if((COMPANY_TYPE.equals("2") || COMPANY_TYPE.equals("3") ||  COMPANY_TYPE.equals("4") ||  COMPANY_TYPE.equals("5") || COMPANY_TYPE.equals("6") || COMPANY_TYPE.equals("7")) && IS_ALONE.equals("1")){
				mr.pm.put("IS_ALONE", "1");
			}else{
				mr.pm.put("IS_ALONE", "0");
			}
			
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
			mr.pm.put("SITE_RELA_ID", site.get("ID"));
			if(CommonUtils.checkString(mr.pm.get("PLAN_ID"))){
				mr.pm.put("PRODUCE_TYPE", 2);
			}else{
				mr.pm.put("PRODUCE_TYPE", 3);
			}
			mr.pm.put("detail", detail);
			mr.pm.put("visitors", visitors);
			String WMBP = request.getParameter("WMBP");
			String THBP = request.getParameter("THBP");
			mr.pm.put("WMBP", JSONArray.fromObject(WMBP));
			mr.pm.put("THBP", JSONArray.fromObject(THBP));
			Map<String, Object> result = null;
			/**
			 * 单房差
			 * 单房差数量不能超过游客数量
			 * 一人必须买单房差
			 */
			int single_count = Integer.parseInt((String)mr.pm.get("SINGLE_ROOM_CNT"));
			int person_count = Integer.parseInt((String)mr.pm.get("MAN_COUNT")) + Integer.parseInt((String)mr.pm.get("CHILD_COUNT"));
			JSONObject _detail = JSONObject.fromObject(detail);
			params.put("ID", (String)_detail.get("ID"));
			Map<String, Object> r = this.routeService.routeSingleRoomService(params);
			if(single_count > person_count){
				json.setStatusCode("");//------------------------单房差数量超过游客数量
				json.setSuccess(false);
				return json;
			}
			
			if(person_count == 1){
//				mr.pm.put("SINGLE_ROOM_CNT", 1);
			}
			
			mr.pm.put("RETAIL_SINGLE_ROOM", r.get("RETAIL_SINGLE_ROOM"));
			mr.pm.put("INTER_SINGLE_ROOM", r.get("INTER_SINGLE_ROOM"));
			
			/**
			 * 其他价格
			 */
			mr.pm.put("OTHER_PRICE", request.getParameterValues("OTHER_PRICE"));
			mr.pm.put("OTHER_NUM", request.getParameterValues("OTHER_NUM"));
			mr.pm.put("OTHER_ID", request.getParameterValues("OTHER_ID"));
			mr.pm.put("OTHER_TITLE", request.getParameterValues("OTHER_TITLE"));
			mr.pm.put("OTHER_CONTENT", request.getParameterValues("OTHER_CONTENT"));
			
			/**
			 * 对账方式
			 */
			mr.pm.put("ACCOUNT_WAY", user.get("ACCOUNT_WAY"));
			
			if(!CommonUtils.checkString(ORDER_ID)){
				ORDER_ID = CommonUtils.uuid();
				mr.pm.put("ID", ORDER_ID);
				result = this.orderService.saveRouteOrderService(mr.pm);
			}else{
				result = this.orderService.updateRouteOrderService(mr.pm);
			}
			String otherFee = (String)result.get("otherFee");
			if(CommonUtils.checkString(otherFee)){
				json.setMessage(otherFee);
			}else{
				json.setMessage(ORDER_ID);
			}
			json.setSuccess((Boolean)result.get("success"));
			json.setStatusCode(CommonUtils.checkString((String)result.get("statusCode"))?(String)result.get("statusCode"):"");
			
			this.orderService.updateAccountStatus(mr.pm, ORDER_ID);
			
		} catch (Exception e) {
			log.error("保存订单信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存订单信息异常");
		}
		return json;
	}
}
