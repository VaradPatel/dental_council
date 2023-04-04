package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.jpa.entity.HpProfile;
import in.gov.abdm.nmr.jpa.entity.HpProfileMaster;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;

public interface IWorkflowPostProcessorService {

    void performPostWorkflowUpdates(WorkFlowRequestTO requestTO,HpProfile transactionHpProfile, INextGroup iNextGroup);

    void updateElasticDB(INextGroup iNextGroup, HpProfileMaster hpProfile) throws WorkFlowException;

    String generateNmrId();

}
