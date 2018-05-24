package cn.sd.core.util;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;

public class POIWORD {
  public static void main(String[] args) throws Exception {
    POIWORD t2 = new POIWORD();
    t2.createTable();
  }

  /**
   * @Description: 添加方块♢
   */
  public void setCellContentCommonFunction(XWPFTableCell cell, String content)
          throws Exception {
    XWPFParagraph p = cell.addParagraph();
    setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "300",
            STLineSpacingRule.AUTO);
    setParagraphAlignInfo(p, ParagraphAlignment.BOTH, TextAlignment.CENTER);
    XWPFRun pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
            false, false, 0, 6, 0);
    pRun = getOrAddParagraphFirstRun(p, true, false);
    setParagraphRunFontInfo(p, pRun, content, "宋体", "Times New Roman",
            "21", true, false, false, false, null, null, 0, 6, 0);
  }

  /**
   * @Description: 保存文档
   */
  public void saveDocument(XWPFDocument document, String savePath)
          throws Exception {
    FileOutputStream fos = new FileOutputStream(savePath);
    document.write(fos);
    fos.close();
  }

  /**
   * @Description: 得到单元格第一个Paragraph
   */
  public XWPFParagraph getCellFirstParagraph(XWPFTableCell cell) {
    XWPFParagraph p;
    if (cell.getParagraphs() != null && cell.getParagraphs().size() > 0) {
      p = cell.getParagraphs().get(0);
    } else {
      p = cell.addParagraph();
    }
    return p;
  }

  /**
   * @Description: 得到段落CTPPr
   */
  public CTPPr getParagraphCTPPr(XWPFParagraph p) {
    CTPPr pPPr = null;
    if (p.getCTP() != null) {
      if (p.getCTP().getPPr() != null) {
        pPPr = p.getCTP().getPPr();
      } else {
        pPPr = p.getCTP().addNewPPr();
      }
    }
    return pPPr;
  }

  /**
   * @Description: 设置段落间距信息,一行=100 一磅=20
   */
  public void setParagraphSpacingInfo(XWPFParagraph p, boolean isSpace,
                                      String before, String after, String beforeLines, String afterLines,
                                      boolean isLine, String line, STLineSpacingRule.Enum lineValue) {
    CTPPr pPPr = getParagraphCTPPr(p);
    CTSpacing pSpacing = pPPr.getSpacing() != null ? pPPr.getSpacing()
            : pPPr.addNewSpacing();
    if (isSpace) {
      // 段前磅数
      if (before != null) {
        pSpacing.setBefore(new BigInteger(before));
      }
      // 段后磅数
      if (after != null) {
        pSpacing.setAfter(new BigInteger(after));
      }
      // 段前行数
      if (beforeLines != null) {
        pSpacing.setBeforeLines(new BigInteger(beforeLines));
      }
      // 段后行数
      if (afterLines != null) {
        pSpacing.setAfterLines(new BigInteger(afterLines));
      }
    }
    // 间距
    if (isLine) {
      if (line != null) {
        pSpacing.setLine(new BigInteger(line));
      }
      if (lineValue != null) {
        pSpacing.setLineRule(lineValue);
      }
    }
  }

  /**
   * @Description: 设置段落文本样式(高亮与底纹显示效果不同)设置字符间距信息(CTSignedTwipsMeasure)
   * @param verticalAlign
   *            : SUPERSCRIPT上标 SUBSCRIPT下标
   * @param position
   *            :字符间距位置：>0提升 <0降低=磅值*2 如3磅=6
   * @param spacingValue
   *            :字符间距间距 >0加宽 <0紧缩 =磅值*20 如2磅=40
   * @param indent
   *            :字符间距缩进 <100 缩
   */

  public void setParagraphRunFontInfo(XWPFParagraph p, XWPFRun pRun,
                                      String content, String cnFontFamily, String enFontFamily,
                                      String fontSize, boolean isBlod, boolean isItalic,
                                      boolean isStrike, boolean isShd, String shdColor,
                                      STShd.Enum shdStyle, int position, int spacingValue, int indent) {
    CTRPr pRpr = getRunCTRPr(p, pRun);
    if (StringUtils.isNotBlank(content)) {
      // pRun.setText(content);
      if (content.contains("\n")) {// System.properties("line.separator")
        String[] lines = content.split("\n");
        pRun.setText(lines[0], 0); // set first line into XWPFRun
        for (int i = 1; i < lines.length; i++) {
          // add break and insert new text
          pRun.addBreak();
          pRun.setText(lines[i]);
        }
      } else {
        pRun.setText(content, 0);
      }
    }
    // 设置字体
    CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
            .addNewRFonts();
    if (StringUtils.isNotBlank(enFontFamily)) {
      fonts.setAscii(enFontFamily);
      fonts.setHAnsi(enFontFamily);
    }
    if (StringUtils.isNotBlank(cnFontFamily)) {
      fonts.setEastAsia(cnFontFamily);
      fonts.setHint(STHint.EAST_ASIA);
    }
    // 设置字体大小
    CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
    sz.setVal(new BigInteger(fontSize));

    CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
            .addNewSzCs();
    szCs.setVal(new BigInteger(fontSize));

    // 设置字体样式
    // 加粗
    if (isBlod) {
      pRun.setBold(isBlod);
    }
    // 倾斜
    if (isItalic) {
      pRun.setItalic(isItalic);
    }
    // 删除线
    if (isStrike) {
      pRun.setStrike(isStrike);
    }
    if (isShd) {
      // 设置底纹
      CTShd shd = pRpr.isSetShd() ? pRpr.getShd() : pRpr.addNewShd();
      if (shdStyle != null) {
        shd.setVal(shdStyle);
      }
      if (shdColor != null) {
        shd.setColor(shdColor);
        shd.setFill(shdColor);
      }
    }

    // 设置文本位置
    if (position != 0) {
      pRun.setTextPosition(position);
    }
    if (spacingValue > 0) {
      // 设置字符间距信息
      CTSignedTwipsMeasure ctSTwipsMeasure = pRpr.isSetSpacing() ? pRpr
              .getSpacing() : pRpr.addNewSpacing();
      ctSTwipsMeasure
              .setVal(new BigInteger(String.valueOf(spacingValue)));
    }
    if (indent > 0) {
      CTTextScale paramCTTextScale = pRpr.isSetW() ? pRpr.getW() : pRpr
              .addNewW();
      paramCTTextScale.setVal(indent);
    }
  }

  /**
   * @Description: 得到XWPFRun的CTRPr
   */
  public CTRPr getRunCTRPr(XWPFParagraph p, XWPFRun pRun) {
    CTRPr pRpr = null;
    if (pRun.getCTR() != null) {
      pRpr = pRun.getCTR().getRPr();
      if (pRpr == null) {
        pRpr = pRun.getCTR().addNewRPr();
      }
    } else {
      pRpr = p.getCTP().addNewR().addNewRPr();
    }
    return pRpr;
  }

  /**
   * @Description: 设置段落对齐
   */
  public void setParagraphAlignInfo(XWPFParagraph p,
                                    ParagraphAlignment pAlign, TextAlignment valign) {
    if (pAlign != null) {
      p.setAlignment(pAlign);
    }
    if (valign != null) {
      p.setVerticalAlignment(valign);
    }
  }

  public XWPFRun getOrAddParagraphFirstRun(XWPFParagraph p, boolean isInsert,
                                           boolean isNewLine) {
    XWPFRun pRun = null;
    if (isInsert) {
      pRun = p.createRun();
    } else {
      if (p.getRuns() != null && p.getRuns().size() > 0) {
        pRun = p.getRuns().get(0);
      } else {
        pRun = p.createRun();
      }
    }
    if (isNewLine) {
      pRun.addBreak();
    }
    return pRun;
  }

  /**
   * @Description: 设置Table的边框
   */
  public void setTableBorders(XWPFTable table, STBorder.Enum borderType,
                              String size, String color, String space) {
    CTTblPr tblPr = getTableCTTblPr(table);
    CTTblBorders borders = tblPr.isSetTblBorders() ? tblPr.getTblBorders()
            : tblPr.addNewTblBorders();
    CTBorder hBorder = borders.isSetInsideH() ? borders.getInsideH()
            : borders.addNewInsideH();
    hBorder.setVal(borderType);
    hBorder.setSz(new BigInteger(size));
    hBorder.setColor(color);
    hBorder.setSpace(new BigInteger(space));

    CTBorder vBorder = borders.isSetInsideV() ? borders.getInsideV()
            : borders.addNewInsideV();
    vBorder.setVal(borderType);
    vBorder.setSz(new BigInteger(size));
    vBorder.setColor(color);
    vBorder.setSpace(new BigInteger(space));

    CTBorder lBorder = borders.isSetLeft() ? borders.getLeft() : borders
            .addNewLeft();
    lBorder.setVal(borderType);
    lBorder.setSz(new BigInteger(size));
    lBorder.setColor(color);
    lBorder.setSpace(new BigInteger(space));

    CTBorder rBorder = borders.isSetRight() ? borders.getRight() : borders
            .addNewRight();
    rBorder.setVal(borderType);
    rBorder.setSz(new BigInteger(size));
    rBorder.setColor(color);
    rBorder.setSpace(new BigInteger(space));

    CTBorder tBorder = borders.isSetTop() ? borders.getTop() : borders
            .addNewTop();
    tBorder.setVal(borderType);
    tBorder.setSz(new BigInteger(size));
    tBorder.setColor(color);
    tBorder.setSpace(new BigInteger(space));

    CTBorder bBorder = borders.isSetBottom() ? borders.getBottom()
            : borders.addNewBottom();
    bBorder.setVal(borderType);
    bBorder.setSz(new BigInteger(size));
    bBorder.setColor(color);
    bBorder.setSpace(new BigInteger(space));
  }

  /**
   * @Description: 得到Table的CTTblPr,不存在则新建
   */
  public CTTblPr getTableCTTblPr(XWPFTable table) {
    CTTbl ttbl = table.getCTTbl();
    CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl
            .getTblPr();
    return tblPr;
  }

  /**
   * @Description: 设置列宽和垂直对齐方式
   */
  public void setCellWidthAndVAlign(XWPFTableCell cell, String width,
                                    STTblWidth.Enum typeEnum, STVerticalJc.Enum vAlign) {
    CTTcPr tcPr = getCellCTTcPr(cell);
    CTTblWidth tcw = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
    if (width != null) {
      tcw.setW(new BigInteger(width));
    }
    if (typeEnum != null) {
      tcw.setType(typeEnum);
    }
    if (vAlign != null) {
      CTVerticalJc vJc = tcPr.isSetVAlign() ? tcPr.getVAlign() : tcPr
              .addNewVAlign();
      vJc.setVal(vAlign);
    }
  }

  /**
   * @Description: 跨列合并
   */
  public void mergeCellsHorizontal(XWPFTable table, int row, int fromCell,
                                   int toCell) {
    for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
      XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
      if (cellIndex == fromCell) {
        // The first merged cell is set with RESTART merge value
        getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.RESTART);
      } else {
        // Cells which join (merge) the first one,are set with CONTINUE
        getCellCTTcPr(cell).addNewHMerge().setVal(STMerge.CONTINUE);
      }
    }
  }

  /**
   *
   * @Description: 得到Cell的CTTcPr,不存在则新建
   */
  public CTTcPr getCellCTTcPr(XWPFTableCell cell) {
    CTTc cttc = cell.getCTTc();
    CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
    return tcPr;
  }

  /**
   * @Description: 设置表格列宽
   */
  public void setTableGridCol(XWPFTable table, int[] colWidths) {
    CTTbl ttbl = table.getCTTbl();
    CTTblGrid tblGrid = ttbl.getTblGrid() != null ? ttbl.getTblGrid()
            : ttbl.addNewTblGrid();
    for (int j = 0, len = colWidths.length; j < len; j++) {
      CTTblGridCol gridCol = tblGrid.addNewGridCol();
      gridCol.setW(new BigInteger(String.valueOf(colWidths[j])));
    }
  }

  public void setParagraphRunSymInfo(XWPFParagraph p, XWPFRun pRun,
                                     String cnFontFamily, String enFontFamily, String fontSize,
                                     boolean isBlod, boolean isItalic, boolean isStrike, int position,
                                     int spacingValue, int indent) throws Exception {
    CTRPr pRpr = getRunCTRPr(p, pRun);
    // 设置字体
    CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr
            .addNewRFonts();
    if (StringUtils.isNotBlank(enFontFamily)) {
      fonts.setAscii(enFontFamily);
      fonts.setHAnsi(enFontFamily);
    }
    if (StringUtils.isNotBlank(cnFontFamily)) {
      fonts.setEastAsia(cnFontFamily);
      fonts.setHint(STHint.EAST_ASIA);
    }
    // 设置字体大小
    CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
    sz.setVal(new BigInteger(fontSize));

    CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr
            .addNewSzCs();
    szCs.setVal(new BigInteger(fontSize));

    // 设置字体样式
    // 加粗
    if (isBlod) {
      pRun.setBold(isBlod);
    }
    // 倾斜
    if (isItalic) {
      pRun.setItalic(isItalic);
    }
    // 删除线
    if (isStrike) {
      pRun.setStrike(isStrike);
    }
    // 设置文本位置
    if (position != 0) {
      pRun.setTextPosition(position);
    }
    if (spacingValue > 0) {
      // 设置字符间距信息
      CTSignedTwipsMeasure ctSTwipsMeasure = pRpr.isSetSpacing() ? pRpr
              .getSpacing() : pRpr.addNewSpacing();
      ctSTwipsMeasure
              .setVal(new BigInteger(String.valueOf(spacingValue)));
    }
    if (indent > 0) {
      CTTextScale paramCTTextScale = pRpr.isSetW() ? pRpr.getW() : pRpr
              .addNewW();
      paramCTTextScale.setVal(indent);
    }
    List<CTSym> symList = pRun.getCTR().getSymList();
    CTSym sym = CTSym.Factory.parse("<xml-fragment w:font=\"Wingdings 2\" w:char=\"00A3\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"> </xml-fragment>");
    symList.add(sym);
  }

  /**
   * @Description: 设置表格总宽度与水平对齐方式
   */
  public void setTableWidthAndHAlign(XWPFTable table, String width,
                                     STJc.Enum enumValue) {
    CTTblPr tblPr = getTableCTTblPr(table);
    CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr
            .addNewTblW();
    if (enumValue != null) {
      CTJc cTJc = tblPr.addNewJc();
      cTJc.setVal(enumValue);
    }
    tblWidth.setW(new BigInteger(width));
    tblWidth.setType(STTblWidth.DXA);
  }

  /**
   * @Description: 设置单元格Margin
   */
  public void setTableCellMargin(XWPFTable table, int top, int left,
                                 int bottom, int right) {
    table.setCellMargins(top, left, bottom, right);
  }

  /**
   * @Description: 得到CTTrPr,不存在则新建
   */
  public CTTrPr getRowCTTrPr(XWPFTableRow row) {
    CTRow ctRow = row.getCtRow();
    CTTrPr trPr = ctRow.isSetTrPr() ? ctRow.getTrPr() : ctRow.addNewTrPr();
    return trPr;
  }

  /**
   * @Description: 设置行高
   */
  public void setRowHeight(XWPFTableRow row, String hight,
                           STHeightRule.Enum heigthEnum) {
    CTTrPr trPr = getRowCTTrPr(row);
    CTHeight trHeight;
    if (trPr.getTrHeightList() != null && trPr.getTrHeightList().size() > 0) {
      trHeight = trPr.getTrHeightList().get(0);
    } else {
      trHeight = trPr.addNewTrHeight();
    }
    trHeight.setVal(new BigInteger(hight));
    if (heigthEnum != null) {
      trHeight.setHRule(heigthEnum);
    }
  }

  /**
   * @Description: 设置底纹
   */
  public void setCellShdStyle(XWPFTableCell cell, boolean isShd,
                              String shdColor, STShd.Enum shdStyle) {
    CTTcPr tcPr = getCellCTTcPr(cell);
    if (isShd) {
      // 设置底纹
      CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
      if (shdStyle != null) {
        shd.setVal(shdStyle);
      }
      if (shdColor != null) {
        shd.setColor(shdColor);
        shd.setFill(shdColor);
      }
    }
  }

  public void createTable() throws Exception {
    FileInputStream is = new FileInputStream(new File("d:\\tmp.docx"));
    XWPFDocument xdoc = new XWPFDocument(is);
    XWPFHeaderFooterPolicy headerFooterPolicy = xdoc.getHeaderFooterPolicy();

    XWPFHeader header = headerFooterPolicy.getDefaultHeader();
    XWPFParagraph header_paragraph = header.getListParagraph().get(0);
    setParagraphAlignInfo(header_paragraph, ParagraphAlignment.RIGHT, TextAlignment.BOTTOM);
    XWPFRun header_run = getOrAddParagraphFirstRun(header_paragraph, false, false);
    setParagraphRunFontInfo(header_paragraph, header_run, "\r\n光华路门市", "宋体",
            "Times New Roman", "28", true, false, false, true, null, null,
            10, 0, 90);


    XWPFFooter footer = headerFooterPolicy.getDefaultFooter();
    XWPFParagraph footer_paragraph = footer.getListParagraph().get(0);
    setParagraphAlignInfo(footer_paragraph, ParagraphAlignment.LEFT, TextAlignment.BOTTOM);
    XWPFRun footer_run = getOrAddParagraphFirstRun(footer_paragraph, false, false);
    setParagraphRunFontInfo(footer_paragraph, footer_run, "地址:西安市雁塔区高新路凯创国际A座2103        电话: 15383460856", "宋体",
            "Times New Roman", "24", true, false, false, true, null, null,
            10, 0, 90);

    XWPFParagraph p = xdoc.createParagraph();
    // 固定值25磅
    setParagraphSpacingInfo(p, true, "0", "80", null, null, true, "500",
            STLineSpacingRule.EXACT);
    // 居中
    setParagraphAlignInfo(p, ParagraphAlignment.CENTER,
            TextAlignment.CENTER);
    XWPFRun pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "兵马俑、华清池、秦陵地宫、半坡博物馆完美一日游", "宋体",
            "Times New Roman", "36", true, false, false, false, null, null,
            0, 0, 90);

    p = xdoc.createParagraph();
    // 固定值25磅
    setParagraphSpacingInfo(p, true, "0", "80", null, null, true, "500",
            STLineSpacingRule.EXACT);
    // 居中
    setParagraphAlignInfo(p, ParagraphAlignment.RIGHT,
            TextAlignment.CENTER);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "编号:LQLYB000001", "宋体", "Times New Roman",
            "20", true, false, false, false, null, null, 0, 0, 90);



    XWPFTable table = xdoc.createTable(5, 2);

    setTableBorders(table, STBorder.SINGLE, "2", "auto", "0");
    setTableWidthAndHAlign(table, "9024", STJc.CENTER);
    setTableCellMargin(table, 0, 108, 0, 108);
    int[] colWidths1 = new int[] {50, 8974};
    setTableGridCol(table, colWidths1);

    XWPFTableRow row = table.getRow(0);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    XWPFTableCell cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "销售须知", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "行程时间仅供参考，因路况或游客众多，行程顺序将视情况前后变更！", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);


    row = table.getRow(1);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "产品推荐", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "秦始皇兵马俑被誉为“世界第八大奇迹”、二十世纪考古史上最伟大发现，被联合国教科文组织列入世界文化遗产名录。", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    row = table.getRow(2);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "产品特色", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "移步文化殿堂，翻开五千年历史画卷。经典线路 ，完美旅程，贴心服务。", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    row = table.getRow(3);
    setRowHeight(row, "387", STHeightRule.AT_LEAST);
    cell = row.getCell(0);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "西安", "宋体", "Times New Roman",
            "24", true, false, false, false, null, null, 0, 6, 0);
    mergeCellsHorizontal(table, 3, 0, 1);

    row = table.getRow(4);
    setRowHeight(row, "387", STHeightRule.AT_LEAST);
    cell = row.getCell(0);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, " D1 西安 - 咸阳 ", "宋体", "Times New Roman",
            "24", true, false, false, false, null, null, 0, 6, 0);
    mergeCellsHorizontal(table, 4, 0, 1);


    row = table.getRow(2);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "当日提示", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "赠送矿泉水", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);


    row = table.getRow(2);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "住宿提示", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "长野地区酒店", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);



    row = table.getRow(2);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "自费提示", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "可以选择参加当地自费推荐项目【迪士尼乐园】或【台场海滨公园+丰田汽车馆】，每项目满10人方可成团", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);


    row = table.getRow(2);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "三餐", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "早：自理中：自理晚：自理", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);



    row = table.getRow(3);
    setRowHeight(row, "387", STHeightRule.AT_LEAST);
    cell = row.getCell(0);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "费用包含", "宋体", "Times New Roman",
            "24", true, false, false, false, null, null, 0, 6, 0);
    mergeCellsHorizontal(table, 3, 0, 1);


    row = table.getRow(2);
    setRowHeight(row, "460", STHeightRule.AT_LEAST);

    cell = row.getCell(0);
    cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "门票", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);

    cell = row.getCell(1);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "行程中所含的景点首道大门票", "宋体",
            "Times New Roman", "24", false, false, false, false, null, null,
            0, 6, 0);


    row = table.getRow(3);
    setRowHeight(row, "387", STHeightRule.AT_LEAST);
    cell = row.getCell(0);
    setCellShdStyle(cell, true, "FFFFFF", null);
    p = getCellFirstParagraph(cell);
    pRun = getOrAddParagraphFirstRun(p, false, false);
    setParagraphRunFontInfo(p, pRun, "费用不含", "宋体", "Times New Roman",
            "24", true, false, false, false, null, null, 0, 6, 0);
    mergeCellsHorizontal(table, 3, 0, 1);



    //-----------------------------------------------------------------------------------------------------------------
