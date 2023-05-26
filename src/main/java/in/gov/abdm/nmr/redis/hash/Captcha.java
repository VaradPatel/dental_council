package in.gov.abdm.nmr.redis.hash;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("nmr-captcha")
public class Captcha {
    
    @Id
    private String id;
    private Integer num1;
    private Integer num2;
    private String operation;
    private Integer result;
    private Boolean expired;
    
    @TimeToLive(unit = TimeUnit.MINUTES)
    private long timeToLive;
}
