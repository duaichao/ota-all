package cn.sd.entity;

import java.util.List;
import java.util.Map;

public class RouteEntity {

	private String  ID;
	private String NO;
	private String FACE;
	private String TITLE;
	private String FEATURE;
	private String NOTICE;
	private String TYPE;
	private String COMPANY_ID;
	private String COMPANY_NAME;
	private String PORDUCE_CONCAT;
	private String PRODUCE_MOBILE;
	private String PHONE;
	private String CREATE_USER;
	private String CREATE_TIME;
	private String CREATE_USER_ID;
	private String UPDATE_USER;
	private String UPDATE_TIME;
	private String UPDATE_USER_ID;
	private String CITY_ID;
	private String CITY_NAME;
	private String IS_DEL;
	private String IS_PUB;
	private String SOURCE_URL;
	private String IS_SINGLE_PUB;
	private String IS_GRANT;
	private String IS_SHARE;
	private String DAY_COUNT;
	private String ATTR;
	private String RETAIL_SINGLE_ROOM;
	private String INTER_SINGLE_ROOM;
	private String ROUTE_PRICE;
	private String ROUTE_INTER_PRICE;
	private String SUM_PRICE ;
	private String SUM_INTER_PRICE;
	private String RQ;
	private String QQ1;
	private String QQ2;
	private String ROUTE_TRAFFIC_CNT;
	private String BASE_PRICE_CNT;
	private String THEMES;
	private String BEGIN_CITY;
	private String END_CITY;
	private String DISCOUNT_INFO;
	private String DISCOUNT_ID;
	private String ROUTE_WAP_PRICE;
	private String ORDER_CNT;
	private String TRAFFIC_ID;
	
	private String TRAFFIC_END_DATE;
	private String TRAFFIC_END_TIME;
	
	private String parentCompany;
	private String shortName;
	private String chinaName;
	private String userPhone;
	private String address;
	
	
	private String IS_EARNEST;
	private String EARNEST_INTER;
	private String EARNEST_RETAIL;
	private String EARNEST_DAY_COUNT;
	private String EARNEST_TYPE;
	
