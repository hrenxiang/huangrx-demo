package com.huangrx.easypoi.model.template;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 导出时在excel中每个列的高度 单位为字符，一个汉字=2个字符 优先选择@ExportParams中的 height
 *
 * @author        hrenxiang
 * @since         2022-09-22 15:06:19
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ExcelTarget(value = "goodsExcelVo")
public class GoodsExcelVo implements Serializable {

    private static final long serialVersionUID = 6134969246483928554L;
    @Excel(orderNum = "1", name = "物品ID", width = 20)
    private String goodsId;

    @Excel(orderNum = "2", name = "物品编码", width = 20)
    private String productSn;

    @Excel(orderNum = "3", name = "物品名称", width = 20)
    private String goodsName;

    @Excel(orderNum = "4", name = "物品描述", width = 20)
    private String goodsDescription;

    @Excel(orderNum = "5", name = "物品价格", width = 20, type = 10)
    private BigDecimal price;

    @Excel(orderNum = "6", name = "物品成本价", width = 20, type = 10)
    private BigDecimal cost;

    @Excel(orderNum = "7", name = "销售渠道", replace = { "自营_0", "成本代销_1", "分成代销_2" })
    private Integer belongType;

    @Excel(orderNum = "8", name = "物品类型", width = 20, replace = { "家居_0","食品_1","服装_2","美妆_3","母婴_4" })
    private Integer goodsType;

    @Excel(orderNum = "9", name = "销售状态", width = 20, replace = { "已上架_0", "已下架_1" })
    private Integer saleStatus;

    @Excel(orderNum = "10", name = "创建日期", width = 20, exportFormat = "yyyy-MM-dd")
    private LocalDateTime createTime;
}