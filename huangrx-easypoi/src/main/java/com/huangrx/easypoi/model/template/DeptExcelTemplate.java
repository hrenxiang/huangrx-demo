package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.huangrx.easypoi.utils.ExcelExportStatisticStyler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@ExcelTarget("deptExcel")
public class DeptExcelTemplate extends ExcelVerifyInfo implements Serializable {

    private static final long serialVersionUID = 6241459712917210470L;
    /**
     * 部门代码<br/>
     * <br/>
     * type 是代表文本类型，1文本，10数值 还有其他取值<br/>
     * 有时候设置 type =10 不生效，我们自定义数据格式, 此处设置后，我们也需要代码中进行设置，<br/>
     * ExportParams exportParams = new ExportParams(fileName, fileName, ExcelType.XSSF);<br/>
     * exportParams.setStyle(ExcelExportStatisticStyler.class);<br/>
     */
    @NotBlank
    @Excel(name = "部门代码", orderNum = "2", width = 20, needMerge = true, type = 10, dict = ExcelExportStatisticStyler.EXCEL_COVERT_MONEY)
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
