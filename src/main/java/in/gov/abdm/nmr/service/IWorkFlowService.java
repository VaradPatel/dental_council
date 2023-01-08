package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.exception.WorkFlowException;

import java.math.BigInteger;

/**
 * Service Layer to implement the actual Business Logic
 */
public interface IWorkFlowService {

    void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException;

    void initiateCollegeRegistrationWorkFlow(String requestId, BigInteger applicationTypeId, BigInteger actorId, BigInteger actionId) throws WorkFlowException;

    boolean isAnyActiveWorkflowForHealthProfessional(BigInteger hpProfileId);

}
