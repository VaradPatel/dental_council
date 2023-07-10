package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.dto.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.dto.QualificationDetailRequestTO;
import in.gov.abdm.nmr.entity.RequestCounter;
import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;

/**
 * Util class for NMR.
 */
@UtilityClass
public final class NMRUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String buildRequestIdForWorkflow(RequestCounter requestCounter){
        return requestCounter.getApplicationType().getRequestPrefixId().concat(String.valueOf(requestCounter.getCounter()));
    }

    public static void validateWorkProfileDetails(List<CurrentWorkDetailsTO> currentWorkDetailsTOS) throws InvalidRequestException {
        if(CollectionUtils.isEmpty(currentWorkDetailsTOS)){
            throw new InvalidRequestException(NMRError.MISSING_MANDATORY_FIELD.getCode(), NMRError.MISSING_MANDATORY_FIELD.getMessage());
        }
    }

    /**
     * Validate Input for Add Qualification Details API of HP Registration Controller
     * @param qualificationDetailRequestTOs
     * @param proofs
     */
    public static void validateQualificationDetailsAndProofs(List<QualificationDetailRequestTO> qualificationDetailRequestTOs, List<MultipartFile> proofs, List<String> existingQualifications) throws InvalidRequestException {
        if (CollectionUtils.isEmpty(qualificationDetailRequestTOs)) {
            throw new InvalidRequestException(NMRError.MISSING_MANDATORY_FIELD.getCode(), NMRError.MISSING_MANDATORY_FIELD.getMessage());
        }
        if (CollectionUtils.isEmpty(proofs)) {
            throw new InvalidRequestException(NMRError.MISSING_MANDATORY_FIELD.getCode(), NMRError.MISSING_MANDATORY_FIELD.getMessage());
        }
        if (qualificationDetailRequestTOs.size() > proofs.size()) {
            throw new InvalidRequestException(NMRError.MISSING_PROOFS_ERROR.getCode(), NMRError.MISSING_PROOFS_ERROR.getMessage());
        }
        if (qualificationDetailRequestTOs.size() < proofs.size()) {
            throw new InvalidRequestException(NMRError.EXCESS_PROOFS_ERROR.getCode(), NMRError.EXCESS_PROOFS_ERROR.getMessage());
        }
        if (NMRConstants.MAX_QUALIFICATION_SIZE <= qualificationDetailRequestTOs.size() + existingQualifications.size()) {
            throw new InvalidRequestException(NMRError.QUALIFICATION_DETAILS_LIMIT_EXCEEDED.getCode(), NMRError.QUALIFICATION_DETAILS_LIMIT_EXCEEDED.getMessage());
        }
        for (QualificationDetailRequestTO qualificationDetailRequestTO : qualificationDetailRequestTOs) {
            for (String courseName : existingQualifications) {
                if (courseName.equals(qualificationDetailRequestTO.getCourse().getName())) {
                    throw new InvalidRequestException(NMRError.DUPLICATE_QUALIFICATION_ERROR.getCode(), NMRError.DUPLICATE_QUALIFICATION_ERROR.getMessage());
                }
            }
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

    public static long generateRandom(int length) {
        char[] digits = new char[length];
        digits[0] = (char) (RANDOM.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (RANDOM.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    public static boolean isVoluntarySuspension(WorkFlow workFlow) {
        return (ApplicationType.HP_TEMPORARY_SUSPENSION.getId().equals(workFlow.getApplicationType().getId()) ||
                ApplicationType.HP_PERMANENT_SUSPENSION.getId().equals(workFlow.getApplicationType().getId()))
                && Group.HEALTH_PROFESSIONAL.getId().equals(workFlow.getPreviousGroup().getId());
    }



}
