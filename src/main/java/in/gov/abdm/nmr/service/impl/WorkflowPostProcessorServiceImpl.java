package in.gov.abdm.nmr.service.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class WorkflowPostProcessorServiceImpl implements IWorkflowPostProcessorService {

    @Autowired
    IHpProfileRepository hpProfileRepository;

    @Autowired
    IHpProfileMasterRepository hpProfileMasterRepository;

    @Autowired
    IHpProfileMasterMapper hpProfileMasterMapper;

    @Autowired
    IAddressMasterMapper addressMasterMapper;

    @Autowired
    IForeignQualificationDetailsMasterMapper customQualificationDetailsMasterMapper;

    @Autowired
    IRegistrationDetailRepository registrationDetailRepository;

    @Autowired
    RegistrationDetailMasterRepository registrationDetailAuditRepository;

    @Autowired
    IAddressRepository addressRepository;

    @Autowired
    IAddressMasterRepository addressMasterRepository;

    @Autowired
    IForeignQualificationDetailRepository customQualificationDetailRepository;

    @Autowired
    IForeignQualificationDetailMasterRepository customQualificationDetailMasterRepository;

    @Autowired
    HpNbeDetailsRepository hpNbeDetailsRepository;

    @Autowired
    IHpNbeMasterMapper iHpNbeMasterMapper;

    @Autowired
    HpNbeDetailsMasterRepository hpNbeDetailsMasterRepository;

    @Autowired
    LanguagesKnownRepository languagesKnownRepository;

    @Autowired
    LanguagesKnownMasterRepository languagesKnownMasterRepository;

    @Autowired
    ILanguagesKnownMasterMapper languagesKnownMasterMapper;

    @Autowired
    INmrHprLinkageRepository nmrHprLinkageRepository;

    @Autowired
    INmrHprLinkageMasterRepository nmrHprLinkageMasterRepository;

    @Autowired
    INmrHprLinkageMasterMapper nmrHprLinkageMasterMapper;

    @Autowired
    IQualificationDetailRepository qualificationDetailRepository;

    @Autowired
    IQualificationDetailMasterRepository qualificationDetailMasterRepository;

    @Autowired
    IQualificationDetailMasterMapper qualificationDetailMasterMapper;

    @Autowired
    SuperSpecialityRepository superSpecialityRepository;

    @Autowired
    SuperSpecialityMasterRepository superSpecialityMasterRepository;

    @Autowired
    ISuperSpecialityMasterMapper superSpecialityMasterMapper;

    @Autowired
    WorkProfileMasterRepository workProfileAuditRepository;

    @Autowired
    WorkProfileRepository workProfileRepository;

    @Autowired
    IWorkProfileMasterMapper workProfileMasterMapper;

    @Autowired
    private IElasticsearchDaoService elasticsearchDaoService;

    @Autowired
    IHpProfileStatusRepository hpProfileStatusRepository;

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void performPostWorkflowUpdates(WorkFlowRequestTO requestTO, HpProfile transactionHpProfile, INextGroup iNextGroup) {

        updateTransactionHealthProfessionalDetails(requestTO, iNextGroup, transactionHpProfile);

        HpProfileMaster masterHpProfileDetails = updateHpProfileToMaster(transactionHpProfile.getId(), transactionHpProfile.getRegistrationId());

        updateRegistrationDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        updateAddressToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateForeignQualificationToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        updateHpNbeDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateLanguagesKnownToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        updateNmrHprLinkageToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateQualificationDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        updateSuperSpecialityToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateWorkflowToMaster(transactionHpProfile.getId(), masterHpProfileDetails);

        try {
            updateElasticDB(iNextGroup, masterHpProfileDetails);
        } catch (WorkFlowException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTransactionHealthProfessionalDetails(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) {

        if (!ApplicationType.QUALIFICATION_ADDITION.getId().equals(requestTO.getApplicationTypeId())) {
            hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
        }
        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        qualificationDetails.forEach(qualificationDetail -> qualificationDetail.setIsVerified(1));
    }

    private void updateRegistrationDetailsToMaster(BigInteger transactionHpProfileId, HpProfileMaster hpProfileMaster) {

        RegistrationDetails registrationDetailsByHpProfileId = registrationDetailRepository.getRegistrationDetailsByHpProfileId(transactionHpProfileId);

        if (registrationDetailsByHpProfileId != null) {

            RegistrationDetailsMaster registrationDetailsAudit = hpProfileMasterMapper.registrationDetailsToRegistrationDetailsMaster(registrationDetailsByHpProfileId);
            RegistrationDetailsMaster fetchedFromMaster = registrationDetailAuditRepository.getRegistrationDetailsByHpProfileId(hpProfileMaster.getId());

            if (fetchedFromMaster != null) {
                registrationDetailsAudit.setId(fetchedFromMaster.getId());
            }
            registrationDetailsAudit.setHpProfileMaster(hpProfileMaster);
            registrationDetailAuditRepository.save(registrationDetailsAudit);
        }
    }

    private HpProfileMaster updateHpProfileToMaster(BigInteger transactionHpProfileId, BigInteger hpRegistrationId) {

        HpProfileMaster masterHpProfileDetails = hpProfileMasterRepository.findByRegistrationId(hpRegistrationId);


        HpProfile transactionHpeProfileDetails = hpProfileRepository.findHpProfileById(transactionHpProfileId);
        HpProfileMaster masterHpProfileEntity = hpProfileMasterMapper.hpProfileToHpProfileMaster(transactionHpeProfileDetails);

        if (masterHpProfileDetails != null) {
            masterHpProfileEntity.setId(masterHpProfileDetails.getId());
        }
        return hpProfileMasterRepository.save(masterHpProfileEntity);
    }

    private void updateAddressToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {

        Address address = addressRepository.getCommunicationAddressByHpProfileId(transactionHpProfileId, AddressType.COMMUNICATION.getId());

        if (address != null) {
            AddressMaster addressMasters = addressMasterMapper.addressToAddressMaster(address);
            AddressMaster fetchedFromMasters = addressMasterRepository.getCommunicationAddressByHpProfileId(masterHpProfileId, AddressType.COMMUNICATION.getId());

            if (fetchedFromMasters != null) {
                addressMasters.setId(fetchedFromMasters.getId());
            }
            addressMasters.setHpProfileId(masterHpProfileId);
            addressMasterRepository.save(addressMasters);
        }
    }

    private void updateForeignQualificationToMaster(BigInteger transactionHpProfileId, HpProfileMaster masterHpProfile) {

        List<ForeignQualificationDetails> qualificationDetails = customQualificationDetailRepository.getQualificationDetailsByHpProfileId(transactionHpProfileId);

        if (!qualificationDetails.isEmpty()) {
            List<ForeignQualificationDetailsMaster> qualificationDetailsMasters = customQualificationDetailsMasterMapper.qualificationToQualificationMaster(qualificationDetails);
            List<ForeignQualificationDetailsMaster> fetchedFromMasters = customQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(masterHpProfile.getId());

            for (int i = 0; i < qualificationDetailsMasters.size(); i++) {
                if (!fetchedFromMasters.isEmpty()) {
                    if (fetchedFromMasters.get(i) != null) {
                        qualificationDetailsMasters.get(i).setId(fetchedFromMasters.get(i).getId());
                    }
                }
                qualificationDetailsMasters.get(i).setHpProfileMaster(masterHpProfile);
            }
            customQualificationDetailMasterRepository.saveAll(qualificationDetailsMasters);
        }
    }

    private void updateHpNbeDetailsToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {

        HpNbeDetails hpNbeDetails = hpNbeDetailsRepository.findByHpProfileId(transactionHpProfileId);

        if (hpNbeDetails != null) {
            HpNbeDetailsMaster hpNbeDetailsMaster = iHpNbeMasterMapper.hpNbeToHpNbeMaster(hpNbeDetails);
            HpNbeDetailsMaster fetchedFromMaster = hpNbeDetailsMasterRepository.findByHpProfileId(masterHpProfileId);
            if (fetchedFromMaster != null) {
                hpNbeDetailsMaster.setId(fetchedFromMaster.getId());

            }
            hpNbeDetailsMaster.setHpProfileId(masterHpProfileId);
            hpNbeDetailsMasterRepository.save(hpNbeDetailsMaster);

        }
    }

    private void updateLanguagesKnownToMaster(BigInteger transactionHpProfileId, HpProfileMaster masterHpProfile) {

        List<LanguagesKnown> languagesKnown = languagesKnownRepository.getLanguagesKnownByHpProfileId(transactionHpProfileId);

        if (!languagesKnown.isEmpty()) {
            List<LanguagesKnownMaster> languagesKnownMasters = languagesKnownMasterMapper.languagesKnownToLanguagesKnownMaster(languagesKnown);
            List<LanguagesKnownMaster> fetchedFromMasters = languagesKnownMasterRepository.getLanguagesKnownByHpProfileId(masterHpProfile.getId());

            for (int i = 0; i < languagesKnownMasters.size(); i++) {
                if (!fetchedFromMasters.isEmpty()) {
                    if (fetchedFromMasters.get(i) != null) {
                        languagesKnownMasters.get(i).setId(fetchedFromMasters.get(i).getId());
                    }
                }
                languagesKnownMasters.get(i).setHpProfileMaster(masterHpProfile);
            }
            languagesKnownMasterRepository.saveAll(languagesKnownMasters);
        }
    }

    private void updateNmrHprLinkageToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {

        NmrHprLinkage nmrHprLinkage = nmrHprLinkageRepository.findByHpProfileId(transactionHpProfileId);

        if (nmrHprLinkage != null) {
            NmrHprLinkageMaster nmrHprLinkageToMaster = nmrHprLinkageMasterMapper.nmrHprLinkageToNmrHprLinkageMaster(nmrHprLinkage);
            NmrHprLinkageMaster fetchedFromMaster = nmrHprLinkageMasterRepository.findByHpProfileId(masterHpProfileId);
            if (fetchedFromMaster != null) {
                nmrHprLinkageToMaster.setId(fetchedFromMaster.getId());
            }
            nmrHprLinkage.setHpProfileId(masterHpProfileId);
            nmrHprLinkageMasterRepository.save(nmrHprLinkageToMaster);
        }
    }

    private void updateQualificationDetailsToMaster(BigInteger transactionHpProfileId, HpProfileMaster masterHpProfile) {

        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getQualificationDetailsByHpProfileId(transactionHpProfileId);

        if (!qualificationDetails.isEmpty()) {
            List<QualificationDetailsMaster> qualificationDetailsMasters = qualificationDetailMasterMapper.qualificationDetailsToQualificationDetailsMaster(qualificationDetails);
            List<QualificationDetailsMaster> fetchedFromMasters = qualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(masterHpProfile.getId());

            for (int i = 0; i < qualificationDetailsMasters.size(); i++) {
                if (!fetchedFromMasters.isEmpty()) {
                    if (fetchedFromMasters.get(i) != null) {
                        qualificationDetailsMasters.get(i).setId(fetchedFromMasters.get(i).getId());
                    }
                }
                qualificationDetailsMasters.get(i).setHpProfileMaster(masterHpProfile);
            }
            qualificationDetailMasterRepository.saveAll(qualificationDetailsMasters);
        }
    }

    private void updateSuperSpecialityToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {

        List<SuperSpeciality> superSpecialities = superSpecialityRepository.getSuperSpecialityFromHpProfileId(transactionHpProfileId);

        if (!superSpecialities.isEmpty()) {
            List<SuperSpecialityMaster> superSpecialityMasters = superSpecialityMasterMapper.superSpecialityToSuperSpecialityMaster(superSpecialities);
            List<SuperSpecialityMaster> fetchedFromMasters = superSpecialityMasterRepository.getSuperSpecialityFromHpProfileId(masterHpProfileId);

            for (int i = 0; i < superSpecialityMasters.size(); i++) {
                if (!fetchedFromMasters.isEmpty()) {
                    if (fetchedFromMasters.get(i) != null) {
                        superSpecialityMasters.get(i).setId(fetchedFromMasters.get(i).getId());
                    }
                }
                superSpecialityMasters.get(i).setHpProfileId(masterHpProfileId);
            }
            superSpecialityMasterRepository.saveAll(superSpecialityMasters);
        }
    }

    private void updateWorkflowToMaster(BigInteger transactionHpProfileId, HpProfileMaster hpProfileMaster) {

        WorkProfile workProfile = workProfileRepository.getWorkProfileByHpProfileId(transactionHpProfileId);

        if (workProfile != null) {
            WorkProfileMaster workProfileAudit = workProfileMasterMapper.workProfileToWorkProfileMaster(workProfile);
            WorkProfileMaster fetchedFromMaster = workProfileAuditRepository.getWorkProfileByHpProfileId(hpProfileMaster.getId());

            if (fetchedFromMaster != null) {
                workProfileAudit.setId(fetchedFromMaster.getId());
            }
            workProfileAudit.setHpProfileId(hpProfileMaster.getId());
            workProfileAuditRepository.save(workProfileAudit);

        }
    }

    @Override
    public void updateElasticDB(INextGroup iNextGroup, HpProfileMaster hpProfileMaster) throws WorkFlowException {

        try {
            elasticsearchDaoService.indexHP(hpProfileMaster.getId());
        } catch (ElasticsearchException | IOException e) {
            LOGGER.error("Exception while indexing HP", e);
            throw new WorkFlowException("Exception while indexing HP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void generateNmrId() {

    }
}
