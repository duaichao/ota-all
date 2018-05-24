package cn.sd.entity.produce;

import java.util.List;

public class RouteDayDetailList {

	private List<RouteDayDetail> lp;
	private List<RouteDayDetail> include;
	private List<RouteDayDetail> noclude;
	private List<RouteDayDetail> notice;
	private List<RouteDayDetail> tips;
	private List<RouteDayDetail> other;
	
	public List<RouteDayDetail> getOther() {
		return other;
	}

	public void setOther(List<RouteDayDetail> other) {
		this.other = other;
	}

	public List<RouteDayDetail> getLp() {
		return lp;
	}

	public void setLp(List<RouteDayDetail> lp) {
		this.lp = lp;
	}

	public List<RouteDayDetail> getInclude() {
		return include;
	}

	public void setInclude(List<RouteDayDetail> include) {
		this.include = include;
	}

	public List<RouteDayDetail> getNoclude() {
		return noclude;
	}

	public void setNoclude(List<RouteDayDetail> noclude) {
		this.noclude = noclude;
	}

	public List<RouteDayDetail> getNotice() {
		return notice;
	}

	public void setNotice(List<RouteDayDetail> notice) {
		this.notice = notice;
	}

	public List<RouteDayDetail> getTips() {
		return tips;
	}

	public void setTips(List<RouteDayDetail> tips) {
		this.tips = tips;
	}
}
