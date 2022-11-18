package in.gov.abdm.nmr.domain.user_detail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    Optional<UserDetail> findByUsername(String username);

    @Query("SELECT ud.refreshTokenId FROM userDetail ud where ud.id = :id and ud.refreshTokenId = :refreshTokenId")
    Optional<String> findRefreshTokenIdByIdAndRefreshTokenId(Long id, String refreshTokenId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE userDetail ud SET ud.refreshTokenId = :refreshTokenId WHERE ud.id = :id")
    Optional<Integer> updateRefreshTokenId(Long id, String refreshTokenId);
}
