package in.gov.abdm.nmr.nosql.repository;


import in.gov.abdm.nmr.nosql.entity.Council;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICouncilRepository extends MongoRepository<Council, String> {

    @Query(value = "{ 'registration_details.registration_no' : '?0' , 'registration_details.council_name' : '?1'}")
    List<Council> findCouncilByRegistrationNumberAndCouncilName(String registrationNumber, String councilName);

}
