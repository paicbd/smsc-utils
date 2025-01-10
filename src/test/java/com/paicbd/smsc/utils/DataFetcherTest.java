package com.paicbd.smsc.utils;

import com.paicbd.smsc.dto.MessageEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import redis.clients.jedis.JedisCluster;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataFetcherTest {

    @Test
    void testEmptyRedisList() {
        String redisListName = "testList";
        int tpsPerGateway = 1000;
        int executionEveryInMs = 1000;
        int workersPerGateway = 5;
        JedisCluster jedisCluster = mock(JedisCluster.class);

        when(jedisCluster.llen(redisListName)).thenReturn(0L);

        Flux<List<MessageEvent>> result = DataFetcher.fetchAllEventsFromRedis(
                jedisCluster, tpsPerGateway, executionEveryInMs, workersPerGateway, redisListName
        );

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jedisCluster).llen(redisListName);
    }

    @Test
    void testSmallRedisList() {
        String redisListName = "testList";
        int tpsPerGateway = 1000;
        int executionEveryInMs = 1000;
        int workersPerGateway = 5;
        JedisCluster jedisCluster = mock(JedisCluster.class);

        when(jedisCluster.llen(redisListName)).thenReturn(3L);
        when(jedisCluster.lpop(redisListName, 3)).thenReturn(List.of(new MessageEvent().toString(), new MessageEvent().toString(), new MessageEvent().toString()));

        Flux<List<MessageEvent>> result = DataFetcher.fetchAllEventsFromRedis(
                jedisCluster, tpsPerGateway, executionEveryInMs, workersPerGateway, redisListName
        );

        StepVerifier.create(result)
                .expectNextMatches(events -> events.size() == 3)
                .expectComplete()
                .verify();

        verify(jedisCluster).llen(redisListName);
        verify(jedisCluster).lpop(redisListName, 3);
    }

    @Test
    void testInvalidMessageEventConversion() {
        String redisListName = "testList";
        int tpsPerGateway = 1000;
        int executionEveryInMs = 1000;
        int workersPerGateway = 5;
        JedisCluster jedisCluster = mock(JedisCluster.class);

        when(jedisCluster.llen(redisListName)).thenReturn(3L);
        when(jedisCluster.lpop(redisListName, 3)).thenReturn(
                List.of("invalid1", "invalid2", "invalid3"));

        Flux<List<MessageEvent>> result = DataFetcher.fetchAllEventsFromRedis(
                jedisCluster, tpsPerGateway, executionEveryInMs, workersPerGateway, redisListName
        );

        StepVerifier.create(result)
                .expectNextMatches(List::isEmpty)
                .expectComplete()
                .verify();

        verify(jedisCluster).llen(redisListName);
        verify(jedisCluster).lpop(redisListName, 3);
    }

    @Test
    void testWorkersPerGatewayZero() {
        String redisListName = "testList";
        int tpsPerGateway = 1000;
        int executionEveryInMs = 1000;
        int workersPerGateway = 10;
        JedisCluster jedisCluster = mock(JedisCluster.class);

        when(jedisCluster.llen(redisListName)).thenReturn(0L);

        Flux<List<MessageEvent>> result = DataFetcher.fetchAllEventsFromRedis(
                jedisCluster, tpsPerGateway, executionEveryInMs, workersPerGateway, redisListName
        );

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(jedisCluster).llen(redisListName);
    }
}