//package com.www.file.download.easyexcel.test;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.metadata.CellExtra;
//import com.alibaba.excel.metadata.data.ReadCellData;
//import com.alibaba.excel.read.listener.ReadListener;
//import com.alibaba.fastjson.JSON;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Description Listener
// * @Author 张卫刚
// * @Date Created on 2024/1/5
// */
//
///**
// * excel导入的工具类
// * 是不是很奇怪这里是实现的ReadListener接口而不是继承的AnalysisEventListener类
// * 因为源码里AnalysisEventListener类也是继承的ReadListener接口，何必多此一举呢
// */
//public class ExcelUpload implements ReadListener<TestExcel> {
//
//    private TestExcelService testExcelService;
//    /**
//     * 注意这里是不能交给spring管理的，就是不能使用@Resource,或者@Autowired注入
//     * 可以使用构造方法的方式获取你想要的对象
//     * @param testExcelService testExcelService
//     */
//    public ExcelUpload(TestExcelService testExcelService){
//        this.testExcelService = testExcelService;
//    }
//
//    /**
//     * 无参构造方法，不能省略
//     */
//    public ExcelUpload(){}
//
//    public static final Logger log = LoggerFactory.getLogger(ExcelUpload.class);
//
//    private static final int count = 10000;//设置读取的条数，每次达到指定条数时就保存入数据库
//    private List<TestExcel> testExcelListTure = new ArrayList<>();//校验正确的数据集合，数量达到设定值后插入数据库，然后再清空一直循环
//    private List<TestExcel> testExcelListFalse = new ArrayList<>();//校验失败、保存数据库失败的数据集合，可以插入到一个失败数据表，或者显示在前端提醒用户哪些数据导入失败
//    /**
//     * 很明显，onException这个就是用来处理异常的，当其它侦听器出现异常时会触发此方法
//     * @param e Exception
//     * @param analysisContext analysisContext
//     * Exception 默认是直接抛出异常的，要注意处理异常
//     */
//    @Override
//    public void onException(Exception e, AnalysisContext analysisContext) {
//        log.info("兄嘚，你的代码出现异常了！");
//        e.printStackTrace();
//    }
//
//    /**
//     * 获取excel的第一行head数据
//     * @param map 数据map
//     * @param analysisContext analysisContext
//     */
//    @Override
//    public void invokeHead(Map<Integer, ReadCellData<?>> map, AnalysisContext analysisContext) {
//        log.info("第一列:{} 第二列:{} 第三列:{}",map.get(0).getStringValue(),map.get(1).getStringValue(),map.get(2).getStringValue());
//
//    }
//
//    /**
//     * 读取正文数据，一次只读取一行
//     * @param testExcel 实体类对象
//     * @param analysisContext analysisContext
//     */
//    @Override
//    public void invoke(TestExcel testExcel, AnalysisContext analysisContext) {
//        log.info("读取到一条数据:{}", JSON.toJSONString(testExcel));
//        //因为是测试，这里只做一些简单的为空判断，正式的可以根据业务需求自己写校验条件
//        if(testExcel == null){//对象为空直接跳出
//            return;
//        }else if(StringUtils.isBlank(testExcel.getUserName())){//判断名字是否为空
//            testExcelListFalse.add(testExcel);//放入错误集合列表
//            return;
//        }else if(StringUtils.isBlank(testExcel.getUserCardid())){//判断身份证是否为空
//            /*
//             * 身份证号的判断，以前碰到过一个需求，就是不能和数据库已有的数据重复，数据库存在这个身份证号则表示数据已导入/不能再次导入
//             * 我使用的方式是，先把那个表的”身份证号“字段全部查询出来加载到内存里，然后这里直接和查询出来的身份证号进行对比，存在的就不
//             * 导入，并记录到错误集合列表标注为”重复导入“，不存在的才存入正确的集合列表，并把这个身份证号也存入内存，给后面的数据校验
//             * 是否有重复的数据，这样所有的校验都在内存里进行，优点是：速度会很快、数据库压力也会很小，但是缺点也很明显：很占内存。
//             * 不过通过测试：数据在百万级的，只查询身份证号的话，内存的占用是很少的，即使是微型服务器也能满足需求，而如果是千万级数据，相信
//             * 能有这个数据量的，服务器也差不了，上亿数据量的还没处理过，以后有机会碰到了再进行测试吧，这里不进行身份证号相同的校验
//             */
//            testExcelListFalse.add(testExcel);//放入错误集合列表
//            return;
//        }
//        testExcelListTure.add(testExcel);//校验通过的方法正确集合列表
//        if(count <= testExcelListTure.size()){//集合数据大于设定的数量时，提交数据库保存
//            testExcelService.saveBatch(testExcelListTure);//批量存入数据库
//            testExcelListTure = new ArrayList<>();//清空正确列表数据，再次循环
//        }
//
//    }
//
//    /**
//     * 额外单元格返回的数据，这个方法还没详细了解过，一直没用到过
//     * @param cellExtra cellExtra对象
//     * @param analysisContext analysisContext
//     */
//    @Override
//    public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {
//        log.info("extra:{}",JSON.toJSONString(cellExtra));
//    }
//
//    /**
//     * 当读取完所有数据后就会执行次方法
//     * @param analysisContext analysisContext
//     */
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        log.info("兄嘚，所有数据读取完了哦！");
//        //读取完excel后再次判断是否还要未存入数据库的数据
//        if(!testExcelListTure.isEmpty()){
//            testExcelService.saveBatch(testExcelListTure);//不为空，则存入数据库
//        }
//        //这里也可以处理错误列表，保存入错误列表数据库，或者显示到前端给用户查看
//    }
//
//    /**
//     * 这个方法最坑，默认返回的是false，一定要记得改成true，为false时会只返回一条数据，
//     * 意思是只会执行invokeHead方法
//     * @param analysisContext analysisContext
//     * @return 是否读取下一行
//     */
//    @Override
//    public boolean hasNext(AnalysisContext analysisContext) {
//        return true;
//    }
//}