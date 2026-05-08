package com.sc.qisi_system.module.admin.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@ExcelIgnoreUnannotated
@Data
public class TeacherWhitelistImportDTO {


    @ExcelProperty(value = "人员代码", index = 0)
    private String personCode;


    @ExcelProperty(value = "姓名", index = 1)
    private String name;


    @ExcelProperty(value = "性别", index = 2)
    private String genderStr;


    @ExcelProperty(value = "出生年月", index = 3)
    private String birthYear;


    @ExcelProperty(value = "现任单位", index = 4)
    private String unitName;


    @ExcelProperty(value = "民族", index = 5)
    private String nation;


    @ExcelProperty(value = "专业职称资格", index = 6)
    private String professionalTitle;


    @ExcelProperty(value = "职级", index = 7)
    private String rank;


    @ExcelProperty(value = "最高学历", index = 8)
    private String highestEducation;


    @ExcelProperty(value = "最高学位", index = 9)
    private String highestDegree;


    @ExcelProperty(value = "来校时间", index = 10)
    private String comeTimeStr;
}
