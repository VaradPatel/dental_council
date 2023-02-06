package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IActionService;
import org.springframework.web.bind.annotation.*;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@RestController
@RequestMapping(ACTION_REQUEST_URL)
public class ActionController {

    private IActionService iActionService;

    public ActionController(IActionService iActionService) {
        this.iActionService = iActionService;
    }

    @PostMapping(SUSPENSION_REQUEST_URL)
    public String suspensionHealthProfessional(@RequestBody ActionRequestTo actionRequestTo) throws WorkFlowException {
        return iActionService.suspendRequest(actionRequestTo);
    }

    @PostMapping(REACTIVATE_REQUEST_URL)
    public String reactivateHealthProfessional(@RequestBody ActionRequestTo actionRequestTo) throws WorkFlowException {
        return iActionService.reactiveRequest(actionRequestTo);
    }

    /**
     * Endpoint for fetching the reactivation records of all the suspended health professionals
     * for the NMC to approve or reject their request.
     *
     * @param pageNo       - Gives the current page number
     * @param offset        - Gives the number of records to be displayed in a page
     * @param search       - Gives the search criteria like HP_Id, HP_name, Submitted_Date, Remarks
     * @param sortBy -  According to which column the sort has to happen
     * @param sortType    -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    @GetMapping(REACTIVATE_REQUEST_URL)
    public ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc(@RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo, @RequestParam(required = false, value = "offset", defaultValue = "2") String offset, @RequestParam(required = false, value = "search") String search, @RequestParam(required = false, value = "sortBy") String sortBy, @RequestParam(required = false, value = "sortType") String sortType) {
        return iActionService.getReactivationRecordsOfHealthProfessionalsToNmc(pageNo, offset, search, sortBy, sortType);
    }
}
