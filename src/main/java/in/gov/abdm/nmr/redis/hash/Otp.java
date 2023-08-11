package in.gov.abdm.nmr.redis.hash;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
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

    @Id
    private String id;
    private String otp;
    private Integer attempts;
    
    @Indexed
    private String contact;
    private boolean expired;
    private BigInteger userType;
    
    @TimeToLive(unit = TimeUnit.MINUTES)
    private long timeToLive;
}
