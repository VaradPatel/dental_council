package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.dto.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.dto.QualificationDetailRequestTO;
import in.gov.abdm.nmr.entity.RequestCounter;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Util class for NMR.
 */
@UtilityClass
public final class NMRUtil {

    public static String buildRequestIdForWorkflow(RequestCounter requestCounter){
        return requestCounter.getApplicationType().getRequestPrefixId().concat(String.valueOf(requestCounter.getCounter()));
    }

    public static void validateWorkProfileDetails(List<CurrentWorkDetailsTO> currentWorkDetailsTOS) throws InvalidRequestException {
        if(currentWorkDetailsTOS==null ){
            throw new InvalidRequestException(WORK_PROFILE_DETAILS_NULL_ERROR);
        }
        if(currentWorkDetailsTOS.isEmpty()){
            throw new InvalidRequestException(WORK_PROFILE_DETAILS_EMPTY_ERROR);
        }
    }
    /**
     * Validate Input for Update Work Profile Details API of HP Registration Controller
     * @param currentWorkDetailsTOS
     * @param proofs
     */
    public static void validateWorkProfileDetailsAndProofs(List<CurrentWorkDetailsTO> currentWorkDetailsTOS, List<MultipartFile> proofs) throws InvalidRequestException {

        if(proofs.isEmpty()){
            throw new InvalidRequestException(PROOFS_EMPTY_ERROR);
        }
        if(currentWorkDetailsTOS.size() > proofs.size()){
            throw new InvalidRequestException(MISSING_PROOFS_FOR_WORK_PROFILE_DETAILS_ERROR);
        }
        if(currentWorkDetailsTOS.size() < proofs.size()){
            throw new InvalidRequestException(EXCESS_PROOFS_FOR_WORK_PROFILE_DETAILS_ERROR);
        }
        if(currentWorkDetailsTOS.size()>6){
            throw new InvalidRequestException(WORK_PROFILE_DETAILS_LIMIT_EXCEEDED);
        }

    }

    /**
     * Validate Input for Add Qualification Details API of HP Registration Controller
     * @param qualificationDetailRequestTOs
     * @param proofs
     */
    public static void validateQualificationDetailsAndProofs(List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs) throws InvalidRequestException {
        if(qualificationDetailRequestTOs==null ){
            throw new InvalidRequestException(QUALIFICATION_DETAILS_NULL_ERROR);
        }
        if(qualificationDetailRequestTOs.isEmpty()){
            throw new InvalidRequestException(QUALIFICATION_DETAILS_EMPTY_ERROR);
        }
        if(proofs==null){
            throw new InvalidRequestException(PROOFS_NULL_ERROR);
        }
        if(proofs.isEmpty()){
            throw new InvalidRequestException(PROOFS_EMPTY_ERROR);
        }
        if(qualificationDetailRequestTOs.size() > proofs.size()){
            throw new InvalidRequestException(MISSING_PROOFS_ERROR);
        }
        if(qualificationDetailRequestTOs.size() < proofs.size()){
            throw new InvalidRequestException(EXCESS_PROOFS_ERROR);
        }
        if(qualificationDetailRequestTOs.size()>6){
            throw new InvalidRequestException(QUALIFICATION_DETAILS_LIMIT_EXCEEDED);
        }

    }

    /**
     * Return value if it is not null otherwise fallBackValue.
     * @param value the main value to be returned when not null.
     * @param fallBackValue the fallback value to be returned when main value is not available.
     * @param <T> the object data type.
     * @return the value when it's not null otherwise returns fallBackValue.
     */
    public static <T> T coalesce(T value, T fallBackValue){
        return value != null ? value : fallBackValue;
    }

    /**
     * Return value if it is not null otherwise fallBackValue.
     * @param value the main value to be returned when not null.
     * @param fallBackValue the fallback value to be returned when main value is not available.
     * @param <T> the object data type.
     * @return the value when it's not null otherwise returns fallBackValue.
     */
    public static <T extends Collection> T coalesceCollection(T value, T fallBackValue){
        return !value.isEmpty()  ? value : fallBackValue;
    }
}
