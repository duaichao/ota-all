package cn.sd.core.util.wordfilter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sd.core.util.jdbc;


/**
 * 敏感字 工具类
 */
@SuppressWarnings("all") 
public class WordFilterUtil extends jdbc{
	private static Node tree = new Node();
	public static int I = 0;
	static {
		loadinit();
	}

	/**
	 * 
	 * TODO 所有的字符
	 */
	public static void loadinit() {
		I++;
		try {
			jdbc conn = new jdbc();  
			ResultSet result = conn.executeQuery("select * from SD_BASE_SENSITIVE_WORDS");
			while(result.next()){
				insertWord(result.getString("CONTENT"), result.getInt("MYLEVEL"));
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void insertWord(String word, int level) {
		Node node = tree;
		System.out.println(word);
		for (int i = 0; i < word.length(); ++i) {
			node = node.addChar(word.charAt(i));
		}
		node.setEnd(true);
		node.setLevel(level);
	}

	/**
	 * 标点符号
	 */
	private static boolean isPunctuationChar(String c) {
		String regex = "[\\pP\\pZ\\pS\\pM\\pC]";
		Pattern p = Pattern.compile(regex, 2);
		Matcher m = p.matcher(c);
		return m.find();
	}

	/**
	 * 标点符号 或者 HTML 字符过滤的结果
	 * 
	 */
	private static PunctuationOrHtmlFilteredResult filterPunctation(String originalString) {
		StringBuffer filteredString = new StringBuffer();
		ArrayList charOffsets = new ArrayList();
		for (int i = 0; i < originalString.length(); ++i) {
			String c = String.valueOf(originalString.charAt(i));
			if (!(isPunctuationChar(c))) {
				filteredString.append(c);
				charOffsets.add(Integer.valueOf(i));
			}
		}
		PunctuationOrHtmlFilteredResult result = new PunctuationOrHtmlFilteredResult(null);
		result.setOriginalString(originalString);
		result.setFilteredString(filteredString);
		result.setCharOffsets(charOffsets);
		return result;
	}

	private static PunctuationOrHtmlFilteredResult filterPunctationAndHtml(String originalString) {
		StringBuffer filteredString = new StringBuffer();
		ArrayList charOffsets = new ArrayList();
		int i = 0;
		for (int k = 0; i < originalString.length(); ++i) {
			String c = String.valueOf(originalString.charAt(i));
			if (originalString.charAt(i) == '<') {
				for (k = i + 1; k < originalString.length(); ++k) {
					if (originalString.charAt(k) == '<') {
						k = i;
						break;
					}
					if (originalString.charAt(k) == '>') {
						break;
					}
				}
				i = k;
			} else if (!(isPunctuationChar(c))) {
				filteredString.append(c);
				charOffsets.add(Integer.valueOf(i));
			}
		}

		PunctuationOrHtmlFilteredResult result = new PunctuationOrHtmlFilteredResult(null);
		result.setOriginalString(originalString);
		result.setFilteredString(filteredString);
		result.setCharOffsets(charOffsets);
		return result;
	}

	private static FilteredResult filter(PunctuationOrHtmlFilteredResult pohResult, char replacement) {
		StringBuffer sentence = pohResult.getFilteredString();
		ArrayList charOffsets = pohResult.getCharOffsets();
		StringBuffer resultString = new StringBuffer(pohResult.getOriginalString());
		StringBuffer badWords = new StringBuffer();
		int level = 0;
		Node node = tree;
		int start = 0;
		int end = 0;
		for (int i = 0; i < sentence.length(); ++i) {
			start = i;
			end = i;
			node = tree;
			for (int j = i; j < sentence.length(); ++j) {
				node = node.findChar(sentence.charAt(j));
				if (node == null) {
					break;
				}
				if (node.isEnd()) {
					end = j;
					level = node.getLevel();
				}
			}
			if (end > start) {
				for (int k = start; k <= end; ++k) {
					resultString.setCharAt(((Integer) charOffsets.get(k)).intValue(), replacement);
				}
				if (badWords.length() > 0)
					badWords.append(",");
				badWords.append(sentence.substring(start, end + 1));
				i = end;
			}
		}
		FilteredResult result = new FilteredResult();
		result.setOriginalContent(pohResult.getOriginalString());
		result.setFilteredContent(resultString.toString());
		result.setBadWords(badWords.toString());
		result.setLevel(Integer.valueOf(level));
		return result;
	}

	/**
	 * 简单的过滤 sentence：目标字符 replacement：替换字符
	 */
	public static String simpleFilter(String sentence, char replacement) {
		StringBuffer sb = new StringBuffer();
		Node node = tree;
		int start = 0;
		int end = 0;
		for (int i = 0; i < sentence.length(); ++i) {
			start = i;
			end = i;
			node = tree;
			for (int j = i; j < sentence.length(); ++j) {
				node = node.findChar(sentence.charAt(j));
				if (node == null) {
					break;
				}
				if (node.isEnd()) {
					end = j;
				}
			}
			if (end > start) {
				for (int k = start; k <= end; ++k) {
					sb.append(replacement);
				}
				i = end;
			} else {
				sb.append(sentence.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 文本过滤 1.0 首先过滤标点符号和 HTML 字符 TODO
	 */
	public static FilteredResult filterText(String originalString, char replacement) {
		return filter(filterPunctation(originalString), replacement);
	}

	public static FilteredResult filterHtml(String originalString, char replacement) {
		return filter(filterPunctationAndHtml(originalString), replacement);
	}

	/**
	 * 是否包含敏感词
	 * 
	 */
	public static boolean isContainSensitiveWords(String originalString, char replacement) {
		FilteredResult fr = filterText(originalString, replacement);
		return fr.getBadWords() != null && fr.getBadWords().length() > 0;
	}
	
	
	public static void main(String[] args) {
		String string =  WordFilterUtil.filterText("我操士大夫似的开房间上弹了封建时代李开复", '*').getFilteredContent();
		System.out.println(string);
	}
}