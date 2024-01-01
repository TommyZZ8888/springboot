package com.www.file.download.easyexcel;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @ClassName:ExcelExportService
 * @Author: vren
 * @Date: 2022/9/16 13:50
 */
public class ExcelExportService {

    private String fileName;

    private HttpServletResponse response;

    private ExcelWriter excelWriter;

    private FillConfig fillConfig;

    public ExcelExportService(String fileName, HttpServletResponse response, WriteHandler... writeHandlers) {
        this(fileName, response, null, writeHandlers);
    }

    public ExcelExportService(String fileName, HttpServletResponse response, InputStream templateInputStream, WriteHandler... writeHandlers) {
        this.fileName = fileName;
        this.response = response;
        this.fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        try {
            ExcelWriterBuilder write = EasyExcel.write(response.getOutputStream());
            if (templateInputStream != null) {
                write.withTemplate(templateInputStream);
            }
            for (WriteHandler writeHandler : writeHandlers) {
                write.registerWriteHandler(writeHandler);
            }
            this.excelWriter = write.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ExcelExportService write(Collection<?> data, String sheetName, Class head) {
        WriteSheet writeSheet = getWriteSheet(sheetName, head, null);
        excelWriter.write(data, writeSheet);
        return this;
    }

    private WriteSheet getWriteSheet(String sheetName, Class head, WriteHandler writeHandler) {
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.writerSheet(sheetName).head(head);
        if (writeHandler != null) {
            excelWriterSheetBuilder.registerWriteHandler(writeHandler);
        }
        return excelWriterSheetBuilder.build();
    }

    public void fill(Object data, Integer sheetIndex, String sheetName) {
        excelWriter.fill(data, this.fillConfig, EasyExcel.writerSheet(sheetIndex, sheetName).build());
    }

    public void fill(Object data, Integer sheetIndex) {
        this.fill(data, sheetIndex, null);
    }

    public void fill(Object data, String sheetName) {
        this.fill(data, null, sheetName);
    }

    public ExcelExportService write(Collection<?> data, String sheetName, Class head, WriteHandler writeHandler) {
        WriteSheet writeSheet = getWriteSheet(sheetName, head, writeHandler);
        excelWriter.write(data, writeSheet);
        return this;
    }

    public void export() {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        excelWriter.finish();
    }

}
