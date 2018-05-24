package cn.sd.core.filter.city;

import java.util.List;

public class UserCity {
	private String localCity = null;
	private String localCityID = null;
	private String localCityPinYin = null;
	private String ora_id = null;
	private String hd = null;
	private String img = null;
	private String qq = null;
	private String img2 = null;
	
	private int fxs;
	private int gxs;
	private int dd;
	private int yk;
	private int cp;
	
	//登陆页 焦点图
	private List adList;
	
	/**
	 * 2012-01-06
	 * @author G.X.L
	 * 精度: LONGITUDE, LATITUDE: 纬度, key: MAPABC_KEY
	 */
	private String LATITUDE;
	private String LONGITUDE;
	private String MAPABC_KEY;
	private String company_id;	//一日游供应商ID
	private String retail_id;	//一日游门店ID
	
	public String getLocalCity() {
		return localCity;
	}
	public void setLocalCity(String localCity) {
		this.localCity = localCity;
	}
	public String getLocalCityPinYin() {
		return localCityPinYin;
	}
	public void setLocalCityPinYin(String localCityPinYin) {
		this.localCityPinYin = localCityPinYin;
	}
	public String getLocalCityID() {
		return localCityID;
	}
	public void setLocalCityID(String localCityID) {
		this.localCityID = localCityID;
	}
	
	
	public String getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(String latitude) {
		LATITUDE = latitude;
	}
	public String getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(String longitude) {
		LONGITUDE = longitude;
	}
	public String getMAPABC_KEY() {
		return MAPABC_KEY;
	}
	public void setMAPABC_KEY(String mapabc_key) {
		MAPABC_KEY = mapabc_key;
	}
	public String getOra_id() {
		return ora_id;
	}
	public void setOra_id(String ora_id) {
		this.ora_id = ora_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getRetail_id() {
		return retail_id;
	}
	public void setRetail_id(String retail_id) {
		this.retail_id = retail_id;
	}
	public String getHd() {
		return hd;
	}
	public void setHd(String hd) {
		this.hd = hd;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getFxs() {
		return fxs;
	}
	public void setFxs(int fxs) {
		this.fxs = fxs;
	}
	public int getGxs() {
		return gxs;
	}
	public void setGxs(int gxs) {
		this.gxs = gxs;
	}
	public int getDd() {
		return dd;
	}
	public void setDd(int dd) {
		this.dd = dd;
	}
	public int getYk() {
		return yk;
	}
	public void setYk(int yk) {
		this.yk = yk;
	}
	public int getCp() {
		return cp;
	}
	public void setCp(int cp) {
		this.cp = cp;
	}
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	public List getAdList() {
		return adList;
	}
	public void setAdList(List adList) {
		this.adList = adList;
	}
	
}
