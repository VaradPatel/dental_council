package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.exception.WorkFlowException;

/**
 * An interface that deals with the reactivation and suspension requests
 * */
public interface IActionService {

    /**
     * This method is used to suspend a health professional based on the request provided.
     * @param actionRequestTo the request object containing necessary information to suspend a health professional.
     * @return a string indicating the result of the suspension request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    String suspendRequest(ActionRequestTo actionRequestTo) throws WorkFlowException;

    /**
     * This method is used to reactivate a health professional based on the request provided.
     * @param actionRequestTo the request object containing necessary information to reactivate a health professional.
     * @return a string indicating the result of the reactivate request.
     * @throws WorkFlowException if there is any error while processing the suspension request.
     */
    String reactiveRequest(ActionRequestTo actionRequestTo) throws WorkFlowException;

    /**
     * Service for fetching the reactivation records of the health professionals
     * for the NMC to approve or reject their request.
     *
     * @param pageNo       - Gives the current page number
     * @param offset        - Gives the number of records to be displayed
     * @param search       - Gives the search criteria like HP_Id, HP_name, Submiited_Date, Remarks
     * @param sortBy -  According to which column the sort has to happen
     * @param sortType    -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(String pageNo, String offset, String search, String sortBy, String sortType);
}
