package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestParamsTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationResponseTO;
import org.springframework.data.domain.Pageable;


public interface ICollegeRepositoryCustom {
    CollegeRegistrationResponseTO getCollegeRegistrationData(CollegeRegistrationRequestParamsTO collegeRegistrationRequestParamsTO, Pageable pageable);
}
