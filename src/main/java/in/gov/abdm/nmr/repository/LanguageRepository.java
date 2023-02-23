package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, BigInteger> {

    @Query(value = "SELECT name, id FROM language", nativeQuery = true)
    List<Language> getLanguage();

}
