package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationTo;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.repository.IFetchTrackApplicationDetailsCustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static in.gov.abdm.nmr.util.NMRConstants.FETCH_TRACK_DETAILS_COUNT_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_TRACK_DETAILS_QUERY;

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
            sb.append("AND calculate.application_type_id IN (").append(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId()).append(") ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getRegistrationNo()) && !healthProfessionalApplicationRequestParamsTo.getRegistrationNo().isEmpty()) {
            sb.append("AND rd.registration_no ILIKE '%").append(healthProfessionalApplicationRequestParamsTo.getRegistrationNo()).append("%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getSmcId()) && !healthProfessionalApplicationRequestParamsTo.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = '").append(healthProfessionalApplicationRequestParamsTo.getSmcId()).append("' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("AND  work_flow_status_id = '").append(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()).append("' ");
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
        sb.append("  ");
        sb.append("ORDER BY  ");
        sb.append(healthProfessionalApplicationRequestParamsTo.getSortBy());
        sb.append("  ");
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
    private static final BiFunction<HealthProfessionalApplicationRequestParamsTo,List<BigInteger>, String> TRACK_APPLICATION = (healthProfessionalApplicationRequestParamsTo, hpProfiles) -> {
        StringBuilder sb = new StringBuilder();

        sb.append(FETCH_TRACK_DETAILS_QUERY);

        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            StringBuilder hpIds = new StringBuilder();
            hpProfiles.forEach(hpProfile -> {
                if(hpProfiles.indexOf(hpProfile) == hpProfiles.size()-1){
                    hpIds.append(hpProfile);
                }
                else{
                    hpIds.append(hpProfile).append(",");
                }
            });
            sb.append("AND rd.hp_profile_id IN (").append(hpIds).append(")");
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
    private BigInteger getCount(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, List<BigInteger> hpProfiles) {
        BigInteger totalRecords = null;
        try {
            Query query = entityManager.createNativeQuery(GET_RECORD_COUNT.apply(healthProfessionalApplicationRequestParamsTo, hpProfiles));
            Object result = query.getSingleResult();
            totalRecords = (BigInteger) result;
        } catch (Exception e) {
            log.error("Repository:: getRecords " + e.getMessage());
        }
        return totalRecords;
    }

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the HealthProfessionalApplicationRequestParamsTo
     * object passed as a parameter.
     *
     * @param healthProfessionalApplicationRequestParamsTo - an object that contains parameters for the function
     * @return a query to get the count of the Health Professional's application requests.
     */
    private static final BiFunction<HealthProfessionalApplicationRequestParamsTo, List<BigInteger>, String> GET_RECORD_COUNT = (healthProfessionalApplicationRequestParamsTo, hpProfiles) -> {
        StringBuilder sb = new StringBuilder();

        sb.append(FETCH_TRACK_DETAILS_COUNT_QUERY);

        if (Objects.nonNull(hpProfiles) && !hpProfiles.isEmpty()) {
            StringBuilder hpIds = new StringBuilder();
            hpProfiles.forEach(hpProfile -> {
                if(hpProfiles.indexOf(hpProfile) == hpProfiles.size()-1){
                    hpIds.append(hpProfile);
                }
                else {
                    hpIds.append(hpProfile).append(",");
                }
            });
            sb.append("AND rd.hp_profile_id IN  (").append(hpIds).append(")");
        }

        String parameters = TRACK_APPLICATION_PARAMETERS.apply(healthProfessionalApplicationRequestParamsTo);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        return sb.toString();
    };

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
        List<HealthProfessionalApplicationTo> healthProfessionalApplicationToList = new ArrayList<>();

        Query query = entityManager.createNativeQuery(TRACK_APPLICATION.apply(healthProfessionalApplicationRequestParamsTo, hpProfiles));

        query.setFirstResult((pagination.getPageNumber() - 1) * pagination.getPageSize());
        query.setMaxResults(pagination.getPageSize());

        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            HealthProfessionalApplicationTo healthProfessionalApplicationTo = new HealthProfessionalApplicationTo();
            healthProfessionalApplicationTo.setDoctorStatus((String) result[0]);
            healthProfessionalApplicationTo.setSmcStatus((String) result[1]);
            healthProfessionalApplicationTo.setCollegeDeanStatus((String) result[2]);
            healthProfessionalApplicationTo.setCollegeRegistrarStatus((String) result[3]);
            healthProfessionalApplicationTo.setNmcStatus((String) result[4]);
            healthProfessionalApplicationTo.setNbeStatus((String) result[5]);
            healthProfessionalApplicationTo.setHpProfileId((BigInteger) result[6]);
            healthProfessionalApplicationTo.setRequestId((String) result[7]);
            healthProfessionalApplicationTo.setRegistrationNo((String) result[8]);
            healthProfessionalApplicationTo.setCreatedAt((String) result[9]);
            healthProfessionalApplicationTo.setCouncilName((String) result[10]);
            healthProfessionalApplicationTo.setApplicantFullName((String) result[11]);
            healthProfessionalApplicationTo.setApplicationTypeId((BigInteger) result[12]);
            healthProfessionalApplicationTo.setApplicationTypeName((String) result[13]);
            healthProfessionalApplicationTo.setPendency((Double) result[14]);
            healthProfessionalApplicationTo.setWorkFlowStatusId((BigInteger) result[15]);
            healthProfessionalApplicationToList.add(healthProfessionalApplicationTo);
        });
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(healthProfessionalApplicationToList);
        healthProfessionalApplicationResponseTo.setTotalNoOfRecords(getCount(healthProfessionalApplicationRequestParamsTo, hpProfiles));
        return healthProfessionalApplicationResponseTo;
    }
}
