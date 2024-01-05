package com.www.file.download.word;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * word/excel导出工具类<br>
 * <a url="http://doc.wupaas.com/docs/easypoi">官网文档</a>
 * @author 耿让
 * 目的是将初始模板改变成第二章节中的模板
 */
@Slf4j
public class OfficeExportUtils {

    public static void copyWordTable(InputStream inputStream, String targetPath, String varPrefix, int copyNum, boolean isNewPage) {
        File targetFile = new File(targetPath);
        XWPFDocument document = null;
        FileOutputStream out = null;
        try {
            document = new XWPFDocument(inputStream);
            List<XWPFTable> tables = document.getTables();
            if (CollectionUtils.isEmpty(tables)) {
                return;
            }
            List<XWPFTable> srcTables = new ArrayList<>(tables);
            for (int i = 1; i <= copyNum; i++) {
                XWPFParagraph paragraph = document.createParagraph();
                if (isNewPage) {
                    //新增空白页
                    paragraph.createRun().addBreak(BreakType.PAGE);
                }
                String newVarPreFix = "" + varPrefix + i;
                for (int j = 0; j < srcTables.size(); j++) {
                    XWPFTable srcTable = srcTables.get(j);
                    XWPFTable newTable = document.createTable();
                    for (int n = 0; n < srcTable.getRows().size(); n++) {
                        XWPFTableRow srcRow = srcTable.getRows().get(n);
                        XWPFTableRow newRow = newTable.insertNewTableRow(n);
                        copyTableRow(srcRow, newRow, varPrefix, newVarPreFix);
                    }
                    newTable.removeRow(newTable.getRows().size() - 1);
                }
            }
            out = new FileOutputStream(targetFile);
            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (document != null) {
                    document.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制word中的表格
     * @param templatePath 源文件
     * @param targetPath 目标文件
     * @param varPrefix 变量前缀
     * @param copyNum 拷贝数量
     * @param isNewPage 是否新增页
     */
    public static void copyWordTable(String templatePath, String targetPath, String varPrefix, int copyNum, boolean isNewPage) {
        File targetFile = new File(targetPath);
        XWPFDocument document = null;
        FileOutputStream out = null;
        try {
            document = new XWPFDocument(new FileInputStream(templatePath));
            List<XWPFTable> tables = document.getTables();
            if (CollectionUtils.isEmpty(tables)) {
                return;
            }
            List<XWPFTable> srcTables = new ArrayList<>(tables);
            for (int i = 1; i <= copyNum; i++) {
                XWPFParagraph paragraph = document.createParagraph();
                if (isNewPage) {
                    //新增空白页
                    paragraph.createRun().addBreak(BreakType.PAGE);
                }
                String newVarPreFix = "" + varPrefix + i;
                for (int j = 0; j < srcTables.size(); j++) {
                    XWPFTable srcTable = srcTables.get(j);
                    XWPFTable newTable = document.createTable();
                    for (int n = 0; n < srcTable.getRows().size(); n++) {
                        XWPFTableRow srcRow = srcTable.getRows().get(n);
                        XWPFTableRow newRow = newTable.insertNewTableRow(n);
                        copyTableRow(srcRow, newRow, varPrefix, newVarPreFix);
                    }
                    newTable.removeRow(newTable.getRows().size() - 1);
                }
            }
            out = new FileOutputStream(targetFile);
            document.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (document != null) {
                    document.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制word中的表格行
     * @param srcRow 源表格行
     * @param newRow 目标表格行
     * @param varPrefix 源变量前缀
     * @param newVarPreFix 新变量前缀
     */
    private static void copyTableRow(XWPFTableRow srcRow, XWPFTableRow newRow, String varPrefix, String newVarPreFix) {
        int size = srcRow.getTableCells().size();
        for (int i = 0; i < size; i++) {
            newRow.addNewTableCell();
        }
        newRow.getCtRow().setTrPr(srcRow.getCtRow().getTrPr());
        for (int i = 0; i < size; i++) {
            copyTableCell(srcRow.getCell(i), newRow.getCell(i), varPrefix, newVarPreFix);
        }
    }

    /**
     * 复制word中的表格cell
     * @param srcCell 源表格cell
     * @param newCell 目标表格cell
     * @param varPrefix 源变量前缀
     * @param newVarPreFix 新变量前缀
     */
    private static void copyTableCell(XWPFTableCell srcCell, XWPFTableCell newCell, String varPrefix, String newVarPreFix) {
        newCell.getCTTc().setTcPr(srcCell.getCTTc().getTcPr());
        for (int i = 0; i < newCell.getParagraphs().size(); i++) {
            newCell.removeParagraph(i);
        }
        for (XWPFParagraph srcParagraph : srcCell.getParagraphs()) {
            XWPFParagraph newParagraph = newCell.addParagraph();
            copyParagraph(srcParagraph, newParagraph, varPrefix, newVarPreFix);
        }
    }

    /**
     * 复制word中的表格Paragraph
     * @param srcParagraph 源表格Paragraph
     * @param newParagraph 目标表格Paragraph
     * @param varPrefix 源变量前缀
     * @param newVarPreFix 新变量前缀
     */
    private static void copyParagraph(XWPFParagraph srcParagraph, XWPFParagraph newParagraph, String varPrefix, String newVarPreFix) {
        newParagraph.getCTP().setPPr(srcParagraph.getCTP().getPPr());
        for (int i = 0; i < newParagraph.getRuns().size(); ++i) {
            newParagraph.removeRun(i);
        }
        for (XWPFRun srcRun : srcParagraph.getRuns()) {
            XWPFRun newRun = newParagraph.createRun();
            copyRun(srcRun, newRun, varPrefix, newVarPreFix);
        }
    }

    /**
     * 复制word中的表格Run
     * @param srcRun 源表格Run
     * @param newRun 目标表格Run
     * @param varPrefix 源变量前缀
     * @param newVarPreFix 新变量前缀
     */
    private static void copyRun(XWPFRun srcRun, XWPFRun newRun, String varPrefix, String newVarPreFix) {
        newRun.getCTR().setRPr(srcRun.getCTR().getRPr());
        newRun.setText(StringUtils.isNoneBlank(varPrefix) && StringUtils.isNotBlank(srcRun.text()) ? srcRun.text().replace(varPrefix, newVarPreFix) : srcRun.text());
    }

}


