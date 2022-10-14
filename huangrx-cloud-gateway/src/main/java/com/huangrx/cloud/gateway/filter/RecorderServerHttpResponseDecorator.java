package com.huangrx.cloud.gateway.filter;

import com.huangrx.cloud.gateway.util.DataBufferUtilFix;
import com.huangrx.cloud.gateway.util.DataBufferWrapper;
import lombok.NonNull;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hrenxiang
 * @since 2022-10-14 17:44:49
 */
public class RecorderServerHttpResponseDecorator extends ServerHttpResponseDecorator {
    private DataBufferWrapper data = null;

    public RecorderServerHttpResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
        return DataBufferUtilFix.join(Flux.from(body))
                .doOnNext(d -> this.data = d)
                .flatMap(d -> super.writeWith(copy()));
    }

    @Override
    public @NonNull Mono<Void> writeAndFlushWith(@NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body)
                .flatMapSequential(p -> p));
    }

    public Flux<DataBuffer> copy() {
        //如果data为null 就出错了 正好可以调试
        DataBuffer buffer = this.data.newDataBuffer();
        if (buffer == null)
            return Flux.empty();

        return Flux.just(buffer);
    }
}