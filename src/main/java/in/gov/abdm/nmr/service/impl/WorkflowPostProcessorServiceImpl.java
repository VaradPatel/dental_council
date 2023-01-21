package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileAudit;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetailsAudit;
import in.gov.abdm.nmr.mapper.IHpProfileAuditMapper;
import in.gov.abdm.nmr.repository.IHpProfileAuditRepository;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.IRegistrationDetailRepository;
import in.gov.abdm.nmr.repository.RegistrationDetailAuditRepository;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class WorkflowPostProcessorServiceImpl implements IWorkflowPostProcessorService {

    @Autowired
    IHpProfileRepository hpProfileRepository;

    @Autowired
    IHpProfileAuditRepository hpProfileAuditRepository;

    @Autowired
    IHpProfileAuditMapper hpProfileAuditMapper;

    @Autowired
    IRegistrationDetailRepository registrationDetailRepository;

    @Autowired
    RegistrationDetailAuditRepository registrationDetailAuditRepository;

    @Override
    public void updateMasterRecord(BigInteger transactionHpProfileId, BigInteger hpRegistrationId) {
        HpProfileAudit masterHpProfileDetails = hpProfileAuditRepository.findByRegistrationId(hpRegistrationId);
        updateHpProfile(masterHpProfileDetails, transactionHpProfileId, hpRegistrationId);
        updateRegistrationDetails(masterHpProfileDetails.getId(), transactionHpProfileId);


    }

    @Override
    public void updateElasticDB() {

    }

    private void updateRegistrationDetails(BigInteger id, BigInteger transactionHpProfileId) {
        RegistrationDetails registrationDetailsByHpProfileId = registrationDetailRepository.getRegistrationDetailsByHpProfileId(transactionHpProfileId);
        RegistrationDetailsAudit registrationDetailsAudit = hpProfileAuditMapper.registrationDetailsToRegistrationDetailsAudit(registrationDetailsByHpProfileId);
        if(registrationDetailsByHpProfileId != null){
            registrationDetailsAudit.setId(registrationDetailsByHpProfileId.getId());
        }
        registrationDetailAuditRepository.save(registrationDetailsAudit);
    }


    private void updateHpProfile( HpProfileAudit masterHpProfileDetails,BigInteger transactionHpProfileId, BigInteger hpRegistrationId) {
        HpProfile transactionHpeProfileDetails = hpProfileRepository.findHpProfileById(transactionHpProfileId);
        HpProfileAudit hpProfileAudit = hpProfileAuditMapper.hpProfileToHpProfileAudit(transactionHpeProfileDetails);
        if(masterHpProfileDetails != null){
            hpProfileAudit.setId(masterHpProfileDetails.getId());
        }
        hpProfileAuditRepository.save(hpProfileAudit);
    }


}
