package in.gov.abdm.nmr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableJpaRepositories(basePackages = {"in.gov.abdm.nmr.repository"})
@EnableRedisRepositories(basePackages = {"in.gov.abdm.nmr.redis"})
@EnableMongoRepositories(basePackages = {"in.gov.abdm.nmr.nosql"})
public class  NmrApplication {

    public static void main(String[] args) {
        SpringApplication.run(NmrApplication.class, args);
    }
}
