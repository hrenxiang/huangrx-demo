package com.huangrx.http.feign;

import com.huangrx.nacos.api.NacosApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * feign
 *
 * @author hrenxiang
 * @since 2022-04-28 6:51 PM
 */
@FeignClient("huangrx-nacos")
public interface HuangrxNacosClient extends NacosApi {

}
