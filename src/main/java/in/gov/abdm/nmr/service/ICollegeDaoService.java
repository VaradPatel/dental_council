package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestParamsTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.dto.CollegeRegistrationResponseTO;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.exception.NmrException;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

public interface ICollegeDaoService {

    List<CollegeTO> getCollegeData(BigInteger universityId);

    College saveCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException;

    College findById(BigInteger collegeId) throws NmrException;

    College findByUserDetail(BigInteger userDetailId);

    /**
     * Service for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param collegeRegistrationRequestParamsTO - Object with all the attributes related to pagination, filter and sorting
     * @param pageable                           - Object of Pageable that helps in pagination
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval
     */
    CollegeRegistrationResponseTO getCollegeRegistrationData(CollegeRegistrationRequestParamsTO collegeRegistrationRequestParamsTO, Pageable pageable);
}
