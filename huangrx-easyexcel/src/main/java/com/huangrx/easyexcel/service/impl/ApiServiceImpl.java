package com.huangrx.easyexcel.service.impl;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.huangrx.easyexcel.model.ExcelModel;
import com.huangrx.easyexcel.service.ApiService;
import com.huangrx.easyexcel.utils.ExcelUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author hrenxiang
 * @since 2022-04-26 3:10 PM
 */
@Service
public class ApiServiceImpl implements ApiService {
    
    @Override
    public void export(HttpServletResponse response) {
        ArrayList<ExcelModel> excelModels = new ArrayList<>();
        excelModels.add(generateUser());
        excelModels.add(generateUser());
        excelModels.add(generateUser());
        try {
            ExcelUtil.writeExcel(response, excelModels, "easy-excel", "sheet1", ExcelTypeEnum.XLSX);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ExcelModel generateUser() {
        ExcelModel template = new ExcelModel();
        template.setGg("gg");
        template.setLast("last");
        template.setDateJuly("dateJuly");
        template.setOffDuty("off");
        template.setOnDuty("on");
        template.setOvertime("over");
        template.setTt("tt");
        return template;
    }
}
