package in.gov.abdm.nmr.service.impl;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import feign.FeignException;
import in.gov.abdm.nmr.client.HPRFClient;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.AddressType;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.IHPRRegisterProfessionalService;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
@Slf4j
public class WorkflowPostProcessorServiceImpl implements IWorkflowPostProcessorService {

    @Autowired
    IHpProfileRepository hpProfileRepository;

    @Autowired
    IHpProfileMasterRepository hpProfileMasterRepository;

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
    HpNbeDetailsMasterRepository hpNbeDetailsMasterRepository;
    @Autowired
    INmrHprLinkageRepository nmrHprLinkageRepository;

    @Autowired
    INmrHprLinkageMasterRepository nmrHprLinkageMasterRepository;

    @Autowired
    IQualificationDetailRepository qualificationDetailRepository;

    @Autowired
    IForeignQualificationDetailRepository foreignQualificationDetailRepository;

    @Autowired
    IQualificationDetailMasterRepository qualificationDetailMasterRepository;
    @Autowired
    IElasticsearchDaoService elasticsearchDaoService;

    @Autowired
    IHpProfileStatusRepository hpProfileStatusRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    HPRFClient hprClient;

    @Autowired
    INotificationService notificationService;
    @Autowired
    IHPRRegisterProfessionalService ihprRegisterProfessionalService;

    @Autowired
    IWorkFlowRepository iWorkFlowRepository;

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void performPostWorkflowUpdates(WorkFlowRequestTO requestTO, HpProfile transactionHpProfile, INextGroup iNextGroup) throws WorkFlowException {
        WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
        updateTransactionHealthProfessionalDetails(requestTO, iNextGroup, transactionHpProfile);

        HpProfileMaster masterHpProfileDetails = updateHpProfileToMaster(transactionHpProfile.getId(), transactionHpProfile.getRegistrationId());

        RegistrationDetailsMaster registrationMaster = updateRegistrationDetailsToMaster(transactionHpProfile.getId(), masterHpProfileDetails);
        AddressMaster addressMaster = updateAddressToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        List<ForeignQualificationDetailsMaster> foreignQualificationDetailsMasterList = updateForeignQualificationToMaster(transactionHpProfile, masterHpProfileDetails, registrationMaster);
        updateNmrHprLinkageToMaster(transactionHpProfile.getId(), masterHpProfileDetails.getId());
        List<QualificationDetailsMaster> qualificationDetailsMasterList = updateQualificationDetailsToMaster(transactionHpProfile, masterHpProfileDetails, registrationMaster);
        updateElasticDB(workFlow, masterHpProfileDetails);

        WorkFlow workFlowDetailsByHpProfile = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
        if ((in.gov.abdm.nmr.enums.HpProfileStatus.APPROVED.getId().equals(workFlowDetailsByHpProfile.getWorkFlowStatus().getId())) &&
                ((ApplicationType.HP_REGISTRATION.getId().equals((workFlowDetailsByHpProfile.getApplicationType().getId()))) ||
                        (ApplicationType.FOREIGN_HP_REGISTRATION.getId().equals(workFlowDetailsByHpProfile.getApplicationType().getId())))
                && (masterHpProfileDetails.getConsent() != null && masterHpProfileDetails.getConsent() == 1)) {
            HPRRequestTo hprRequestTo = ihprRegisterProfessionalService.createRequestPayloadForHPRProfileCreation(transactionHpProfile, masterHpProfileDetails, registrationMaster, addressMaster, qualificationDetailsMasterList, foreignQualificationDetailsMasterList);
            try {
                hprClient.registerHealthProfessional(hprRequestTo.getAuthorization(), hprRequestTo.getPractitionerRequestTO());
                log.info(HPR_REGISTER_SUCCESS);
            } catch (FeignException.UnprocessableEntity e) {
                log.error(HPR_REGISTER_MISSING_VALUES + e.getMessage());
            } catch (Exception e) {
                log.error(HPR_REGISTER_FAILED + e.getMessage());
            }
        }
    }

