package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Notification;
import in.gov.abdm.nmr.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigInteger;

public interface INotificationRepository extends JpaRepository<Notification, BigInteger> {

}
