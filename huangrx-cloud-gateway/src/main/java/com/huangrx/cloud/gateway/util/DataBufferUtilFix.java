package com.huangrx.cloud.gateway.util;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 当前版本的spring的DataBufferUtil有bug，官方已经在后续版本修复，这里是参考的后续版本的GIT的代码
 *
 * @author hrenxiang
 * @since 2022-10-14 17:16:17
 */
public class DataBufferUtilFix {
    public static Mono<DataBufferWrapper> join(Flux<DataBuffer> dataBuffers) {
        return dataBuffers.collectList()
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0).factory().join(list))
                .publishOn(Schedulers.boundedElastic())
                .map(buf -> {
                    InputStream source = buf.asInputStream();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] buff = new byte[4096];

                    try {
                        int n;
                        while ((n = source.read(buff)) != -1) {
                            stream.write(buff, 0, n);
                        }
                    } catch (IOException e) {
                        //
                    }

                    DataBufferWrapper wrapper = new DataBufferWrapper(stream.toByteArray(), buf.factory());
                    //当前版本的 DataBufferUtils::join 没有这一句
                    DataBufferUtils.release(buf);

                    return wrapper;
                })
                .defaultIfEmpty(new DataBufferWrapper());
    }
}