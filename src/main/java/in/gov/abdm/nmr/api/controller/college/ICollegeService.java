package in.gov.abdm.nmr.api.controller.college;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;

public interface ICollegeService {
    CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo);

    CollegeRegistrarProfileTo registerRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);

    CollegeDeanProfileTo registerDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);

    CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId);

    CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId);

    CollegeDeanProfileTo retrieveDeanProfile(BigInteger id);
}
