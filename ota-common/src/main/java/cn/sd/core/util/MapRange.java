package cn.sd.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapRange {
	public static final String ASC = "ASC"; // 排序 : 升序
	public static final String DESC = "DESC"; // 排序 : 降序
	
	public Map<String, Object> pm = new LinkedHashMap<String, Object>();

	public Map<String, Object> getPm() {
		return pm;
	}

	public void setPm(Map<String, Object> pm) {
		this.pm = pm;
	}
}
