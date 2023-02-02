package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsCustomRepository;
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
public class FetchSpecificDetailsCustomRepositoryImpl implements IFetchSpecificDetailsCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Function<DashboardRequestParamsTO, String> DASHBOARD_PARAMETERS = (dashboardRequestParamsTO) -> {
        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(dashboardRequestParamsTO.getSearch()) && !dashboardRequestParamsTO.getSearch().isEmpty()) {
            sb.append("AND ( rd.registration_no ILIKE '%" + dashboardRequestParamsTO.getSearch() + "%' OR stmc.name ILIKE '%" + dashboardRequestParamsTO.getSearch() + "%' OR hp.full_name ILIKE '%" + dashboardRequestParamsTO.getSearch() + "%') ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getName()) && !dashboardRequestParamsTO.getName().isEmpty()) {
            sb.append("AND stmc.name ILIKE '%" + dashboardRequestParamsTO.getName() + "%' ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getNmrId()) && !dashboardRequestParamsTO.getNmrId().isEmpty()) {
            sb.append("AND rd.registration_no ILIKE '%" + dashboardRequestParamsTO.getNmrId() + "%' ");
        }
        return sb.toString();
    };

    private static final Function<DashboardRequestParamsTO, String> SORT_RECORDS = dashboardRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY  ");
        sb.append(dashboardRequestParamsTO.getSortBy());
        sb.append("  ");
        sb.append(dashboardRequestParamsTO.getSortOrder());
        return sb.toString();
    };

    private static final Function<DashboardRequestParamsTO, String> DASHBOARD = (dashboardRequestParamsTO) -> {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT doctor, smc, College_Dean, College_Registrar, nmc,calculate.hp_profile_id, calculate.request_id ,rd.registration_no, rd.created_at,stmc.name,hp.full_name from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as SMC, CASE when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and wf.action_id IN (1,2,3) THEN 'PENDING' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and wf.action_id = 3 THEN 'QUERY RAISED' END as College_Dean, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 5 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and wf.action_id = 3 THEN 'QUERY RAISED' END as College_Registrar, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "WHEN wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 3 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as NMC, wf.id,request_id, hp_profile_id, previous_group_id, current_group_id, action_id from work_flow_audit as wf INNER JOIN ( select  request_id as lat_request_id, hp_profile_id as lat_hp_profile_id from work_flow order by hp_profile_id desc ) latest_wf on request_id = lat_request_id " +
                        "WHERE  work_flow_status_id = " + dashboardRequestParamsTO.getWorkFlowStatusId() + " " +
                        "ORDER BY lat_hp_profile_id desc, wf.id asc ) calculate INNER JOIN registration_details as rd ON rd.hp_profile_id = calculate.hp_profile_id " +
                        "INNER JOIN state_medical_council as stmc ON rd.state_medical_council_id = stmc.id INNER JOIN hp_profile as hp ON rd.hp_profile_id = hp.id " +
                        "WHERE calculate.hp_profile_id IS NOT NULL and current_status = 1 ");

        String parameters = DASHBOARD_PARAMETERS.apply(dashboardRequestParamsTO);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        if (Objects.nonNull(dashboardRequestParamsTO.getSortBy()) && !dashboardRequestParamsTO.getSortBy().isEmpty()) {
            String sortRecords = SORT_RECORDS.apply(dashboardRequestParamsTO);
            sb.append(sortRecords);
        }
        return sb.toString();
    };

    private BigInteger getCount(DashboardRequestParamsTO dashboardRequestParamsTO) {
        BigInteger totalRecords = null;
        try {
            Query query = entityManager.createNativeQuery(GET_RECORD_COUNT.apply(dashboardRequestParamsTO));
            Object result = query.getSingleResult();
            totalRecords = (BigInteger) result;
        } catch (Exception e) {
            log.error("Repository:: getRecords " + e.getMessage());
        }
        return totalRecords;
    }

    private static final Function<DashboardRequestParamsTO, String> GET_RECORD_COUNT = (dashboardRequestParamsTO) -> {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT count(*) from ( select rank() over (PARTITION BY hp_profile_id order by wf.id desc) as current_status, CASE when wf.previous_group_id IN (2,3,4) and wf.action_id = 5 THEN 'SUBMITTED' when wf.previous_group_id = 1 and wf.action_id = 1 THEN 'SUBMITTED' when wf.current_group_id = 1 and wf.action_id = 3 THEN 'PENDING' when wf.previous_group_id IN (2,3,4,5) and wf.action_id IN (2,4,5) THEN 'SUBMITTED' END as doctor, CASE when wf.current_group_id = 2 and wf.action_id IN (1,3,4) THEN 'PENDING' when wf.previous_group_id = 2 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id = 2 THEN 'FORWARDED' when wf.previous_group_id = 2 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 2 and wf.action_id = 3 THEN 'QUERY RAISED' when wf.previous_group_id IN (4,5) and wf.action_id IN (3,4,5) THEN 'FORWARDED' when wf.previous_group_id = 3 and wf.action_id IN (3,4,5) THEN 'APPROVED' END as SMC, CASE when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) IN (3,4,5) THEN 'APPROVED' when wf.previous_group_id = 2 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.current_group_id = 4 and wf.action_id IN (1,2,3) THEN 'PENDING' when wf.previous_group_id = 4 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 4 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 4 and wf.action_id = 3 THEN 'QUERY RAISED' END as College_Dean, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when lag(wf.previous_group_id) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when lag(wf.previous_group_id,2) over (partition by hp_profile_id order by id) = 5 and lag(wf.action_id,2) over (partition by hp_profile_id order by id) = 4 THEN 'APPROVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 5 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 5 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 5 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 5 and wf.action_id = 3 THEN 'QUERY RAISED' END as College_Registrar, CASE when wf.previous_group_id = 4 and wf.action_id IN (3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 2 and wf.action_id IN (2,3,5) THEN 'NOT YET RECEIVED' when wf.previous_group_id = 1 and wf.action_id IN (1,3) THEN 'NOT YET RECEIVED' " +
                        "WHEN wf.previous_group_id = 4 and wf.action_id = 4 THEN 'NOT YET RECEIVED' when wf.previous_group_id = 5 and wf.action_id IN (3,4,5) THEN 'NOT YET RECEIVED' when wf.current_group_id = 3 and wf.action_id IN (4,3) THEN 'PENDING' when wf.previous_group_id = 3 and wf.action_id = 4 THEN 'APPROVED' when wf.previous_group_id = 3 and wf.action_id = 5 THEN 'REJECTED' when wf.previous_group_id = 3 and wf.action_id = 3 THEN 'QUERY RAISED' END as NMC, wf.id,request_id, hp_profile_id, previous_group_id, current_group_id, action_id from work_flow_audit as wf INNER JOIN ( select  request_id as lat_request_id, hp_profile_id as lat_hp_profile_id from work_flow order by hp_profile_id desc ) latest_wf on request_id = lat_request_id " +
                        "WHERE  work_flow_status_id = " + dashboardRequestParamsTO.getWorkFlowStatusId() + " " +
                        "ORDER BY lat_hp_profile_id desc, wf.id asc ) calculate INNER JOIN registration_details as rd ON rd.hp_profile_id = calculate.hp_profile_id " +
                        "INNER JOIN state_medical_council as stmc ON rd.state_medical_council_id = stmc.id INNER JOIN hp_profile as hp ON rd.hp_profile_id = hp.id " +
                        "WHERE calculate.hp_profile_id IS NOT NULL and current_status = 1 ");

        String parameters = DASHBOARD_PARAMETERS.apply(dashboardRequestParamsTO);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        return sb.toString();
    };


    @Override
    public DashboardResponseTO fetchDashboardData(DashboardRequestParamsTO dashboardRequestParamsTO, Pageable pagination) {
        DashboardResponseTO dashBoardResponseTO = new DashboardResponseTO();
        List<DashboardTO> dashboardTOList = new ArrayList<>();

        Query query  = entityManager.createNativeQuery(DASHBOARD.apply(dashboardRequestParamsTO));

        query.setFirstResult((pagination.getPageNumber() - 1) * pagination.getPageSize());
        query.setMaxResults(pagination.getPageSize());

        List<Object[]> results =query.getResultList();
        results.forEach(result -> {
            DashboardTO dashBoardTO = new DashboardTO();
            dashBoardTO.setDoctor((String) result[0]);
            dashBoardTO.setSmc((String) result[1]);
            dashBoardTO.setCollegeDean((String) result[2]);
            dashBoardTO.setCollegeRegistrar((String) result[3]);
            dashBoardTO.setNmc((String) result[4]);
            dashBoardTO.setHpProfileId((BigInteger) result[5]);
            dashBoardTO.setRequestId((String) result[6]);
            dashBoardTO.setRegistrationNo((String) result[7]);
            dashBoardTO.setCreatedAt((String) result[8]);
            dashBoardTO.setName((String) result[9]);
            dashBoardTO.setFullName((String) result[10]);
            dashboardTOList.add(dashBoardTO);
        });
        dashBoardResponseTO.setDashboardTOList(dashboardTOList);
        dashBoardResponseTO.setTotalNoOfRecords(getCount(dashboardRequestParamsTO));
        return dashBoardResponseTO;
    }
}
