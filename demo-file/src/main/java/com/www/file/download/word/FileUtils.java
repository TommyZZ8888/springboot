package com.www.file.download.word;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 耿让
 */
public class FileUtils {

    /**
     * EasyPoi 替换数据 导出 word
     * @param templatePath word模板地址
     * @param tempDir      临时文件存放地址
     * @param filename     文件名称
     * @param data         替换参数
     */
    public static void easyPoiExport(String templatePath, String tempDir, String filename, Map<String, Object> data, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(templatePath, "模板路径不能为空");
        Assert.notNull(tempDir, "临时文件路径不能为空");
        Assert.notNull(filename, "文件名称不能为空");
        Assert.isTrue(filename.endsWith(".docx"), "文件名称仅支持docx格式");

        if (!tempDir.endsWith("/")) {
            tempDir = tempDir + File.separator;
        }

        File file = new File(tempDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
//            String userAgent = request.getHeader("user-agent").toLowerCase();
//            if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
//                filename = URLEncoder.encode(filename, "UTF-8");
//            } else {
//                filename = new String(filename.getBytes("utf-8"), "ISO-8859-1");
//            }

            XWPFDocument document = WordExportUtil.exportWord07(templatePath, data);
            String tempPath = tempDir + filename;
            FileOutputStream out = new FileOutputStream(tempPath);
            document.write(out);

            // 设置响应规则
            // 设置强制下载不打开
            //设置响应体内容类型
            response.setContentType("application/octet-stream");
            //添加响应头
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            //暴露新添加的响应头
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream stream = response.getOutputStream();
            document.write(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTempFile(tempDir, filename);
        }
    }


    public static void easyPoiExport(String templatePath, String tempDir, String filename, Map<String, Object> data, HttpServletResponse response) {
        Assert.notNull(templatePath, "模板路径不能为空");
        Assert.notNull(tempDir, "临时文件路径不能为空");
        Assert.notNull(filename, "文件名称不能为空");
        Assert.isTrue(filename.endsWith(".docx"), "文件名称仅支持docx格式");

        if (!tempDir.endsWith("/")) {
            tempDir = tempDir + File.separator;
        }

        File file = new File(tempDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            XWPFDocument document = WordExportUtil.exportWord07(templatePath, data);
            String tempPath = tempDir + filename;
            FileOutputStream out = new FileOutputStream(tempPath);
            document.write(out);

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            OutputStream stream = response.getOutputStream();
            document.write(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTempFile(tempDir, filename);
        }
    }


    public static void easyPoiExport(String templatePath, String tempDir, String filename, Map<String, Object> data, FileOutputStream outputStream) {
        Assert.notNull(templatePath, "模板路径不能为空");
        Assert.notNull(tempDir, "临时文件路径不能为空");
        Assert.notNull(filename, "文件名称不能为空");
        Assert.isTrue(filename.endsWith(".docx"), "文件名称仅支持docx格式");

        if (!tempDir.endsWith("/")) {
            tempDir = tempDir + File.separator;
        }

        File file = new File(tempDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
//            String userAgent = request.getHeader("user-agent").toLowerCase();
//            if (userAgent.contains("msie") || userAgent.contains("like gecko")) {
//                filename = URLEncoder.encode(filename, "UTF-8");
//            } else {
//                filename = new String(filename.getBytes("utf-8"), "ISO-8859-1");
//            }

            XWPFDocument document = WordExportUtil.exportWord07(templatePath, data);
            String tempPath = tempDir + filename;
            FileOutputStream out = new FileOutputStream(tempPath);
            document.write(out);
            out.close();
            // 设置响应规则
//            response.setContentType("application/force-download");
//            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
//            OutputStream stream = response.getOutputStream();
            document.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTempFile(tempDir, filename);
        }
    }

    /**
     * 删除临时生成的文件
     */
    public static void deleteTempFile(String filePath, String fileName) {
        File file = new File(filePath + fileName);
        File f = new File(filePath);
        file.delete();
        f.delete();
    }

    private void writeZip(List<String> files, String zipName, HttpServletResponse response) throws IOException {
        String fileName = zipName + ".zip";
        OutputStream os = response.getOutputStream();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/x-zip-compressed");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ZipOutputStream zos = new ZipOutputStream(os);
        byte[] buf = new byte[8192];
        int len;
        for (int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            if (!file.isFile()) {
                continue;
            }
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((len = bis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
        }
        zos.closeEntry();
        zos.close();
    }

    public static void main(String[] args) {
//        String blankingInspectionExport = blankingInspectionExport(dto.getId(), fileName);
//        String exportWeldInspection = exportWeldInspection(dto.getId(), fileName);
//        String picklingPassivationInspection = exportPicklingPassivationInspection(dto.getId(), fileName);
//        String assembleInspection = exportAssembleInspection(dto.getId(), fileName);
//        String exportWeldFilletInspection = exportWeldFilletInspection(dto.getId(), fileName);
//        String exportGasLiquidPressureInspection = exportGasLiquidPressureInspection(dto.getId(), fileName);
//        String exportNondestructiveEntrustment = exportNondestructiveEntrustment(dto.getId(), fileName);
//        String exportLeakageInspection = exportLeakageInspection(dto.getId(), fileName);
//        String exportPaintInspection = exportPaintInspection(dto.getId(), fileName);
//        String exportHardnessRecord = hardnessService.hardnessRecord(dto.getId(), fileName);
//        String exportAppearanceSizeRecord = appearanceSizeService.exportAppearanceSizeInspection(dto.getId(), fileName);
//        String exportShellRingRecord = shellRingService.exportShellRingRecordInspection(dto.getId(), fileName);
//        String exportShellRingTest = shellRingService.exportShellRingTestInspection(dto.getId(), fileName);
//
//        List<String> files = Arrays.asList(blankingInspectionExport, exportWeldInspection, picklingPassivationInspection, assembleInspection, exportWeldFilletInspection, exportGasLiquidPressureInspection
//                , exportNondestructiveEntrustment, exportLeakageInspection, exportPaintInspection, exportHardnessRecord, exportAppearanceSizeRecord, exportShellRingRecord, exportShellRingTest);
//        files = files.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
//        writeZip(files, fileName.replace("temp/", ""), response);
//        //删除文件夹下面文件，再删除文件夹
//        org.apache.commons.io.FileUtils.deleteDirectory(file);
    }
}

