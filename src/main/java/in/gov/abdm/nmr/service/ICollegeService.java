package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
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
     * Service for fetching the College registration records
     * for the NMC that has been submitted for approval
     *
     * @param pageNo       - Gives the current page number
     * @param offset        - Gives the number of records to be displayed
     * @param search       - Gives the search criteria like HP_Id, HP_name, Submiited_Date, Remarks
     * @param sortBy -  According to which column the sort has to happen
     * @param sortType    -  Sorting order ASC or DESC
     * @return the CollegeRegistrationResponseTO  response Object
     * which contains all the details related to the College submitted to NMC
     * for approval
     */
    CollegeRegistrationResponseTO getCollegeRegistrationDetails(String pageNo, String offset, String search, String collegeId, String collegeName, String councilName, String sortBy, String sortType);

    CollegeRegistrationResponseTO getCollegeRegistrationDetails(String pageNo, String limit, String filterCriteria, String filterValue, String columnToSort, String sortOrder);
}
