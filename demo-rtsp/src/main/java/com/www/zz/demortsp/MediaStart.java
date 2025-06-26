package com.www.zz.demortsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 
 * * @projectName videoservice
 * * @title   MadiaStart 
 * * @package  com.de.rtsp 
 * * @description  程序启动时加载一次，rtspj解析传输到websocket启动类
 * * @author IT_CREAT  
 * * @date 2020 2020/4/18 0018 下午 22:48 
 * * @version V1.0.0
 * 
 */
@Component
public class MediaStart {

    @Autowired
    MediaTransfer mediaTransfer;

    @PostConstruct
    public void init() {
        //异步加载，因为初始化时执行，live里面是死循环监听rtsp,如果不异步操作，就会卡死在初始化阶段，项目就会起不来
        mediaTransfer.live();
    }

}
