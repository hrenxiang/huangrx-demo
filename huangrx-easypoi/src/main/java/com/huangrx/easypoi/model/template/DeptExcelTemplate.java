package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author        hrenxiang
 * @since         2022/4/26 3:04 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget("deptExcel")
public class DeptExcelTemplate extends InformationExcelVerifyInfo implements Serializable {
 
    /**
     * 部门代码
     */
    @Excel(name = "部门代码", orderNum = "2", width = 20, needMerge = true)
    private String code;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称", orderNum = "1", width = 20, needMerge = true)
    private String name;
 
    @ExcelCollection(name = "员工信息", orderNum = "3")
    private List<UserExcelTemplate> users;
 
 
}
