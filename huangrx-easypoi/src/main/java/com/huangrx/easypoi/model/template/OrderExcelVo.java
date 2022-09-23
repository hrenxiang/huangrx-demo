package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author        hrenxiang
 * @since         2022-09-22 16:12:53
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ExcelTarget(value = "orderExcelVo")
public class OrderExcelVo implements Serializable {

    private static final long serialVersionUID = 716502395555783020L;
    @Excel(orderNum = "1", name = "订单ID", width = 20,needMerge = true, groupName = "基本信息", type = 10)
    private String orderId;

    @Excel(orderNum = "2", name = "订单价格", width = 20,needMerge = true, groupName = "基本信息", type = 10)
    private String orderPrice;

    @Excel(orderNum = "3", name = "订单名称", width = 20,needMerge = true, groupName = "基本信息")
    private String orderName;

    @Excel(orderNum = "4", name = "订单状态", replace = {"待支付_0","部分支付_1"," 已支付_2"})
    private Integer orderStatus;

    @Excel(orderNum = "5", name = "付款状态", width = 20, replace = {"未支付_0", "部分支付_1", "已支付_2"})
    private Integer payStatus;

    @Excel(orderNum = "6", name = "应付金额", width = 20, needMerge = true, groupName = "支付信息", type = 10)
    private BigDecimal needPayMoney;

    @Excel(orderNum = "7", name = "已支付金额", width = 20, needMerge = true, groupName = "支付信息", type = 10)
    private BigDecimal payMoney;

    @Excel(orderNum = "8", name = "剩余支付金额", width = 20, needMerge = true, groupName = "支付信息", type = 10)
    private BigDecimal remainPayMoney;

    /** 多sheet导出时，不知道为什么 这个导出不出来*/
    @ExcelCollection(orderNum = "9", name = "商品信息", type = GoodsExcelVo.class)
    private List<GoodsExcelVo> goods;
}