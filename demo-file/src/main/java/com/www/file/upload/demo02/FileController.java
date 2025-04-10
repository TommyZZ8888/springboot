package com.www.file.upload.demo02;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传测试接口
 *
 * @author liyh
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 单个文件上传，支持断点续传
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public void upload(@RequestParam("file") MultipartFile file,
                       @RequestParam("fileId") String fileId,
                       @RequestParam("fileName") String fileName,
                       @RequestParam("fileSize") long fileSize,
                       @RequestParam("totalChunks") int totalChunks,
                       @RequestParam("chunkIndex") int chunkIndex,
                       HttpServletResponse response) throws IOException {

        fileService.upload2(file, fileId, fileName, fileSize, totalChunks, chunkIndex, response);
    }


    /**
     * 普通文件下载
     */
    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        fileService.download(request, response);
    }

    /**
     * 分片文件下载
     */
    @GetMapping("/downloads")
    public String downloads() throws IOException {
        fileService.downloads();
        return "下载成功";
    }

}
