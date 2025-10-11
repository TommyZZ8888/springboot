package com.www.orm.mybatisplus.config;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum ComplainantEnums  {
    @JsonEnumDefaultValue
//    CUSTOMER(1, "Customer", "عميل"),
//
//    CONTRACTOR(2, "Contractor", "اول"),
//
//    EMPLOYEE(3, "Employee", "موظف"),
//
//    EX_EMPLOYEE(4, "Ex-Employee", "موظف سابق"),
//
//    WHISTLEBLOWER(5, "Whistleblower", "لشخص الذي يفضح الفساد"),

    WHISTLEBLOWER(1, "Whistleblower", "مبلغ"),
    CUSTOMER(2, "Customer", "متعامل"),
    EX_EMPLOYEE(3, "Ex-Employee", "موظف سابق"),
    EMPLOYEE(4, "Employee", "موظف"),
    CONTRACTOR(5, "Contractor", "متعاقد"),
    OTHER(6, "Others", "أخرى"),
    ;

    private int id;
    private String name;
    private String nameAr;

    public String getEn() {
        return this.name;
    }

    public String getAr() {
        return this.nameAr;
    }
}
