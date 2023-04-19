package in.gov.abdm.nmr.service.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @Autowired
    INotificationService notificationService;

    @Autowired
    private IWorkFlowRepository iWorkFlowRepository;

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void performPostWorkflowUpdates(WorkFlowRequestTO requestTO, HpProfile transactionHpProfile, INextGroup iNextGroup) {
        WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
        updateTransactionHealthProfessionalDetails(requestTO, iNextGroup, transactionHpProfile);

        HpProfileMaster masterHpProfileDetails = updateHpProfileToMaster(transactionHpProfile.getId(), transactionHpProfile.getRegistrationId());

        RegistrationDetailsMaster registrationMaster = updateRegistrationDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        updateAddressToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateForeignQualificationToMaster(transactionHpProfile, masterHpProfileDetails, registrationMaster);
        updateHpNbeDetailsToMaster(transactionHpProfile, masterHpProfileDetails);
        updateNmrHprLinkageToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        updateQualificationDetailsToMaster(transactionHpProfile, masterHpProfileDetails, registrationMaster);
        try {
            updateElasticDB(workFlow, masterHpProfileDetails);
        } catch (WorkFlowException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateTransactionHealthProfessionalDetails(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) {

        if (!ApplicationType.QUALIFICATION_ADDITION.getId().equals(requestTO.getApplicationTypeId())) {
            log.debug("The given Application type is Qualification Addition. ");
            log.debug("Setting the hp_profile_status as the work_flow_status retrieved from nmr_work_flow_configuration. ");
            hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
            if (hpProfile.getNmrId() == null) {
                log.debug("Generating NMR id as the hp_profile doesn't have an NMR id associated.");
                log.debug("Updating NMR id in hp_profile and user table");
                hpProfile.setNmrId(generateNmrId());
                User user = userRepository.findById(hpProfile.getUser().getId()).get();
                user.setNmrId(hpProfile.getNmrId());
                log.debug("Initiating a notification indicating the NMR creation");
                notificationService.sendNotificationForNMRCreation(user.getNmrId(),user.getMobileNumber(),user.getEmail());
            }
        }
        log.debug("Marking all the qualification details associated with the current request_id as verified.");
        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        qualificationDetails.forEach(qualificationDetail -> qualificationDetail.setIsVerified(1));

        List<ForeignQualificationDetails> foreignQualificationDetails = foreignQualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        foreignQualificationDetails.forEach(foreignQualificationDetail -> foreignQualificationDetail.setIsVerified(1));
    }

    private RegistrationDetailsMaster updateRegistrationDetailsToMaster(BigInteger transactionHpProfileId, HpProfileMaster hpProfileMaster) {
        log.debug("Mapping the current registration_details to registration_details_master table");
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

        log.debug("Mapping the current hp_profile to hp_profile_master table");
        HpProfileMaster masterHpProfileDetails = hpProfileMasterRepository.findByRegistrationId(hpRegistrationId);


        HpProfile transactionHpeProfileDetails = hpProfileRepository.findHpProfileById(transactionHpProfileId);
        HpProfileMaster masterHpProfileEntity = hpProfileMasterMapper.hpProfileToHpProfileMaster(transactionHpeProfileDetails);

        if (masterHpProfileDetails != null) {
            log.debug("Initiating updation flow since there is an existing hp_profile_master entry for the current registration id");
            masterHpProfileEntity.setId(masterHpProfileDetails.getId());
        }
        return hpProfileMasterRepository.save(masterHpProfileEntity);
    }

    private void updateAddressToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {

        log.debug("Mapping the current Address to Address Master table");

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

    private void updateForeignQualificationToMaster(HpProfile transactionHpProfile, HpProfileMaster masterHpProfile, RegistrationDetailsMaster registrationMaster) {
        log.debug("Mapping the Foreign Qualification Details to Foreign Qualification Details Master table");

        List<ForeignQualificationDetails> qualificationDetails = customQualificationDetailRepository.getQualificationDetailsByUserId(transactionHpProfile.getUser().getId());
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

    private void updateHpNbeDetailsToMaster(HpProfile transactionHpProfile, HpProfileMaster masterHpProfile) {
        log.debug("Mapping the current HP NBE Details to Hp Nbe Details Master table");
        HpNbeDetails hpNbeDetails = hpNbeDetailsRepository.findByHpProfileId(transactionHpProfile.getUser().getId());

        if (hpNbeDetails != null) {
            HpNbeDetailsMaster hpNbeDetailsMaster = iHpNbeMasterMapper.hpNbeToHpNbeMaster(hpNbeDetails);
            HpNbeDetailsMaster fetchedFromMaster = hpNbeDetailsMasterRepository.findByHpProfileId(masterHpProfile.getId());
            if (fetchedFromMaster != null) {
                hpNbeDetailsMaster.setId(fetchedFromMaster.getId());

            }
            hpNbeDetailsMaster.setHpProfile(masterHpProfile);
            hpNbeDetailsMasterRepository.save(hpNbeDetailsMaster);

        }
    }

    private void updateLanguagesKnownToMaster(BigInteger transactionUserId, BigInteger masterUserId) {

        List<LanguagesKnown> languagesKnown = languagesKnownRepository.findByUserId(transactionUserId);

        if (languagesKnown !=null && !languagesKnown.isEmpty()) {
            List<LanguagesKnownMaster> languagesKnownMasters = languagesKnownMasterMapper.languagesKnownToLanguagesKnownMaster(languagesKnown);

            List<LanguagesKnownMaster> fetchedFromMasters = languagesKnownMasterRepository.findByUserId(masterUserId);

            for (int i = 0; i < languagesKnownMasters.size(); i++) {
                if (!fetchedFromMasters.isEmpty() && fetchedFromMasters.get(i) != null) {
                    languagesKnownMasters.get(i).setId(fetchedFromMasters.get(i).getId());
                }
            }
            languagesKnownMasterRepository.saveAll(languagesKnownMasters);
        }
    }

    private void updateNmrHprLinkageToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {
        log.debug("Mapping the current NmrHprLinkage to NmrHprLinkage Master table");

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

    private void updateQualificationDetailsToMaster(HpProfile transactionHpProfile, HpProfileMaster masterHpProfile, RegistrationDetailsMaster registrationMaster) {
        log.debug("Mapping the current Qualification Details to Qualification Details Master table");

        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getQualificationDetailsByUserId(transactionHpProfile.getUser().getId());
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
    public void updateElasticDB(WorkFlow workFlow, HpProfileMaster hpProfileMaster) throws WorkFlowException {
        log.debug("Updating the Elastic DB");
        try {
            if (!NMRUtil.isVoluntarySuspension(workFlow)) {
                elasticsearchDaoService.indexHP(hpProfileMaster.getId());
            }
        } catch (ElasticsearchException | IOException e) {
            LOGGER.error("Exception while indexing HP", e);
            throw new WorkFlowException("Exception while indexing HP");
        }
    }


    @Override
    public String generateNmrId() {
        return String.valueOf(NMRUtil.generateRandom(12));
    }

}
