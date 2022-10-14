package com.huangrx.cloud.gateway.filter;

import com.huangrx.cloud.gateway.util.DataBufferUtilFix;
import com.huangrx.cloud.gateway.util.DataBufferWrapper;
import lombok.NonNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hrenxiang
 * @since 2022-10-14 17:38:02
 */
public class RecorderServerHttpRequestDecorator extends ServerHttpRequestDecorator {
    private DataBufferWrapper data = null;

    public RecorderServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public @NonNull Flux<DataBuffer> getBody() {
        synchronized (this) {
            Mono<DataBuffer> mono;
            if (data == null) {
                mono = DataBufferUtilFix.join(super.getBody())
                        .doOnNext(d -> this.data = d)
                        .filter(d -> d.getFactory() != null)
                        .map(DataBufferWrapper::newDataBuffer);
            } else {
                mono = Mono.justOrEmpty(data.newDataBuffer());
            }

            return Flux.from(mono);
        }
    }
}