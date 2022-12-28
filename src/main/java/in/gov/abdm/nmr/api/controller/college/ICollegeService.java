package in.gov.abdm.nmr.api.controller.college;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeDeanProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeService {
    CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException;

    CollegeRegistrarProfileTo registerRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;

    CollegeDeanProfileTo registerDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;

    CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId) throws NmrException;

    CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId) throws NmrException;

    CollegeDeanProfileTo retrieveDeanProfile(BigInteger id) throws NmrException;
}
