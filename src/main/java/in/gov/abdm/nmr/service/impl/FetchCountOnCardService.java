package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IStatusWiseCountMapper;
import in.gov.abdm.nmr.repository.IFetchCountOnCardRepository;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;


@Service
public class FetchCountOnCardService implements IFetchCountOnCardService {

    /**
     * Injecting a Dashboard Repository bean instead of an explicit object creation to achieve
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

    @Override
    public FetchCountOnCardResponseTO fetchCountOnCard(String userType, String userSubType) throws InvalidRequestException {

        if(userSubType!=null){
            validateUserSubType(userSubType);
            return fetchCountOnCardByUserTypeAndSubType(userType, userSubType);
        }
        validateUserType(userType);
        return fetchCountOnCardByUserType(userType);

    }

    private FetchCountOnCardResponseTO fetchCountOnCardByUserTypeAndSubType(String userType, String userSubType){

        /**
         * Data retrieval - Registration
         */
        List<StatusWiseCountTO> registrationRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(REGISTRATION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(REGISTRATION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Updation
         */
        List<StatusWiseCountTO> updationRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(UPDATION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(UPDATION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Suspension
         */
        List<StatusWiseCountTO> suspensionRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(SUSPENSION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        suspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_SUSPENSION_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(SUSPENSION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Black-List
         */
        List<StatusWiseCountTO> blackListRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(BLACK_LIST, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        blackListRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_BLACK_LIST_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(BLACK_LIST, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Voluntary Retirement
         */
        List<StatusWiseCountTO> voluntaryRetirementRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(VOLUNTARY_RETIREMENT, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        voluntaryRetirementRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_VOLUNTARY_RETIREMENT_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(VOLUNTARY_RETIREMENT, userType, userSubType).size()))
                .build());

        return FetchCountOnCardResponseTO.builder()
                .registrationRequests(registrationRequests)
                .updationRequests(updationRequests)
                .suspensionRequests(suspensionRequests)
                .blackListRequests(blackListRequests)
                .voluntaryRetirementRequests(voluntaryRetirementRequests)
                .build();
    }

    private FetchCountOnCardResponseTO fetchCountOnCardByUserType(String userType){

        /**
         * Data retrieval - Registration
         */
        List<StatusWiseCountTO> registrationRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(REGISTRATION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserType(REGISTRATION, userType).size()))
                .build());

        /**
         * Data retrieval - Updation
         */
        List<StatusWiseCountTO> updationRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(UPDATION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserType(UPDATION, userType).size()))
                .build());

        /**
         * Data retrieval - Suspension
         */
        List<StatusWiseCountTO> suspensionRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(SUSPENSION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        suspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_SUSPENSION_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserType(SUSPENSION, userType).size()))
                .build());

        /**
         * Data retrieval - Black-List
         */
        List<StatusWiseCountTO> blackListRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(BLACK_LIST, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        blackListRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_BLACK_LIST_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserType(BLACK_LIST, userType).size()))
                .build());

        /**
         * Data retrieval - Voluntary Retirement
         */
        List<StatusWiseCountTO> voluntaryRetirementRequests= iFetchCountOnCardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(VOLUNTARY_RETIREMENT, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        voluntaryRetirementRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_VOLUNTARY_RETIREMENT_REQUESTS)
                .count(BigInteger.valueOf(iFetchCountOnCardRepository.fetchTotalCountByAppStatusAndUserType(VOLUNTARY_RETIREMENT, userType).size()))
                .build());

        return FetchCountOnCardResponseTO.builder()
                .registrationRequests(registrationRequests)
                .updationRequests(updationRequests)
                .suspensionRequests(suspensionRequests)
                .blackListRequests(blackListRequests)
                .voluntaryRetirementRequests(voluntaryRetirementRequests)
                .build();
    }

    private void validateUserSubType(String userSubType) throws InvalidRequestException {
        if(Arrays.stream(UserSubTypeEnum.values()).noneMatch(t -> t.getName().equals(userSubType))){
            throw new InvalidRequestException(INVALID_USER_SUB_TYPE);
        }
    }

    private void validateUserType(String userType) throws InvalidRequestException {
       if(userType==null || Arrays.stream(UserTypeEnum.values()).noneMatch(t -> t.getName().equals(userType))){
            throw new InvalidRequestException(INVALID_USER_TYPE);
        }
    }
}
