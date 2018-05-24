package cn.sd.entity.produce;

import java.util.List;
import java.util.Map;


public class CompanyDetail {
	private String ID;
	private String PID; 
	private String USER_ID;
	private String USER_NAME;
	private String TYPE;
	private String COMPANY;
	private String CITY_ID;
	private String LOGO;
	private String ICON;
	private String CODE;
	private String PARTNER;
	private String KEY;
	private String SELLER_EMAIL;
	private String ABOUT;
	private String IS_ALONE;
	private String TITLE;
	private String DOMAIN;
	private String RECORD_NO;
	private String QQ1;
	private String QQ2;
	private String WX_IMG;
	private String ACCOUNT_WAY;
	private String START_CITY_NAME;
	private String PHONE1;
	private String PHONE2;
	private String MANAGER;
	private String WAP_ID;
	private String ENTITY_ID;
	private String ENTITY_NAME;
	private String SETTING_TYPE;
	private String PDOMAIN;
	private String ACCOUNT_USER_ID;
	private String ACCOUNT_USER_NAME;
	private String DEPART_ID;
	
	private List<Map<String, Object>> categorys;
	private Map<String, Object> weixinSign;
	
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	public String getSTART_CITY_NAME() {
		return START_CITY_NAME;
	}
	public void setSTART_CITY_NAME(String sTART_CITY_NAME) {
		START_CITY_NAME = sTART_CITY_NAME;
	}
	public String getACCOUNT_WAY() {
		return ACCOUNT_WAY;
	}
	public void setACCOUNT_WAY(String aCCOUNT_WAY) {
		ACCOUNT_WAY = aCCOUNT_WAY;
	}
	public String getWX_IMG() {
		return WX_IMG;
	}
	public void setWX_IMG(String wX_IMG) {
		WX_IMG = wX_IMG;
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
	public String getPHONE1() {
		return PHONE1;
	}
	public void setPHONE1(String pHONE1) {
		PHONE1 = pHONE1;
	}
	public String getPHONE2() {
		return PHONE2;
	}
	public void setPHONE2(String pHONE2) {
		PHONE2 = pHONE2;
	}
	
	public String getRECORD_NO() {
		return RECORD_NO;
	}
	public void setRECORD_NO(String rECORD_NO) {
		RECORD_NO = rECORD_NO;
	}
	public String getDOMAIN() {
		return DOMAIN;
	}
	public void setDOMAIN(String dOMAIN) {
		DOMAIN = dOMAIN;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCOMPANY() {
		return COMPANY;
	}
	public void setCOMPANY(String cOMPANY) {
		COMPANY = cOMPANY;
	}
	public String getCITY_ID() {
		return CITY_ID;
	}
	public void setCITY_ID(String cITY_ID) {
		CITY_ID = cITY_ID;
	}
	public String getLOGO() {
		return LOGO;
	}
	public void setLOGO(String lOGO) {
		LOGO = lOGO;
	}
	public String getICON() {
		return ICON;
	}
	public void setICON(String iCON) {
		ICON = iCON;
	}

	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getPARTNER() {
		return PARTNER;
	}
	public void setPARTNER(String pARTNER) {
		PARTNER = pARTNER;
	}
	public String getKEY() {
		return KEY;
	}
	public void setKEY(String kEY) {
		KEY = kEY;
	}
	public String getSELLER_EMAIL() {
		return SELLER_EMAIL;
	}
	public void setSELLER_EMAIL(String sELLER_EMAIL) {
		SELLER_EMAIL = sELLER_EMAIL;
	}
	public String getABOUT() {
		return ABOUT;
	}
	public void setABOUT(String aBOUT) {
		ABOUT = aBOUT;
	}
	public String getIS_ALONE() {
		return IS_ALONE;
	}
	public void setIS_ALONE(String iS_ALONE) {
		IS_ALONE = iS_ALONE;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getMANAGER() {
		return MANAGER;
	}
	public void setMANAGER(String mANAGER) {
		MANAGER = mANAGER;
	}
	public List<Map<String, Object>> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Map<String, Object>> categorys) {
		this.categorys = categorys;
	}
	public String getWAP_ID() {
		return WAP_ID;
	}
	public void setWAP_ID(String wAP_ID) {
		WAP_ID = wAP_ID;
	}
	public String getENTITY_ID() {
		return ENTITY_ID;
	}
	public void setENTITY_ID(String eNTITY_ID) {
		ENTITY_ID = eNTITY_ID;
	}
	public String getENTITY_NAME() {
		return ENTITY_NAME;
	}
	public void setENTITY_NAME(String eNTITY_NAME) {
		ENTITY_NAME = eNTITY_NAME;
	}
	public String getSETTING_TYPE() {
		return SETTING_TYPE;
	}
	public void setSETTING_TYPE(String sETTING_TYPE) {
		SETTING_TYPE = sETTING_TYPE;
	}
	public String getPDOMAIN() {
		return PDOMAIN;
	}
	public void setPDOMAIN(String pDOMAIN) {
		PDOMAIN = pDOMAIN;
	}
	public Map<String, Object> getWeixinSign() {
		return weixinSign;
	}
	public void setWeixinSign(Map<String, Object> weixinSign) {
		this.weixinSign = weixinSign;
	}
	public String getACCOUNT_USER_ID() {
		return ACCOUNT_USER_ID;
	}
	public void setACCOUNT_USER_ID(String aCCOUNT_USER_ID) {
		ACCOUNT_USER_ID = aCCOUNT_USER_ID;
	}
	public String getACCOUNT_USER_NAME() {
		return ACCOUNT_USER_NAME;
	}
	public void setACCOUNT_USER_NAME(String aCCOUNT_USER_NAME) {
		ACCOUNT_USER_NAME = aCCOUNT_USER_NAME;
	}
	public String getDEPART_ID() {
		return DEPART_ID;
	}
	public void setDEPART_ID(String dEPART_ID) {
		DEPART_ID = dEPART_ID;
	}
}