    private void updateTransactionHealthProfessionalDetails(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) throws WorkFlowException {

        if (!ApplicationType.QUALIFICATION_ADDITION.getId().equals(requestTO.getApplicationTypeId())) {
            hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).orElseThrow(WorkFlowException::new));
            if (StringUtils.isEmpty(hpProfile.getNmrId())) {
                log.info("Generating NMR id as the hp_profile doesn't have an NMR id associated.");
                log.info("Updating NMR id in hp_profile and user table");
                hpProfile.setNmrId(generateNmrId());
                User user = userRepository.findById(hpProfile.getUser().getId()).orElseThrow(WorkFlowException::new);
                user.setNmrId(hpProfile.getNmrId());
                log.info("Initiating a notification indicating the NMR creation");
                try {
                    notificationService.sendNotificationForNMRCreation(user.getNmrId(), user.getMobileNumber());
                    if (hpProfile.getConsent().toString().equals("1")) {
                        notificationService.sendNotificationForHprAccountCreation(user.getUserName(), user.getHprId(), user.getMobileNumber());
                    }
                } catch (Exception exception) {
                    log.debug("error occurred while sending notification:" + exception.getLocalizedMessage());
                }
            }
        }
        log.debug("Marking all the qualification details associated with the current request_id as verified.");
        QualificationDetails qualificationDetails = qualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        if(qualificationDetails!=null) {
            qualificationDetails.setIsVerified(QUALIFICATION_STATUS_APPROVED);
        }
        ForeignQualificationDetails foreignQualificationDetails = foreignQualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        if(foreignQualificationDetails!=null) {
            foreignQualificationDetails.setIsVerified(QUALIFICATION_STATUS_APPROVED);
        }
    }

    private RegistrationDetailsMaster updateRegistrationDetailsToMaster(BigInteger transactionHpProfileId, HpProfileMaster hpProfileMaster) {
        log.debug("Mapping the current registration_details to registration_details_master table");
        RegistrationDetailsMaster registrationDetailsAudit = new RegistrationDetailsMaster();
        RegistrationDetails registrationDetailsByHpProfileId = registrationDetailRepository.getRegistrationDetailsByHpProfileId(transactionHpProfileId);

        if (registrationDetailsByHpProfileId != null) {
            registrationDetailsAudit = IHpProfileMasterMapper.HP_PROFILE_MASTER_MAPPER.registrationDetailsToRegistrationDetailsMaster(registrationDetailsByHpProfileId);
            RegistrationDetailsMaster fetchedFromMaster = registrationDetailAuditRepository.getRegistrationDetailsByHpProfileId(hpProfileMaster.getId());

            if (fetchedFromMaster != null) {
                registrationDetailsAudit.setId(fetchedFromMaster.getId());
            }
            registrationDetailsAudit.setHpProfileMaster(hpProfileMaster);
            registrationDetailsAudit = registrationDetailAuditRepository.save(registrationDetailsAudit);
        }
        return registrationDetailsAudit;
    }

    private HpProfileMaster updateHpProfileToMaster(BigInteger transactionHpProfileId, String hpRegistrationId) {

        log.debug("Mapping the current hp_profile to hp_profile_master table");
        HpProfileMaster masterHpProfileDetails = hpProfileMasterRepository.findByRegistrationId(hpRegistrationId);


        HpProfile transactionHpeProfileDetails = hpProfileRepository.findHpProfileById(transactionHpProfileId);
        HpProfileMaster masterHpProfileEntity = IHpProfileMasterMapper.HP_PROFILE_MASTER_MAPPER.hpProfileToHpProfileMaster(transactionHpeProfileDetails);

        if (masterHpProfileDetails != null) {
            log.debug("Initiating updation flow since there is an existing hp_profile_master entry for the current registration id");
            masterHpProfileEntity.setId(masterHpProfileDetails.getId());
        }
        return hpProfileMasterRepository.save(masterHpProfileEntity);
    }

    private AddressMaster updateAddressToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {
        AddressMaster addressMasters = new AddressMaster();

        log.debug("Mapping the current Address to Address Master table");

        Address address = addressRepository.getCommunicationAddressByHpProfileId(transactionHpProfileId, AddressType.COMMUNICATION.getId());

        if (address != null) {
            addressMasters = IAddressMasterMapper.ADDRESS_MASTER_MAPPER.addressToAddressMaster(address);
            AddressMaster fetchedFromMasters = addressMasterRepository.getCommunicationAddressByHpProfileId(masterHpProfileId, AddressType.COMMUNICATION.getId());

            if (fetchedFromMasters != null) {
                addressMasters.setId(fetchedFromMasters.getId());
            }
            addressMasters.setHpProfileId(masterHpProfileId);
            addressMasters = addressMasterRepository.save(addressMasters);
        }
        return addressMasters;
    }

    private List<ForeignQualificationDetailsMaster> updateForeignQualificationToMaster(HpProfile transactionHpProfile, HpProfileMaster masterHpProfile, RegistrationDetailsMaster registrationMaster) {
        log.debug("Mapping the Foreign Qualification Details to Foreign Qualification Details Master table");
        List<ForeignQualificationDetailsMaster> qualificationDetailsMasters = new ArrayList<>();
        List<ForeignQualificationDetails> qualificationDetails = customQualificationDetailRepository.getApprovedQualificationDetailsByUserId(transactionHpProfile.getUser().getId());
        List<String> masterCourseIds = customQualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(masterHpProfile.getId())
                .stream().map(ForeignQualificationDetailsMaster::getCourse).toList();
        List<ForeignQualificationDetails> filterQualifications = qualificationDetails.stream().filter(qualificationDetail -> !masterCourseIds.contains(qualificationDetail.getCourse())).toList();
        if (!filterQualifications.isEmpty()) {
            qualificationDetailsMasters = IForeignQualificationDetailsMasterMapper.FOREIGN_QUALIFICATION_DETAILS_MASTER_MAPPER.qualificationToQualificationMaster(filterQualifications);
            qualificationDetailsMasters.forEach(qualificationDetailsMaster -> {
                qualificationDetailsMaster.setHpProfileMaster(masterHpProfile);
                qualificationDetailsMaster.setRegistrationDetails(registrationMaster);
            });
            qualificationDetailsMasters = customQualificationDetailMasterRepository.saveAll(qualificationDetailsMasters);
        }
        return qualificationDetailsMasters;
    }

    private void updateHpNbeDetailsToMaster(HpProfile transactionHpProfile, HpProfileMaster masterHpProfile) {
        log.debug("Mapping the current HP NBE Details to Hp Nbe Details Master table");
        HpNbeDetails hpNbeDetails = hpNbeDetailsRepository.findByHpProfileId(transactionHpProfile.getUser().getId());

        if (hpNbeDetails != null) {
            HpNbeDetailsMaster hpNbeDetailsMaster = IHpNbeMasterMapper.HP_NBE_MASTER_MAPPER.hpNbeToHpNbeMaster(hpNbeDetails);
            HpNbeDetailsMaster fetchedFromMaster = hpNbeDetailsMasterRepository.findByHpProfileId(masterHpProfile.getId());
            if (fetchedFromMaster != null) {
                hpNbeDetailsMaster.setId(fetchedFromMaster.getId());

            }
            hpNbeDetailsMaster.setHpProfile(masterHpProfile);
            hpNbeDetailsMasterRepository.save(hpNbeDetailsMaster);

        }
    }

    private void updateNmrHprLinkageToMaster(BigInteger transactionHpProfileId, BigInteger masterHpProfileId) {
        log.debug("Mapping the current NmrHprLinkage to NmrHprLinkage Master table");

        NmrHprLinkage nmrHprLinkage = nmrHprLinkageRepository.findByHpProfileId(transactionHpProfileId);

        if (nmrHprLinkage != null) {
            NmrHprLinkageMaster nmrHprLinkageToMaster = INmrHprLinkageMasterMapper.HPR_LINKAGE_MASTER_MAPPER.nmrHprLinkageToNmrHprLinkageMaster(nmrHprLinkage);
            NmrHprLinkageMaster fetchedFromMaster = nmrHprLinkageMasterRepository.findByHpProfileId(masterHpProfileId);
            if (fetchedFromMaster != null) {
                nmrHprLinkageToMaster.setId(fetchedFromMaster.getId());
            }
            nmrHprLinkage.setHpProfileId(masterHpProfileId);
            nmrHprLinkageMasterRepository.save(nmrHprLinkageToMaster);
        }
    }


    private List<QualificationDetailsMaster> updateQualificationDetailsToMaster(HpProfile transactionHpProfile, HpProfileMaster masterHpProfile, RegistrationDetailsMaster registrationMaster) {
        log.debug("Mapping the current Qualification Details to Qualification Details Master table");
        List<QualificationDetailsMaster> qualificationDetailsMasters = new ArrayList<>();
        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.getApprovedQualificationDetailsByUserId(transactionHpProfile.getUser().getId());
        List<BigInteger> masterCourseIds = qualificationDetailMasterRepository.getQualificationDetailsByHpProfileId(masterHpProfile.getId())
                .stream().map(qualificationDetailsMaster -> qualificationDetailsMaster.getCourse().getId()).toList();
        List<QualificationDetails> filterQualifications = qualificationDetails.stream().filter(qualificationDetail -> !masterCourseIds.contains(qualificationDetail.getCourse().getId())).toList();
        if (!filterQualifications.isEmpty()) {
            qualificationDetailsMasters = IQualificationDetailMasterMapper.QUALIFICATION_DETAIL_MASTER_MAPPER.qualificationDetailsToQualificationDetailsMaster(filterQualifications);
            qualificationDetailsMasters.forEach(qualificationDetailsMaster -> {
                qualificationDetailsMaster.setHpProfileMaster(masterHpProfile);
                qualificationDetailsMaster.setRegistrationDetails(registrationMaster);
            });
            qualificationDetailsMasters = qualificationDetailMasterRepository.saveAll(qualificationDetailsMasters);
        }
        return qualificationDetailsMasters;
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
            throw new WorkFlowException(NMRError.NMR_EXCEPTION.getCode(), NMRError.NMR_EXCEPTION.getMessage());
        }
    }


    @Override
    public String generateNmrId() {
        return String.valueOf(NMRUtil.generateRandom(12));
    }
}
