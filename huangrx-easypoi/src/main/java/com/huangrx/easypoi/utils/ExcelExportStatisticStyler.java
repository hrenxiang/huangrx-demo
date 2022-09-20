package com.huangrx.easypoi.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;
import org.apache.poi.ss.usermodel.*;

import javax.validation.constraints.NotBlank;

/**
 *  自定义数字类型，解决easyPoi 设置type不生效问题<br/>
 *  <br/>
 *  type 是代表文本类型，1文本，10数值 还有其他取值<br/>
 *  有时候设置 type =10 不生效，我们自定义数据格式, 此处设置后，我们也需要代码中进行设置，<br/>
 *  ExportParams exportParams = new ExportParams(fileName, fileName, ExcelType.XSSF);<br/>
 *  exportParams.setStyle(ExcelExportStatisticStyler.class);<br/>
 *  <br/>
 *  <br/>
 * '@Excel(name = "部门代码", orderNum = "2", width = 20, needMerge = true, type = 10, dict = ExcelExportStatisticStyler.EXCEL_COVERT_MONEY)'<br/>
 * 'private String code;'<br/>
 */
public class ExcelExportStatisticStyler extends ExcelExportStylerDefaultImpl {

    public static final String EXCEL_COVERT_MONEY = "EXCEL_COVERT_MONEY";

    private CellStyle numberCellStyle;

    public ExcelExportStatisticStyler(Workbook workbook) {
        super(workbook);
        createNumberCellStyler();
    }

    private void createNumberCellStyler() {
        numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setAlignment(HorizontalAlignment.CENTER);
        numberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 参数格式会影响导出的数据类型，提供的格式在BuiltinFormats中可以查看
        numberCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0.00"));
        numberCellStyle.setWrapText(true);
    }

    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        //自定义Dict转换
        if (entity != null
                && ExcelExportStatisticStyler.EXCEL_COVERT_MONEY.equals(entity.getDict())) {
            return numberCellStyle;
        }
        return super.getStyles(noneStyler, entity);
    }
}