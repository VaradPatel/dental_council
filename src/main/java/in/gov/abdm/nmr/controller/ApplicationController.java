package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IApplicationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.security.common.ProtectedPaths.PATH_HEALTH_PROFESSIONAL_APPLICATIONS;
import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Presentation Layer to expose the endpoints of Health Professional Suspend and Reactivate API
 */
@RestController
@Slf4j
public class ApplicationController {

    @Autowired
    private IApplicationService applicationService;

    @Autowired
    private IWorkFlowService iWorkFlowService;
    @Autowired
    private IRequestCounterService requestCounterService;

    /**
     * This API endpoint is used to Suspend a health professional.
     *
     * @param applicationRequestTo - An ActionRequestTo object that contains information about the request to Suspend a health professional.
     * @return String representation of the result of suspension request
     * @throws WorkFlowException If an error occurs during the suspension request process
     *                           POST endpoint for suspension request of a health professional. This method invokes the IActionService#suspendRequest(ActionRequestTo)
     *                           to perform the suspension request and return the result of the process.
     */
    @PostMapping(ProtectedPaths.SUSPENSION_REQUEST_URL)
    public SuspendRequestResponseTo suspendHealthProfessional(@Valid @RequestBody ApplicationRequestTo applicationRequestTo) throws WorkFlowException, NmrException, InvalidRequestException {
        if(iWorkFlowService.isAnyActiveWorkflowExceptAdditionalQualification(applicationRequestTo.getHpProfileId())){
            throw new WorkFlowException(NMRError.WORK_FLOW_CREATION_FAIL.getCode(), "A suspension request is already in progress.");
        }
        log.info("In Application Controller: suspensionHealthProfessional method ");
        log.debug("Request Payload: ApplicationRequestTo: ");
        log.debug(applicationRequestTo.toString());

        SuspendRequestResponseTo suspendRequestResponseTo = applicationService.suspendRequest(applicationRequestTo);

        log.info("Application Controller: suspensionHealthProfessional method: Execution Successful. ");
        log.debug("Response Payload: SuspendRequestResponseTo: ");
        log.debug(suspendRequestResponseTo.toString());

        return suspendRequestResponseTo;


    }

