package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * user 导出 模版
 *
 * @author hrenxiang
 * @since 2022-04-26 3:02 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget("userExcel")
public class UserExcelTemplate {

    /**
     * 名称
     */
    @Excel(name = "姓名", orderNum = "1")
    private String name;

    /**
     * 年龄
     */
    @Excel(name = "年龄", orderNum = "2", groupName = "基本信息")
    private Integer age;

    /**
     * 性别
     */
    @Excel(name = "性别", orderNum = "3", replace = {"男_M", "女_F", "未知_X"}, groupName = "基本信息")
    private String sex;

    /**
     * 手机
     */
    @Excel(name = "手机", orderNum = "4", width = 20, groupName = "联系方式")
    private String mobilePhone;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱", orderNum = "5", width = 20, groupName = "联系方式", isWrap = true)
    private String email;

    /**
     * 昵称
     */
    @Excel(name = "昵称", orderNum = "6", groupName = "其他信息")
    private String nickName;

    @Excel(name = "生日", orderNum = "7", exportFormat = "yyyy-MM-dd", groupName = "其他信息")
    private Date birth;
}
