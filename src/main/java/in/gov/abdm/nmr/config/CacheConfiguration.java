package in.gov.abdm.nmr.config;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfiguration {

    @Bean
    public RedisCacheConfiguration searchCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Scheduled(fixedRate = NMRConstants.MASTER_CACHE_CRON_TIME)
    @CacheEvict(value = {NMRConstants.MASTER_CACHE_NAME}, allEntries = true)
    public void clearMasterCache() {
        log.info("Cache '{}' cleared.", NMRConstants.MASTER_CACHE_NAME);
    }

    @Scheduled(fixedRate = NMRConstants.TEMPLATE_CACHE_CRON_TIME, timeUnit = TimeUnit.HOURS)
    @CacheEvict(value = {NMRConstants.TEMPLATE_CACHE_NAME}, allEntries = true)
    public void clearTemplateCache() {
        log.info("Cache '{}' cleared.", NMRConstants.TEMPLATE_CACHE_NAME);
    }
}