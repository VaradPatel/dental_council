package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;

/**
 * An interface that deals with the reactivation and suspension requests
 */
public interface IApplicationService {

    /**
     * This method is used to suspend a health professional based on the request provided.
     *
     * @param applicationRequestTo the request object containing necessary information to suspend a health professional.
     * @return a string indicating the result of the suspension request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    SuspendRequestResponseTo suspendRequest(ApplicationRequestTo applicationRequestTo) throws WorkFlowException;

    /**
     * This method is used to reactivate a health professional based on the request provided.
     *
     * @param applicationRequestTo the request object containing necessary information to reactivate a health professional.
     * @return a string indicating the result of the reactivate request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    ReactivateRequestResponseTo reactivateRequest(ApplicationRequestTo applicationRequestTo) throws WorkFlowException, NmrException;

    /**
     * Service for fetching the reactivation records of the health professionals
     * for the NMC to approve or reject their request.
     *
     * @param pageNo   - Gives the current page number
     * @param offset   - Gives the number of records to be displayed
     * @param sortBy   -  According to which column the sort has to happen
     * @param sortType -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(String pageNo, String offset, String search, String value, String sortBy, String sortType) throws InvalidRequestException;

    /**
     * Retrieves information about the status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
     *
     * @param pageNo            - Gives the current page number
     * @param offset            - Gives the number of records to be displayed
     * @param sortBy            -  According to which column the sort has to happen
     * @param sortType          -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    HealthProfessionalApplicationResponseTo fetchApplicationDetails(String pageNo, String offset, String sortBy, String sortType, String search, String value, String smcId, String registrationNo) throws InvalidRequestException;

    /**
     * Retrieves information about a health professional's application requests to track by health professional.
     *
     * @param healthProfessionalId - the health professional id.
     * @param pageNo               - Gives the current page number
     * @param offset               - Gives the number of records to be displayed
     * @param sortBy               -  According to which column the sort has to happen
     * @param sortType             -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    HealthProfessionalApplicationResponseTo fetchApplicationDetailsForHealthProfessional(BigInteger healthProfessionalId, String pageNo, String offset, String sortBy, String sortType, String search, String value) throws InvalidRequestException;

    ApplicationDetailResponseTo fetchApplicationDetail(String requestId) throws InvalidRequestException;
}
