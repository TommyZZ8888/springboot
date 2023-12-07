package com.www.upload.service;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;

/**
 * @Description IQiNiuService
 * @Author 张卫刚
 * @Date Created on 2023/12/7
 */
public interface IQiNiuService {

    /**
     * 七牛云上传文件
     *
     * @param file 文件
     * @return 七牛上传Response
     * @throws QiniuException 七牛异常
     */
    Response uploadFile(File file) throws QiniuException;

}
