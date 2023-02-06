package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.exception.WorkFlowException;
/**
 * An interface that deals with the reactivation and suspension requests
 * */
public interface IActionService {

    String suspendRequest(ActionRequestTo actionRequestTo) throws WorkFlowException;

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
