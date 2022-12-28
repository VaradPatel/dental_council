package in.gov.abdm.nmr.api.controller.dashboard;

import in.gov.abdm.nmr.api.controller.dashboard.to.IStatusWiseCountMapper;
import in.gov.abdm.nmr.api.controller.dashboard.to.ResponseTO;
import in.gov.abdm.nmr.api.controller.dashboard.to.StatusWiseCountTO;
import in.gov.abdm.nmr.api.exception.InvalidRequestException;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubTypeEnum;
import in.gov.abdm.nmr.db.sql.domain.user_type.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static in.gov.abdm.nmr.api.constant.NMRConstants.*;


@Service
public class DashboardServiceImpl implements DashboardService{

    /**
     * Injecting a Dashboard Repository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private DashboardRepository dashboardRepository;

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
        List<StatusWiseCountTO> registrationRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(REGISTRATION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(REGISTRATION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Updation
         */
        List<StatusWiseCountTO> updationRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(UPDATION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(UPDATION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Suspension
         */
        List<StatusWiseCountTO> suspensionRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(SUSPENSION, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        suspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_SUSPENSION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(SUSPENSION, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Black-List
         */
        List<StatusWiseCountTO> blackListRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(BLACK_LIST, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        blackListRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_BLACK_LIST_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(BLACK_LIST, userType, userSubType).size()))
                .build());

        /**
         * Data retrieval - Voluntary Retirement
         */
        List<StatusWiseCountTO> voluntaryRetirementRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(VOLUNTARY_RETIREMENT, userType, userSubType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        voluntaryRetirementRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_VOLUNTARY_RETIREMENT_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserTypeAndSubType(VOLUNTARY_RETIREMENT, userType, userSubType).size()))
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
        List<StatusWiseCountTO> registrationRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(REGISTRATION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        registrationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_REGISTRATION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserType(REGISTRATION, userType).size()))
                .build());

        /**
         * Data retrieval - Updation
         */
        List<StatusWiseCountTO> updationRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(UPDATION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        updationRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_UPDATION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserType(UPDATION, userType).size()))
                .build());

        /**
         * Data retrieval - Suspension
         */
        List<StatusWiseCountTO> suspensionRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(SUSPENSION, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        suspensionRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_SUSPENSION_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserType(SUSPENSION, userType).size()))
                .build());

        /**
         * Data retrieval - Black-List
         */
        List<StatusWiseCountTO> blackListRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(BLACK_LIST, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        blackListRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_BLACK_LIST_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserType(BLACK_LIST, userType).size()))
                .build());

        /**
         * Data retrieval - Voluntary Retirement
         */
        List<StatusWiseCountTO> voluntaryRetirementRequests=dashboardRepository.fetchHpStatusWiseCountByAppStatusAndUserType(VOLUNTARY_RETIREMENT, userType)
                .stream()
                .map(statusWiseCount-> iStatusWiseCountMapper.toStatusWiseCountTO(statusWiseCount))
                .collect(Collectors.toList());

        voluntaryRetirementRequests.add(StatusWiseCountTO.builder()
                .name(TOTAL_VOLUNTARY_RETIREMENT_REQUESTS)
                .count(BigInteger.valueOf(dashboardRepository.fetchTotalCountByAppStatusAndUserType(VOLUNTARY_RETIREMENT, userType).size()))
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
