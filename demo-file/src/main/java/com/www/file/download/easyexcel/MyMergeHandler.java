package com.www.file.download.easyexcel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.util.List;

/**
 * @ClassName:MyMergeHandler
 * @Author: vren
 * @Date: 2022/10/19 17:56
 */
public class MyMergeHandler extends AbstractMergeStrategy {
    /**
     * 合并开始行
     */
    private Integer startRow = 0;

    private List<String> sheetNames;
    /**
     * list表格所有的合并列集合
     */
    private List<CellRangeAddress> cellRangeAddressList = null;

    public MyMergeHandler() {
    }

    public MyMergeHandler(List<String> sheetNames, int startRow, List<CellRangeAddress> cellRangeAddressList) {
        this.startRow = startRow;
        this.cellRangeAddressList = cellRangeAddressList;
        this.sheetNames = sheetNames;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        if (!sheetNames.contains(sheet.getSheetName())) {
            return;
        }
        // 设置样式
        CellStyle cellStyle = cell.getCellStyle();
        //水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //自动换行
        cellStyle.setWrapText(true);
        //在这里判断从哪一行开始调用合并的方法
        if (cell.getRowIndex() > this.startRow) {
            if (relativeRowIndex == null || relativeRowIndex == 0) {
                return;
            }
            mergeColumn(sheet, cell, head, relativeRowIndex);
        }
    }

    /**
     * 合并单元格
     *
     * @param sheet
     * @param cell
     * @param head
     * @param relativeRowIndex
     */
    protected void mergeColumn(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();
        sheet = cell.getSheet();
        // 获取前一行
        Row preRow = sheet.getRow(rowIndex - 1);
        //获取前一列
        Cell preCell = preRow.getCell(colIndex);
        List<CellRangeAddress> list = this.cellRangeAddressList;
        for (int i = 0; i < list.size(); i++) {
            CellRangeAddress cellRangeAddress = list.get(i);
            if (cellRangeAddress.containsColumn(preCell.getColumnIndex())) {
                int lastColIndex = cellRangeAddress.getLastColumn();
                int firstColIndex = cellRangeAddress.getFirstColumn();
                CellRangeAddress cra = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), firstColIndex, lastColIndex);
                sheet.addMergedRegion(cra);
                // 加边框
                RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet);
                return;
            }
        }

    }

}
