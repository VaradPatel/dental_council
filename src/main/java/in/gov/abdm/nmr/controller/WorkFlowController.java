package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.NMRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Presentation Layer to expose the Action endpoints
 */
@RestController
@RequestMapping(ACTION_REQUEST_URL)
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
    @PostMapping(INITIATE_WORK_FLOW_URL)
    public ResponseEntity<String> initiateWorkFlow(@RequestBody WorkFlowRequestTO requestTO) throws WorkFlowException {
    public ResponseEntity<String> initiateWorkFlow(@RequestBody WorkFlowRequestTO requestTO) throws InvalidRequestException, WorkFlowException {

        if(requestTO.getRequestId() == null || REQUEST_ID_CREATION_STATUSES.contains(requestTO.getProfileStatus())){
            requestTO.setRequestId(NMRUtil.buildRequestIdForWorkflow(requestCounterService.incrementAndRetrieveCount(requestTO.getApplicationTypeId())));
        }
        iWorkFlowService.initiateSubmissionWorkFlow(requestTO);
        return ResponseEntity.ok(SUCCESS);
    }

    /**
     * This endpoint can be accessed to initiate workflow
     * @return
     */
    @PostMapping(INITIATE_COLLEGE_WORK_FLOW_URL)
    public ResponseEntity<String> initiateCollegeWorkFlow(@RequestBody WorkFlowRequestTO requestTO) throws InvalidRequestException, WorkFlowException {
        iWorkFlowService.initiateCollegeRegistrationWorkFlow(requestTO.getRequestId(),requestTO.getApplicationTypeId(),requestTO.getActorId(),requestTO.getActionId());
        return ResponseEntity.ok("Success");
    }

}