//    XWPFTable table = xdoc.createTable(22, 4);
//    setTableBorders(table, STBorder.SINGLE, "4", "auto", "0");
//    setTableWidthAndHAlign(table, "9024", STJc.CENTER);
//    setTableCellMargin(table, 0, 108, 0, 108);
//    int[] colWidths = new int[] { 2169, 2638, 525, 3692 };
//    setTableGridCol(table, colWidths);
//
//    XWPFTableRow row = table.getRow(0);
//    setRowHeight(row, "460", STHeightRule.AT_LEAST);
//    XWPFTableCell cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "A.负责人或部门资料 ﹡", "宋体",
//        "Times New Roman", "24", true, false, false, false, null, null,
//        0, 6, 0);
//    mergeCellsHorizontal(table, 0, 0, 3);
//
//    row = table.getRow(1);
//    setRowHeight(row, "567", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellWidthAndVAlign(cell, "2169", STTblWidth.DXA, STVerticalJc.TOP);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "1.负责人：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//
//    cell = row.getCell(1);
//    setCellWidthAndVAlign(cell, "3163", STTblWidth.DXA, STVerticalJc.TOP);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "2.负责部门：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//
//    cell = row.getCell(2);
//    setCellWidthAndVAlign(cell, "3692", STTblWidth.DXA, STVerticalJc.TOP);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "3.事件发生地点：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    cell = row.getCell(3);
//    setCellWidthAndVAlign(cell, "0", STTblWidth.AUTO, STVerticalJc.TOP);
//    mergeCellsHorizontal(table, 1, 2, 3);
//
//    row = table.getRow(2);
//    setRowHeight(row, "657", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "4.在场相关人员：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 2, 0, 3);
//
//    row = table.getRow(3);
//    setRowHeight(row, "387", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "B.不良事件情况 ﹡", "宋体", "Times New Roman",
//        "24", true, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 3, 0, 3);
//
//    row = table.getRow(4);
//    setRowHeight(row, "1613", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "5.事件主要表现：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 4, 0, 3);
//
//    row = table.getRow(5);
//    setRowHeight(row, "369", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "C.不良事件类别 ﹡", "宋体", "Times New Roman",
//        "24", true, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 5, 0, 3);
//
//    row = table.getRow(6);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "300",
//        STLineSpacingRule.AUTO);
//    setParagraphAlignInfo(p, ParagraphAlignment.BOTH, TextAlignment.CENTER);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//    setParagraphRunFontInfo(p, pRun, "××××××××××××××××××××××××××××", "宋体",
//        "Times New Roman", "21", true, false, false, false, null, null,
//        0, 6, 0);
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//
//    cell = row.getCell(2);
//    p = getCellFirstParagraph(cell);
//    setParagraphSpacingInfo(p, true, "0", "0", "0", "0", true, "300",
//        STLineSpacingRule.AUTO);
//    setParagraphAlignInfo(p, ParagraphAlignment.BOTH, TextAlignment.CENTER);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//    setParagraphRunFontInfo(p, pRun, "××××××××××××××××××××××××××××", "宋体",
//        "Times New Roman", "21", true, false, false, false, null, null,
//        0, 6, 0);
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//    setCellContentCommonFunction(cell, "××××××××××××××××××××××××××××");
//
//    mergeCellsHorizontal(table, 6, 0, 1);
//    mergeCellsHorizontal(table, 6, 2, 3);
//
//    row = table.getRow(7);
//    setRowHeight(row, "467", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "D.不良事件的等级 *", "宋体",
//        "Times New Roman", "24", true, false, false, false, null, null,
//        0, 6, 0);
//    mergeCellsHorizontal(table, 7, 0, 3);
//
//    row = table.getRow(8);
//    setRowHeight(row, "467", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    setParagraphAlignInfo(p, ParagraphAlignment.CENTER,
//        TextAlignment.CENTER);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "28", true,
//        false, false, 0, 6, 0);
//    setParagraphRunFontInfo(p, pRun, "Ⅰ级事件", "宋体", "Times New Roman", "28",
//        true, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "28", true,
//        false, false, 0, 6, 0);
//    setParagraphRunFontInfo(p, pRun, "Ⅱ级事件     ", "宋体", "Times New Roman",
//        "28", true, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "28", true,
//        false, false, 0, 6, 0);
//    setParagraphRunFontInfo(p, pRun, "Ⅲ级事件 ", "宋体", "Times New Roman",
//        "28", true, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "28", true,
//        false, false, 0, 6, 0);
//    setParagraphRunFontInfo(p, pRun, "Ⅳ级事件 ", "宋体", "Times New Roman",
//        "28", true, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 8, 0, 3);
//
//    row = table.getRow(9);
//    setRowHeight(row, "467", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "E.事件发生的影响 *", "宋体",
//        "Times New Roman", "24", true, false, false, false, null, null,
//        0, 6, 0);
//    mergeCellsHorizontal(table, 9, 0, 3);
//
//    row = table.getRow(10);
//    setRowHeight(row, "722", STHeightRule.AT_LEAST);
//    mergeCellsHorizontal(table, 10, 0, 3);
//
//    row = table.getRow(11);
//    setRowHeight(row, "427", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "F.事件发生后及时处理与分析 *", "宋体",
//        "Times New Roman", "24", true, false, false, false, null, null,
//        0, 6, 0);
//    mergeCellsHorizontal(table, 11, 0, 3);
//
//    row = table.getRow(12);
//    setRowHeight(row, "936", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "立即通知的人员：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 12, 0, 3);
//
//    row = table.getRow(13);
//    setRowHeight(row, "936", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "可能相关的因素：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 13, 0, 3);
//
//    row = table.getRow(14);
//    setRowHeight(row, "936", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "立即采取的措施：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 14, 0, 3);
//
//    row = table.getRow(15);
//    setRowHeight(row, "936", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "事件处理情况：", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 15, 0, 3);
//
//    row = table.getRow(16);
//    setRowHeight(row, "460", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "G.不良事件评价(主管部门填写) *", "宋体",
//        "Times New Roman", "24", true, false, false, false, null, null,
//        0, 6, 0);
//    mergeCellsHorizontal(table, 16, 0, 3);
//
//    row = table.getRow(17);
//    setRowHeight(row, "580", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "主管部门意见陈述:", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 17, 0, 3);
//
//    row = table.getRow(18);
//    setRowHeight(row, "1157", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "H.持续改进措施(主管部门填写) *", "宋体",
//        "Times New Roman", "24", true, false, false, false, null, null,
//        0, 6, 0);
//    mergeCellsHorizontal(table, 18, 0, 3);
//
//    row = table.getRow(19);
//    setRowHeight(row, "457", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    setCellShdStyle(cell, true, "FFFFFF", null);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "I.选择性填写项目(Ⅰ、Ⅱ级事件必填 *，Ⅲ、Ⅳ级事件建议填写)",
//        "宋体", "Times New Roman", "24", true, false, false, false, null,
//        null, 0, 6, 0);
//    mergeCellsHorizontal(table, 19, 0, 3);
//
//    row = table.getRow(20);
//    setRowHeight(row, "567", STHeightRule.AT_LEAST);
//    cell = row.getCell(0);
//    p = getCellFirstParagraph(cell);
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "报 告 人：    行政后勤人员 ", "宋体",
//        "Times New Roman", "21", false, false, false, false, null,
//        null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunFontInfo(p, pRun, "其他  ", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//
//    p = cell.addParagraph();
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "当事人的类别：本院", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunFontInfo(p, pRun, "    其他 ", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//
//    p = cell.addParagraph();
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(p, pRun, "职    称：    高级 ", "宋体",
//        "Times New Roman", "21", false, false, false, false, null,
//        null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunFontInfo(p, pRun, "    中级 ", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunFontInfo(p, pRun, "    初级 ", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunFontInfo(p, pRun, "   其他 ", "宋体", "Times New Roman",
//        "21", false, false, false, false, null, null, 0, 6, 0);
//    pRun = getOrAddParagraphFirstRun(p, true, false);
//    setParagraphRunSymInfo(p, pRun, "宋体", "Times New Roman", "21", true,
//        false, false, 0, 6, 0);
//
//    p = cell.addParagraph();
//    p = cell.addParagraph();
//    pRun = getOrAddParagraphFirstRun(p, false, false);
//    setParagraphRunFontInfo(
//        p,
//        pRun,
//        "报告人签名：         科室：         联系电话：             Email:           ",
//        "宋体", "Times New Roman", "21", false, false, false, false,
//        null, null, 0, 6, 0);
//    mergeCellsHorizontal(table, 20, 0, 3);

    saveDocument(xdoc, "d:/1.docx");
  }

}
