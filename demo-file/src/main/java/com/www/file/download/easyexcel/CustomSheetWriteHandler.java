//package com.www.file.download.easyexcel;
//
//import com.alibaba.excel.write.handler.SheetWriteHandler;
//import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
//import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
//
//import java.util.List;
//
///**
// * @Description 隐藏sheet页
// * @Author 张卫刚
// * @Date Created on 2023/9/15
// */
//public class CustomSheetWriteHandler implements SheetWriteHandler {
//
//    private final List<String> deleteSheetNameList;
//
//    public CustomSheetWriteHandler(List<String> deleteSheetNameList) {
//        this.deleteSheetNameList = deleteSheetNameList;
//    }
//
//    @Override
//    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
//        for (String deleteSheetName : deleteSheetNameList) {
//            int sheetIndex = writeWorkbookHolder.getWorkbook().getSheetIndex(deleteSheetName);
//            if (-1 != sheetIndex) {
//                writeWorkbookHolder.getWorkbook().setSheetHidden(sheetIndex, true);
//            }
//        }
//    }
//}
//
