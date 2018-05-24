package cn.sd.core.util.excel;

import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class ExcelUtilFormat implements IExcelUtilFormat {

    public WritableCellFormat getTitleFormat() {
        // TODO Auto-generated method stub
        WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//设置居中
        return format;
    }

}
