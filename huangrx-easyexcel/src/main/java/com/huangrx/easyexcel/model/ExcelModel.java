package com.huangrx.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author        hrenxiang
 * @since         2022/4/26 3:53 PM
 */
@Data
public class ExcelModel {

    public ExcelModel(){
    }

    public ExcelModel(String dateJuly, String onDuty, String offDuty, String overtime, String last){
        this.dateJuly = dateJuly;
        this.onDuty = onDuty;
        this.offDuty = offDuty;
        this.overtime = overtime;
        this.last = last;
    }

    @ExcelProperty(value = {"时间", "日期"}, index = 0)
    private String dateJuly;
    @ExcelProperty(value = {"时间", "上班时间"}, index = 1)
    private String onDuty;
    @ExcelProperty(value = "下班时间", index = 2)
    private String offDuty;
    @ExcelProperty(value = "加班时长", index = 3)
    private String overtime;
    @ExcelProperty(value = "备注", index = 4)
    private String last;
    @ExcelProperty(value = "gg", index = 5)
    @ColumnWidth(value = 2)
    private String gg;
    @ExcelProperty(value = "tt", index = 7)
    private String tt;
}