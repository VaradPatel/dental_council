package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.DashboardStatus;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.repository.IFetchTrackApplicationDetailsCustomRepository;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static in.gov.abdm.nmr.util.NMRConstants.*;
import static in.gov.abdm.nmr.util.NMRConstants.GENDER_FEMALE;

/**
 * A class that implements all the methods of the Custom Repository interface IFetchTrackApplicationDetailsCustomRepository
 * which deals with health professional's applications and track status
 */
@Repository
@Transactional
@Slf4j
public class FetchTrackApplicationDetailsCustomRepositoryImpl implements IFetchTrackApplicationDetailsCustomRepository {

    /**
     * Injecting a EntityManager bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the HealthProfessionalApplicationRequestParamsTo
     * object passed as a parameter.
     *
     * @param healthProfessionalApplicationRequestParamsTo - object that contains the criteria for the query.
     * @return a string query with appended WHERE clause for the query.
     */
    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> TRACK_APPLICATION_PARAMETERS = healthProfessionalApplicationRequestParamsTo -> {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId())) {
            sb.append("AND d.application_type_id IN (:applicationTypeId) ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getRegistrationNumber())) {
            sb.append("AND rd.registration_no ILIKE :registrationNumber ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getSmcId())) {
            sb.append("AND rd.state_medical_council_id = :smcId ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId())) {
            sb.append("AND work_flow_status_id = :workFlowStatusId ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getApplicantFullName())) {
            sb.append("AND hp.full_name ILIKE :applicantFullName ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getGender())) {
            sb.append(" AND hp.gender ILIKE :gender");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getEmailId())) {
            sb.append("AND hp.email_id ILIKE :emailId ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getMobileNumber())) {
            sb.append("AND hp.mobile_number ILIKE :mobileNumber ");
        }

        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getYearOfRegistration())) {
            sb.append(" AND EXTRACT(YEAR FROM rd.registration_date) = :yearOfRegistration");
        }
        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getCollegeId())) {
            sb.append(" AND qd.college_id = :collegeId");
        }
        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getRequestId())) {
            sb.append(" AND d.request_id ILIKE :requestId");
        }

        return sb.toString();
    };

    /**
     * Represents a functional interface to sort the results based on the parameters specified in HealthProfessionalApplicationRequestParamsTo.
     *
     * @param healthProfessionalApplicationRequestParamsTo - which holds the parameters.
     * @return A query string with appended sort order in the format "ORDER BY column_name sort_order"
     */
    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> SORT_RECORDS = healthProfessionalApplicationRequestParamsTo -> {
        StringBuilder sb = new StringBuilder();
        sb.append(" ORDER BY " + healthProfessionalApplicationRequestParamsTo.getSortBy()  + " " +healthProfessionalApplicationRequestParamsTo.getSortOrder());
        return sb.toString();
    };

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the HealthProfessionalApplicationRequestParamsTo
     * object passed as a parameter.
     *
     * @param healthProfessionalApplicationRequestParamsTo - an object that contains parameters for the function
     * @return a string query to get the status of the Health Professional's application requests.
     */
    private static final BiFunction<HealthProfessionalApplicationRequestParamsTo, List<BigInteger>, String> TRACK_APPLICATION = (healthProfessionalApplicationRequestParamsTo, hpProfiles) -> {
        StringBuilder sb = new StringBuilder();

        sb.append(FETCH_TRACK_DETAILS_QUERY);
        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getCollegeId())) {
            sb.append("INNER JOIN main.qualification_details as qd on qd.hp_profile_id = rd.hp_profile_id AND qd.request_id = d.request_id ");
        }

        sb.append(" where D.hp_profile_id IS NOT NULL and d.application_type_id !=").append(ApplicationType.HP_MODIFICATION.getId());
        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            sb.append(" AND rd.hp_profile_id IN (:healthProfessionalIds)");
        }

        String parameters = TRACK_APPLICATION_PARAMETERS.apply(healthProfessionalApplicationRequestParamsTo);

        if (StringUtils.isNotBlank(parameters)) {
            sb.append(parameters);
        }
        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getSortBy())) {
            String sortRecords = SORT_RECORDS.apply(healthProfessionalApplicationRequestParamsTo);
            sb.append(sortRecords);
        }
        return sb.toString();
    };

    /**
     * Retrieves the details of health professional applications requests based on the provided parameters.
     *
     * @param healthProfessionalApplicationRequestParamsTo - object containing the filter criteria for fetching application details
     * @param pageable                                   - object for pagination
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @Override
    public HealthProfessionalApplicationResponseTo fetchTrackApplicationDetails(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, Pageable pageable, List<BigInteger> hpProfiles) {
        HealthProfessionalApplicationResponseTo healthProfessionalApplicationResponseTo = new HealthProfessionalApplicationResponseTo();
        healthProfessionalApplicationResponseTo.setTotalNoOfRecords(BigInteger.ZERO);
        List<HealthProfessionalApplicationTo> healthProfessionalApplicationToList = new ArrayList<>();

        Query query = entityManager.createNativeQuery(TRACK_APPLICATION.apply(healthProfessionalApplicationRequestParamsTo, hpProfiles));
        setParameters(query, healthProfessionalApplicationRequestParamsTo, hpProfiles);
        query.setFirstResult(pageable.getPageNumber() != 0 ? (pageable.getPageNumber() - 1) * pageable.getPageSize() : 0);
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            HealthProfessionalApplicationTo healthProfessionalApplicationTo = new HealthProfessionalApplicationTo();
            healthProfessionalApplicationTo.setDoctorStatus(result[0] != null ? WorkflowStatus.getWorkflowStatus((BigInteger) result[0]).getDescription() : "");
            healthProfessionalApplicationTo.setSmcStatus(result[1] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[1]).getSmcStatus() : HYPHEN);
            healthProfessionalApplicationTo.setNmcStatus(result[2] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[2]).getStatus() : HYPHEN);
            healthProfessionalApplicationTo.setNbeStatus(result[3] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[3]).getStatus() : HYPHEN);
            healthProfessionalApplicationTo.setHpProfileId((BigInteger) result[4]);
            healthProfessionalApplicationTo.setRequestId((String) result[5]);
            healthProfessionalApplicationTo.setRegistrationNo((String) result[6]);
            healthProfessionalApplicationTo.setCreatedAt(((Timestamp) result[7]).toString());
            healthProfessionalApplicationTo.setCouncilName((String) result[8]);
            healthProfessionalApplicationTo.setApplicantFullName((String) result[9]);
            healthProfessionalApplicationTo.setApplicationTypeId((BigInteger) result[10]);
            healthProfessionalApplicationTo.setApplicationTypeName((String) result[11]);
            healthProfessionalApplicationTo.setPendency(result[12] != null ? (int) Math.floor((Double) result[12]) :Integer.valueOf("0"));
            healthProfessionalApplicationTo.setWorkFlowStatusId((BigInteger) result[13]);
            healthProfessionalApplicationTo.setGender((String) result[14]);
            healthProfessionalApplicationTo.setEmailId((String) result[15]);
            healthProfessionalApplicationTo.setMobileNumber((String) result[16]);
            healthProfessionalApplicationTo.setNmrId((String)result[17]);
            healthProfessionalApplicationTo.setYearOfRegistration(result[18] != null ? ((Timestamp) result[18]).toString() : null);
            healthProfessionalApplicationTo.setCollegeStatus(result[19] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[19]).getStatus() : HYPHEN);
            healthProfessionalApplicationResponseTo.setTotalNoOfRecords((BigInteger) result[20]);
            healthProfessionalApplicationToList.add(healthProfessionalApplicationTo);
        });
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(healthProfessionalApplicationToList);
        return healthProfessionalApplicationResponseTo;
    }

    private Query setParameters(Query query, HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, List<BigInteger> hpProfiles) {
        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            query.setParameter("healthProfessionalIds", hpProfiles);
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId()) && !healthProfessionalApplicationRequestParamsTo.getApplicationTypeId().isEmpty()) {
            query.setParameter("applicationTypeId", healthProfessionalApplicationRequestParamsTo.getApplicationTypeId());
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getRegistrationNumber()) && !healthProfessionalApplicationRequestParamsTo.getRegistrationNumber().isEmpty()) {
            query.setParameter("registrationNumber", "%".concat(healthProfessionalApplicationRequestParamsTo.getRegistrationNumber()).concat("%"));
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getSmcId()) && !healthProfessionalApplicationRequestParamsTo.getSmcId().isEmpty()) {
            query.setParameter("smcId", healthProfessionalApplicationRequestParamsTo.getSmcId());
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            query.setParameter("workFlowStatusId", healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId());
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicantFullName()) && !healthProfessionalApplicationRequestParamsTo.getApplicantFullName().isEmpty()) {
            query.setParameter("applicantFullName", "%".concat(healthProfessionalApplicationRequestParamsTo.getApplicantFullName()).concat("%"));
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getGender()) && !healthProfessionalApplicationRequestParamsTo.getGender().isEmpty()) {
            String gender = healthProfessionalApplicationRequestParamsTo.getGender();
            if (StringUtils.isNotBlank(gender)) {
                if (GENDER_MALE.equalsIgnoreCase(gender)) {
                    gender = "m";
                } else if (GENDER_FEMALE.equalsIgnoreCase(gender)) {
                    gender = "f";
                }
                query.setParameter("gender", "%".concat(gender).concat("%"));
            }
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getEmailId()) && !healthProfessionalApplicationRequestParamsTo.getEmailId().isEmpty()) {
            query.setParameter("emailId", "%".concat(healthProfessionalApplicationRequestParamsTo.getEmailId()).concat("%"));
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getMobileNumber()) && !healthProfessionalApplicationRequestParamsTo.getMobileNumber().isEmpty()) {
            query.setParameter("mobileNumber", "%".concat(healthProfessionalApplicationRequestParamsTo.getMobileNumber()).concat("%"));
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getYearOfRegistration()) && !healthProfessionalApplicationRequestParamsTo.getYearOfRegistration().isEmpty()) {
            query.setParameter("yearOfRegistration", healthProfessionalApplicationRequestParamsTo.getYearOfRegistration());
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getCollegeId()) && !healthProfessionalApplicationRequestParamsTo.getCollegeId().isEmpty()) {
            query.setParameter("collegeId", healthProfessionalApplicationRequestParamsTo.getCollegeId());
        }
        if (StringUtils.isNotBlank(healthProfessionalApplicationRequestParamsTo.getRequestId())) {
            query.setParameter("requestId", "%".concat(healthProfessionalApplicationRequestParamsTo.getRequestId()).concat("%"));
        }
        return query;
    }

    @Override
    public ApplicationDetailResponseTo fetchApplicationDetails(String requestId) throws InvalidRequestException {
        ApplicationDetailResponseTo response = new ApplicationDetailResponseTo();
        List<ApplicationDetailsTo> applicationDetail = new ArrayList<>();
        ApplicationDetailsTo detailsTo;
        Query query = entityManager.createNativeQuery(NMRConstants.APPLICATION_REQUEST_DETAILS);
        query.setParameter("requestId", requestId);
        List<Object[]> results = query.getResultList();
        if (results == null || results.isEmpty()) {
            log.error("unable to complete fetch application details process due workflow audit records for request ID: {}", requestId);
            throw new InvalidRequestException("Invalid input request ID: " + requestId + ". Please enter a valid input and try again");
        }
        log.debug("Fetched {} workflow audit records for request ID: {}", results.size(), requestId);
        response.setRequestId((String) results.get(0)[0]);
        response.setApplicationType((BigInteger) results.get(0)[1]);
        response.setSubmissionDate(String.valueOf(results.get(0)[2]));
        if (WorkflowStatus.PENDING.getId().equals(results.get(results.size() - 1)[3])
                || WorkflowStatus.QUERY_RAISED.getId().equals(results.get(results.size() - 1)[3])) {
            response.setPendency(Math.abs(((Timestamp) results.get(0)[2]).getTime() - Timestamp.from(Instant.now()).getTime()) / 86400000);
        } else if (results.size() > 1) {
            response.setPendency(Math.abs(((Timestamp) results.get(0)[2]).getTime() - ((Timestamp) results.get(results.size() - 1)[2]).getTime()) / 86400000);
        } else {
            response.setPendency(0L);
        }
        response.setCurrentStatus((BigInteger) results.get(results.size() - 1)[3]);
        response.setCurrentGroupId(results.get(results.size() - 1)[4] != null ? (BigInteger) results.get(results.size() - 1)[4] : null);
        if (ApplicationType.ADDITIONAL_QUALIFICATION.getId().equals(results.get(0)[1])) {
            response.setDegreeName((String) (results.get(0)[8] == null ? results.get(0)[9] : results.get(0)[8]));
        }
        for (Object[] result : results) {
            detailsTo = new ApplicationDetailsTo();
            detailsTo.setWorkflowStatusId((BigInteger) result[3]);
            detailsTo.setActionId((BigInteger) result[5]);
            detailsTo.setGroupId((BigInteger) result[6]);
            detailsTo.setActionDate(String.valueOf(result[2]));
            detailsTo.setRemarks((String) result[7]);
            applicationDetail.add(detailsTo);
        }
        response.setApplicationDetails(applicationDetail);
        log.info("Fetched application detail successfully for request ID: {}", requestId);
        return response;
    }
}
