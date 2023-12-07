package com.www.upload.service.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.www.upload.config.UploadConfig;
import com.www.upload.service.IQiNiuService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Description QiNiuServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/12/7
 */
@Service
public class QiNiuServiceImpl implements IQiNiuService, InitializingBean {

    private final UploadManager uploadManager;

    private final Auth auth;

    @Value("${qiniu.bucket}")
    private String bucket;

    private StringMap putPolicy;

    @Autowired
    public QiNiuServiceImpl(UploadManager uploadManager, Auth auth) {
        this.uploadManager = uploadManager;
        this.auth = auth;
    }

    @Override
    public Response uploadFile(File file) throws QiniuException {
        Response response = this.uploadManager.put(file, file.getName(), getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3) {
            response = this.uploadManager.put(file, file.getName(), getUploadToken());
            retry++;
        }
        return response;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width), \"height\":${imageInfo.height}}");
    }

    public String getUploadToken() {
        return this.auth.uploadToken(bucket, null, 3600, putPolicy);
    }
}
