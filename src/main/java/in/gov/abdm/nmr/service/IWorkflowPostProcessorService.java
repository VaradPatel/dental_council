package in.gov.abdm.nmr.service;

import java.math.BigInteger;

public interface IWorkflowPostProcessorService {

    void updateMasterRecord(BigInteger transactionHpProfileId, BigInteger hpRegistrationId);

    void updateElasticDB();

}
