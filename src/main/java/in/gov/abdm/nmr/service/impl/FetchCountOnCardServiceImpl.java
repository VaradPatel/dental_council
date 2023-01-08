package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.repository.IFetchCountOnCardRepository;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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

    /**
     * Mapper Interface to transform the StatusWiseCount Bean
     * to the StatusWiseCountTO Bean transferring its contents
     */
    @Autowired
    private IStatusWiseCountMapper iStatusWiseCountMapper;

    private BigInteger counter=BigInteger.ZERO;

    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard(String groupName) throws InvalidRequestException {
        validateGroupName(groupName);
        return fetchCountOnCardByGroupId(fetchGroupIdByGroupName(groupName));
    }

    private FetchCountOnCardResponseTO fetchCountOnCardByGroupId(BigInteger groupId){

        /**
         * Data retrieval - HP Registration
         */
        List<StatusWiseCountTO> hpRegistrationRequests= iFetchCountOnCardRepository.fetchStatusWiseCountByGroupAndApplicationType(ApplicationType.HP_REGISTRATION.getId(), groupId)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    return iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                })
                .collect(Collectors.toList());

        hpRegistrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_HP_REGISTRATION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - HP Modification
         */
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> hpModificationRequests= iFetchCountOnCardRepository.fetchStatusWiseCountByGroupAndApplicationType(ApplicationType.HP_MODIFICATION.getId(), groupId)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    return iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                })
                .collect(Collectors.toList());

        hpModificationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_HP_MODIFICATION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - Temporary Suspension
         */
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> temporarySuspensionRequests= iFetchCountOnCardRepository.fetchStatusWiseCountByGroupAndApplicationType(ApplicationType.HP_TEMPORARY_SUSPENSION.getId(), groupId)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    return iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                })
                .collect(Collectors.toList());

        temporarySuspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_TEMPORARY_SUSPENSION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - Permanent Suspension
         */
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> permanentSuspensionRequests= iFetchCountOnCardRepository.fetchStatusWiseCountByGroupAndApplicationType(ApplicationType.HP_PERMANENT_SUSPENSION.getId(), groupId)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    return iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                })
                .collect(Collectors.toList());

        permanentSuspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_PERMANENT_SUSPENSION_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - Activate License
         */
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> activateLicenseRequests= iFetchCountOnCardRepository.fetchStatusWiseCountByGroupAndApplicationType(ApplicationType.HP_ACTIVATE_LICENSE.getId(), groupId)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    return iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                })
                .collect(Collectors.toList());

        activateLicenseRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_ACTIVATE_LICENSE_REQUESTS)
                .count(counter)
                .build());

        /**
         * Data retrieval - College Registration
         */
        counter=BigInteger.ZERO;
        List<StatusWiseCountTO> collegeRegistrationRequests= iFetchCountOnCardRepository.fetchStatusWiseCountByGroupAndApplicationType(ApplicationType.COLLEGE_REGISTRATION.getId(), groupId)
                .stream()
                .map(statusWiseCount-> {
                    counter=counter.add(statusWiseCount.getCount());
                    return iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount);
                })
                .collect(Collectors.toList());

        collegeRegistrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_COLLEGE_REGISTRATION_REQUESTS)
                .count(counter)
                .build());

        return FetchCountOnCardResponseTO.builder()
                .hpRegistrationRequests(hpRegistrationRequests)
                .hpModificationRequests(hpModificationRequests)
                .temporarySuspensionRequests(temporarySuspensionRequests)
                .permanentSuspensionRequests(permanentSuspensionRequests)
                .activateLicenseRequests(activateLicenseRequests)
                .collegeRegistrationRequests(collegeRegistrationRequests)
                .build();
    }

    private void validateGroupName(String groupName) throws InvalidRequestException {
       if(groupName==null || Arrays.stream(Group.values()).noneMatch(t -> t.getDescription().equals(groupName))){
            throw new InvalidRequestException(INVALID_GROUP);
        }
    }

    private BigInteger fetchGroupIdByGroupName(String groupName) {
        return Arrays.stream(Group.values())
                .filter(t->t.getDescription().equals(groupName))
                .findAny()
                .get()
                .getId();
    }
}
