package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;

public interface IWorkflowPostProcessorService {

    void performPostWorkflowUpdates(WorkFlowRequestTO requestTO,HpProfile transactionHpProfile, INextGroup iNextGroup) throws WorkFlowException;

    void updateElasticDB(WorkFlow workFlow, HpProfileMaster hpProfile) throws WorkFlowException;

    String generateNmrId();

}
