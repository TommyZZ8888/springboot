package com.www.zz.demortsp;

import com.www.zz.demortsp.entity.Image;
import com.www.zz.demortsp.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * * @projectName videoservice
 * * @title   MediaUtils 
 * * @package  com.de.rtsp 
 * * @description   获取rtsp流，解析为视频帧，websocket传递到前台显示
 * * @author IT_CREAT  
 * * @date 2020 2020/4/12 0012 下午 18:24 
 * * @version V1.0.0
 * 
 */
@Slf4j
@Component
@EnableAsync
public class MediaTransfer {
    @Value("${rtsp.enabled}")
    private boolean enabled;

    @Value("${rtsp.url}")
    private String rtspUrl;

    @Value("${rtsp.transport.type}")
    private String rtspTransportType;

    private static FFmpegFrameGrabber grabber;

    private static boolean isStart = false;

    /**
     * 视频帧率
     */
    public static int frameRate = 30;
    /**
     * 视频宽度
     */
    public static int frameWidth = 1080;
    /**
     * 视频高度
     */
    public static int frameHeight = 720;

    /**
     * 开启获取rtsp流，通过websocket传输数据
     */
    @Async
    public void live() {
        if(enabled) {
            log.info("开启自动链接rtsp地址");
            log.info("连接rtsp："+rtspUrl+",开始创建grabber");
            grabber = createGrabber(rtspUrl);
            if (grabber != null) {
                log.info("创建grabber成功");
            } else {
                log.info("创建grabber失败");
            }
            startCameraPush();
        } else {
            log.warn("未开启自动链接rtsp地址");
        }
    }

    /**
     * 构造视频抓取器
     *
     * @param rtsp 拉流地址
     * @return FFmpegFrameGrabber
     */
    public FFmpegFrameGrabber createGrabber(String rtsp) {
        // 获取视频源
        try {
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtsp);
            grabber.setOption("rtsp_transport", rtspTransportType);
            //设置帧率
            grabber.setFrameRate(frameRate);
            //设置获取的视频宽度
            grabber.setImageWidth(frameWidth);
            //设置获取的视频高度
            grabber.setImageHeight(frameHeight);
            //设置视频bit率
            grabber.setVideoBitrate(2000000);
            return grabber;
        } catch (FrameGrabber.Exception e) {
            log.error("创建解析rtsp FFmpegFrameGrabber 失败");
            log.error("create rtsp FFmpegFrameGrabber exception: ", e);
            return null;
        }
    }

    /**
     * 推送图片（摄像机直播）
     */
    public void startCameraPush() {
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        while (true) {
            if (grabber == null) {
                log.info("重试连接rtsp："+rtspUrl+",开始创建grabber");
                grabber = createGrabber(rtspUrl);
                log.info("创建grabber成功");
            }
            try {
                if (grabber != null && !isStart) {
                    grabber.start();
                    isStart = true;
                    log.info("启动grabber成功");
                }
                if (grabber != null) {
                    Frame frame = grabber.grabImage();
                    if (null == frame) {
                        continue;
                    }
                    BufferedImage bufferedImage = java2DFrameConverter.getBufferedImage(frame);
                    byte[] bytes = imageToBytes(bufferedImage, "jpg");
                    //使用websocket发送视频帧数据
                    WebSocketServer.sendAllByObject(new Image(bytes));
                }
            } catch (FrameGrabber.Exception | RuntimeException e) {
                log.error("因为异常，grabber关闭，rtsp连接断开，尝试重新连接");
                log.error("exception : " , e);
                if (grabber != null) {
                    try {
                        grabber.stop();
                    } catch (FrameGrabber.Exception ex) {
                        log.error("grabber stop exception: ", ex);
                    } finally {
                        grabber = null;
                        isStart = false;
                    }
                }
            }
        }
    }


    /**
     * 图片转字节数组
     *
     * @param bImage 图片数据
     * @param format 格式
     * @return 图片字节码
     */
    private byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            log.error("bufferImage 转 byte 数组异常");
            log.error("bufferImage transfer byte[] exception: ", e);
            return null;
        }
        return out.toByteArray();
    }

}
