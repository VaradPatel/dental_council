package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.CollegeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface CollegeMasterRepository extends JpaRepository<CollegeMaster, BigInteger> {
    @Query(value = "SELECT *  FROM college_master WHERE state_id= :stateId", nativeQuery = true)
    List<CollegeMaster> getCollegesByStateId(BigInteger stateId);


    @Query(value = """
            select cm.id, cm.college_id, cm.name, cm.status, cm.visible_status, cm.system_of_medicine_id, 
            cm.state_id, cm.course_id, cm.created_at, cm.updated_at FROM college_master cm join university_master                                                                                                 
            um on cm.college_id=um.college_id where um.university_id = :universityId""", nativeQuery = true)
    List<CollegeMaster> getCollegesByUniversity(BigInteger universityId);


    @Query(value = "select cm.id,cm.college_id,cm.name,cm.state_id FROM main.college_master cm join main.university_master um on cm.college_id=um.college_id where um.university_id =:universityId", nativeQuery = true)
    List<CollegeMaster> getCollegesByUniversity1(BigInteger universityId);
}
