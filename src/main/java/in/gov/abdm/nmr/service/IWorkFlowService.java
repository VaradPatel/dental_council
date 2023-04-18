package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IWorkFlowService {

    void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException, InvalidRequestException;

    boolean isAnyActiveWorkflowForHealthProfessional(BigInteger hpProfileId);

    boolean isAnyApprovedWorkflowForHealthProfessional(BigInteger hpProfileId);

    void assignQueriesBackToQueryCreator(String requestId);

    boolean isAnyActiveWorkflowWithOtherApplicationType(BigInteger hpProfileId, BigInteger applicationTypeId);
}
