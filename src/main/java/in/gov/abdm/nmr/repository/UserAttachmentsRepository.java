package in.gov.abdm.nmr.repository;
import in.gov.abdm.nmr.entity.UserAttachments;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigInteger;

public interface UserAttachmentsRepository extends JpaRepository<UserAttachments, BigInteger> {
}
