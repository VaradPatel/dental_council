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

@Repository
@Transactional
@Slf4j
public class FetchTrackApplicationDetailsCustomRepositoryImpl implements IFetchTrackApplicationDetailsCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> TRACK_APPLICATION_PARAMETERS = (healthProfessionalApplicationRequestParamsTo) -> {
        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getApplicationTypeId()) && !healthProfessionalApplicationRequestParamsTo.getApplicationTypeId().isEmpty()) {
            sb.append("AND calculate.application_type_id = '" + healthProfessionalApplicationRequestParamsTo.getApplicationTypeId() + "' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getRegistrationNo()) && !healthProfessionalApplicationRequestParamsTo.getRegistrationNo().isEmpty()) {
            sb.append("AND rd.registration_no ILIKE '%" + healthProfessionalApplicationRequestParamsTo.getRegistrationNo() + "%' ");
        }

        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getSmcId()) && !healthProfessionalApplicationRequestParamsTo.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = '" + healthProfessionalApplicationRequestParamsTo.getSmcId() + "' ");
        }
        return sb.toString();
    };

    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> SORT_RECORDS = healthProfessionalApplicationRequestParamsTo -> {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY  ");
        sb.append(healthProfessionalApplicationRequestParamsTo.getSortBy());
        sb.append("  ");
        sb.append(healthProfessionalApplicationRequestParamsTo.getSortOrder());
        return sb.toString();
    };

    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> TRACK_APPLICATION = (healthProfessionalApplicationRequestParamsTo) -> {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select doctor_status, smc_status, college_dean_status, college_registrar_status, nmc_status, nbe_status, calculate.hp_profile_id, calculate.request_id, rd.registration_no, rd.created_at,stmc.name, hp.full_name, application_type_id from " +
                        "( " +
                        "select     " +
                        "rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status,   " +
                        "CASE " +
                        "when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' " +
                        "when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' " +
                        "when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' " +
                        "when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' " +
                        "END as doctor_status, " +
                        "CASE " +
                        "when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' " +
                        "when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' " +
                        "END as smc_status, " +
                        "CASE " +
                        "when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'APPROVED' " +
                        "when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' " +
                        "when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "when wf.current_group_id = 4 and wf.action_id IN (1,2,3) THEN 'PENDING' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "END as college_dean_status, " +
                        "CASE " +
                        "when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' " +
                        "when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' " +
                        "when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.current_group_id = 5 and wf.action_id IN (4,3) THEN 'PENDING' " +
                        "when wf.previous_group_id = 5 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 5 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 5 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "END as college_registrar_status, " +
                        "CASE " +
                        "when wf.current_group_id = 7 and wf.action_id IN (1,3,4) THEN 'PENDING' " +
                        "when wf.previous_group_id = 7 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 7 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 7 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' " +
                        "END as nbe_status,    " +
                        "CASE " +
                        "when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.current_group_id = 3 and wf.action_id IN (4,3) THEN 'PENDING' " +
                        "when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "END as nmc_status, " +
                        "wf.id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id " +
                        "from main.work_flow_audit as wf ");
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("WHERE  work_flow_status_id = '" + healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId() + "' ");
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

    private static final Function<HealthProfessionalApplicationRequestParamsTo, String> GET_RECORD_COUNT = (healthProfessionalApplicationRequestParamsTo) -> {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select count(*) from " +
                        "( " +
                        "select     " +
                        "rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status,   " +
                        "CASE " +
                        "when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' " +
                        "when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' " +
                        "when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' " +
                        "when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' " +
                        "END as doctor_status, " +
                        "CASE " +
                        "when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' " +
                        "when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' " +
                        "END as smc_status, " +
                        "CASE " +
                        "when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'APPROVED' " +
                        "when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' " +
                        "when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "when wf.current_group_id = 4 and wf.action_id IN (1,2,3) THEN 'PENDING' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "END as college_dean_status, " +
                        "CASE " +
                        "when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' " +
                        "when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' " +
                        "when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and " +
                        "lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.current_group_id = 5 and wf.action_id IN (4,3) THEN 'PENDING' " +
                        "when wf.previous_group_id = 5 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 5 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 5 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "END as college_registrar_status, " +
                        "CASE " +
                        "when wf.current_group_id = 7 and wf.action_id IN (1,3,4) THEN 'PENDING' " +
                        "when wf.previous_group_id = 7 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 7 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 7 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' " +
                        "END as nbe_status,    " +
                        "CASE " +
                        "when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' " +
                        "when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' " +
                        "when wf.current_group_id = 3 and wf.action_id IN (4,3) THEN 'PENDING' " +
                        "when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' " +
                        "when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' " +
                        "when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' " +
                        "END as nmc_status, " +
                        "wf.id, request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id " +
                        "from main.work_flow_audit as wf ");
        if (Objects.nonNull(healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId()) && !healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId().isEmpty()) {
            sb.append("WHERE  work_flow_status_id = '" + healthProfessionalApplicationRequestParamsTo.getWorkFlowStatusId() + "' ");
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
            healthProfessionalApplicationToList.add(healthProfessionalApplicationTo);
        });
        healthProfessionalApplicationResponseTo.setHealthProfessionalApplications(healthProfessionalApplicationToList);
        healthProfessionalApplicationResponseTo.setTotalNoOfRecords(getCount(healthProfessionalApplicationRequestParamsTo));
        return healthProfessionalApplicationResponseTo;
    }
}
