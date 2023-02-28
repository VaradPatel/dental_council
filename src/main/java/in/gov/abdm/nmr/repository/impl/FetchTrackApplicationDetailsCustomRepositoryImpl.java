package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.HealthProfessionalApplicationRequestParamsTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationResponseTo;
import in.gov.abdm.nmr.dto.HealthProfessionalApplicationTo;
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
import java.util.function.Function;

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
    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> TRACK_APPLICATION = healthProfessionalApplicationRequestParamsTo -> {
        StringBuilder sb = new StringBuilder();
        sb.append("select doctor_status, smc_status, college_dean_status, college_registrar_status, nmc_status, nbe_status, calculate.hp_profile_id, calculate.request_id, rd.registration_no, rd.created_at,stmc.name, hp.full_name, application_type_id, (SELECT a.name FROM main.application_type a WHERE a.id=application_type_id) as application_type_name, DATE_PART('day', (now() - TO_TIMESTAMP(rd.created_at, 'YYYY-MM-DD HH24:MI:SS'))) as pendency from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor_status, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as smc_status, CASE when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and wf.action_id IN (1,2,3) THEN 'PENDING' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and wf.action_id = 3 THEN 'QUERY RAISED' END as college_dean_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 5 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and wf.action_id = 3 THEN 'QUERY RAISED' END as college_registrar_status, CASE when wf.current_group_id = 7 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 7 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 7 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 7 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as nbe_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 3 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as nmc_status, wf.id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id from main.work_flow_audit as wf ");
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("WHERE  work_flow_status_id = '").append(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()).append("' ");
        }
        sb.append("order by wf.hp_profile_id desc, wf.id asc " +
                ")  " +
                "calculate " +
                "INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id " +
                "INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id " +
                "INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id " +
                "INNER JOIN main.qualification_details as qd on qd.hp_profile_id = hp.id AND qd.request_id = hp.request_id " +
                "where calculate.hp_profile_id IS NOT NULL and current_status = 1 ");

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
     * @return totalRecords the count of health professional application requests
     */
    private BigInteger getCount(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo) {
        BigInteger totalRecords = null;
        try {
            Query query = entityManager.createNativeQuery(GET_RECORD_COUNT.apply(healthProfessionalApplicationRequestParamsTo));
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
    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> GET_RECORD_COUNT = healthProfessionalApplicationRequestParamsTo -> {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor_status, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as smc_status, CASE when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and wf.action_id IN (1,2,3) THEN 'PENDING' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and wf.action_id = 3 THEN 'QUERY RAISED' END as college_dean_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 5 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and wf.action_id = 3 THEN 'QUERY RAISED' END as college_registrar_status, CASE when wf.current_group_id = 7 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 7 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 7 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 7 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as nbe_status, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 3 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as nmc_status, wf.id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id from main.work_flow_audit as wf ");
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("WHERE  work_flow_status_id = '").append(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()).append("' ");
        }
        sb.append("order by wf.hp_profile_id desc, wf.id asc " +
                ")  " +
                "calculate " +
                "INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id " +
                "INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id " +
                "INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id " +
                "INNER JOIN main.qualification_details as qd on qd.hp_profile_id = hp.id AND qd.request_id = hp.request_id " +
                "where calculate.hp_profile_id IS NOT NULL and current_status = 1 ");

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
    public HealthProfessionalApplicationResponseTo fetchTrackApplicationDetails(HealthProfessionalApplicationRequestParamsTo healthProfessionalApplicationRequestParamsTo, Pageable pagination) {
        HealthProfessionalApplicationResponseTo healthProfessionalApplicationResponseTo = new HealthProfessionalApplicationResponseTo();
        List<HealthProfessionalApplicationTo> healthProfessionalApplicationToList = new ArrayList<>();

        Query query = entityManager.createNativeQuery(TRACK_APPLICATION.apply(healthProfessionalApplicationRequestParamsTo));

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
            healthProfessionalApplicationToList.add(healthProfessionalApplicationTo);
        });
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(healthProfessionalApplicationToList);
        healthProfessionalApplicationResponseTo.setTotalNoOfRecords(getCount(healthProfessionalApplicationRequestParamsTo));
        return healthProfessionalApplicationResponseTo;
    }
}
