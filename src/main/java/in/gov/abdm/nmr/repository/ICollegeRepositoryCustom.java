package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestParamsTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationResponseTO;
import org.springframework.data.domain.Pageable;

/**
 * An interface for the customized repository of college repository
 */
public interface ICollegeRepositoryCustom {
    /**
     * Customized College Repository Interface with a method for fetching the records
     * of the college for the NMC
     *
     * @param collegeRegistrationRequestParamsTO - Object with all the attributes related to pagination, filter and sorting
     * @param pageable                           - Object of Pageable that helps in pagination
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the college who have
     * raised a request to NMC for approval.
     */
    CollegeRegistrationResponseTO getCollegeRegistrationData(CollegeRegistrationRequestParamsTO collegeRegistrationRequestParamsTO, Pageable pageable);
}
