package in.gov.abdm.nmr.mongodb.repository;


import in.gov.abdm.nmr.mongodb.entity.Council;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import javax.persistence.Tuple;
import java.util.List;

@Repository
public interface ICouncilRepository extends MongoRepository<Council, String> {

    //@Query(value = "{ 'registrationDetails': { $elemMatch: { 'registrationDetails.registrationNo' : ?0  } }}")
//    @Query("{$and :[{ 'registrationDetails': { $elemMatch: { 'registrationDetails.registrationNo' : ?0  } }},{ 'registrationDetails': { $elemMatch: { 'registrationDetails.council_name' : ?1  } }}] }")
//    @Query("{ 'registration_details.registration_no' : '?0' , 'registration_details.council_name' : '?1' }, { sort: { '_id' : '-1' }, limit: '1' }")
    @Query(value = "{ 'registration_details.registration_no' : '?0' , 'registration_details.council_name' : '?1'}",sort = "{ _id : -1 }")
    List<Council> findCouncilByRegistrationNumberAndCouncilName(String registrationNumber, String councilName, Pageable pageable);
//    List<Council> findCouncilByRegistrationNumber(String registrationNumber, String councilName);

//    List<Council> findCouncilByRegistrationNumberAndCouncilName(String registrationNumber, String councilName, Pageable pageable);

    @Query("{id :'?0'}")
    Tuple findCouncilById(String id);

    //@Query("{$and :[{author: ?0},{cost: ?1}] }")
}
