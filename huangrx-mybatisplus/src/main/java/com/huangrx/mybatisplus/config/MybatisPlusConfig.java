package com.huangrx.mybatisplus.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author    hrenxiang
 * @since    2022/4/27 11:07 AM
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    @Resource
    private ApiContextRunner runner;

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
         paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
         paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));


        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        // 创建租户SQL解析器
        TenantSqlParser tenantSqlParser = new TenantSqlParser();

        // 设置租户处理器
        tenantSqlParser.setTenantHandler(new TenantHandler() {

            // 设置当前租户ID，实际情况你可以从cookie、或者缓存中拿都行
            @Override
            public Expression getTenantId(boolean select) {
                // 从当前系统上下文中取出当前请求的服务商ID，通过解析器注入到SQL中。
                Integer currentProviderId = runner.getCurrentTenantId();
                if (null == currentProviderId) {
                    throw new RuntimeException("Get CurrentProviderId error.");
                }
                LongValue longValue = new LongValue(currentProviderId);
                return new LongValue(currentProviderId);
            }

            @Override
            public String getTenantIdColumn() {
                // 对应数据库中租户ID的列名
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 是否需要需要过滤某一张表
                List<String> tableNameList = Arrays.asList("dept");
                return tableNameList.contains(tableName);
            }
        });

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);

        return paginationInterceptor;
    }
}
