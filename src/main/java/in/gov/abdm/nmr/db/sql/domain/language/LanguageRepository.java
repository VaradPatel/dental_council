package in.gov.abdm.nmr.db.sql.domain.language;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LanguageRepository extends JpaRepository<Language, BigInteger> {

    @Query(value = "SELECT name, id FROM language", nativeQuery = true)
    List<Language> getLanguage();

}