    /**
     * This API endpoint is used to reactivate a health professional.
     *
     * @param applicationRequestTo - An ActionRequestTo object that contains information about the request to reactivate a health professional.
     * @return a string indicating the status of the reactivation process.
     * @throws WorkFlowException If an error occurs during the reactivate request process
     *                           POST endpoint for reactivate request of a health professional. This method invokes the IActionService#reactiveRequest(ActionRequestTo)
     *                           to perform the reactivate request and return the result of the process.
     */
    @PostMapping(path=ProtectedPaths.REACTIVATE_REQUEST_URL,consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReactivateRequestResponseTo reactivateHealthProfessional(@RequestParam(value = "reactivationFile", required = false) MultipartFile reactivationFile,
                                                                    @RequestPart("data") @Valid ApplicationRequestTo applicationRequestTo) throws WorkFlowException, NmrException, InvalidRequestException, IOException {

        log.info("In Application Controller: reactivateHealthProfessional method ");
        log.debug("Request Payload: ApplicationRequestTo: ");
        log.debug(applicationRequestTo.toString());

        ReactivateRequestResponseTo reactivateRequestResponseTo = applicationService.reactivateRequest(reactivationFile, applicationRequestTo);

        log.info("Application Controller: reactivateHealthProfessional method: Execution Successful. ");
        log.debug("Response Payload: ReactivateRequestResponseTo: ");
        log.debug(reactivateRequestResponseTo.toString());

        return reactivateRequestResponseTo;

    }

    /**
     * Endpoint for fetching the reactivation records of all the suspended health professionals
     * for the NMC to approve or reject their request.
     *
     * @param pageNo   - Gives the current page number
     * @param offset   - Gives the number of records to be displayed in a page
     * @param sortBy   -  According to which column the sort has to happen
     * @param sortType -  Sorting order ASC or DESC
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    @GetMapping(ProtectedPaths.REACTIVATE_REQUEST_URL)
    public ReactivateHealthProfessionalResponseTO reactivationRecordsOfHealthProfessionalsToNmc(@RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo,
                                                                                                @RequestParam(required = false, value = "offset", defaultValue = "10") String offset,
                                                                                                @RequestParam(required = false, value = "search") String search,
                                                                                                @RequestParam(required = false, value = "value") String value,
                                                                                                @RequestParam(required = false, value = "sortBy") String sortBy,
                                                                                                @RequestParam(required = false, value = "sortType") String sortType) throws InvalidRequestException {
        return applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(pageNo, offset, search, value, sortBy, sortType);
    }

    /**
     * Endpoint for retrieving information about a health professional's application requests to track by health professional.
     *
     * @param pageNo            - Gives the current page number
     * @param offset            - Gives the number of records to be displayed
     * @param sortBy            -  According to which column the sort has to happen
     * @param sortType          -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @GetMapping(PATH_HEALTH_PROFESSIONAL_APPLICATIONS)
    public HealthProfessionalApplicationResponseTo trackApplicationDetails(@PathVariable("healthProfessionalId") BigInteger healthProfessionalId,
                                                                           @RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo,
                                                                           @RequestParam(required = false, value = "offset", defaultValue = "10") String offset,
                                                                           @RequestParam(required = false, value = "sortBy") String sortBy,
                                                                           @RequestParam(required = false, value = "sortOrder") String sortType,
                                                                           @RequestParam(required = false, value = "search") String search,
                                                                           @RequestParam(required = false, value = "value") String value) throws InvalidRequestException {
        return applicationService.fetchApplicationDetailsForHealthProfessional(healthProfessionalId, pageNo, offset, sortBy, sortType, search, value);
    }

    /**
     * Endpoint for retrieving information about the track status of a health professional's requests for NMC, NBE, SMC, Dean, Registrar and Admin.
     *
     * @param pageNo            - Gives the current page number
     * @param offset            - Gives the number of records to be displayed
     * @param sortBy            -  According to which column the sort has to happen
     * @param sortType          -  Sorting order ASC or DESC
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @GetMapping(APPLICATION_REQUEST_URL)
    public HealthProfessionalApplicationResponseTo trackStatusDetails(@RequestParam(required = false, value = "pageNo", defaultValue = "1") String pageNo,
                                                                      @RequestParam(required = false, value = "offset", defaultValue = "10") String offset,
                                                                      @RequestParam(required = false, value = "sortBy") String sortBy,
                                                                      @RequestParam(required = false, value = "sortOrder") String sortType,
                                                                      @RequestParam(required = false, value = "search") String search,
                                                                      @RequestParam(required = false, value = "value") String value,
                                                                      @RequestParam(required = false, value = "smcId") String smcId,
                                                                      @RequestParam(required = false, value = "registrationNo") String registrationNumber) throws InvalidRequestException {
        return applicationService.fetchApplicationDetails(NMRPagination.builder().pageNo(Integer.valueOf(pageNo)).offset(Integer.valueOf(offset)).sortBy(sortBy).sortType(sortType).build(), search, value, smcId, registrationNumber);
    }

    /**
     * This endpoint can be accessed to initiate workflow
     *
     * @return
     */
    @PatchMapping(HEALTH_PROFESSIONAL_ACTION)
    public ResponseEntity<ResponseMessageTo> executeActionOnHealthProfessional(@Valid @RequestBody WorkFlowRequestTO requestTO) throws WorkFlowException, InvalidRequestException {
        if (iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(requestTO.getHpProfileId(), List.of(requestTO.getApplicationTypeId(),ApplicationType.ADDITIONAL_QUALIFICATION.getId()))) {
            if (requestTO.getRequestId() == null || !iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(requestTO.getHpProfileId())) {
                requestTO.setRequestId(NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(requestTO.getApplicationTypeId())));
            }
            iWorkFlowService.initiateSubmissionWorkFlow(requestTO);
            return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());

        }
        throw new WorkFlowException(NMRError.WORK_FLOW_CREATION_FAIL.getCode(),NMRError.WORK_FLOW_CREATION_FAIL.getMessage());
    }
    /**
     * Retrieves the details of a specific application.
     *
     * @param requestId the unique identifier for the application request
     * @return an {@link ApplicationDetailResponseTo} object containing the details of the application
     */
    @GetMapping(APPLICATION_DETAILS)
    public ApplicationDetailResponseTo applicationDetail(@PathVariable(name = "requestId") String requestId) throws InvalidRequestException {
        return applicationService.fetchApplicationDetail(requestId);
    }

}
