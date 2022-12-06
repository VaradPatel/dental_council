package in.gov.abdm.nmr.db.sql.domain.state;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StateRepository extends JpaRepository<State, Long> {

    @Query(value = "SELECT * FROM state where country=:country", nativeQuery = true)
    List<State> getState(Long country);

}
