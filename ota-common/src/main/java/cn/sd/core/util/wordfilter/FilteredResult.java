package cn.sd.core.util.wordfilter;

/**
 * TODO 敏感字 过滤 返回的结果  
 */
public class FilteredResult {
	private Integer level;
	private String filteredContent; // 过滤后的字符串
	private String badWords; // 非法字符
	private String originalContent; // 原来的字符

	public String getBadWords() {
		return this.badWords;
	}
 
	public void setBadWords(String badWords) {
		this.badWords = badWords;
	}

	public FilteredResult() {
	}

	public FilteredResult(String originalContent, String filteredContent,
			Integer level, String badWords) {
		this.originalContent = originalContent;
		this.filteredContent = filteredContent;
		this.level = level;
		this.badWords = badWords;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getFilteredContent() {
		return this.filteredContent;
	}

	public void setFilteredContent(String filteredContent) {
		this.filteredContent = filteredContent;
	}

	public String getOriginalContent() {
		return this.originalContent;
	}

	public void setOriginalContent(String originalContent) {
		this.originalContent = originalContent;
	}
}