package com.joukou.fxexchangeservice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;

public class FxCacheScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(FxCacheScheduler.class);

    @Scheduled(cron = "0 0 0 * * *") // midnight daily
    @CacheEvict(value = "fx-rates", allEntries = true)
    public void clearFxCache() {
        log.info("FX cache cleared at midnight");
    }
}
