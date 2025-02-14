package com.paicbd.smsc.utils;

import com.paicbd.smsc.dto.MessageEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @ParameterizedTest
    @MethodSource("workersAndBatchSizeParams")
    @DisplayName("Calculate workers and batch size")
    void calculateWorkersAndBatchSizeTests(int tps, int listSize, int workers, Map.Entry<Integer, Integer> expected) {
        var jedisCluster = mock(JedisCluster.class);
        when(jedisCluster.llen("testList")).thenReturn((long) listSize);

        Map.Entry<Integer, Integer> result = DataFetcher.calculateWorkersAndBatchSize(
                jedisCluster, tps, 1000, workers, "testList"
        );

        assertEquals(expected, result);
        System.out.println("workers: " + result.getKey() + ", batchSize: " + result.getValue());
    }

    static Stream<Arguments> workersAndBatchSizeParams() {
        return Stream.of(
                Arguments.of(1000, 1000, 5, Map.entry(5, 200)),
                Arguments.of(1000, 0, 5, Map.entry(0, 0)),
                Arguments.of(3, 2, 2, Map.entry(2, 1)),
                Arguments.of(1000, 3, 5, Map.entry(1, 3))
        );
    }
}