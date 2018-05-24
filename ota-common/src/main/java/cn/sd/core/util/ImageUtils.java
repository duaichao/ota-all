package cn.sd.core.util;

import com.sun.image.codec.jpeg.*;

import javax.imageio.ImageIO;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 图片操作工具类
 * @author Rex
 */
public final class ImageUtils {
	
	public static int PressTopCenter = 1;	//顶部水平居中
	public static int PressButtomCenter = 2;//底部水平居中
	public static int PressCenter = 3;		//居中
	public static int PressTopLeft = 4;		//顶部水平居左
	public static int PressTopRight = 5;	//顶部水平居右
	public static int PressLeftCenter = 6;	//左边垂直居中
	public static int PressRightCenter = 7;	//右边垂直居中
	public static int PressButtomLeft = 8;	//底部水平居左
	public static int PressButtomRight = 9;	//底部水平居右
	
	public static class ImageParameter{
		private Color background;		//背景色
		private Color color;			//前景色
		private Font font;				//字体
		private int lineSpace = 10;		//行间距
		private int wordSpace = 2;		//字间距
		private int lineMaxChar = 0;	//一行最大字数
		private int maxHeight = 0;		//最大高度
		private int maxWidth = 0;		//最大宽度
		
		private int marginTop = 0;		//下移0
		
		public ImageParameter(){}
		
		public Color getBackground() {
			return background;
		}
		public void setBackground(Color background) {
			this.background = background;
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public Font getFont() {
			return font;
		}
		public void setFont(Font font) {
			this.font = font;
		}
		public int getLineSpace() {
			return lineSpace;
		}
		public void setLineSpace(int lineSpace) {
			this.lineSpace = lineSpace;
		}
		public int getWordSpace() {
			return wordSpace;
		}
		public void setWordSpace(int wordSpace) {
			this.wordSpace = wordSpace;
		}
		public int getLineMaxChar() {
			return lineMaxChar;
		}
		public void setLineMaxChar(int lineMaxChar) {
			this.lineMaxChar = lineMaxChar;
		}
		public int getMaxHeight() {
			return maxHeight;
		}
		public void setMaxHeight(int maxHeight) {
			this.maxHeight = maxHeight;
		}
		public int getMaxWidth() {
			return maxWidth;
		}
		public void setMaxWidth(int maxWidth) {
			this.maxWidth = maxWidth;
		}

		public int getMarginTop() {
			return marginTop;
		}

		public void setMarginTop(int marginTop) {
			this.marginTop = marginTop;
		}
	}
	
	public ImageUtils() {
	}
	
