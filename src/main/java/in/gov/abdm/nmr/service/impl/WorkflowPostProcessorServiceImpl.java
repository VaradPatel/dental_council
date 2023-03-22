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
import in.gov.abdm.nmr.util.NMRUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    IForeignQualificationDetailRepository foreignQualificationDetailRepository;

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

    @Autowired
    IUserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void performPostWorkflowUpdates(WorkFlowRequestTO requestTO, HpProfile transactionHpProfile, INextGroup iNextGroup) {

        updateTransactionHealthProfessionalDetails(requestTO, iNextGroup, transactionHpProfile);

        HpProfileMaster masterHpProfileDetails = updateHpProfileToMaster(transactionHpProfile.getId(), transactionHpProfile.getRegistrationId());

        RegistrationDetailsMaster registrationMaster = updateRegistrationDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        updateAddressToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateForeignQualificationToMaster(transactionHpProfile.getId(), masterHpProfileDetails, registrationMaster);
        updateHpNbeDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateNmrHprLinkageToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateQualificationDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails, registrationMaster);
        try {
            updateElasticDB(iNextGroup, masterHpProfileDetails);
        } catch (WorkFlowException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTransactionHealthProfessionalDetails(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) {

        if (!ApplicationType.QUALIFICATION_ADDITION.getId().equals(requestTO.getApplicationTypeId())) {
            hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
            if (hpProfile.getNmrId() == null) {
                hpProfile.setNmrId(generateNmrId());
                User user = userRepository.findById(hpProfile.getUser().getId()).get();
                user.setNmrId(hpProfile.getNmrId());
            }
        }
        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        qualificationDetails.forEach(qualificationDetail -> qualificationDetail.setIsVerified(1));

        List<ForeignQualificationDetails> foreignQualificationDetails = foreignQualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        foreignQualificationDetails.forEach(foreignQualificationDetail -> foreignQualificationDetail.setIsVerified(1));
    }

    private RegistrationDetailsMaster updateRegistrationDetailsToMaster(BigInteger transactionHpProfileId, HpProfileMaster hpProfileMaster) {

        RegistrationDetails registrationDetailsByHpProfileId = registrationDetailRepository.getRegistrationDetailsByHpProfileId(transactionHpProfileId);

        if (registrationDetailsByHpProfileId != null) {

            RegistrationDetailsMaster registrationDetailsAudit = hpProfileMasterMapper.registrationDetailsToRegistrationDetailsMaster(registrationDetailsByHpProfileId);
            RegistrationDetailsMaster fetchedFromMaster = registrationDetailAuditRepository.getRegistrationDetailsByHpProfileId(hpProfileMaster.getId());

            if (fetchedFromMaster != null) {
                registrationDetailsAudit.setId(fetchedFromMaster.getId());
            }
            registrationDetailsAudit.setHpProfileMaster(hpProfileMaster);
            return registrationDetailAuditRepository.save(registrationDetailsAudit);
        }
        return null;
    }

    private HpProfileMaster updateHpProfileToMaster(BigInteger transactionHpProfileId, String hpRegistrationId) {

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

    private void updateForeignQualificationToMaster(BigInteger transactionHpProfileId, HpProfileMaster masterHpProfile, RegistrationDetailsMaster registrationMaster) {
        List<ForeignQualificationDetails> qualificationDetails = customQualificationDetailRepository.getQualificationDetailsByHpProfileId(transactionHpProfileId);
        List<String> masterCourseIds = customQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(masterHpProfile.getId())
                .stream().map(qualificationDetailsMaster -> qualificationDetailsMaster.getCourse()).collect(Collectors.toList());
        List<ForeignQualificationDetails> filterQualifications = qualificationDetails.stream().filter(qualificationDetail -> !masterCourseIds.contains(qualificationDetail.getCourse())).toList();
        if (!filterQualifications.isEmpty()) {
            List<ForeignQualificationDetailsMaster> qualificationDetailsMasters = customQualificationDetailsMasterMapper.qualificationToQualificationMaster(filterQualifications);
            qualificationDetailsMasters.forEach(qualificationDetailsMaster -> {
                qualificationDetailsMaster.setHpProfileMaster(masterHpProfile);
                qualificationDetailsMaster.setRegistrationDetails(registrationMaster);
            });
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
                if (!fetchedFromMasters.isEmpty() && fetchedFromMasters.get(i) != null) {
                    languagesKnownMasters.get(i).setId(fetchedFromMasters.get(i).getId());
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

    private void updateQualificationDetailsToMaster(BigInteger transactionHpProfileId, HpProfileMaster masterHpProfile, RegistrationDetailsMaster registrationMaster) {
        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getQualificationDetailsByHpProfileId(transactionHpProfileId);
        List<BigInteger> masterCourseIds = qualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(masterHpProfile.getId())
                .stream().map(qualificationDetailsMaster -> qualificationDetailsMaster.getCourse().getId()).collect(Collectors.toList());
        List<QualificationDetails> filterQualifications = qualificationDetails.stream().filter(qualificationDetail -> !masterCourseIds.contains(qualificationDetail.getCourse().getId())).toList();
        if (!filterQualifications.isEmpty()) {
            List<QualificationDetailsMaster> qualificationDetailsMasters = qualificationDetailMasterMapper.qualificationDetailsToQualificationDetailsMaster(filterQualifications);
            qualificationDetailsMasters.forEach(qualificationDetailsMaster -> {
                qualificationDetailsMaster.setHpProfileMaster(masterHpProfile);
                qualificationDetailsMaster.setRegistrationDetails(registrationMaster);
            });
            qualificationDetailMasterRepository.saveAll(qualificationDetailsMasters);
        }
    }

    private void updateSuperSpecialityToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {

        List<SuperSpeciality> superSpecialities = superSpecialityRepository.getSuperSpecialityFromHpProfileId(transactionHpProfileId);

        if (!superSpecialities.isEmpty()) {
            List<SuperSpecialityMaster> superSpecialityMasters = superSpecialityMasterMapper.superSpecialityToSuperSpecialityMaster(superSpecialities);
            List<SuperSpecialityMaster> fetchedFromMasters = superSpecialityMasterRepository.getSuperSpecialityFromHpProfileId(masterHpProfileId);

            for (int i = 0; i < superSpecialityMasters.size(); i++) {
                if (!fetchedFromMasters.isEmpty() && fetchedFromMasters.get(i) != null) {
                    superSpecialityMasters.get(i).setId(fetchedFromMasters.get(i).getId());
                }
                superSpecialityMasters.get(i).setHpProfileId(masterHpProfileId);
            }
            superSpecialityMasterRepository.saveAll(superSpecialityMasters);
        }
    }

    private void updateWorkflowToMaster(BigInteger transactionHpProfileId, HpProfileMaster hpProfileMaster) {
        List<WorkProfileMaster> workProfileMasterList = new ArrayList<>();
        List<WorkProfile> workProfiles = workProfileRepository.getWorkProfileDetailsByHPId(transactionHpProfileId);
        workProfiles.forEach(workProfile -> {
            if (workProfile != null) {
                WorkProfileMaster workProfileAudit = workProfileMasterMapper.workProfileToWorkProfileMaster(workProfile);
                WorkProfileMaster fetchedFromMaster = workProfileAuditRepository.getWorkProfileByHpProfileId(hpProfileMaster.getId());

                if (fetchedFromMaster != null) {
                    workProfileAudit.setId(fetchedFromMaster.getId());
                }
                workProfileAudit.setHpProfileId(hpProfileMaster.getId());
                workProfileMasterList.add(workProfileAudit);
            }
        });
        workProfileAuditRepository.saveAll(workProfileMasterList);
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
    public String generateNmrId() {
        return String.valueOf(NMRUtil.generateRandom(12));
    }

}
