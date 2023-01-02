package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.FetchSpecificDetailsResponseTO;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetailsMapper;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsRepository;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_USER_SUB_TYPE;
import static in.gov.abdm.nmr.util.NMRConstants.INVALID_USER_TYPE;


@Service
public class FetchSpecificDetailsService implements IFetchSpecificDetailsService {

    /**
     * Injecting a IFetchSpecificDetailsRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IFetchSpecificDetailsRepository iFetchSpecificDetailsRepository;

    /**
     * Mapper Interface to transform the IFetchSpecificDetails Bean
     * to the FetchSpecificDetailsResponseTO Bean transferring its contents
     */
    @Autowired
    private IFetchSpecificDetailsMapper iFetchSpecificDetailsMapper;

    @Override
    public List<FetchSpecificDetailsResponseTO> fetchSpecificDetails(String userType, String userSubType, String applicationStatusType, String hpProfileStatus) throws InvalidRequestException {

        if(userSubType!=null){
            validateUserType(userType);
            validateUserSubType(userSubType);
            return fetchSpecificDetailsByUserTypeAndSubType(userType, userSubType, applicationStatusType, hpProfileStatus);
        }
        validateUserType(userType);
        return fetchSpecificDetailsByUserType(userType, applicationStatusType, hpProfileStatus);

    }

    private List<FetchSpecificDetailsResponseTO> fetchSpecificDetailsByUserTypeAndSubType(String userType, String userSubType, String applicationStatusType, String hpProfileStatus){

        return iFetchSpecificDetailsRepository.fetchDetailsForListingByUserTypeAndSubType(userType, userSubType, applicationStatusType, hpProfileStatus)
                .stream()
                .map(fetchSpecificDetails-> {
                    FetchSpecificDetailsResponseTO fetchSpecificDetailsResponseTO=iFetchSpecificDetailsMapper.toFetchSpecificDetailsResponseTO(fetchSpecificDetails);

                    if(UserTypeEnum.COLLEGE.getName().equals(fetchSpecificDetails.getVerifiedByUserType())){
                        fetchSpecificDetailsResponseTO.setCollegeVerificationStatus(fetchSpecificDetails.getHpProfileStatus());
                    }
                    if(UserTypeEnum.STATE_MEDICAL_COUNCIL.getName().equals(fetchSpecificDetails.getVerifiedByUserType())){
                        fetchSpecificDetailsResponseTO.setCouncilVerificationStatus(fetchSpecificDetails.getHpProfileStatus());
                    }
                    if(UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getName().equals(fetchSpecificDetails.getVerifiedByUserType())){
                        fetchSpecificDetailsResponseTO.setNMCVerificationStatus(fetchSpecificDetails.getHpProfileStatus());
                    }

                    long diffInMillis = Math.abs(new Date().getTime() - fetchSpecificDetails.getDateOfSubmission().getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                    fetchSpecificDetailsResponseTO.setPendency(BigInteger.valueOf(diff));

                    return fetchSpecificDetailsResponseTO;
                })
                .toList();


    }

    private List<FetchSpecificDetailsResponseTO> fetchSpecificDetailsByUserType(String userType, String applicationStatusType, String hpProfileStatus){

         return iFetchSpecificDetailsRepository.fetchDetailsForListingByUserType(userType,applicationStatusType,hpProfileStatus)
                .stream()
                .map(fetchSpecificDetails-> {
                    FetchSpecificDetailsResponseTO fetchSpecificDetailsResponseTO=iFetchSpecificDetailsMapper.toFetchSpecificDetailsResponseTO(fetchSpecificDetails);

                    if(UserTypeEnum.COLLEGE.getName().equals(fetchSpecificDetails.getVerifiedByUserType())){
                        fetchSpecificDetailsResponseTO.setCollegeVerificationStatus(fetchSpecificDetails.getHpProfileStatus());
                    }
                    if(UserTypeEnum.STATE_MEDICAL_COUNCIL.getName().equals(fetchSpecificDetails.getVerifiedByUserType())){
                        fetchSpecificDetailsResponseTO.setCouncilVerificationStatus(fetchSpecificDetails.getHpProfileStatus());
                    }
                    if(UserTypeEnum.NATIONAL_MEDICAL_COUNCIL.getName().equals(fetchSpecificDetails.getVerifiedByUserType())){
                        fetchSpecificDetailsResponseTO.setNMCVerificationStatus(fetchSpecificDetails.getHpProfileStatus());
                    }

                    long diffInMillis = Math.abs(new Date().getTime() - fetchSpecificDetails.getDateOfSubmission().getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                    fetchSpecificDetailsResponseTO.setPendency(BigInteger.valueOf(diff));

                    return fetchSpecificDetailsResponseTO;
                })
                .toList();
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
