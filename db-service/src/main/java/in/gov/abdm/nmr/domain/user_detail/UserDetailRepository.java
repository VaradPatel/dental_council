package in.gov.abdm.nmr.domain.user_detail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    Optional<UserDetail> findByUsername(String username);

    @Query("SELECT ud.refreshTokenId FROM userDetail ud where ud.username = :username")
    Optional<String> findRefreshTokenIdByUsername(String username);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE userDetail ud SET ud.refreshTokenId = :refreshTokenId WHERE ud.username = :username")
    Optional<Integer> updateRefreshTokenId(String username, String refreshTokenId);
}
