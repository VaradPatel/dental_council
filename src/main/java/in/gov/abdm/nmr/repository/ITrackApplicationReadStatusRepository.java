package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.TrackApplicationReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface ITrackApplicationReadStatusRepository extends JpaRepository<TrackApplicationReadStatus, BigInteger> {

      @Query(value = "Select * from track_application_read_status where user_id = :userId",nativeQuery = true)
      TrackApplicationReadStatus findByUserId(BigInteger userId);
}
