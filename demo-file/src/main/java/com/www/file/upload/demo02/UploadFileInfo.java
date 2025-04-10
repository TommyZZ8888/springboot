package com.www.file.upload.demo02;

import lombok.Data;

@Data
public class UploadFileInfo {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 上传文件会有多个分片，记录当前为那个分片
     */
    private Integer currentChunk;

    /**
     * 总分片数
     */
    private Integer chunks;

}
