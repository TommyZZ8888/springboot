package com.www.file.upload.demo01;

import java.io.BufferedOutputStream;
import java.io.File;
import  java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.RandomAccessFile;

/**
 * @Describtion: FileUploader
 * @Author: 张卫刚
 * @Date: 2023/12/30 13:02
 */
public class FileUploader {

    public static void main(String[] args) {
        // 设置文件路径
        String filePath = "path/to/your/file.ext";

        // 设置切片大小
        int chunkSize = 1024 * 1024; // 1MB

        // 获取文件大小
        File file = new File(filePath);
        long fileSize = file.length();

        // 计算切片数量
        int numChunks = (int) Math.ceil((double) fileSize / chunkSize);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            byte[] buffer = new byte[chunkSize];

            for (int i = 0; i < numChunks; i++) {
                // 定位到当前切片的起始位置
                long offset = i * chunkSize;
                raf.seek(offset);

                // 读取当前切片的数据
                int bytesRead = raf.read(buffer);

                // 处理上传逻辑
                uploadChunk(buffer, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void uploadChunk(byte[] chunkData, int bytesRead) {
        try {
            // 构建上传URL
            URL url = new URL("https://your-upload-api.com/upload");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法和属性
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            // 获取输出流并写入切片数据
            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            outputStream.write(chunkData, 0, bytesRead);
            outputStream.flush();

            // 获取响应码
            int responseCode = connection.getResponseCode();

            // 处理响应逻辑（此处只是示例）
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("切片上传成功");
            } else {
                System.out.println("切片上传失败：" + responseCode);
            }

            // 关闭连接和输出流
            outputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



