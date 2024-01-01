package com.www.file.download.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.ibatis.type.TypeReference;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName:ExcelUtil
 * @Description:
 * @Author: vren
 * @Date: 2022/6/1 17:13
 */
public class ExcelUtil {

    public static <T> LinkedList<T> read(String path, Class<T> clazz) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(path, clazz, excelListener).sheet().headRowNumber(5).doRead();
        return excelListener.getData();
    }
    public static <T> LinkedList<T> read(String path, Integer rowNumber, Class<T> clazz) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(path, clazz, excelListener).sheet().headRowNumber(rowNumber).doRead();
        return excelListener.getData();
    }

    public static <T> LinkedList<T> read(InputStream inputStream, Class<T> clazz) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(inputStream, clazz, excelListener).sheet().doRead();
        return excelListener.getData();
    }

    public static <T> LinkedList<T> read(String path, TypeReference<T> typeReference) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(path, typeReference.getClass(), excelListener).sheet().doRead();
        return excelListener.getData();
    }

    public static <T> LinkedList<T> read(InputStream inputStream, TypeReference<T> typeReference) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(inputStream, typeReference.getClass(), excelListener).sheet().doRead();
        return excelListener.getData();
    }

    public static <T> LinkedList<T> read(String path) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(path, excelListener).sheet().doRead();
        return excelListener.getData();
    }

    public static <T> LinkedList<T> read(InputStream inputStream) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(inputStream, excelListener).sheet().doRead();
        return excelListener.getData();
    }

    public static void export(HttpServletResponse response, String fileName, List list, Class tClass) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), tClass).sheet().registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static <T> LinkedList<T> readSheet(InputStream inputStream, Class<T> clazz,String sheetName) {
        ExcelListener<T> excelListener = new ExcelListener<>();
        EasyExcel.read(inputStream, clazz, excelListener).sheet(sheetName).doRead();
        return excelListener.getData();
    }
}
