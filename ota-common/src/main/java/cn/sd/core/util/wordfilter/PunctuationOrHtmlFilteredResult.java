package cn.sd.core.util.wordfilter;

import java.util.ArrayList;
/**
 * 过滤标点 和 HTML 代码的结果
 */
public  class PunctuationOrHtmlFilteredResult {
	private String originalString;  // 原始的字符串
	private StringBuffer filteredString; // 过滤后的
	private ArrayList<Integer> charOffsets; //有效字符的 位置

	public String getOriginalString() {
		return this.originalString;
	}

	public void setOriginalString(String originalString) {
		this.originalString = originalString;
	}

	public StringBuffer getFilteredString() {
		return this.filteredString;
	}

	public void setFilteredString(StringBuffer filteredString) {
		this.filteredString = filteredString;
	}

	public ArrayList<Integer> getCharOffsets() {
		return this.charOffsets;
	}

	public void setCharOffsets(ArrayList<Integer> charOffsets) {
		this.charOffsets = charOffsets;
	}

	public PunctuationOrHtmlFilteredResult(ArrayList<Integer> charOffsets) {
		this.charOffsets = charOffsets;
	}
}