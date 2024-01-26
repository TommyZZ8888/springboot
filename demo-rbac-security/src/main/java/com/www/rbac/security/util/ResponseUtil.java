package com.www.rbac.security.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.www.rbac.security.common.ApiResponse;
import com.www.rbac.security.common.BaseException;
import com.www.rbac.security.common.IStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Description ResponseUtil
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@Slf4j
public class ResponseUtil {

    /**
     * 往 response 写出 json
     *
     * @param response 响应
     * @param status   状态
     * @param data     返回数据
     */
public static void readerJson(HttpServletResponse response, IStatus status,Object data){
    try{
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setContentType("application/json;character=UTF-8");
    response.setStatus(200);

    response.getWriter().write(JSONUtil.toJsonStr(new JSONObject(ApiResponse.ofStatus(status, data), false)));
    }catch(Exception e){
        log.error("Response写出JSON异常，", e);
    }
}


    /**
     * 往 response 写出 json
     *
     * @param response  响应
     * @param exception 异常
     */
    public static void renderJson(HttpServletResponse response, BaseException exception) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);

            // FIXME: hutool 的 BUG：JSONUtil.toJsonStr()
            //  将JSON转为String的时候，忽略null值的时候转成的String存在错误
            response.getWriter().write(JSONUtil.toJsonStr(new JSONObject(ApiResponse.ofException(exception), false)));
        } catch (IOException e) {
            log.error("Response写出JSON异常，", e);
        }
    }


}
