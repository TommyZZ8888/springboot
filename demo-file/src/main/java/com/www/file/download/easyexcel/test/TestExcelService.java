//package com.www.file.download.easyexcel.test;
//
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
///**
// * @Description TestExcelService
// * @Author 张卫刚
// * @Date Created on 2024/1/5
// */
//@Service
//public class TestExcelService {
//    public void saveBatch(List<TestExcel> testExcelListTure) {
//    }
//
//    public long count() {
//        return 1000000;
//    }
//
//    public List<TestExcel> query() {
//        List<TestExcel> collect = IntStream.range(0, 500).mapToObj(item ->
//                TestExcel.builder().userId(String.valueOf(item))
//                        .userAge("2")
//                        .userName("2")
//                        .userCardid("2")
//                        .build()).collect(Collectors.toList());
//        return collect;
//    }
//}
