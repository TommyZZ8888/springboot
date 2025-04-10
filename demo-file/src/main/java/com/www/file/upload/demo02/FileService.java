package com.www.file.upload.demo02;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class FileService {

    /**
     * 编码
     */
    private static final String UTF_8 = "UTF-8";

    /**
     * 文件上传路径(当前项目路径下，也可配置固定路径)
     */
    private String uploadPath = System.getProperty("user.dir") + "/springboot-file/upload/";

    /**
     * 下载指定文件
     */
    private String downloadFile = "E:\\Git\\Git-2.22.0-64-bit.exe";

    /**
     * 文件下载地址(当前项目路径下，也可配置固定路径)
     */
    private String downloadPath = System.getProperty("user.dir") + "/springboot-file/download/";

    /**
     * 分片下载每一片大小为50M
     */
    private static final Long PER_SLICE = 1024 * 1024 * 50L;

    /**
     * 定义分片下载线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * final string
     */
    private static final String RANGE = "Range";

    /**
     * 上传文件
     */
//    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // 获取ServletFileUpload
//        ServletFileUpload servletFileUpload = getServletFileUpload();
//        List<FileItem> items = servletFileUpload.parseRequest((RequestContext) request);
//        // 获取文件信息
//        UploadFileInfo uploadFileInfo = getFileInfo(items);
//        // 写入临时文件
//        writeTempFile(items, uploadFileInfo);
//        // 判断是否合并
//        mergeFile(uploadFileInfo);
//        // 返回结果
//        response.setCharacterEncoding(UTF_8);
//        response.getWriter().write("上传成功");
//    }

    public void upload2(MultipartFile file,
                        String fileId,
                        String fileName,
                        long fileSize,
                        int totalChunks,
                        int chunkIndex,
                        HttpServletResponse response) throws IOException {

        // 创建目录
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        // 创建临时分片文件名
        String tempFileName = chunkIndex + "_" + fileName;
        File tempFile = new File(uploadPath, tempFileName);

        // 如果文件不存在，写入
        if (!tempFile.exists()) {
            file.transferTo(tempFile);
        }

        // 如果已经是最后一个分片，执行合并
        if (chunkIndex == totalChunks - 1) {
            File mergedFile = new File(uploadPath, fileName);
            try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(mergedFile))) {
                for (int i = 0; i < totalChunks; i++) {
                    File part = new File(uploadPath, i + "_" + fileName);
                    while (!part.exists()) {
                        Thread.sleep(100); // 等待
                    }
                    byte[] bytes = FileUtils.readFileToByteArray(part);
                    os.write(bytes);
                    part.delete(); // 删除临时文件
                }
                os.flush();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("上传成功");
    }

    /**
     * 获取ServletFileUpload
     */
    public ServletFileUpload getServletFileUpload() {
        // 设置缓冲区大小，先读到内存里在从内存写
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024);
        File file = new File(uploadPath);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        factory.setRepository(file);
        // 解析
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置单个大小与最大大小
        upload.setFileSizeMax(1 * 1024 * 1024 * 1024L);
        upload.setSizeMax(10 * 1024 * 1024 * 1024L);
        return upload;
    }

    /**
     * 获取文件信息
     *
     * @param items
     * @return
     * @throws UnsupportedEncodingException
     */
    public UploadFileInfo getFileInfo(List<FileItem> items) throws UnsupportedEncodingException {
        UploadFileInfo uploadFileInfo = new UploadFileInfo();
        for (FileItem item : items) {
            if (item.isFormField()) {
                // 获取分片数据
                if ("chunk".equals(item.getFieldName())) {
                    uploadFileInfo.setCurrentChunk(Integer.parseInt(item.getString(UTF_8)));
                }
                if ("chunks".equals(item.getFieldName())) {
                    uploadFileInfo.setChunks(Integer.parseInt(item.getString(UTF_8)));
                }
                if ("name".equals(item.getFieldName())) {
                    uploadFileInfo.setFileName(item.getString(UTF_8));
                }
            }
        }
        return uploadFileInfo;
    }

    /**
     * 写入临时文件
     *
     * @param items
     * @param uploadFileInfo
     * @throws Exception
     */
    public void writeTempFile(List<FileItem> items, UploadFileInfo uploadFileInfo) throws Exception {
        // 获取文件基本信息后
        for (FileItem item : items) {
            if (!item.isFormField()) {
                // 有分片需要临时目录
                String tempFileName = uploadFileInfo.getFileName();
                if (StringUtils.isNotBlank(tempFileName)) {
                    if (uploadFileInfo.getCurrentChunk() != null) {
                        tempFileName = uploadFileInfo.getCurrentChunk() + "_" + uploadFileInfo.getFileName();
                    }
                    // 判断文件是否存在
                    File tempFile = new File(uploadPath, tempFileName);
                    // 断点续传，判断文件是否存在，若存在则不传
                    if (!tempFile.exists()) {
                        item.write(tempFile);
                    }
                }
            }
        }
    }

    /**
     * 判断是否合并
     *
     * @param uploadFileInfo
     * @throws IOException
     * @throws InterruptedException
     */
    public void mergeFile(UploadFileInfo uploadFileInfo) throws IOException, InterruptedException {
        Integer currentChunk = uploadFileInfo.getCurrentChunk();
        Integer chunks = uploadFileInfo.getChunks();
        String fileName = uploadFileInfo.getFileName();
        // 如果当前分片等于总分片那么合并文件
        if (currentChunk != null && chunks != null && currentChunk.equals(chunks - 1)) {
            File tempFile = new File(uploadPath, fileName);
            try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                // 根据之前命名规则找到所有分片
                for (int i = 0; i < chunks; i++) {
                    File file = new File(uploadPath, i + "_" + fileName);
                    // 并发情况，需要判断所有，因为可能最后一个分片传完，之前有的还没传完
                    while (!file.exists()) {
                        // 不存在休眠100毫秒后在重新判断
                        Thread.sleep(100);
                    }
                    // 分片存在，读入数组中
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    os.write(bytes);
                    os.flush();
                    file.delete();
                }
                os.flush();
            }
        }
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取文件
        File file = new File(downloadFile);
        // 获取下载文件信息
        DownloadFileInfo downloadFileInfo = getDownloadFileInfo(file.length(), request, response);
        // 设置响应头
        setResponse(response, file.getName(), downloadFileInfo);
        // 下载文件
        try (InputStream is = new BufferedInputStream(new FileInputStream(file));
             OutputStream os = new BufferedOutputStream(response.getOutputStream())) {
            // 跳过已经读取文件
            is.skip(downloadFileInfo.getPos());
            byte[] buffer = new byte[1024];
            long sum = 0;
            // 读取
            while (sum < downloadFileInfo.getRangeLength()) {
                int length = is.read(buffer, 0, (downloadFileInfo.getRangeLength() - sum) <= buffer.length ? (int) (downloadFileInfo.getRangeLength() - sum) : buffer.length);
                sum = sum + length;
                os.write(buffer, 0, length);
            }
        }

    }

    /**
     * 有两个map，我要去判断里面相同键的值一致不一致，除了双重for循环，有没有别的好办法
     */
    private DownloadFileInfo getDownloadFileInfo(long fSize, HttpServletRequest request, HttpServletResponse response) {
        long pos = 0;
        long last = fSize - 1;
        // 判断前端是否需要分片下载
        if (request.getHeader(RANGE) != null) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            String numRange = request.getHeader(RANGE).replace("bytes=", "");
            String[] strRange = numRange.split("-");
            if (strRange.length == 2) {
                pos = Long.parseLong(strRange[0].trim());
                last = Long.parseLong(strRange[1].trim());
                // 若结束字节超出文件大小，取文件大小
                if (last > fSize - 1) {
                    last = fSize - 1;
                }
            } else {
                // 若只给一个长度，开始位置一直到结束
                pos = Long.parseLong(numRange.replace("-", "").trim());
            }
        }
        long rangeLength = last - pos + 1;
        String contentRange = "bytes " + pos + "-" + last + "/" + fSize;
        return new DownloadFileInfo(fSize, pos, last, rangeLength, contentRange);
    }

    /**
     * 分片下载
     *
     * @throws IOException
     */
    public void downloads() throws IOException {
        File file = new File(downloadPath);
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        // 探测下载，获取文件相关信息
        FileInfo fileInfoDto = sliceDownload(1, 10, -1, null);
        // 如果不为空，执行分片下载
        if (fileInfoDto != null) {
            // 计算有多少分片
            long pages = fileInfoDto.getFileSize() / PER_SLICE;
            // 适配最后一个分片
            for (long i = 0; i <= pages; i++) {
                long start = i * PER_SLICE;
                long end = (i + 1) * PER_SLICE - 1;
                executorService.execute(new SliceDownloadRunnable(start, end, i, fileInfoDto.getFileName()));
            }
        }
    }

    /**
     * 分片下载
     *
     * @param start 分片起始位置
     * @param end   分片结束位置
     * @param page  第几个分片, page=-1时是探测下载
     */
    private FileInfo sliceDownload(long start, long end, long page, String fName) throws IOException {
        // 断点下载
        File file = new File(downloadPath, page + "-" + fName);
        // 如果当前文件已经存在，并且不是探测任务，并且文件的长度等于分片的大小，那么不用下载当前文件
        if (file.exists() && page != -1 && file.length() == PER_SLICE) {
            return null;
        }
        // 创建HttpClient
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8018/file/download");
        httpGet.setHeader(RANGE, "bytes=" + start + "-" + end);
        HttpResponse httpResponse = client.execute(httpGet);
        String fSize = httpResponse.getFirstHeader("fSize").getValue();
        fName = URLDecoder.decode(httpResponse.getFirstHeader("fName").getValue(), UTF_8);
        HttpEntity entity = httpResponse.getEntity();
        // 下载
        try (InputStream is = entity.getContent();
             FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int ch;
            while ((ch = is.read(buffer)) != -1) {
                fos.write(buffer, 0, ch);
            }
            fos.flush();
        }
        // 判断是否是最后一个分片，如果是那么合并
        if (end - Long.parseLong(fSize) > 0) {
            mergeFile(fName, page);
        }
        return new FileInfo(Long.parseLong(fSize), fName);
    }

    private void mergeFile(String fName, long page) throws IOException {
        File file = new File(downloadPath, fName);

        try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i <= page; i++) {
                File tempFile = new File(downloadPath, i + "-" + fName);
                // 文件不存在或文件没写完
                while (!tempFile.exists() || (i != page && tempFile.length() < PER_SLICE)) {
                    Thread.sleep(100);
                }
                byte[] bytes = FileUtils.readFileToByteArray(tempFile);
                os.write(bytes);
                os.flush();
                tempFile.delete();
            }
            // 删除文件
            File f = new File(downloadPath, "-1" + "-null");
            if (f.exists()) {
                f.delete();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class SliceDownloadRunnable implements Runnable {
        private final long start;
        private final long end;
        private final long page;
        private final String fName;

        private SliceDownloadRunnable(long start, long end, long page, String fName) {
            this.start = start;
            this.end = end;
            this.page = page;
            this.fName = fName;
        }

        @Override
        public void run() {
            try {
                sliceDownload(start, end, page, fName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置响应头
     */
    private void setResponse(HttpServletResponse response, String fileName, DownloadFileInfo downloadFileInfo) throws UnsupportedEncodingException {
        response.setCharacterEncoding(UTF_8);
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, UTF_8));
        // 支持分片下载
        response.setHeader("Accept-Range", "bytes");
        response.setHeader("fSize", String.valueOf(downloadFileInfo.getFSize()));
        response.setHeader("fName", URLEncoder.encode(fileName, UTF_8));
        // range响应头
        response.setHeader("Content-Range", downloadFileInfo.getContentRange());
        response.setHeader("Content-Length", String.valueOf(downloadFileInfo.getRangeLength()));
    }

}
