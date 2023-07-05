package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationTo;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.DashboardStatus;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.repository.IFetchTrackApplicationDetailsCustomRepository;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static in.gov.abdm.nmr.util.NMRConstants.FETCH_TRACK_DETAILS_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.NOT_YET_RECEIVED;

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

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId()) && !healthProfessionalApplicationRequestParamsTo.getApplicationTypeId().isEmpty()) {
            sb.append("AND d.application_type_id IN (:applicationTypeId) ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getRegistrationNumber()) && !healthProfessionalApplicationRequestParamsTo.getRegistrationNumber().isEmpty()) {
            sb.append("AND rd.registration_no ILIKE :registrationNumber ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getSmcId()) && !healthProfessionalApplicationRequestParamsTo.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = :smcId ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("AND work_flow_status_id = :workFlowStatusId ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicantFullName()) && !healthProfessionalApplicationRequestParamsTo.getApplicantFullName().isEmpty()) {
            sb.append("AND hp.full_name ILIKE :applicantFullName ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getGender()) && !healthProfessionalApplicationRequestParamsTo.getGender().isEmpty()) {
            String gender = "";
            if (healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase(NMRConstants.GENDER_MALE) || healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase("m")) {
                gender = "m";
            } else if (healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase(NMRConstants.GENDER_FEMALE) || healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase("f")) {
                gender = "f";
            }
            sb.append("AND hp.gender ILIKE :gender ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getEmailId()) && !healthProfessionalApplicationRequestParamsTo.getEmailId().isEmpty()) {
            sb.append("AND hp.email_id ILIKE :emailId ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getMobileNumber()) && !healthProfessionalApplicationRequestParamsTo.getMobileNumber().isEmpty()) {
            sb.append("AND hp.mobile_number ILIKE :mobileNumber ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getYearOfRegistration()) && !healthProfessionalApplicationRequestParamsTo.getYearOfRegistration().isEmpty()) {
            sb.append(" AND EXTRACT(YEAR FROM rd.registration_date) = :yearOfRegistration");
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
        sb.append(" ORDER BY  :sort");
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

        sb.append(" where D.hp_profile_id IS NOT NULL and d.application_type_id !=").append(ApplicationType.HP_MODIFICATION.getId());
        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            sb.append(" AND rd.hp_profile_id IN (:healthProfessionalIds)");
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
            healthProfessionalApplicationTo.setSmcStatus(result[1] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[1]).getSmcStatus() : NOT_YET_RECEIVED);
            healthProfessionalApplicationTo.setNmcStatus(result[2] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[2]).getStatus() : NOT_YET_RECEIVED);
            healthProfessionalApplicationTo.setNbeStatus(result[3] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[3]).getStatus() : NOT_YET_RECEIVED);
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
            healthProfessionalApplicationTo.setCollegeStatus(result[19] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[19]).getStatus() : NOT_YET_RECEIVED);
            healthProfessionalApplicationResponseTo.setTotalNoOfRecords((BigInteger) result[20]);
            healthProfessionalApplicationToList.add(healthProfessionalApplicationTo);
        });
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(healthProfessionalApplicationToList);
        return healthProfessionalApplicationResponseTo;
    }

    private Query setParameters(Query query, HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, List<BigInteger> hpProfiles) {
        query.setParameter("sort", healthProfessionalApplicationRequestParamsTo.getSortBy() + " " + healthProfessionalApplicationRequestParamsTo.getSortOrder());
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
            String gender = "";
            if (healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase(NMRConstants.GENDER_MALE) || healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase("m")) {
                gender = "m";
            } else if (healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase(NMRConstants.GENDER_FEMALE) || healthProfessionalApplicationRequestParamsTo.getGender().equalsIgnoreCase("f")) {
                gender = "f";
            }
            query.setParameter("gender", "%".concat(gender.concat("%")));
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
        return query;
    }
}
