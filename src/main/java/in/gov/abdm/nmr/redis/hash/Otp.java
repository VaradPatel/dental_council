package in.gov.abdm.nmr.redis.hash;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("nmr-otp")
public class Otp {

    private String id;
    private String otp;
    private Integer attempts;
    
    @Indexed
    private String contact;
    private boolean expired;
    
    @TimeToLive(unit = TimeUnit.MINUTES)
    private long timeToLive;
}
