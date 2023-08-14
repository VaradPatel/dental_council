package in.gov.abdm.nmr.redis.repository;
import in.gov.abdm.nmr.redis.hash.BlacklistToken;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface IBlacklistTokenRepository extends KeyValueRepository<BlacklistToken, String> {
}
