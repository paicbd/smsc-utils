package com.paicbd.smsc.utils;

import com.paicbd.smsc.dto.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.paicbd.smsc.utils.Converter.stringToObject;

@Slf4j
public class DataFetcher {
    @Generated
    private DataFetcher() {
        throw new IllegalStateException("Utility class");
    }

    public static Flux<List<MessageEvent>> fetchAllEventsFromRedis(
            JedisCluster jedisCluster, int tpsPerGateway, int executionEveryInMs, int workersPerGateway, String redisListName) {
        Map.Entry<Integer, Integer> workersAndBatch = calculateWorkersAndBatchSize(jedisCluster, tpsPerGateway, executionEveryInMs, workersPerGateway, redisListName);
        int workers = workersAndBatch.getKey();
        int batchSize = workersAndBatch.getValue();
        if (batchSize == 0) {
            return Flux.empty();
        }
        log.debug("RedisListName: {}, Workers: {}, BatchSize: {}", redisListName, workers, batchSize);
        return Flux.range(0, workers)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(worker -> {
                    List<String> batch = jedisCluster.lpop(redisListName, batchSize);
                    log.debug("Fetching all events from Redis: {}", batch);
                    List<MessageEvent> submitSmEvents = batch
                            .parallelStream()
                            .map(msgRaw -> stringToObject(msgRaw, MessageEvent.class))
                            .filter(Objects::nonNull)
                            .toList();
                    return Flux.just(submitSmEvents);
                }).subscribeOn(Schedulers.boundedElastic());
    }

    private static Map.Entry<Integer, Integer> calculateWorkersAndBatchSize(
            JedisCluster jedisCluster, int tpsPerGateway, int executionEveryInMs, int workersPerGateway, String redisListName) {
        int recordsToTake = tpsPerGateway * (executionEveryInMs / 1000);
        int listSize = (int) jedisCluster.llen(redisListName);
        if (listSize == 0) {
            return Map.entry(0, 0);
        }
        int min = Math.min(recordsToTake, listSize);
        var bpw = min / workersPerGateway;
        return bpw > 0 ? Map.entry(workersPerGateway, bpw) : Map.entry(1, min);
    }
}
