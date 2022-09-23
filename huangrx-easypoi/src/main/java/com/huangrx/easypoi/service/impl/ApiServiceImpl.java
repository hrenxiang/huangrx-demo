package com.huangrx.easypoi.service.impl;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.huangrx.easypoi.model.template.DeptExcelTemplate;
import com.huangrx.easypoi.model.template.GoodsExcelVo;
import com.huangrx.easypoi.model.template.OrderExcelVo;
import com.huangrx.easypoi.model.template.UserExcelTemplate;
import com.huangrx.easypoi.service.ApiService;
import com.huangrx.easypoi.utils.ExcelUtils;
import com.huangrx.easypoi.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hrenxiang
 * @since 2022-04-26 3:10 PM
 */
@Service
public class ApiServiceImpl implements ApiService {
    
    @Override
    public String export(HttpServletResponse response) {
        ArrayList<UserExcelTemplate> userExcelTemplates = new ArrayList<>();
        userExcelTemplates.add(generateUser());
        userExcelTemplates.add(generateUser());
        userExcelTemplates.add(generateUser());

        DeptExcelTemplate deptExcelTemplate = new DeptExcelTemplate();
        deptExcelTemplate.setCode("123");
        deptExcelTemplate.setName("123");
        deptExcelTemplate.setUsers(userExcelTemplates);
        ArrayList<DeptExcelTemplate> deptExcelTemplates = new ArrayList<>();
        deptExcelTemplates.add(deptExcelTemplate);
        try {
            //ExcelUtils.exportExcel(userExcelTemplates, "user", "sheet1", UserExcelTemplate.class, "基本信息", response);
            ExcelUtils.exportExcel(deptExcelTemplates, "dept", "sheet1", DeptExcelTemplate.class, "基本信息", response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private UserExcelTemplate generateUser() {
        UserExcelTemplate template = new UserExcelTemplate();
        template.setName("rx");
        template.setNickName("huang");
        template.setAge(20);
        template.setBirth(TimeUtil.strToDate("1919-10-12", TimeUtil.YYYY_MM_DD));
        template.setEmail("12122@dd.com");
        template.setMobilePhone("17283746283");
        template.setSex("男");
        return template;
    }

    @Override
    public void exportMoreSheet(HttpServletResponse response) {

        List<GoodsExcelVo> goodsExcelVos = new ArrayList<>();
        GoodsExcelVo goodsExcelVo = new GoodsExcelVo();
        goodsExcelVo
                .setGoodsId("1")
                .setGoodsDescription("红红的辣条")
                .setGoodsName("辣条")
                .setCost(new BigDecimal("1.00"))
                .setPrice(new BigDecimal("3.00"))
                .setCreateTime(LocalDateTime.now())
                .setBelongType(1)
                .setSaleStatus(0)
                .setGoodsType(1)
                .setProductSn("affadfadfadfadfa");
        goodsExcelVos.add(goodsExcelVo);

        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("商品数据报表");
        exportParams.setTitle("商品数据报表");
        exportParams.setHeight(new Short("30"));
        Map<String, Object> goodsMap = new HashMap<>();
        goodsMap.put("title", exportParams);
        goodsMap.put("entity", GoodsExcelVo.class);
        goodsMap.put("data", goodsExcelVos);


        // 多sheet导出时，如果order里的goods 用 goodsExcelVo 导出的excel中 订单数据报表中商品的数据是空的
        // 但是我们重新创建一个 GoodsExcelVo 并设置到 order里，导出中是有数据的
        // 表明我们使用多sheet 导出时，最好不要使用同一个数据源
        List<GoodsExcelVo> goodsExcelVos2 = new ArrayList<>();
        GoodsExcelVo goodsExcelVo2 = new GoodsExcelVo();
        goodsExcelVo2
                .setGoodsId("1")
                .setGoodsDescription("红红的辣条")
                .setGoodsName("辣条")
                .setCost(new BigDecimal("1.00"))
                .setPrice(new BigDecimal("3.00"))
                .setCreateTime(LocalDateTime.now())
                .setBelongType(1)
                .setSaleStatus(0)
                .setGoodsType(1)
                .setProductSn("affadfadfadfadfa");
        goodsExcelVos2.add(goodsExcelVo2);

        List<OrderExcelVo> orderExcelVos = new ArrayList<>();
        OrderExcelVo orderExcelVo = new OrderExcelVo();
        OrderExcelVo or = orderExcelVo
                .setGoods(goodsExcelVos2)
                .setOrderId("24214231")
                .setOrderName("一笔订单")
                .setOrderStatus(1)
                .setOrderPrice("20000")
                .setPayMoney(new BigDecimal("10000"))
                .setPayStatus(1)
                .setNeedPayMoney(new BigDecimal("20000"))
                .setRemainPayMoney(new BigDecimal("10000"));
        orderExcelVos.add(or);
        ExportParams exportParams1 = new ExportParams();
        exportParams1.setSheetName("订单数据报表");
        exportParams1.setTitle("订单数据报表");
        exportParams1.setHeight(new Short("30"));
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("title", exportParams1);
        orderMap.put("entity", OrderExcelVo.class);
        orderMap.put("data", orderExcelVos);

        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(goodsMap);
        sheetsList.add(orderMap);

        try {
            ExcelUtils.exportExcel(sheetsList, "营销数据报表", response);
            //ExcelUtils.exportExcel(orderExcelVos, OrderExcelVo.class, "订单信息", exportParams1, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
