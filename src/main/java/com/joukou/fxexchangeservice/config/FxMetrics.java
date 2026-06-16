package com.joukou.fxexchangeservice.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class FxMetrics {

    private final Counter apiCounter;
    private final Counter cacheHitCounter;
    private final Counter cacheMissCounter;

    public FxMetrics(MeterRegistry registry) {

        this.apiCounter = Counter.builder("fx.api.calls")
                .register(registry);

        this.cacheHitCounter = Counter.builder("fx.cache.hits")
                .register(registry);

        this.cacheMissCounter = Counter.builder("fx.cache.misses")
                .register(registry);
    }

    public void apiCall() {
        apiCounter.increment();
    }

    public void cacheHit() {
        cacheHitCounter.increment();
    }

    public void cacheMiss() {
        cacheMissCounter.increment();
    }
}
