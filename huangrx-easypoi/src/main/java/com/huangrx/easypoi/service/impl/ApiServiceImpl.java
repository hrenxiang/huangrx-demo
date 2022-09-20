package com.huangrx.easypoi.service.impl;

import com.huangrx.easypoi.model.template.UserExcelTemplate;
import com.huangrx.easypoi.service.ApiService;
import com.huangrx.easypoi.utils.ExcelUtils;
import com.huangrx.easypoi.utils.TimeUtil;
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
    public String export(HttpServletResponse response) {
        ArrayList<UserExcelTemplate> userExcelTemplates = new ArrayList<>();
        userExcelTemplates.add(generateUser());
        userExcelTemplates.add(generateUser());
        userExcelTemplates.add(generateUser());
        try {
            ExcelUtils.exportExcel(userExcelTemplates, "user", "sheet1", UserExcelTemplate.class, "基本信息", response);
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
}
