package com.www.zz.demortsp.entity;

import org.springframework.stereotype.Component;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

/**
 *
 * * @projectName videoservice
 * * @title   ImgMarker
 * * @package  com.de.entity
 * * @description  视频加水印，暂时没用
 * * @author IT_CREAT
 * * @date 2020 2020/4/12 0012 下午 18:26
 * * @version V1.0.0
 *
 */
//@Component
//public class ImgMarker {
//    /**
//     * 视频水印图片
//     */
//    private BufferedImage logoImg;
//
//    private Font font;
//    private Font font2;
//
//    public ImgMarker() {
//        init();
//    }
//
//    private void init() {
//        // 加载水印图片
//        try {
//            logoImg = ImageIO.read(new File("path/to/your/watermark.png")); // 请替换为实际路径
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        font = new Font("黑体", Font.BOLD, 16);
//        font2 = new Font("黑体", Font.BOLD, 24);
//    }
//
//    /**
//     * 加水印
//     * @param bufImg 视频帧
//     */
//    public void mark(BufferedImage bufImg) {
//        if (bufImg == null || logoImg == null) {
//            return;
//        }
//        int width = bufImg.getWidth();
//        int height = bufImg.getHeight();
//        Graphics2D graphics = bufImg.createGraphics();
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
//        // 设置图片背景
//        graphics.drawImage(bufImg, 0, 0, width, height, null);
//        // 添加右上角水印
//        graphics.drawImage(logoImg, width - 130, 8, 121, 64, null);
//        graphics.dispose(); // 记得释放资源
//    }
//
//    /**
//     *
//     * @param bufImg 视频帧
//     */
//    public void markTag(BufferedImage bufImg, String msg, int videoWidth) {
//        int width = bufImg.getWidth();
//        int height = bufImg.getHeight();
//        Graphics2D graphics = bufImg.createGraphics();
//        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
//        // 设置图片背景
//        graphics.drawImage(bufImg, 0, 0, width, height, null);
//
//        FontMetrics metrics = graphics.getFontMetrics(font);
//        FontMetrics metrics2 = graphics.getFontMetrics(font2);
//
//        // 设置由上方标签号
//        graphics.setColor(Color.orange);
//        if (videoWidth <= 400) {
//            graphics.setFont(font2);
//            graphics.drawString(msg, width - metrics2.stringWidth(msg) - 24, metrics2.getAscent());
//        } else {
//            graphics.setFont(font);
//            graphics.drawString(msg, width - metrics.stringWidth(msg) - 12, metrics.getAscent());
//        }
//        graphics.dispose(); // 记得释放资源
//    }
//}