	public String getIS_EARNEST() {
		return IS_EARNEST;
	}
	public void setIS_EARNEST(String iS_EARNEST) {
		IS_EARNEST = iS_EARNEST;
	}
	public String getEARNEST_INTER() {
		return EARNEST_INTER;
	}
	public void setEARNEST_INTER(String eARNEST_INTER) {
		EARNEST_INTER = eARNEST_INTER;
	}
	public String getEARNEST_RETAIL() {
		return EARNEST_RETAIL;
	}
	public void setEARNEST_RETAIL(String eARNEST_RETAIL) {
		EARNEST_RETAIL = eARNEST_RETAIL;
	}
	public String getEARNEST_DAY_COUNT() {
		return EARNEST_DAY_COUNT;
	}
	public void setEARNEST_DAY_COUNT(String eARNEST_DAY_COUNT) {
		EARNEST_DAY_COUNT = eARNEST_DAY_COUNT;
	}
	public String getEARNEST_TYPE() {
		return EARNEST_TYPE;
	}
	public void setEARNEST_TYPE(String eARNEST_TYPE) {
		EARNEST_TYPE = eARNEST_TYPE;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getParentCompany() {
		return parentCompany;
	}
	public void setParentCompany(String parentCompany) {
		this.parentCompany = parentCompany;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getChinaName() {
		return chinaName;
	}
	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getTRAFFIC_END_DATE() {
		return TRAFFIC_END_DATE;
	}
	public void setTRAFFIC_END_DATE(String tRAFFIC_END_DATE) {
		TRAFFIC_END_DATE = tRAFFIC_END_DATE;
	}
	public String getTRAFFIC_END_TIME() {
		return TRAFFIC_END_TIME;
	}
	public void setTRAFFIC_END_TIME(String tRAFFIC_END_TIME) {
		TRAFFIC_END_TIME = tRAFFIC_END_TIME;
	}
	public String getTRAFFIC_ID() {
		return TRAFFIC_ID;
	}
	public void setTRAFFIC_ID(String tRAFFIC_ID) {
		TRAFFIC_ID = tRAFFIC_ID;
	}
	public String getIS_FULL_PRICE() {
		return IS_FULL_PRICE;
	}
	public void setIS_FULL_PRICE(String iS_FULL_PRICE) {
		IS_FULL_PRICE = iS_FULL_PRICE;
	}
	private String FEATURE1;
	private String IS_FULL_PRICE;
	
	
	private List<Map<String, Object>> include;
	private List<Map<String, Object>> noclude;
	private List<Map<String, Object>> notice;
	public List<Map<String, Object>> getInclude() {
		return include;
	}
	public void setInclude(List<Map<String, Object>> include) {
		this.include = include;
	}
	public List<Map<String, Object>> getNoclude() {
		return noclude;
	}
	public void setNoclude(List<Map<String, Object>> noclude) {
		this.noclude = noclude;
	}
	public List<Map<String, Object>> getNotice() {
		return notice;
	}
	public void setNotice(List<Map<String, Object>> notice) {
		this.notice = notice;
	}
	public List<Map<String, Object>> getTips() {
		return tips;
	}
	public void setTips(List<Map<String, Object>> tips) {
		this.tips = tips;
	}
	public List<Map<String, Object>> getCitys() {
		return citys;
	}
	public void setCitys(List<Map<String, Object>> citys) {
		this.citys = citys;
	}
	private List<Map<String, Object>> tips;
	private List<Map<String, Object>> citys;
	
	private List<Map<String, Object>> begin;
	private List<Map<String, Object>> end;
	private List<String> zt;
	
	public List<Map<String, Object>> getBegin() {
		return begin;
	}
	public void setBegin(List<Map<String, Object>> begin) {
		this.begin = begin;
	}
	public List<Map<String, Object>> getEnd() {
		return end;
	}
	public void setEnd(List<Map<String, Object>> end) {
		this.end = end;
	}
	
	public List<String> getZt() {
		return zt;
	}
	public void setZt(List<String> zt) {
		this.zt = zt;
	}
	public String getFEATURE1() {
		return FEATURE1;
	}
	public void setFEATURE1(String fEATURE1) {
		FEATURE1 = fEATURE1;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNO() {
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getFACE() {
		return FACE;
	}
	public void setFACE(String fACE) {
		FACE = fACE;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getFEATURE() {
		return FEATURE;
	}
	public void setFEATURE(String fEATURE) {
		FEATURE = fEATURE;
	}
	public String getNOTICE() {
		return NOTICE;
	}
	public void setNOTICE(String nOTICE) {
		NOTICE = nOTICE;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getPORDUCE_CONCAT() {
		return PORDUCE_CONCAT;
	}
	public void setPORDUCE_CONCAT(String pORDUCE_CONCAT) {
		PORDUCE_CONCAT = pORDUCE_CONCAT;
	}
	public String getPRODUCE_MOBILE() {
		return PRODUCE_MOBILE;
	}
	public void setPRODUCE_MOBILE(String pRODUCE_MOBILE) {
		PRODUCE_MOBILE = pRODUCE_MOBILE;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getCREATE_USER() {
		return CREATE_USER;
	}
	public void setCREATE_USER(String cREATE_USER) {
		CREATE_USER = cREATE_USER;
	}
	public String getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(String cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public String getCREATE_USER_ID() {
		return CREATE_USER_ID;
	}
	public void setCREATE_USER_ID(String cREATE_USER_ID) {
		CREATE_USER_ID = cREATE_USER_ID;
	}
	public String getUPDATE_USER() {
		return UPDATE_USER;
	}
	public void setUPDATE_USER(String uPDATE_USER) {
		UPDATE_USER = uPDATE_USER;
	}
	public String getUPDATE_TIME() {
		return UPDATE_TIME;
	}
	public void setUPDATE_TIME(String uPDATE_TIME) {
		UPDATE_TIME = uPDATE_TIME;
	}
	public String getUPDATE_USER_ID() {
		return UPDATE_USER_ID;
	}
	public void setUPDATE_USER_ID(String uPDATE_USER_ID) {
		UPDATE_USER_ID = uPDATE_USER_ID;
	}
	public String getCITY_ID() {
		return CITY_ID;
	}
	public void setCITY_ID(String cITY_ID) {
		CITY_ID = cITY_ID;
	}
	public String getCITY_NAME() {
		return CITY_NAME;
	}
	public void setCITY_NAME(String cITY_NAME) {
		CITY_NAME = cITY_NAME;
	}
	public String getIS_DEL() {
		return IS_DEL;
	}
	public void setIS_DEL(String iS_DEL) {
		IS_DEL = iS_DEL;
	}
	public String getIS_PUB() {
		return IS_PUB;
	}
	public void setIS_PUB(String iS_PUB) {
		IS_PUB = iS_PUB;
	}
	public String getSOURCE_URL() {
		return SOURCE_URL;
	}
	public void setSOURCE_URL(String sOURCE_URL) {
		SOURCE_URL = sOURCE_URL;
	}
	public String getIS_SINGLE_PUB() {
		return IS_SINGLE_PUB;
	}
	public void setIS_SINGLE_PUB(String iS_SINGLE_PUB) {
		IS_SINGLE_PUB = iS_SINGLE_PUB;
	}
	public String getIS_GRANT() {
		return IS_GRANT;
	}
	public void setIS_GRANT(String iS_GRANT) {
		IS_GRANT = iS_GRANT;
	}
	public String getIS_SHARE() {
		return IS_SHARE;
	}
	public void setIS_SHARE(String iS_SHARE) {
		IS_SHARE = iS_SHARE;
	}
	public String getDAY_COUNT() {
		return DAY_COUNT;
	}
	public void setDAY_COUNT(String dAY_COUNT) {
		DAY_COUNT = dAY_COUNT;
	}
	public String getATTR() {
		return ATTR;
	}
	public void setATTR(String aTTR) {
		ATTR = aTTR;
	}
	public String getRETAIL_SINGLE_ROOM() {
		return RETAIL_SINGLE_ROOM;
	}
	public void setRETAIL_SINGLE_ROOM(String rETAIL_SINGLE_ROOM) {
		RETAIL_SINGLE_ROOM = rETAIL_SINGLE_ROOM;
	}
	public String getINTER_SINGLE_ROOM() {
		return INTER_SINGLE_ROOM;
	}
	public void setINTER_SINGLE_ROOM(String iNTER_SINGLE_ROOM) {
		INTER_SINGLE_ROOM = iNTER_SINGLE_ROOM;
	}
	public String getROUTE_PRICE() {
		return ROUTE_PRICE;
	}
	public void setROUTE_PRICE(String rOUTE_PRICE) {
		ROUTE_PRICE = rOUTE_PRICE;
	}
	public String getROUTE_INTER_PRICE() {
		return ROUTE_INTER_PRICE;
	}
	public void setROUTE_INTER_PRICE(String rOUTE_INTER_PRICE) {
		ROUTE_INTER_PRICE = rOUTE_INTER_PRICE;
	}
	public String getSUM_PRICE() {
		return SUM_PRICE;
	}
	public void setSUM_PRICE(String sUM_PRICE) {
		SUM_PRICE = sUM_PRICE;
	}
	public String getSUM_INTER_PRICE() {
		return SUM_INTER_PRICE;
	}
	public void setSUM_INTER_PRICE(String sUM_INTER_PRICE) {
		SUM_INTER_PRICE = sUM_INTER_PRICE;
	}
	public String getRQ() {
		return RQ;
	}
	public void setRQ(String rQ) {
		RQ = rQ;
	}
	public String getQQ1() {
		return QQ1;
	}
	public void setQQ1(String qQ1) {
		QQ1 = qQ1;
	}
	public String getQQ2() {
		return QQ2;
	}
	public void setQQ2(String qQ2) {
		QQ2 = qQ2;
	}
	public String getROUTE_TRAFFIC_CNT() {
		return ROUTE_TRAFFIC_CNT;
	}
	public void setROUTE_TRAFFIC_CNT(String rOUTE_TRAFFIC_CNT) {
		ROUTE_TRAFFIC_CNT = rOUTE_TRAFFIC_CNT;
	}
	public String getBASE_PRICE_CNT() {
		return BASE_PRICE_CNT;
	}
	public void setBASE_PRICE_CNT(String bASE_PRICE_CNT) {
		BASE_PRICE_CNT = bASE_PRICE_CNT;
	}
	public String getTHEMES() {
		return THEMES;
	}
	public void setTHEMES(String tHEMES) {
		THEMES = tHEMES;
	}
	public String getBEGIN_CITY() {
		return BEGIN_CITY;
	}
	public void setBEGIN_CITY(String bEGIN_CITY) {
		BEGIN_CITY = bEGIN_CITY;
	}
	public String getEND_CITY() {
		return END_CITY;
	}
	public void setEND_CITY(String eND_CITY) {
		END_CITY = eND_CITY;
	}
	public String getDISCOUNT_INFO() {
		return DISCOUNT_INFO;
	}
	public void setDISCOUNT_INFO(String dISCOUNT_INFO) {
		DISCOUNT_INFO = dISCOUNT_INFO;
	}
	public String getDISCOUNT_ID() {
		return DISCOUNT_ID;
	}
	public void setDISCOUNT_ID(String dISCOUNT_ID) {
		DISCOUNT_ID = dISCOUNT_ID;
	}
	public String getROUTE_WAP_PRICE() {
		return ROUTE_WAP_PRICE;
	}
	public void setROUTE_WAP_PRICE(String rOUTE_WAP_PRICE) {
		ROUTE_WAP_PRICE = rOUTE_WAP_PRICE;
	}
	public String getORDER_CNT() {
		return ORDER_CNT;
	}
	public void setORDER_CNT(String oRDER_CNT) {
		ORDER_CNT = oRDER_CNT;
	}
	
}
