package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.util.NMRConstants.BLACK_LIST;
import static in.gov.abdm.nmr.util.NMRConstants.INVALID_USER_SUB_TYPE;
import static in.gov.abdm.nmr.util.NMRConstants.INVALID_USER_TYPE;
import static in.gov.abdm.nmr.util.NMRConstants.REGISTRATION;
import static in.gov.abdm.nmr.util.NMRConstants.SUSPENSION;
import static in.gov.abdm.nmr.util.NMRConstants.TOTAL_BLACK_LIST_REQUESTS;
import static in.gov.abdm.nmr.util.NMRConstants.TOTAL_REGISTRATION_REQUESTS;
import static in.gov.abdm.nmr.util.NMRConstants.TOTAL_SUSPENSION_REQUESTS;
import static in.gov.abdm.nmr.util.NMRConstants.TOTAL_UPDATION_REQUESTS;
import static in.gov.abdm.nmr.util.NMRConstants.TOTAL_VOLUNTARY_RETIREMENT_REQUESTS;
import static in.gov.abdm.nmr.util.NMRConstants.UPDATION;
import static in.gov.abdm.nmr.util.NMRConstants.VOLUNTARY_RETIREMENT;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import in.gov.abdm.nmr.repository.IDashboardRepository;
import in.gov.abdm.nmr.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.IStatusWiseCountMapper;
import in.gov.abdm.nmr.dto.ResponseTO;
import in.gov.abdm.nmr.dto.StatusWiseCountTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;


@Service
public class DashboardServiceImpl implements IDashboardService {

    /**
     * Injecting a Dashboard Repository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IDashboardRepository iDashboardRepository;

    /**
     * Mapper Interface to transform the StatusWiseCount Bean
     * to the StatusWiseCountTO Bean transferring its contents
     */
    @Autowired
    private IStatusWiseCountMapper iStatusWiseCountMapper;

    @Override
    public ResponseTO fetchCountOnCard(String userType,String userSubType) throws InvalidRequestException {

        if(userSubType!=null){
            validateUserSubType(userSubType);
            return fetchCountOnCardByUserTypeAndSubType(userType, userSubType);
        }
        validateUserType(userType);
        return fetchCountOnCardByUserType(userType);

    }

    private ResponseTO fetchCountOnCardByUserTypeAndSubType(String userType, String userSubType){

        /**
         * Data retrieval - Registration
         */
        List<StatusWiseCountTO> registrationRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(REGISTRATION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(REGISTRATION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Updation
         */
        List<StatusWiseCountTO> updationRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(UPDATION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(UPDATION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Suspension
         */
        List<StatusWiseCountTO> suspensionRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(SUSPENSION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        suspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_SUSPENSION_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(SUSPENSION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Black-List
         */
        List<StatusWiseCountTO> blackListRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(BLACK_LIST, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        blackListRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_BLACK_LIST_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(BLACK_LIST, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Voluntary Retirement
         */
        List<StatusWiseCountTO> voluntaryRetirementRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(VOLUNTARY_RETIREMENT, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        voluntaryRetirementRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_VOLUNTARY_RETIREMENT_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(VOLUNTARY_RETIREMENT, userType, userSubType).size()))
                .build());

        return ResponseTO.builder()
                .registrationRequests(registrationRequests)
                .updationRequests(updationRequests)
                .suspensionRequests(suspensionRequests)
                .blackListRequests(blackListRequests)
                .voluntaryRetirementRequests(voluntaryRetirementRequests)
                .build();
    }

    private ResponseTO fetchCountOnCardByUserType(String userType){

        /**
         * Data retrieval - Registration
         */
        List<StatusWiseCountTO> registrationRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(REGISTRATION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserType(REGISTRATION, userType).size()))
                .build());

        /**
         * Data retrieval - Updation
         */
        List<StatusWiseCountTO> updationRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(UPDATION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserType(UPDATION, userType).size()))
                .build());

        /**
         * Data retrieval - Suspension
         */
        List<StatusWiseCountTO> suspensionRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(SUSPENSION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        suspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_SUSPENSION_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserType(SUSPENSION, userType).size()))
                .build());

        /**
         * Data retrieval - Black-List
         */
        List<StatusWiseCountTO> blackListRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(BLACK_LIST, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        blackListRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_BLACK_LIST_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserType(BLACK_LIST, userType).size()))
                .build());

        /**
         * Data retrieval - Voluntary Retirement
         */
        List<StatusWiseCountTO> voluntaryRetirementRequests= iDashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(VOLUNTARY_RETIREMENT, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .toList();

        voluntaryRetirementRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_VOLUNTARY_RETIREMENT_REQUESTS)
                .count(BigInteger.valueOf(iDashboardRepository.fetchTotalCountByAppStatusAndUserType(VOLUNTARY_RETIREMENT, userType).size()))
                .build());

        return ResponseTO.builder()
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
