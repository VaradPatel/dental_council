package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserGroup;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IStatusWiseCount;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
public class FetchCountOnCardServiceImpl implements IFetchCountOnCardService {

    /**
     * Injecting IFetchCountOnCardRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchCountOnCardRepository iFetchCountOnCardRepository;

    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    /**
     * Mapper Interface to transform the StatusWiseCount Bean
     * to the StatusWiseCountTO Bean transferring its contents
     */
    @Autowired
    private IStatusWiseCountMapper iStatusWiseCountMapper;

    @Autowired
    private IAccessControlService accessControlService;

    @Autowired
    private ICollegeDeanRepository iCollegeDeanRepository;

    @Autowired
    private ICollegeRegistrarRepository iCollegeRegistrarRepository;

    @Autowired
    private ISmcProfileRepository iSmcProfileRepository;

    private BigInteger counter=BigInteger.ZERO;

    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard() throws InvalidRequestException {
        User loggedInUser=accessControlService.getLoggedInUser();

        /**
         * Data retrieval - HP Registration
         */
        List<String> filteredStatusListForRegistration=new ArrayList<>();
        List<StatusWiseCountTO> hpRegistrationRequests= fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_REGISTRATION.getId(), loggedInUser)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    filteredStatusListForRegistration.add(statusWiseCount.getName());
                    StatusWiseCountTO statusWiseCountTO=iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                    statusWiseCountTO.setApplicationType(ApplicationType.HP_REGISTRATION.getDescription());
                    return statusWiseCountTO;
                })
                .collect(Collectors.toList());

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
            if(!filteredStatusListForRegistration.contains(workflowStatus.getDescription())){
                hpRegistrationRequests.add(StatusWiseCountTO.builder()
                        .name(workflowStatus.getDescription())
                        .count(BigInteger.ZERO)
                        .applicationType(ApplicationType.HP_REGISTRATION.getDescription())
                        .build());
            }
        });

        hpRegistrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_HP_REGISTRATION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - HP Modification
         */
        List<String> filteredStatusListForModification=new ArrayList<>();
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> hpModificationRequests= fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_MODIFICATION.getId(), loggedInUser)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    filteredStatusListForModification.add(statusWiseCount.getName());
                    StatusWiseCountTO statusWiseCountTO=iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                    statusWiseCountTO.setApplicationType(ApplicationType.HP_MODIFICATION.getDescription());
                    return statusWiseCountTO;
                })
                .collect(Collectors.toList());

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
            if(!filteredStatusListForModification.contains(workflowStatus.getDescription())){
                hpModificationRequests.add(StatusWiseCountTO.builder()
                        .name(workflowStatus.getDescription())
                        .count(BigInteger.ZERO)
                        .applicationType(ApplicationType.HP_MODIFICATION.getDescription())
                        .build());
            }
        });

        hpModificationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_HP_MODIFICATION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - Temporary Suspension
         */
        List<String> filteredStatusListForTemporarySuspension=new ArrayList<>();
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> temporarySuspensionRequests= fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), loggedInUser)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    filteredStatusListForTemporarySuspension.add(statusWiseCount.getName());
                    StatusWiseCountTO statusWiseCountTO=iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                    statusWiseCountTO.setApplicationType(ApplicationType.HP_TEMPORARY_SUSPENSION.getDescription());
                    return statusWiseCountTO;
                })
                .collect(Collectors.toList());

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
            if(!filteredStatusListForTemporarySuspension.contains(workflowStatus.getDescription())){
                temporarySuspensionRequests.add(StatusWiseCountTO.builder()
                        .name(workflowStatus.getDescription())
                        .count(BigInteger.ZERO)
                        .applicationType(ApplicationType.HP_TEMPORARY_SUSPENSION.getDescription())
                        .build());
            }
        });

        temporarySuspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_TEMPORARY_SUSPENSION_REQUESTS)
                .count(counter)
                .build());

        BigInteger tempCount=counter;

        /**
         * Data retrieval - Permanent Suspension
         */
        List<String> filteredStatusListForPermanentSuspension=new ArrayList<>();
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> permanentSuspensionRequests= fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), loggedInUser)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    filteredStatusListForPermanentSuspension.add(statusWiseCount.getName());
                    StatusWiseCountTO statusWiseCountTO=iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                    statusWiseCountTO.setApplicationType(ApplicationType.HP_PERMANENT_SUSPENSION.getDescription());
                    return statusWiseCountTO;
                })
                .collect(Collectors.toList());

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
            if(!filteredStatusListForPermanentSuspension.contains(workflowStatus.getDescription())){
                permanentSuspensionRequests.add(StatusWiseCountTO.builder()
                        .name(workflowStatus.getDescription())
                        .count(BigInteger.ZERO)
                        .applicationType(ApplicationType.HP_PERMANENT_SUSPENSION.getDescription())
                        .build());
            }
        });

        permanentSuspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_PERMANENT_SUSPENSION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Consolidated Suspension Requests
         */
        List<StatusWiseCountTO> consolidatedSuspensionRequests=new ArrayList<>();

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus ->
            consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                    .name(workflowStatus.getDescription())
                    .build())
        );

        consolidatedSuspensionRequests.forEach(statusWiseCountTO -> {
            BigInteger tempSuspensionRequestCount=temporarySuspensionRequests.stream()
                    .filter(statusWiseCountTO1 -> statusWiseCountTO1.getName().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .get()
                    .getCount();
            BigInteger permanentSuspensionRequestCount=permanentSuspensionRequests.stream()
                    .filter(statusWiseCountTO1 -> statusWiseCountTO1.getName().equals(statusWiseCountTO.getName()))
                    .findFirst()
                    .get()
                    .getCount();
            statusWiseCountTO.setCount(tempSuspensionRequestCount.add(permanentSuspensionRequestCount));
        });

        consolidatedSuspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_CONSOLIDATED_SUSPENSION_REQUESTS)
                .count(counter.add(tempCount))
                .build());

        /**
         * Data retrieval - Activate License
         */
        List<String> filteredStatusListForLicenseRequests=new ArrayList<>();
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> activateLicenseRequests= fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.HP_ACTIVATE_LICENSE.getId(), loggedInUser)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    filteredStatusListForLicenseRequests.add(statusWiseCount.getName());
                    StatusWiseCountTO statusWiseCountTO=iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                    statusWiseCountTO.setApplicationType(ApplicationType.HP_ACTIVATE_LICENSE.getDescription());
                    return statusWiseCountTO;
                })
                .collect(Collectors.toList());

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
            if(!filteredStatusListForLicenseRequests.contains(workflowStatus.getDescription())){
                activateLicenseRequests.add(StatusWiseCountTO.builder()
                        .name(workflowStatus.getDescription())
                        .count(BigInteger.ZERO)
                        .applicationType(ApplicationType.HP_ACTIVATE_LICENSE.getDescription())
                        .build());
            }
        });

        activateLicenseRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_ACTIVATE_LICENSE_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - College Registration
         */
        List<String> filteredStatusListForCollegeRegRequests=new ArrayList<>();
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> collegeRegistrationRequests= fetchStatusWiseCountBasedOnLoggedInUser(ApplicationType.COLLEGE_REGISTRATION.getId(), loggedInUser)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    filteredStatusListForCollegeRegRequests.add(statusWiseCount.getName());
                    StatusWiseCountTO statusWiseCountTO=iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                    statusWiseCountTO.setApplicationType(ApplicationType.COLLEGE_REGISTRATION.getDescription());
                    return statusWiseCountTO;
                })
                .collect(Collectors.toList());

        Arrays.stream(WorkflowStatus.values()).forEach(workflowStatus -> {
            if(!filteredStatusListForCollegeRegRequests.contains(workflowStatus.getDescription())){
                collegeRegistrationRequests.add(StatusWiseCountTO.builder()
                        .name(workflowStatus.getDescription())
                        .count(BigInteger.ZERO)
                        .applicationType(ApplicationType.COLLEGE_REGISTRATION.getDescription())
                        .build());
            }
        });

        collegeRegistrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_COLLEGE_REGISTRATION_REQUESTS)
                .count(counter)
                .build());

        return FetchCountOnCardResponseTO.builder()
                .hpRegistrationRequests(hpRegistrationRequests)
                .hpModificationRequests(hpModificationRequests)
                .temporarySuspensionRequests(temporarySuspensionRequests)
                .permanentSuspensionRequests(permanentSuspensionRequests)
                .consolidatedSuspensionRequests(consolidatedSuspensionRequests)
                .activateLicenseRequests(activateLicenseRequests)
                .collegeRegistrationRequests(collegeRegistrationRequests)
                .build();
    }

    private List<IStatusWiseCount> fetchStatusWiseCountBasedOnLoggedInUser(BigInteger applicationTypeId, User loggedInUser) throws InvalidRequestException {

        UserGroup group=loggedInUser.getGroup();
        BigInteger groupId=group.getId();
        /**
         * Group - Health Professional
         */
        if (Group.HEALTH_PROFESSIONAL.getDescription().equals(group.getName())){
            BigInteger hpProfileId=iHpProfileRepository.getHpProfileIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForHp(applicationTypeId, groupId, hpProfileId);
        }

        /**
         * Group - College Dean
         */
        if (Group.COLLEGE_DEAN.getDescription().equals(group.getName())){
            BigInteger collegeDeanId=iCollegeDeanRepository.getCollegeDeanIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForDean(applicationTypeId, groupId, collegeDeanId);
        }

        /**
         * Group - College Registrar
         */
        if (Group.COLLEGE_REGISTRAR.getDescription().equals(group.getName())){
            BigInteger collegeRegistrarId=iCollegeRegistrarRepository.getCollegeRegistrarIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForRegistrar(applicationTypeId, groupId, collegeRegistrarId);
        }

        /**
         * Group - State Medical council
         */
        if (Group.SMC.getDescription().equals(group.getName())){
            BigInteger smcProfileId=iSmcProfileRepository.getSmcIdByUserId(loggedInUser.getId()).get(0);
            return iFetchCountOnCardRepository.fetchUserSpecificStatusWiseCountForSmc(applicationTypeId, groupId, smcProfileId);
        }

        /**
         * Group - National Medical council or NBE
         */
        if (Group.NMC.getDescription().equals(group.getName()) || Group.NBE.getDescription().equals(group.getName())) {
            return iFetchCountOnCardRepository.fetchStatusWiseCount(applicationTypeId,groupId);
        }
        throw new InvalidRequestException(INVALID_GROUP);

    }

}
