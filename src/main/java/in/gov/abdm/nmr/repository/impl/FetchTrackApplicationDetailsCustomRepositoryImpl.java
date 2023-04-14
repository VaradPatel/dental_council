package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationTo;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.HpProfileStatus;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.repository.IFetchTrackApplicationDetailsCustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static in.gov.abdm.nmr.util.NMRConstants.*;

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

    private static final Function<String, String> UPDATED_DATES = requestId -> "SELECT * FROM (SELECT request_id,previous_group_id,updated_at, DENSE_RANK() OVER(PARTITION BY previous_group_id ORDER BY updated_at DESC) AS previous_group_id_rank " +
            "FROM main.work_flow_audit WHERE request_id = '" + requestId + "' ORDER BY updated_at ASC) work_flow_details WHERE work_flow_details.previous_group_id_rank = 1";


    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the HealthProfessionalApplicationRequestParamsTo
     * object passed as a parameter.
     *
     * @param healthProfessionalApplicationRequestParamsTo - object that contains the criteria for the query.
     * @return a string query with appended WHERE clause for the query.
     */
    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> TRACK_APPLICATION_PARAMETERS = healthProfessionalApplicationRequestParamsTo -> {
        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId()) && !healthProfessionalApplicationRequestParamsTo.getApplicationTypeId().isEmpty()) {
            sb.append("AND d.application_type_id IN (").append(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId()).append(") ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getRegistrationNumber()) && !healthProfessionalApplicationRequestParamsTo.getRegistrationNumber().isEmpty()) {
            sb.append("AND rd.registration_no ILIKE '%").append(healthProfessionalApplicationRequestParamsTo.getRegistrationNumber()).append("%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getSmcId()) && !healthProfessionalApplicationRequestParamsTo.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = '").append(healthProfessionalApplicationRequestParamsTo.getSmcId()).append("' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("AND work_flow_status_id = '").append(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()).append("' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicantFullName()) && !healthProfessionalApplicationRequestParamsTo.getApplicantFullName().isEmpty()) {
            sb.append("AND hp.full_name ILIKE '%").append(healthProfessionalApplicationRequestParamsTo.getApplicantFullName()).append("%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getGender()) && !healthProfessionalApplicationRequestParamsTo.getGender().isEmpty()) {
            sb.append("AND hp.gender ILIKE '%").append(healthProfessionalApplicationRequestParamsTo.getGender()).append("%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getEmailId()) && !healthProfessionalApplicationRequestParamsTo.getEmailId().isEmpty()) {
            sb.append("AND hp.email_id ILIKE '%").append(healthProfessionalApplicationRequestParamsTo.getEmailId()).append("%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getMobileNumber()) && !healthProfessionalApplicationRequestParamsTo.getMobileNumber().isEmpty()) {
            sb.append("AND hp.mobile_number ILIKE '%").append(healthProfessionalApplicationRequestParamsTo.getMobileNumber()).append("%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getYearOfRegistration()) && !healthProfessionalApplicationRequestParamsTo.getYearOfRegistration().isEmpty()) {
            sb.append("AND EXTRACT(YEAR FROM rd.registration_date) = ").append(healthProfessionalApplicationRequestParamsTo.getYearOfRegistration()).append(" ");
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
        sb.append("ORDER BY ");
        sb.append(healthProfessionalApplicationRequestParamsTo.getSortBy());
        sb.append(" ");
        sb.append(healthProfessionalApplicationRequestParamsTo.getSortOrder());
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

        sb.append(" where D.hp_profile_id IS NOT NULL ");

        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            StringBuilder hpIds = new StringBuilder();
            hpProfiles.forEach(hpProfile -> {
                if (hpProfiles.indexOf(hpProfile) == hpProfiles.size() - 1) {
                    hpIds.append(hpProfile);
                } else {
                    hpIds.append(hpProfile).append(",");
                }
            });
            sb.append("AND rd.hp_profile_id IN (").append(hpIds).append(") ");
        }

        String parameters = TRACK_APPLICATION_PARAMETERS.apply(healthProfessionalApplicationRequestParamsTo);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getSortBy()) && !healthProfessionalApplicationRequestParamsTo.getSortBy().isEmpty()) {
            String sortRecords = SORT_RECORDS.apply(healthProfessionalApplicationRequestParamsTo);
            sb.append(sortRecords);
        }
        return sb.toString();
    };

    /**
     * This method is used to retrieve the count of health professional application records based on the provided parameters.
     *
     * @param healthProfessionalApplicationRequestParamsTo - the parameters used to retrieve the health professional application records count
     * @param hpProfiles
     * @return totalRecords the count of health professional application requests
     */
    /*private BigInteger getCount(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, List<BigInteger> hpProfiles) {
        BigInteger totalRecords = null;
        try {
            Query query = entityManager.createNativeQuery(GET_RECORD_COUNT.apply(healthProfessionalApplicationRequestParamsTo, hpProfiles));
            Object result = query.getSingleResult();
            totalRecords = (BigInteger) result;
        } catch (Exception e) {
            log.error("Repository:: getRecords " + e.getMessage());
        }
        return totalRecords;
    }*/

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the HealthProfessionalApplicationRequestParamsTo
     * object passed as a parameter.
     *
     * @param healthProfessionalApplicationRequestParamsTo - an object that contains parameters for the function
     * @return a query to get the count of the Health Professional's application requests.
     */
    /* private static final BiFunction<HealthProfessionalApplicationRequestParamsTo, List<BigInteger>, String> GET_RECORD_COUNT = (healthProfessionalApplicationRequestParamsTo, hpProfiles) -> {
        StringBuilder sb = new StringBuilder();

        sb.append(FETCH_TRACK_DETAILS_COUNT_QUERY);

        sb.append(" where calculate.hp_profile_id IS NOT NULL and current_status = 1 ");

        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            StringBuilder hpIds = new StringBuilder();
            hpProfiles.forEach(hpProfile -> {
                if (hpProfiles.indexOf(hpProfile) == hpProfiles.size() - 1) {
                    hpIds.append(hpProfile);
                } else {
                    hpIds.append(hpProfile).append(",");
                }
            });
            sb.append("AND rd.hp_profile_id IN  (").append(hpIds).append(") ");
        }

        String parameters = TRACK_APPLICATION_PARAMETERS.apply(healthProfessionalApplicationRequestParamsTo);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        return sb.toString();
    };*/

    /**
     * Retrieves the details of health professional applications requests based on the provided parameters.
     *
     * @param healthProfessionalApplicationRequestParamsTo - object containing the filter criteria for fetching application details
     * @param pagination                                   - object for pagination
     * @return the HealthProfessionalApplicationResponseTo object representing the response object
     * which contains all the details used to track the health professionals who have
     * raised a request
     */
    @Override
    public HealthProfessionalApplicationResponseTo fetchTrackApplicationDetails(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, Pageable pagination, List<BigInteger> hpProfiles) {
        HealthProfessionalApplicationResponseTo healthProfessionalApplicationResponseTo = new HealthProfessionalApplicationResponseTo();
        healthProfessionalApplicationResponseTo.setTotalNoOfRecords(BigInteger.ZERO);
        List<HealthProfessionalApplicationTo> healthProfessionalApplicationToList = new ArrayList<>();

        Query query = entityManager.createNativeQuery(TRACK_APPLICATION.apply(healthProfessionalApplicationRequestParamsTo, hpProfiles));

        query.setFirstResult((pagination.getPageNumber() - 1) * pagination.getPageSize());
        query.setMaxResults(pagination.getPageSize());

        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            HealthProfessionalApplicationTo healthProfessionalApplicationTo = new HealthProfessionalApplicationTo();
            healthProfessionalApplicationTo.setDoctorStatus(result[0] != null ? WorkflowStatus.getWorkflowStatus((BigInteger) result[0]).getDescription() : "");
            healthProfessionalApplicationTo.setSmcStatus(result[1] != null ? Action.getAction((BigInteger) result[1]).getStatus() : "");
            healthProfessionalApplicationTo.setNmcStatus(result[2] != null ? Action.getAction((BigInteger) result[2]).getStatus() : "");
            healthProfessionalApplicationTo.setNbeStatus(result[3] != null ? Action.getAction((BigInteger) result[3]).getStatus() : "");
            healthProfessionalApplicationTo.setHpProfileId((BigInteger) result[4]);
            String requestId = (String) result[5];
            Query queryDate = entityManager.createNativeQuery(UPDATED_DATES.apply(requestId));
            List<Object[]> resultsDate = queryDate.getResultList();
            Timestamp submissionDate = (Timestamp)resultsDate.get(0)[2];
            healthProfessionalApplicationTo.setCreatedAt(submissionDate.toString());
            resultsDate.forEach(objects -> {
                BigInteger previousGroupId = (BigInteger) objects[1];
                Timestamp timestamp = (Timestamp) objects[2];
                switch (previousGroupId.intValue()) {
                    case 1:
                        healthProfessionalApplicationTo.setDoctorActionDate(timestamp.toString());
                        break;
                    case 2:
                        healthProfessionalApplicationTo.setSmcActionDate(timestamp.toString());
                        break;
                    case 3:
                        healthProfessionalApplicationTo.setNmcActionDate(timestamp.toString());
                        break;
                    case 4:
//                        healthProfessionalApplicationTo.setCollegeDeanActionDate(timestamp.toString());
                        healthProfessionalApplicationTo.setCollegeActionDate(timestamp.toString());
                        break;
                    case 5:
                        healthProfessionalApplicationTo.setCollegeRegistrarActionDate(timestamp.toString());
                        break;
                    case 7:
                        healthProfessionalApplicationTo.setNbeActionDate(timestamp.toString());
                        break;
                }
            });
            healthProfessionalApplicationTo.setRequestId((String) result[5]);
            healthProfessionalApplicationTo.setRegistrationNo((String) result[6]);
            healthProfessionalApplicationTo.setCouncilName((String) result[8]);
            healthProfessionalApplicationTo.setApplicantFullName((String) result[9]);
            healthProfessionalApplicationTo.setApplicationTypeId((BigInteger) result[10]);
            healthProfessionalApplicationTo.setApplicationTypeName((String) result[11]);
            healthProfessionalApplicationTo.setPendency((int) Math.floor((Double) result[12]));
            healthProfessionalApplicationTo.setWorkFlowStatusId((BigInteger) result[13]);
            healthProfessionalApplicationTo.setGender((String) result[14]);
            healthProfessionalApplicationTo.setEmailId((String) result[15]);
            healthProfessionalApplicationTo.setMobileNumber((String) result[16]);
            healthProfessionalApplicationTo.setNmrId((String)result[17]);
            healthProfessionalApplicationTo.setYearOfRegistration(((Timestamp) result[18]).toString());
            healthProfessionalApplicationTo.setCollegeStatus(Action.getAction((BigInteger) result[19]).getStatus());
            healthProfessionalApplicationResponseTo.setTotalNoOfRecords((BigInteger) result[20]);
            healthProfessionalApplicationTo.setCouncilName((String) result[8]);
            healthProfessionalApplicationTo.setApplicantFullName((String) result[9]);
            healthProfessionalApplicationTo.setApplicationTypeId((BigInteger) result[10]);
            healthProfessionalApplicationTo.setApplicationTypeName((String) result[11]);
            healthProfessionalApplicationTo.setPendency((int) Math.floor((Double) result[12]));
            healthProfessionalApplicationTo.setWorkFlowStatusId((BigInteger) result[13]);
            healthProfessionalApplicationTo.setGender((String) result[14]);
            healthProfessionalApplicationTo.setEmailId((String) result[15]);
            healthProfessionalApplicationTo.setMobileNumber((String) result[16]);
            healthProfessionalApplicationTo.setNmrId((String)result[17]);
            healthProfessionalApplicationTo.setYearOfRegistration(((Timestamp) result[18]).toString());
            healthProfessionalApplicationTo.setCollegeStatus(result[19] != null ? Action.getAction((BigInteger) result[19]).getStatus() : "");
            healthProfessionalApplicationResponseTo.setTotalNoOfRecords((BigInteger) result[20]);
            healthProfessionalApplicationToList.add(healthProfessionalApplicationTo);
        });
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(healthProfessionalApplicationToList);
        return healthProfessionalApplicationResponseTo;
    }
}
