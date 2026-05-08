package com.sc.qisi_system.module.admin.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@ExcelIgnoreUnannotated
@Data
public class StudentWhitelistImportDTO {


    @ExcelProperty(value = "学生姓名", index = 0)
    private String name;


    @ExcelProperty(value = "学号", index = 1)
    private String studentId;


    @ExcelProperty(value = "学院", index = 2)
    private String college;


    @ExcelProperty(value = "专业", index = 3)
    private String major;


    @ExcelProperty(value = "年级", index = 4)
    private String grade;


    @ExcelProperty(value = "学历层次", index = 5)
    private String educationLevel;


    @ExcelProperty(value = "班级", index = 6)
    private String className;
}
