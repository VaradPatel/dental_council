package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;
import java.util.List;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IWorkFlowService {

    void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException, InvalidRequestException;

    boolean isAnyActiveWorkflowForHealthProfessional(BigInteger hpProfileId);

    boolean isAnyApprovedWorkflowForHealthProfessional(BigInteger hpProfileId);

    void assignQueriesBackToQueryCreator(String requestId, BigInteger hpProfileId) throws WorkFlowException;

    boolean isAnyActiveWorkflowWithOtherApplicationType(BigInteger hpProfileId, List<BigInteger> applicationTypeId);

    boolean isAnyActiveWorkflowExceptAdditionalQualification(BigInteger hpProfileId);
}
