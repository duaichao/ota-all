package cn.sd.entity.b2b;

public class NewsNoticeEntity {
	
	private String ID; 
	private String TITLE; 
	private String CONTENT; 
	private String SITE_ID; 
	private String TYPE; 
	private String CREATE_USER; 
	private String CREATE_TIME; 
	private String UPDATE_USER; 
	private String UPDATE_TIME; 
	private String IS_DEL; 
	private String IS_TOP;
	
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		ID = id;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String title) {
		TITLE = title;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String content) {
		CONTENT = content;
	}
	public String getSITE_ID() {
		return SITE_ID;
	}
	public void setSITE_ID(String site_id) {
		SITE_ID = site_id;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String type) {
		TYPE = type;
	}
	public String getCREATE_USER() {
		return CREATE_USER;
	}
	public void setCREATE_USER(String create_user) {
		CREATE_USER = create_user;
	}
	public String getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(String create_time) {
		CREATE_TIME = create_time;
	}
	public String getUPDATE_USER() {
		return UPDATE_USER;
	}
	public void setUPDATE_USER(String update_user) {
		UPDATE_USER = update_user;
	}
	public String getUPDATE_TIME() {
		return UPDATE_TIME;
	}
	public void setUPDATE_TIME(String update_time) {
		UPDATE_TIME = update_time;
	}
	public String getIS_DEL() {
		return IS_DEL;
	}
	public void setIS_DEL(String is_del) {
		IS_DEL = is_del;
	}
	public String getIS_TOP() {
		return IS_TOP;
	}
	public void setIS_TOP(String is_top) {
		IS_TOP = is_top;
	} 
	
}
