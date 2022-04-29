package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;

/**
 * 导入错误 信息 模版
 *
 * 需要 excel 模版进行 继承
 *
 * importParams.setNeedVerify(true); 开启
 *
 * @author    hrenxiang
 * @since    2022/4/28 6:28 PM
 */
public class ExcelVerifyInfo implements IExcelModel, IExcelDataModel {

    /**
     * 错误提示
     */
    private String errorMsg;

    /**
     * 数据所在行
     */
    private int rowNum;

    @Override
    public Integer getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}