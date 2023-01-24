package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;

import java.math.BigInteger;

public interface IWorkflowPostProcessorService {

    void performPostWorkflowUpdates(WorkFlowRequestTO requestTO,HpProfile transactionHpProfile, INextGroup iNextGroup);

    void updateElasticDB(INextGroup iNextGroup, HpProfileMaster hpProfile) throws WorkFlowException;

    void generateNmrId();

}
