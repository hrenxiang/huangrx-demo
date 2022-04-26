package com.huangrx.easyexcel.service;

import javax.servlet.http.HttpServletResponse;

/**
 * 导入导出方法接口
 *
 * @author hrenxiang
 * @since 2022-04-26 3:10 PM
 */
public interface ApiService {
    /**
     * 导出
     * @param response
     */
    void export(HttpServletResponse response);
}
