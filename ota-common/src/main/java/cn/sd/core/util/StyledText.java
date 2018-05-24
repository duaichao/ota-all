package cn.sd.core.util;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class StyledText {
	
	 public static String delHTMLTag(String htmlStr){ 
	        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(htmlStr); 
	        htmlStr=m_script.replaceAll(""); //过滤script标签 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(htmlStr); 
	        htmlStr=m_style.replaceAll(""); //过滤style标签 
	         
	        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(htmlStr); 
	        htmlStr=m_html.replaceAll(""); //过滤html标签 

	        return htmlStr.trim(); //返回文本字符串 
	    } 
	 
    public static XWPFDocument formatText(XWPFParagraph paragraph, String text, XWPFDocument docX) throws Exception {
        JTextPane pad = new JTextPane();
        text = text.replaceAll("<br />", "#huanhang#");
        pad.setContentType("text/html");
        HTMLEditorKit kit = (HTMLEditorKit)pad.getEditorKit();
        HTMLDocument htmldoc = (HTMLDocument)kit.createDefaultDocument();
        kit.insertHTML(htmldoc, htmldoc.getLength(), text, 0, 0, null);
        pad.setDocument(htmldoc);
        // convert
        StyledDocument doc = pad.getStyledDocument();
        
        int lastPos=-1; 
        while (lastPos < doc.getLength()) {
            Element line = doc.getParagraphElement(lastPos+1);
            lastPos = line.getEndOffset();
//            XWPFParagraph paragraph = docX.createParagraph();
            
            for (int elIdx=0; elIdx < line.getElementCount(); elIdx++) {
                final Element frag = line.getElement(elIdx);
                String subtext = delHTMLTag(doc.getText(frag.getStartOffset(), frag.getEndOffset()-frag.getStartOffset()));
                if(CommonUtils.checkString(subtext)){
                	XWPFRun run = paragraph.createRun();
	                String[] hh = subtext.split("#huanhang#");
	                for (String h : hh) {
	                	run.addBreak();
					}
	                subtext = subtext.replaceAll("#huanhang#", "");
	                run.setText(subtext);
	                final AttributeSet as = frag.getAttributes();
	                final Enumeration<?> ae = as.getAttributeNames();
	
	                while (ae.hasMoreElements()) {
	                    final Object attrib = ae.nextElement();
	
	                    if (CSS.Attribute.COLOR.equals(attrib)) {
	                        // I don't know how to really work with the CSS-swing class ...
	                        Field f = as.getAttribute(attrib).getClass().getDeclaredField("c");
	                        f.setAccessible(true);
	                        Color c = (Color)f.get(as.getAttribute(attrib));
	                        run.setColor(String.format("%1$02X%2$02X%3$02X", c.getRed(),c.getGreen(),c.getBlue()));
	                    } else if (CSS.Attribute.FONT_WEIGHT.equals(attrib)) {
	                        if ("bold".equals(as.getAttribute(attrib).toString())) {
	                            run.setBold(true);
	                        }
	                    }
	                    
	                }     
                }
            }
        }
        return docX;
    }
}