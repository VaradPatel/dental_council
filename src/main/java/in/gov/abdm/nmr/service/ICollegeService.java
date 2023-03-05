package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;

public interface ICollegeService {
    CollegeProfileTo registerCollege(BigInteger collegeId, CollegeRegistrationRequestTo collegeRegistrationRequestTo, boolean update) throws NmrException, WorkFlowException;

    CollegeRegistrarProfileTo registerRegistrar(BigInteger collegeId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;
    CollegeRegistrarProfileTo updateRegisterRegistrar(BigInteger collegeId,BigInteger registrarId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;

    CollegeDeanProfileTo registerDean(BigInteger collegeId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo) throws NmrException;
    CollegeDeanProfileTo updateRegisterDean(BigInteger collegeId, BigInteger deanId, CollegeDeanCreationRequestTo collegeDeanCreationRequestTo)throws NmrException;

    CollegeProfileTo retrieveCollegeProfile(BigInteger collegeId) throws NmrException;

    CollegeRegistrarProfileTo retrieveRegistrarProfile(BigInteger registrarId,BigInteger collegeId) throws NmrException;

    CollegeDeanProfileTo retrieveDeanProfile(BigInteger collegeId, BigInteger deanId) throws NmrException;

    /**
     * Service Implementation's method for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param pageNo   - Gives the current page number
     * @param limit   - Gives the number of records to be displayed
     * @param search
     * @param value
     * @param columnToSort   -  According to which column the sort has to happen
     * @param sortOrder -  Sorting order ASC or DESC
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval
     */
    CollegeRegistrationResponseTO getCollegeRegistrationDetails(String pageNo, String limit, String search, String value, String columnToSort, String sortOrder) throws InvalidRequestException;
}
