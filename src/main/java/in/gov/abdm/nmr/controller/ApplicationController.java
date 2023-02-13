package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IApplicationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_HEALTH_PROFESSIONAL_APPLICATIONS;
import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Presentation Layer to expose the endpoints of Health Professional Suspend and Reactivate API
 */
@RestController
public class ApplicationController {

    @Autowired
    private IApplicationService applicationService;

    @Autowired
    private IWorkFlowService iWorkFlowService;
    @Autowired
    private IRequestCounterService requestCounterService;

    private static final List<BigInteger> REQUEST_ID_CREATION_STATUSES = List.of(HpProfileStatus.REJECTED.getId(),
            HpProfileStatus.APPROVED.getId(), HpProfileStatus.SUSPENDED.getId(), HpProfileStatus.BLACKLISTED.getId());

    /**
     * This API endpoint is used to Suspend a health professional.
     * @param applicationRequestTo - An ActionRequestTo object that contains information about the request to Suspend a health professional.
     * @return String representation of the result of suspension request
     * @throws WorkFlowException If an error occurs during the suspension request process
     * POST endpoint for suspension request of a health professional. This method invokes the IActionService#suspendRequest(ActionRequestTo)
     * to perform the suspension request and return the result of the process.
     */
    @PostMapping(ProtectedPaths.SUSPENSION_REQUEST_URL)
    public String suspensionHealthProfessional(@RequestBody ApplicationRequestTo applicationRequestTo) throws WorkFlowException {
        return applicationService.suspendRequest(applicationRequestTo);
    }

    /**
     * This API endpoint is used to reactivate a health professional.
     * @param applicationRequestTo - An ActionRequestTo object that contains information about the request to reactivate a health professional.
     * @return a string indicating the status of the reactivation process.
     * @throws WorkFlowException If an error occurs during the reactivate request process
     * POST endpoint for reactivate request of a health professional. This method invokes the IActionService#reactiveRequest(ActionRequestTo)
     * to perform the reactivate request and return the result of the process.
     */
    @PostMapping(REACTIVATE_REQUEST_URL)
    public String reactivateHealthProfessional(@RequestBody ApplicationRequestTo applicationRequestTo) throws WorkFlowException {
        return applicationService.reactiveRequest(applicationRequestTo);
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
        return applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(pageNo, offset, search, sortBy, sortType);
    }

    /**
     * Endpoint for retrieving information about a health professional's application requests to track by health professional.
     *
     * @param requestTO - HealthProfessionalApplicationRequestTo object passed as a request body
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @PostMapping(PATH_HEALTH_PROFESSIONAL_APPLICATIONS)
    public HealthProfessionalApplicationResponseTo trackApplicationDetails(@PathVariable("healthProfessionalId") BigInteger healthProfessionalId, @RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return applicationService.fetchApplicationDetailsForHealthProfessional(healthProfessionalId, requestTO);
    }

    /**
     * Endpoint for retrieving information about the track status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
     *
     * @param requestTO - HealthProfessionalApplicationRequestTo object passed as a request body
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @PostMapping(PATH_TRACK_APPLICATIONS_STATUS)
    public HealthProfessionalApplicationResponseTo trackStatusDetails(@RequestBody HealthProfessionalApplicationRequestTo requestTO) {
        return applicationService.fetchApplicationDetails(requestTO);
    }

    /**
     * This endpoint can be accessed to initiate workflow
     * @return
     */
    @PatchMapping(HEALTH_PROFESSIONAL_ACTION)
    public ResponseEntity<ResponseMessageTo> executeActionOnHealthProfessional(@RequestBody WorkFlowRequestTO requestTO) throws InvalidRequestException, WorkFlowException {
        if(iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(requestTO.getHpProfileId(), requestTO.getApplicationTypeId())) {
            if (requestTO.getRequestId() == null || !iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(requestTO.getHpProfileId())) {
                requestTO.setRequestId(NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(requestTO.getApplicationTypeId())));
            }
            iWorkFlowService.initiateSubmissionWorkFlow(requestTO);
            return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());

        }
        throw new WorkFlowException("Cant create new request until an existing request is closed.", HttpStatus.BAD_REQUEST);
    }

    /**
     * This endpoint can be accessed to initiate workflow
     * @return
     */
    @PatchMapping(COLLEGES_ACTION)
    public ResponseEntity<ResponseMessageTo> executeActionOnCollege(@RequestBody WorkFlowRequestTO requestTO) throws InvalidRequestException, WorkFlowException {
        iWorkFlowService.initiateCollegeRegistrationWorkFlow(requestTO.getRequestId(),requestTO.getApplicationTypeId(),requestTO.getActorId(),requestTO.getActionId());
        return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());
    }
}
