package com.example.funding.service.Application;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AppExcel {
    @ExcelProperty(value="经费编号")
    private String ExpenditureId;
    @ExcelProperty(value="概要")
    private String abstracts;
    @ExcelProperty(value="备注")
    private String comment;
    @ExcelProperty(value="申请金额")
    private String amount;
    //TODO: category 待定
    @ExcelProperty(value="一级类别")
    private String category1;
    @ExcelProperty(value="二级类别")
    private String category2;
}
