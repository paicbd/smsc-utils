package com.paicbd.smsc.cdr;

import com.paicbd.smsc.dto.UtilsRecords;
import com.paicbd.smsc.utils.BroadcastMessageStatus;
import com.paicbd.smsc.utils.Converter;
import com.paicbd.smsc.utils.UtilsEnum;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Slf4j
public class CdrProcessor {
    private static final String CDR_LIST_NAME = "cdr";
    private static final String CDR_DETAIL_HASH_NAME = "cdr_details";
    private static final String BROADCAST_STATISTICS_LIST = "broadcast_statistics";
    private final JedisCluster jedisCluster;

    public CdrProcessor(UtilsRecords.JedisConfigParams params) {
        log.debug("Creating CdrProcessor instance with params: {}", params);
        this.jedisCluster = Converter.paramsToJedisCluster(params);
    }

    public CdrProcessor(JedisCluster jedisCluster) {
        log.debug("Creating CdrProcessor instance with jedisCluster {}", jedisCluster);
        this.jedisCluster = jedisCluster;
    }

    public void putCdrDetailOnRedis(@Nonnull UtilsRecords.CdrDetail newCdrDetails) {
        Objects.requireNonNull(newCdrDetails.messageId(), "MessageId is null, cannot put CDR");
        String newCdrRaw = newCdrDetails.toString();
        this.jedisCluster.hset(CDR_DETAIL_HASH_NAME, newCdrDetails.messageId(), newCdrRaw);
    }

    public void createCdr(String messageId) {
        Objects.requireNonNull(messageId, "HashId is null, cannot create CDR");
        String cdrDetailJson = jedisCluster.hget(CDR_DETAIL_HASH_NAME, messageId);
        if (cdrDetailJson != null) {
            UtilsRecords.CdrDetail cdrDetail = Converter.deserializeCdrDetail(cdrDetailJson);
            String cdrJson = Converter.convertCdrDetailToCdrJson(cdrDetail);
            if (cdrJson != null) {
                this.sendBroadcastStatistics(cdrDetail);
                jedisCluster.hdel(CDR_DETAIL_HASH_NAME, messageId);
                jedisCluster.hdel(CDR_DETAIL_HASH_NAME, cdrDetail.parentId());
                jedisCluster.lpush(CDR_LIST_NAME, cdrJson);
                log.debug("Created CDR and added to Redis queue: {}", cdrJson);
            }
            return;
        }

        log.error("CDR detail not found for messageId: {}", messageId);
    }

    private void sendBroadcastStatistics(UtilsRecords.CdrDetail cdrDetail) {
        if (Objects.nonNull(cdrDetail.broadcastId())) {
            var submitDate = Converter.parseSubmitDate(cdrDetail.idEvent());
            Instant instant = Instant.ofEpochMilli(submitDate);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
            var broadcastStatistic = new UtilsRecords.BroadcastStatistic(
                    cdrDetail.broadcastId(),
                    cdrDetail.messageId(),
                    UtilsEnum.CdrStatus.SENT.toString().equals(cdrDetail.cdrStatus()) ?
                            BroadcastMessageStatus.SENT.getValue() : BroadcastMessageStatus.FAILED.getValue(),
                    localDateTime.toString()
            );
            jedisCluster.lpush(BROADCAST_STATISTICS_LIST, broadcastStatistic.toString());
        }
    }
}
