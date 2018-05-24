package cn.sd.entity.produce;

import java.util.List;
import java.util.Map;

public class RouteDayDetail {
	private String ID;
	private String DAY_ID;
	private String TITLE;
	private String CONTENT;
	private List<String> SCENIC;
	private List<Map<String, Object>> scenics;

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public String getDAY_ID() {
		return DAY_ID;
	}

	public void setDAY_ID(String day_id) {
		DAY_ID = day_id;
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

	public List<String> getSCENIC() {
		return SCENIC;
	}

	public void setSCENIC(List<String> scenic) {
		SCENIC = scenic;
	}

	public List<Map<String, Object>> getScenics() {
		return scenics;
	}

	public void setScenics(List<Map<String, Object>> scenics) {
		this.scenics = scenics;
	}
}
