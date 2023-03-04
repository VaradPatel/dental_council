package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;

public interface ICollegeService {
    CollegeProfileTo registerCollege(CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException, WorkFlowException;

    CollegeRegistrarProfileTo registerRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;

    CollegeDeanProfileTo registerDean(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;

    CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId) throws NmrException;

    CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId) throws NmrException;

    CollegeDeanProfileTo retrieveDeanProfile(BigInteger id) throws NmrException;

    CollegeRegistrationResponseTO getCollegeRegistrationDetails(String pageNo, String limit, String filterCriteria, String filterValue, String columnToSort, String sortOrder);
}
