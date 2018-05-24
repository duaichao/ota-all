package cn.sd.core.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.Region;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil  {  
    
	private static IExcelUtilFormat ief = null;
	
	
	/**
	 * 复制一个单元格样式到目的单元格样式
	 * @param fromStyle
	 * @param toStyle
	 */
	public static void copyCellStyle(HSSFCellStyle fromStyle,
			HSSFCellStyle toStyle) {
		
		toStyle.setAlignment(fromStyle.getAlignment());
		//边框和边框颜色
		toStyle.setBorderBottom(fromStyle.getBorderBottom());
		toStyle.setBorderLeft(fromStyle.getBorderLeft());
		toStyle.setBorderRight(fromStyle.getBorderRight());
		toStyle.setBorderTop(fromStyle.getBorderTop());
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
		
		//背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());
		
		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		toStyle.setFont(fromStyle.getFont(null));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());//首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());//旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());
		
	}

	/**
	 * 行复制功能
	 * @param fromRow
	 * @param toRow
	 */
	public static void copyRow(HSSFWorkbook wb,HSSFRow fromRow,HSSFRow toRow,boolean copyValueFlag){
		int i = 0;
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			HSSFCell newCell = toRow.createCell(i);
			copyCell(wb,tmpCell, newCell, copyValueFlag);
			i++;
		}
	}
	
	public static void main(String[] args) {
		try {
			File file = new File("D:\\visitor.xls");
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook workBook= new HSSFWorkbook(fis);
			HSSFSheet hssfSheetTarget = workBook.createSheet("日期(单程)-(西安-北京)");
			ExcelUtil.head(workBook,1,hssfSheetTarget);
//			ExcelUtil.body(workBook, 1, hssfSheetTarget);
			ExcelUtil.emptyRow(workBook, 1, hssfSheetTarget, 20);
//			ExcelUtil.copyRow(workBook, hssfSheet.getRow(1), hssfSheet.getRow(20), true);
//			ExcelUtil.copySheet(workBook, hssfSheet, workBook.getSheetAt(1), true);
//			for (int i = 0; i < 10; i++) {
//				hssfSheet.createRow(21+i);
//				hssfSheet.getRow(21+i).createCell(1).setCellValue("汉子");
//			}
			FileOutputStream fout = new FileOutputStream("d:\\students.xls");  
			workBook.write(fout);  
            fout.close();  
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void head(HSSFWorkbook workBook,int exampleIndex,HSSFSheet hssfSheetTarget){
		HSSFSheet hssfSheet = workBook.getSheetAt(exampleIndex);
		HSSFRow hssfRow = hssfSheet.getRow(1);
		int a = 0,f = 5;

		short height = hssfRow.getHeight();
		
		
		HSSFRow toRow = hssfSheetTarget.createRow(1);
		toRow.setHeight(height);
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$B$2:$E$2")); 
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$G$2:$I$2")); 
		
		for (Iterator cellIt = hssfRow.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			CellStyle style = tmpCell.getCellStyle(); 
			String value = tmpCell.getStringCellValue();
			
			HSSFCell newCell = toRow.createCell(tmpCell.getCellNum());
			newCell.setCellStyle(style); 
			newCell.setCellValue(value); 
		}

		
		hssfSheetTarget.setColumnWidth((short) 0, (short) 3500);
		hssfSheetTarget.setColumnWidth((short) 1, (short) 2000);
		hssfSheetTarget.setColumnWidth((short) 2, (short) 2000);
		hssfSheetTarget.setColumnWidth((short) 3, (short) 2000);
		hssfSheetTarget.setColumnWidth((short) 4, (short) 4000);
		hssfSheetTarget.setColumnWidth((short) 5, (short) 3500);
		hssfSheetTarget.setColumnWidth((short) 6, (short) 2000);
		hssfSheetTarget.setColumnWidth((short) 7, (short) 2000);
		hssfSheetTarget.setColumnWidth((short) 8, (short) 2000);
		
	}
	
	
	public static void body(HSSFWorkbook workBook,int exampleIndex,HSSFSheet hssfSheetTarget, int lastRowNum){
		HSSFSheet hssfSheet = workBook.getSheetAt(exampleIndex);
		HSSFRow hssfRowOne = hssfSheet.getRow(3);
		HSSFRow hssfRowTwo = hssfSheet.getRow(4);
		HSSFRow hssfRowThree = hssfSheet.getRow(5);
		
		short heightone = hssfRowOne.getHeight();
		short heightTwo = hssfRowOne.getHeight();
		short heightThree = hssfRowOne.getHeight();
		
		HSSFRow toRowOne = hssfSheetTarget.createRow(lastRowNum+2);
		toRowOne.setHeight(heightone);
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$A$"+(lastRowNum+3)+":$F$"+(lastRowNum+3))); 
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$G$"+(lastRowNum+3)+":$I$"+(lastRowNum+3))); 
		
		HSSFRow toRowTwo = hssfSheetTarget.createRow(lastRowNum+3);
		toRowTwo.setHeight(heightTwo);
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$B$"+(lastRowNum+4)+":$C$"+(lastRowNum+4))); 
		
		HSSFRow toRowThree = hssfSheetTarget.createRow(lastRowNum+4);
		toRowThree.setHeight(heightThree);
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$G$"+(lastRowNum+5)+":$I$"+(lastRowNum+5))); 
		
		for (Iterator cellIt = hssfRowOne.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			CellStyle style = tmpCell.getCellStyle(); 
			String value = tmpCell.getStringCellValue();
			
			HSSFCell newCell = toRowOne.createCell(tmpCell.getCellNum());
			newCell.setCellStyle(style); 
			newCell.setCellValue(value); 
		}
		
		for (Iterator cellIt = hssfRowTwo.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			CellStyle style = tmpCell.getCellStyle(); 
			String value = tmpCell.getStringCellValue();
			
			HSSFCell newCell = toRowTwo.createCell(tmpCell.getCellNum());
			newCell.setCellStyle(style); 
			newCell.setCellValue(value); 
		}
		
		for (Iterator cellIt = hssfRowThree.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			CellStyle style = tmpCell.getCellStyle(); 
			String value = tmpCell.getStringCellValue();
			
			HSSFCell newCell = toRowThree.createCell(tmpCell.getCellNum());
			newCell.setCellStyle(style); 
			newCell.setCellValue(value); 
		}
		
		
	}
	
	public static void emptyRow(HSSFWorkbook workBook,int exampleIndex,HSSFSheet hssfSheetTarget,int rowIndex){
		HSSFSheet hssfSheet = workBook.getSheetAt(exampleIndex);
		HSSFRow hssfRow = hssfSheet.getRow(6);
		short height = hssfRow.getHeight();
		HSSFRow toRow = hssfSheetTarget.createRow(rowIndex);
		for (Iterator cellIt = hssfRow.cellIterator(); cellIt.hasNext();) {
			HSSFCell tmpCell = (HSSFCell) cellIt.next();
			CellStyle style = tmpCell.getCellStyle(); 
			
			HSSFCell newCell = toRow.createCell(tmpCell.getCellNum());
			newCell.setCellStyle(style);  
		}
		
		hssfSheetTarget.addMergedRegion(CellRangeAddress.valueOf("$G$"+(rowIndex+1)+":$I$"+(rowIndex+1))); 
	}

	
	/**
	 * 复制单元格
	 * 
	 * @param srcCell
	 * @param distCell
	 * @param copyValueFlag
	 *            true则连同cell的内容一起复制
	 */
	public static void copyCell(HSSFWorkbook wb,HSSFCell srcCell, HSSFCell distCell,
			boolean copyValueFlag) {
		HSSFCellStyle newstyle=wb.createCellStyle();
		
//		copyCellStyle(srcCell.getCellStyle(), newstyle);
//		distCell.setEncoding(srcCell.getEncoding());
		//样式
		srcCell.getCellStyle().setFillForegroundColor(HSSFColor.RED.index);
		distCell.setCellStyle(srcCell.getCellStyle());
		//评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
				if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
	}
	
	/** 
     * Sheet复制 
     * @param fromSheet 
     * @param toSheet 
     * @param copyValueFlag 
     */  
    public static void copySheet(HSSFWorkbook wb,HSSFSheet fromSheet, HSSFSheet toSheet,  
            boolean copyValueFlag) {  
        //合并区域处理  
        mergerRegion(fromSheet, toSheet);  
        for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {  
            HSSFRow tmpRow = (HSSFRow) rowIt.next();  
            HSSFRow newRow = toSheet.createRow(tmpRow.getRowNum());  
            //行复制  
            copyRow(wb,tmpRow,newRow,copyValueFlag);  
        }  
    } 
    
    /** 
     * 复制原有sheet的合并单元格到新创建的sheet 
     *  
     * @param sheetCreat 新创建sheet 
     * @param sheet      原有的sheet 
     */  
     public static void mergerRegion(HSSFSheet fromSheet, HSSFSheet toSheet) {  
        int sheetMergerCount = fromSheet.getNumMergedRegions();  
        for (int i = 0; i < sheetMergerCount; i++) {  
         Region mergedRegionAt = fromSheet.getMergedRegionAt(i);  
         toSheet.addMergedRegion(mergedRegionAt);  
        }  
     }  
	
    /** 
     * @MethodName  : listToExcel 
     * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小） 
     * @param list      数据源 
     * @param fieldMap      类的英文属性和Excel中的中文列名的对应关系 
     * 如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式 
     * 如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写 
     * fieldMap.put("college.collegeName","学院名称") 
     * @param sheetName 工作表的名称 
     * @param sheetSize 每个工作表中记录的最大个数 
     * @param out       导出流 
     * @throws ExcelException 
     */  
    public static <T>  void   listToExcel (  
            List<T> list ,  
            LinkedHashMap<String,String> fieldMap,  
            String sheetName,  
            int sheetSize,  
            OutputStream out  
            ) throws ExcelException{  
          
          
        if(list.size()==0 || list==null){  
            throw new ExcelException("数据源中没有任何数据");  
        }  
          
        if(sheetSize>65535 || sheetSize<1){  
            sheetSize=65535;  
        }  
          
        //创建工作簿并发送到OutputStream指定的地方  
        WritableWorkbook wwb;  
        try {  
            wwb = Workbook.createWorkbook(out);  
              
            //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条  
            //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程  
            //1.计算一共有多少个工作表  
            double sheetNum=Math.ceil(list.size()/new Integer(sheetSize).doubleValue());
              
            //2.创建相应的工作表，并向其中填充数据  
            for(int i=0; i<sheetNum; i++){  
                //如果只有一个工作表的情况  
                if(1==sheetNum){  
                    WritableSheet sheet=wwb.createSheet(sheetName, i);  
                    fillSheet(sheet, list, fieldMap, 0, list.size()-1);  
                  
                //有多个工作表的情况  
                }else{  
                    WritableSheet sheet=wwb.createSheet(sheetName+(i+1), i);  
                      
                    //获取开始索引和结束索引  
                    int firstIndex=i*sheetSize;  
                    int lastIndex=(i+1)*sheetSize-1>list.size()-1 ? list.size()-1 : (i+1)*sheetSize-1;  
                    //填充工作表  
                    fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);  
                }  
            }  
              
            wwb.write();  
            wwb.close();  
          
        }catch (Exception e) {  
            e.printStackTrace();  
            //如果是ExcelException，则直接抛出  
            if(e instanceof ExcelException){  
                throw (ExcelException)e;  
              
            //否则将其它异常包装成ExcelException再抛出  
            }else{  
                throw new ExcelException("导出Excel失败");  
            }  
        }  
              
    }  
      
    /** 
     * @MethodName  : listToExcel 
     * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，工作表大小为2003支持的最大值） 
     * @param list      数据源 
     * @param fieldMap      类的英文属性和Excel中的中文列名的对应关系 
     * @param out       导出流 
     * @throws ExcelException 
     */  
    public static  <T>  void   listToExcel (  
            List<T> list ,  
            LinkedHashMap<String,String> fieldMap,  
            String sheetName,  
            OutputStream out  
            ) throws ExcelException{  
          
        listToExcel(list, fieldMap, sheetName, 65535, out);  
          
    }  
      
      
    /** 
     * @MethodName  : listToExcel 
     * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小） 
     * @param list      数据源 
     * @param fieldMap      类的英文属性和Excel中的中文列名的对应关系 
     * @param sheetSize    每个工作表中记录的最大个数 
     * @param response  使用response可以导出到浏览器 
     * @throws ExcelException 
     */  
    public static  <T>  void   listToExcel (  
            List<T> list ,  
            LinkedHashMap<String,String> fieldMap,  
            String sheetName,  
            int sheetSize,  
            HttpServletResponse response   
            ) throws ExcelException{  
          
        //设置默认文件名为当前时间：年月日时分秒  
        String fileName=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();  
          
        //设置response头信息  
        response.reset();            
        response.setContentType("application/vnd.ms-excel");        //改成输出excel文件  
        response.setHeader("Content-disposition","attachment; filename="+fileName+".xls" );  
        
        // 根据浏览器进行转码，使其支持中文文件名
		/*if (BrowserUtils.isIE(request)) {
			response.setHeader(
					"content-disposition",
					"attachment;filename="
							+ java.net.URLEncoder.encode(codedFileName,
									"UTF-8") + ".xls");//这个地方要修改成和模板一样的文件类型
		} else {
			String newtitle = new String(codedFileName.getBytes("UTF-8"),
					"ISO8859-1");
			response.setHeader("content-disposition",
					"attachment;filename=" + newtitle + ".xls");
		}*/
        
        
        //创建工作簿并发送到浏览器  
        try {  
              
            OutputStream out=response.getOutputStream();  
            listToExcel(list, fieldMap, sheetName, sheetSize,out );  
              
        } catch (Exception e) {  
            e.printStackTrace();  
              
            //如果是ExcelException，则直接抛出  
            if(e instanceof ExcelException){  
                throw (ExcelException)e;  
              
            //否则将其它异常包装成ExcelException再抛出  
            }else{  
                throw new ExcelException("导出Excel失败");  
            }  
        }  
    }  
      
      
    /** 
     * @MethodName  : listToExcel 
     * @Description : 导出Excel（导出到浏览器，工作表的大小是2003支持的最大值） 
     * @param list      数据源 
     * @param fieldMap      类的英文属性和Excel中的中文列名的对应关系 
     * @param response  使用response可以导出到浏览器 
     * @throws ExcelException 
     */  
    public static <T>  void   listToExcel (  
            List<T> list ,  
            LinkedHashMap<String,String> fieldMap,  
            String sheetName,  
            HttpServletResponse response   
            ) throws ExcelException{  
          
        listToExcel(list, fieldMap, sheetName, 65535, response);  
    }  
      
    /** 
     * @MethodName          : excelToList 
     * @Description             : 将Excel转化为List 
     * @param in                    ：承载着Excel的输入流 
     * @param sheetIndex        ：要导入的工作表序号 
     * @param entityClass       ：List中对象的类型（Excel中的每一行都要转化为该类型的对象） 
     * @param fieldMap          ：Excel中的中文列头和类的英文属性的对应关系Map 
     * @param uniqueFields  ：指定业务主键组合（即复合主键），这些列的组合不能重复 
     * @return                      ：List 
     * @throws ExcelException 
     */  
    public static <T>  List<T>  excelToList(  
            InputStream in,  
            String sheetName,  
            Class<T> entityClass,  
            LinkedHashMap<String, String> fieldMap,  
            String[] uniqueFields  
            ) throws ExcelException{  
          
        //定义要返回的list  
        List<T> resultList=new ArrayList<T>();  
          
        try {  
              
            //根据Excel数据源创建WorkBook  
            Workbook wb=Workbook.getWorkbook(in);  
            //获取工作表  
            Sheet sheet=wb.getSheet(sheetName);  
              
            //获取工作表的有效行数  
            int realRows=0;  
            for(int i=0;i<sheet.getRows();i++){  
                  
                int nullCols=0;  
                for(int j=0;j<sheet.getColumns();j++){  
                    Cell currentCell=sheet.getCell(j,i);  
                    if(currentCell==null || "".equals(currentCell.getContents().toString())){  
                        nullCols++;  
                    }  
                }  
                  
                if(nullCols==sheet.getColumns()){  
                    break;  
                }else{  
                    realRows++;  
                }  
            }  
              
              
            //如果Excel中没有数据则提示错误  
            if(realRows<=1){  
                throw new ExcelException("Excel文件中没有任何数据");  
            }  
              
              
            Cell[] firstRow=sheet.getRow(0);  
  
            String[] excelFieldNames=new String[firstRow.length];  
              
            //获取Excel中的列名  
            for(int i=0;i<firstRow.length;i++){  
                excelFieldNames[i]=firstRow[i].getContents().toString().trim();  
            }  
              
            //判断需要的字段在Excel中是否都存在  
            boolean isExist=true;  
            List<String> excelFieldList=Arrays.asList(excelFieldNames);  
            for(String cnName : fieldMap.keySet()){  
                if(!excelFieldList.contains(cnName)){  
                    isExist=false;  
                    break;  
                }  
            }  
              
            //如果有列名不存在，则抛出异常，提示错误  
            if(!isExist){  
                throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");  
            }  
              
              
            //将列名和列号放入Map中,这样通过列名就可以拿到列号  
            LinkedHashMap<String, Integer> colMap=new LinkedHashMap<String, Integer>();  
            for(int i=0;i<excelFieldNames.length;i++){  
                colMap.put(excelFieldNames[i], firstRow[i].getColumn());  
            }   
              
              
              
            //判断是否有重复行  
            //1.获取uniqueFields指定的列  
            Cell[][] uniqueCells=new Cell[uniqueFields.length][];  
            for(int i=0;i<uniqueFields.length;i++){  
                int col=colMap.get(uniqueFields[i]);  
                uniqueCells[i]=sheet.getColumn(col);  
            }  
              
            //2.从指定列中寻找重复行  
            for(int i=1;i<realRows;i++){  
                int nullCols=0;  
                for(int j=0;j<uniqueFields.length;j++){  
                    String currentContent=uniqueCells[j][i].getContents();  
                    Cell sameCell=sheet.findCell(currentContent);  
                    if(sameCell!=null){  
                        nullCols++;  
                    }  
                }  
                  
                if(nullCols==uniqueFields.length){  
                    throw new ExcelException("Excel中有重复行，请检查");  
                }  
            }  
  
            //将sheet转换为list  
            for(int i=1;i<realRows;i++){  
                //新建要转换的对象  
                T entity=entityClass.newInstance();  
                  
                //给对象中的字段赋值  
                for(Entry<String, String> entry : fieldMap.entrySet()){  
                    //获取中文字段名  
                    String cnNormalName=entry.getKey();  
                    //获取英文字段名  
                    String enNormalName=entry.getValue();  
                    //根据中文字段名获取列号  
                    int col=colMap.get(cnNormalName);  
                      
                    //获取当前单元格中的内容  
                    String content=sheet.getCell(col, i).getContents().toString().trim();  
                      
                    //给对象赋值  
                    setFieldValueByName(enNormalName, content, entity);  
                }  
                  
                resultList.add(entity);  
            }  
        } catch(Exception e){  
            e.printStackTrace();  
            //如果是ExcelException，则直接抛出  
            if(e instanceof ExcelException){  
                throw (ExcelException)e;  
              
            //否则将其它异常包装成ExcelException再抛出  
            }else{  
                e.printStackTrace();  
                throw new ExcelException("导入Excel失败");  
            }  
        }  
        return resultList;  
    }  
      
      
      
      
      
    /*<-------------------------辅助的私有方法----------------------------------------------->*/  
    /** 
     * @MethodName  : getFieldValueByName 
     * @Description : 根据字段名获取字段值 
     * @param fieldName 字段名 
     * @param o 对象 
     * @return  字段值 
     */  
    private static  Object getFieldValueByName(String fieldName, Object o) throws Exception{  
          
        Object value=null;  
        Field field=getFieldByName(fieldName, o.getClass());  
          
        if(field !=null){  
            field.setAccessible(true);  
            value=field.get(o);  
        }else{  
            throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 "+fieldName);  
        }  
          
        return value;  
    }  
      
    /** 
     * @MethodName  : getFieldByName 
     * @Description : 根据字段名获取字段 
     * @param fieldName 字段名 
     * @param clazz 包含该字段的类 
     * @return 字段 
     */  
    private static Field getFieldByName(String fieldName, Class<?>  clazz){  
        //拿到本类的所有字段  
        Field[] selfFields=clazz.getDeclaredFields();  
          
        //如果本类中存在该字段，则返回  
        for(Field field : selfFields){  
            if(field.getName().equals(fieldName)){  
                return field;  
            }  
        }  
          
        //否则，查看父类中是否存在此字段，如果有则返回  
        Class<?> superClazz=clazz.getSuperclass();  
        if(superClazz!=null  &&  superClazz !=Object.class){  
            return getFieldByName(fieldName, superClazz);  
        }  
          
        //如果本类和父类都没有，则返回空  
        return null;  
    }  
      
      
      
    /** 
     * @MethodName  : getFieldValueByNameSequence 
     * @Description :  
     * 根据带路径或不带路径的属性名获取属性值 
     * 即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等 
     *  
     * @param fieldNameSequence  带路径的属性名或简单属性名 
     * @param o 对象 
     * @return  属性值 
     * @throws Exception 
     */  
    private static  Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception{  
          
        Object value=null;  
        boolean isMap = o instanceof Map;
        if(isMap){
        	Map om = (Map)o;
        	return om.get(fieldNameSequence);
        }else{
	        //将fieldNameSequence进行拆分  
	        String[] attributes=fieldNameSequence.split("\\.");  
	        if(attributes.length==1){  
	            value=getFieldValueByName(fieldNameSequence, o);  
	        }else{  
	            //根据属性名获取属性对象  
	            Object fieldObj=getFieldValueByName(attributes[0], o);  
	            String subFieldNameSequence=fieldNameSequence.substring(fieldNameSequence.indexOf(".")+1);  
	            value=getFieldValueByNameSequence(subFieldNameSequence, fieldObj);  
	        }  
        }
        return value;   
          
    }   
      
      
    /** 
     * @MethodName  : setFieldValueByName 
     * @Description : 根据字段名给对象的字段赋值 
     * @param fieldName  字段名 
     * @param fieldValue    字段值 
     * @param o 对象 
     */  
    private static void setFieldValueByName(String fieldName,Object fieldValue,Object o) throws Exception{  
          
            Field field=getFieldByName(fieldName, o.getClass());  
            if(field!=null){  
                field.setAccessible(true);  
                //获取字段类型  
                Class<?> fieldType = field.getType();    
                  
                //根据字段类型给字段赋值  
                if (String.class == fieldType) {    
                    field.set(o, String.valueOf(fieldValue));    
                } else if ((Integer.TYPE == fieldType)    
                        || (Integer.class == fieldType)) {    
                    field.set(o, Integer.parseInt(fieldValue.toString()));    
                } else if ((Long.TYPE == fieldType)    
                        || (Long.class == fieldType)) {    
                    field.set(o, Long.valueOf(fieldValue.toString()));    
                } else if ((Float.TYPE == fieldType)    
                        || (Float.class == fieldType)) {    
                    field.set(o, Float.valueOf(fieldValue.toString()));    
                } else if ((Short.TYPE == fieldType)    
                        || (Short.class == fieldType)) {    
                    field.set(o, Short.valueOf(fieldValue.toString()));    
                } else if ((Double.TYPE == fieldType)    
                        || (Double.class == fieldType)) {    
                    field.set(o, Double.valueOf(fieldValue.toString()));    
                } else if (Character.TYPE == fieldType) {    
                    if ((fieldValue!= null) && (fieldValue.toString().length() > 0)) {    
                        field.set(o, Character    
                                .valueOf(fieldValue.toString().charAt(0)));    
                    }    
                }else if(Date.class==fieldType){  
                    field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fieldValue.toString()));  
                }else{  
                    field.set(o, fieldValue);  
                }  
            }else{  
                throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 "+fieldName);  
            }  
    }  
      
      
    /** 
     * @MethodName  : setColumnAutoSize 
     * @Description : 设置工作表自动列宽和首行加粗 
     * @param ws 
     */  
    private static void setColumnAutoSize(WritableSheet ws,int extraWith){  
        //获取本列的最宽单元格的宽度  
        for(int i=0;i<ws.getColumns();i++){  
            int colWith=0;  
            for(int j=0;j<ws.getRows();j++){  
                String content=ws.getCell(i,j).getContents().toString();  
                int cellWith=content.length();  
                if(colWith<cellWith){  
                    colWith=cellWith;  
                }  
            }  
            //设置单元格的宽度为最宽宽度+额外宽度  
            ws.setColumnView(i, colWith+extraWith);  
        }  
          
    }  
    /** 
     * @MethodName  : fillSheet 
     * @Description : 向工作表中填充数据 
     * @param sheet     工作表  
     * @param list  数据源 
     * @param fieldMap 中英文字段对应关系的Map 
     * @param firstIndex    开始索引 
     * @param lastIndex 结束索引 
     */  
    private static <T> void fillSheet(  
            WritableSheet sheet,  
            List<T> list,  
            LinkedHashMap<String,String> fieldMap,  
            int firstIndex,  
            int lastIndex  
            )throws Exception{  
          
        //定义存放英文字段名和中文字段名的数组  
        String[] enFields=new String[fieldMap.size()];  
        String[] cnFields=new String[fieldMap.size()];  
          
        //填充数组  
        int count=0;  
        for(Entry<String,String> entry:fieldMap.entrySet()){  
            enFields[count]=entry.getKey();  
            cnFields[count]=entry.getValue();  
            count++;  
        }  
        
        WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);  
        WritableCellFormat format = new WritableCellFormat(font);  
        format.setAlignment(jxl.format.Alignment.CENTRE);//设置居中  
        
        //填充表头  
        for(int i=0;i<cnFields.length;i++){  
        	if(ief==null){
        		Label label=new Label(i,0,cnFields[i]);  
                sheet.addCell(label); 
        	}else{
        		Label label=new Label(i,0,cnFields[i], ief.getTitleFormat());  
                sheet.addCell(label); 
        	}
             
        }  
          
        //填充内容  
        int rowNo=1;  
        for(int index=firstIndex;index<=lastIndex;index++){  
            //获取单个对象  
            T item=list.get(index);
            for(int i=0;i<enFields.length;i++){  
                Object objValue=getFieldValueByNameSequence(enFields[i], item);  
                String fieldValue=objValue==null ? "" : objValue.toString();  
                Label label =new Label(i,rowNo,fieldValue);  
                sheet.addCell(label);  
            }  
              
            rowNo++;  
        }  
          
        //设置自动列宽  
        setColumnAutoSize(sheet, 5);  
    }

	public static void setIef(IExcelUtilFormat ief) {
		ExcelUtil.ief = ief;
	}  
      
}  