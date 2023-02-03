package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Presentation Layer to expose the Action endpoints
 */
@RestController
public class WorkFlowController {

    private static final List<BigInteger> REQUEST_ID_CREATION_STATUSES = List.of(HpProfileStatus.REJECTED.getId(),
            HpProfileStatus.APPROVED.getId(), HpProfileStatus.SUSPENDED.getId(), HpProfileStatus.BLACKLISTED.getId());

    /**
     * Injecting a IWorkFlowService bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowService iWorkFlowService;
    @Autowired
    private IRequestCounterService requestCounterService;

    /**
     * This endpoint can be accessed to initiate workflow
     * @return
     */
    @PostMapping(HEALTH_PROFESSIONAL_ACTION)
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
    @PostMapping(COLLEGES_ACTION)
    public ResponseEntity<ResponseMessageTo> executeActionOnCollege(@RequestBody WorkFlowRequestTO requestTO) throws InvalidRequestException, WorkFlowException {
        iWorkFlowService.initiateCollegeRegistrationWorkFlow(requestTO.getRequestId(),requestTO.getApplicationTypeId(),requestTO.getActorId(),requestTO.getActionId());
        return ResponseEntity.ok(ResponseMessageTo.builder().message(SUCCESS_RESPONSE).build());
    }
}
