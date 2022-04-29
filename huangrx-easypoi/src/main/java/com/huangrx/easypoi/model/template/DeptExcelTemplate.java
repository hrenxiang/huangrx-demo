package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
public class DeptExcelTemplate extends ExcelVerifyInfo implements Serializable {

    private static final long serialVersionUID = 6241459712917210470L;
    /**
     * 部门代码
     */
    @NotBlank
    @Excel(name = "部门代码", orderNum = "2", width = 20, needMerge = true)
    private String code;
    /**
     * 部门名称
     */
    @NotBlank
    @Excel(name = "部门名称", orderNum = "1", width = 20, needMerge = true)
    private String name;

    @Size(min = 6)
    @ExcelCollection(name = "员工信息", orderNum = "3")
    private List<UserExcelTemplate> users;
 
 
}
