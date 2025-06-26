package com.www.zz.demortsp.entity;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * 
 * * @projectName videoservice
 * * @title   ImageEncoder 
 * * @package  com.de.entity 
 * * @description  websocket 传输对象转码
 * * @author IT_CREAT  
 * * @date 2020 2020/4/18 0018 下午 22:53 
 * * @version V1.0.0
 * 
 */
public class ImageEncoder implements Encoder.Text<Image> {

    @Override
    public String encode(Image image) throws EncodeException {
        if(image != null && !ArrayUtils.isEmpty(image.getImageByte())){
            String base64Image= Base64.encode(image.getImageByte());
            return JSON.toJSONString(new AjaxResult(AjaxResult.Type.SUCCESS_IMG_BYTE,"获取视频帧成功",base64Image));
        }
        return JSON.toJSONString(AjaxResult.error("获取视频帧失败"));
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