	/**
	 * 文字生成图片。注：不能正常处理回车换行
	 * @param text		要输出的文字
	 * @param fileName	输出的文件名
	 * @param height	高度
	 * @param charWidth	单个字符宽度
	 * @param maxWidth	最大宽度
	 * @return
	 */
	public final static boolean string2Image(String text,String fileName,ImageParameter parameter){
		boolean returnValue = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			returnValue = string2Image(text,out,parameter);
			out.close();
		} catch (Exception e) {
			if(out!=null){
				try {
					out.close();
				} catch (Exception e1) {
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 文字生成图片。注：不能正常处理回车换行
	 * @param text		要输出的文字
	 * @param out		输出流
	 * @param height	高度
	 * @param charWidth	单个字符宽度
	 * @param maxWidth	最大宽度
	 */
	public final static boolean string2Image(String text,OutputStream out,ImageParameter parameter){
		boolean returnValue = false;
		
		FontMetrics fontMetrics = new StyleContext().getFontMetrics(parameter.getFont());
		int charHeight = fontMetrics.getHeight();		//单个字符高度
		
		int count = text.length();
		
		//计算行数
		int lineNumber = 0;
		double line = 0d;
		if(parameter.lineMaxChar!=0){
			line = (double)count / (double)parameter.lineMaxChar;
			lineNumber = (int)line;
			if(line!=(double)lineNumber)lineNumber = lineNumber +1;
		}
		if(lineNumber==0) lineNumber = 1;
		
		//计算宽度
		int width = 0;
		int lineWidthCount = 0;
		for(int i=0;i<count;i++){
			String getone = text.substring(i,i+1);
			lineWidthCount = lineWidthCount + fontMetrics.stringWidth(getone) + parameter.getWordSpace();
			if((parameter.getLineMaxChar()!=0 && (i + 1) % parameter.getLineMaxChar() == 0)) {
				if(width<lineWidthCount + parameter.getWordSpace()) width=lineWidthCount + parameter.getWordSpace();
				lineWidthCount = 0;
			}
		}
		if(lineWidthCount != 0 && width < (lineWidthCount + parameter.getWordSpace())) width=lineWidthCount + parameter.getWordSpace();
		if(parameter.getMaxWidth() != 0 && width > parameter.getMaxWidth()) width=parameter.getMaxWidth();
		
		//计算高度
		int height = lineNumber * (charHeight / 2 + parameter.getLineSpace()) + parameter.getLineSpace();
		height = height + parameter.getMarginTop();
		if(parameter.getMaxHeight() != 0 && height > parameter.getMaxHeight()) height = parameter.getMaxHeight();
		
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        
        //set background:
        g.setBackground(parameter.getBackground());
        g.clearRect(0, 0, width, height);
        
        g.setColor(parameter.getColor());
        g.setFont(parameter.getFont());
        
        //draw string
        int x=0;
        int y=parameter.getMarginTop();
        int i = 0;
        String getone = null;
        while(count >0){
        	getone = text.substring(i,i+1);
        	if((parameter.getLineMaxChar() ==0 && i==0)|| (parameter.getLineMaxChar()!=0 && i % parameter.getLineMaxChar() == 0)) {
        		y = y + charHeight / 2 + parameter.getLineSpace();
        		x = parameter.getWordSpace();
        	}
        	g.drawString(getone,x,y);
        	x = x + fontMetrics.stringWidth(getone) + parameter.getWordSpace();
        	i++ ;
        	count--;
        }
        
        //end draw:
        g.dispose();
        bi.flush();
        //encode:
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
        param.setQuality(1.0f, true);
		try {
			encoder.encode(bi);
			returnValue = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	/**
	 * 把图片印刷到图片上
	 * @param src
	 * @param pressImg
	 * @param x
	 * @param y
	 * @return
	 */
	private final static BufferedImage pressImage(Image src,Image pressImg,int x, int y){
		int newImage_Width = src.getWidth(null);
		int newImage_Height = src.getHeight(null);
		BufferedImage newImage = new BufferedImage(newImage_Width, newImage_Height,BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(src, 0, 0, newImage_Width, newImage_Height, null);
		
		g.drawImage(pressImg, x, y, pressImg.getWidth(null), pressImg.getHeight(null), null);
		// 水印文件结束
		g.dispose();
		
		return newImage;
	}
	
	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg --
	 *            水印文件
	 * @param targetImg --
	 *            目标文件
	 * @param x
	 *            --x坐标
	 * @param y
	 *            --y坐标
	 */
	public final static void pressImageWithXY(String pressImgFileName, String targetImgFileName,
			int x, int y) {
		try {
			// 目标文件
			File _file = new File(targetImgFileName);
			Image srcImg = ImageIO.read(_file);
			
			// 水印文件
			File _filebiao = new File(pressImgFileName);
			Image pressImg = ImageIO.read(_filebiao);
			
			BufferedImage newImage = pressImage(srcImg,pressImg,x,y);
			
			FileOutputStream out = new FileOutputStream(targetImgFileName);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(newImage);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg --
	 *            水印文件
	 * @param targetImg --
	 *            目标文件
	 * @param position
	 *            --位置
	 * @param offset
	 *            --偏移量，指边框位移量
	 */
	public final static void pressImage(String pressImgFileName, String targetImgFileName,
			int position,int offset) {
		try {
			// 目标文件
			File _file = new File(targetImgFileName);
			Image srcImg = ImageIO.read(_file);
			
			// 水印文件
			File _filebiao = new File(pressImgFileName);
			Image pressImg = ImageIO.read(_filebiao);
			
			int x = 0;
			int y = 0;
			
//			PressTopCenter = 1;		//顶部水平居中
//			PressButtomCenter = 2;	//底部水平居中
//			PressCenter = 3;		//居中
//			PressTopLeft = 4;		//顶部水平居左
//			PressTopRight = 5;		//顶部水平居右
//			PressLeftCenter = 6;	//左边垂直居中
//			PressRightCenter = 7;	//右边垂直居中
//			PressButtomLeft = 8;	//底部水平居左
//			PressButtomRight = 9;	//底部水平居右
			
			int width = srcImg.getWidth(null);
			int height = srcImg.getHeight(null);
			int pressWidth = pressImg.getWidth(null);
			int pressHeight = pressImg.getHeight(null);
			
			if(position==PressTopCenter){
				//顶部水平居中
				x = (width - pressWidth) / 2;
				y = 0 + offset;
			} else if(position==PressButtomCenter){
				//底部水平居中
				x = (width - pressWidth) / 2;
				y = height - pressHeight - offset;
			} else if(position==PressCenter){
				//居中
				x = (width - pressWidth) / 2;
				y = (height - pressHeight) / 2;
			} else if(position==PressTopLeft){
				//顶部水平居左
				x = 0 + offset;
				y = 0 + offset;
			} else if(position==PressTopRight){
				//顶部水平居右
				x = width - pressWidth - offset;
				y = 0 + offset;
			} else if(position==PressLeftCenter){
				//左边垂直居中
				x = 0 + offset;
				y = (height - pressHeight) / 2;
			} else if(position==PressRightCenter){
				//右边垂直居中
				x = width - pressWidth - offset;
				y = (height - pressHeight) / 2;
			} else if(position==PressButtomLeft){
				//底部水平居左
				x = 0 + offset;
				y = height - pressHeight - offset;
			} else if(position==PressButtomRight){
				//底部水平居右
				x = width - pressWidth - offset;
				y = height - pressHeight - offset;
			} else {
				//其他值为居中
				x = (width - pressWidth) / 2;
				y = (height - pressHeight) / 2;
			}
			
			BufferedImage newImage = pressImage(srcImg,pressImg,x,y);
			
			FileOutputStream out = new FileOutputStream(targetImgFileName);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(newImage);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把图片印刷到图片上
	 * @param pressImgFileName
	 * @param targetImgFileName
	 */
	public final static void pressImage(String pressImgFileName, String targetImgFileName) {
		pressImage(pressImgFileName,targetImgFileName,ImageUtils.PressTopRight,2);
	}
	
	/**
	 * 打印文字水印图片
	 * 
	 * @param pressText
	 *            --文字
	 * @param targetImg --
	 *            目标图片
	 * @param fontName --
	 *            字体名
	 * @param fontStyle --
	 *            字体样式
	 * @param color --
	 *            字体颜色
	 * @param fontSize --
	 *            字体大小
	 * @param x --
	 *            偏移量
	 * @param y
	 */
	public static void pressText(String pressText, String targetImg,
			String fontName, int fontStyle, int color, int fontSize, int x,
			int y) {
		try {
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// String s="www.qhd.com.cn";
			g.setColor(Color.RED);
			g.setFont(new Font(fontName, fontStyle, fontSize));

			g.drawString(pressText, wideth - fontSize - x, height - fontSize
					/ 2 - y);
			g.dispose();
			FileOutputStream out = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(image);
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		pressImage("E:/test/logo.png", "E:/test/1.jpg", ImageUtils.PressTopCenter, 20);
		pressImage("E:/test/logo.png", "E:/test/2.jpg", ImageUtils.PressButtomCenter, 20);
		pressImage("E:/test/logo.png", "E:/test/3.jpg", ImageUtils.PressCenter, 20);
		pressImage("E:/test/logo.png", "E:/test/4.jpg", ImageUtils.PressTopLeft, 20);
		pressImage("E:/test/logo.png", "E:/test/5.jpg", ImageUtils.PressTopRight, 20);
		pressImage("E:/test/logo.png", "E:/test/6.jpg", ImageUtils.PressLeftCenter, 20);
		pressImage("E:/test/logo.png", "E:/test/7.jpg", ImageUtils.PressRightCenter, 20);
		pressImage("E:/test/logo.png", "E:/test/8.jpg", ImageUtils.PressButtomLeft, 20);
		pressImage("E:/test/logo.png", "E:/test/9.jpg", ImageUtils.PressButtomRight, 20);
	}
}
