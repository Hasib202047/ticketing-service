package com.hasib.ticketing_service.service;

import com.hasib.ticketing_service.enums.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class RedisTicketScheduler {
    private final RedisTemplate<String, String> redisTemplate;

    private String getZSetKeyForPriority(Priority priority) {
        return "ticket:timeout:" + priority.name().toLowerCase() + ":zset";
    }

    public void scheduleTicket(Long ticketId, Priority priority) {
        long delaySeconds = switch (priority) {
            case URGENT -> Duration.ofMinutes(3).getSeconds();
            case HIGH -> Duration.ofMinutes(5).getSeconds();
            case MEDIUM -> Duration.ofMinutes(7).getSeconds();
            case LOW -> Duration.ofMinutes(9).getSeconds();
        };

        long score = Instant.now().getEpochSecond() + delaySeconds;
        redisTemplate.opsForZSet().add(getZSetKeyForPriority(priority), ticketId.toString(), score);
    }

    public Set<String> popExpiredTickets(Priority priority) {
        long now = Instant.now().getEpochSecond();
        Set<String> expired = redisTemplate.opsForZSet().rangeByScore(getZSetKeyForPriority(priority), 0, now);
        if (expired != null && !expired.isEmpty()) {
            redisTemplate.opsForZSet().removeRangeByScore(getZSetKeyForPriority(priority), 0, now);
        }
        return expired;
    }

    public void  removeTicket(Long ticketId,Priority priority) {
        redisTemplate.opsForZSet().remove(getZSetKeyForPriority(priority), ticketId.toString());
    }

}
