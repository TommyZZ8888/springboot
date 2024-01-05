package com.www.file.download.easyexcel.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Description EasyExcelDownloadController
 * @Author 张卫刚
 * @Date Created on 2024/1/5
 */
@RestController
@Slf4j
public class EasyExcelDownloadController {

    @Resource
    private HttpServletResponse response;
    @Autowired
    private TestExcelService testExcelService;
    /**
     * 3、EasyExcel excel导出（分页查询数据导出，百万级数据测试）
     * 采用分页查询的方式导出excel，内存占用很少，数据量大的时候推荐使用此方式
     */
    @GetMapping("/exportSqlExcel")
    public void exportSqlExcel() {
        log.info("sql分页导出excel。。。");
        //分页查询，每次查询量，这个插件最大只允许一次查500或者直接查全部，要想查更多的就只能去改源码了
        int pageNum = 500;
        ExcelWriter excelWriter = null;
        try {
            //下方使用了用户自己选择文件保存路径的方式，所以需要配请求参数，如果使用固定路径可忽略此代码
            String filename = URLEncoder.encode(System.currentTimeMillis()+".xlsx","UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);//设定输出文件头
            response.setContentType("application/x-xls");// 定义输出类型

            //不需要导出的字段userId，如果没有不需要导出的字段，可以忽略这个方法
            //只需要导入的字段，用includeColumnFiledNames()方法，写法是一样的
            //Set<String> excludeColumnFiledNames = new HashSet<String>();
            //excludeColumnFiledNames.add("userId");
            //这里采用用户自己选择文件保存路径的方式
            OutputStream out = response.getOutputStream();
            //这里其实就是把上面的方法分开写，写入同一个sheet
            excelWriter = EasyExcel.write(out, TestExcel.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("测试模板呀").build();
            //重点是这里的循环调用---分页查询，先查询出需要导出的总数
            long count = testExcelService.count();
            int num = (int)(count / pageNum) + 1;
            for (int i = 1; i < num; i++) {
                //分页查询
                List<TestExcel> testExcelList = testExcelService.query();
                if(!testExcelList.isEmpty()){
                    //导出
                    excelWriter.write(testExcelList, writeSheet);
                }
            }
        }catch (Exception e){
            log.info("兄嘚，你代码又出错啦");
            e.printStackTrace();
        }finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

    }
}
