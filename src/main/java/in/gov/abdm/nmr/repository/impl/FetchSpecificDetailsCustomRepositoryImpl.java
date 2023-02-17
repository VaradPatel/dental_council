package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.DashboardTO;
import in.gov.abdm.nmr.enums.Group;
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

/**
 * A class that implements all the methods of the Custom Repository interface IFetchSpecificDetailsCustomRepository
 * which deals with dashboard count, dashboard fetch specific details
 */
@Repository
@Transactional
@Slf4j
public class FetchSpecificDetailsCustomRepositoryImpl implements IFetchSpecificDetailsCustomRepository {

    /**
     * Injecting a EntityManager bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the DashboardRequestParamsTO
     * object passed as a parameter.
     * @param dashboardRequestParamsTO - object that contains the criteria for the query.
     * @return a string query with appended WHERE clause for the query.
     */
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

        if (Objects.nonNull(dashboardRequestParamsTO.getCouncilId()) && !dashboardRequestParamsTO.getCouncilId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = " + dashboardRequestParamsTO.getCouncilId() + " ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getCollegeId()) && !dashboardRequestParamsTO.getCollegeId().isEmpty()) {
            sb.append("AND qd.college_id = " + dashboardRequestParamsTO.getCollegeId() + " ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getSmcId()) && !dashboardRequestParamsTO.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = " + dashboardRequestParamsTO.getSmcId() + " ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getSmcId()) && !dashboardRequestParamsTO.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = " + dashboardRequestParamsTO.getSmcId() + " ");
        }

    /*    if (Objects.nonNull(dashboardRequestParamsTO.getSmcId()) && !dashboardRequestParamsTO.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = " + dashboardRequestParamsTO.getSmcId() + " ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getSmcId()) && !dashboardRequestParamsTO.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = " + dashboardRequestParamsTO.getSmcId() + " ");
        }*/
        if (Objects.nonNull(dashboardRequestParamsTO.getUserGroupId())){
            BigInteger groupId = dashboardRequestParamsTO.getUserGroupId();
            String userGroupStatus = dashboardRequestParamsTO.getUserGroupStatus().toUpperCase();
            if (!dashboardRequestParamsTO.getUserGroupStatus().contains("Total")) {
                if (groupId.equals(Group.SMC.getId())) {
                    sb.append("AND smc_status = '" + userGroupStatus + "' ");
                } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
                    sb.append("AND college_dean_status = '" + userGroupStatus + "' ");
                } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
                    sb.append("AND college_registrar_status = '" + userGroupStatus + "' ");
                } else if (groupId.equals(Group.NMC.getId())) {
                    sb.append("AND nmc_status = '" + userGroupStatus + "' ");
                } else if (groupId.equals(Group.NBE.getId())) {
                    sb.append("AND nbe_status = '" + userGroupStatus + "' ");
                } else if (groupId.equals(Group.COLLEGE_ADMIN.getId())) {
                    sb.append("AND college_registrar_status = '" + userGroupStatus + "' ");
                    sb.append("AND college_dean_status = '" + userGroupStatus + "' ");
                }
            }
        }
        return sb.toString();
    };

    /**
     * Represents a functional interface to sort the results based on the parameters specified in DashboardRequestParamsTO.
     * @param dashboardRequestParamsTO - which holds the parameters.
     * @return A query string with appended sort order in the format "ORDER BY column_name sort_order"
     */
    private static final Function<DashboardRequestParamsTO, String> SORT_RECORDS = dashboardRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY  ");
        sb.append(dashboardRequestParamsTO.getSortBy());
        sb.append("  ");
        sb.append(dashboardRequestParamsTO.getSortOrder());
        return sb.toString();
    };

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the DashboardRequestParamsTO
     * object passed as a parameter.
     * @param dashboardRequestParamsTO - an object that contains parameters for the function
     * @return a string query to get the request details.
     */
    private static final Function<DashboardRequestParamsTO, String> DASHBOARD = (dashboardRequestParamsTO) -> {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select doctor_status, smc_status, college_dean_status, college_registrar_status, nmc_status, nbe_status, calculate.hp_profile_id, calculate.request_id, rd.registration_no, rd.created_at, stmc.name, hp.full_name " +
                        "from " +
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
                        "END as nbe_status, " +
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
                        "wf.id,request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id " +
                        "from main.work_flow_audit as wf " +
                        "INNER JOIN " +
                        "( " +
                        "select  request_id as lat_request_id, hp_profile_id as lat_hp_profile_id " +
                        "from main.work_flow  " +
                        "order by hp_profile_id desc " +
                        ") latest_wf on request_id = lat_request_id " +
                        "WHERE  work_flow_status_id = " + dashboardRequestParamsTO.getWorkFlowStatusId() + " " +
                        "order by lat_hp_profile_id desc, wf.id asc " +
                        ")  " +
                        "calculate " +
                        "INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id " +
                        "INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id " +
                        "INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id " +
                        "INNER JOIN main.qualification_details as qd on qd.hp_profile_id = hp.id AND qd.request_id = hp.request_id " +
                        "WHERE calculate.hp_profile_id IS NOT NULL and current_status = 1 " +
                        "AND calculate.application_type_id IN ( " + dashboardRequestParamsTO.getApplicationTypeId() + ") ");

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

    /**
     * This method is used to retrieve the count of Dashboard records based on the provided parameters.
     *
     * @param dashboardRequestParamsTO - the parameters used to retrieve the Dashboard records list.
     * @return totalRecords the count of Dashboard records list.
     */
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

    /**
     * Represents a functional interface to generates a dynamic WHERE clause based on the DashboardRequestParamsTO
     * object passed as a parameter.
     *
     * @param dashboardRequestParamsTO - an object that contains parameters for the function
     * @return a query to get the count of the Dashboard records list.
     */
    private static final Function<DashboardRequestParamsTO, String> GET_RECORD_COUNT = (dashboardRequestParamsTO) -> {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select count(*) " +
                        "from " +
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
                        "END as nbe_status, " +
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
                        "wf.id,request_id, hp_profile_id, previous_group_id, current_group_id, action_id, wf.application_type_id " +
                        "from main.work_flow_audit as wf " +
                        "INNER JOIN " +
                        "( " +
                        "select  request_id as lat_request_id, hp_profile_id as lat_hp_profile_id " +
                        "from main.work_flow  " +
                        "order by hp_profile_id desc " +
                        ") latest_wf on request_id = lat_request_id " +
                        "WHERE  work_flow_status_id = " + dashboardRequestParamsTO.getWorkFlowStatusId() + " " +
                        "order by lat_hp_profile_id desc, wf.id asc " +
                        ")  " +
                        "calculate " +
                        "INNER JOIN main.registration_details as rd on rd.hp_profile_id = calculate.hp_profile_id " +
                        "INNER JOIN main.state_medical_council as stmc on rd.state_medical_council_id = stmc.id " +
                        "INNER JOIN main.hp_profile as hp on rd.hp_profile_id = hp.id " +
                        "INNER JOIN main.qualification_details as qd on qd.hp_profile_id = hp.id AND qd.request_id = hp.request_id " +
                        "WHERE calculate.hp_profile_id IS NOT NULL and current_status = 1 AND calculate.application_type_id = " + dashboardRequestParamsTO.getApplicationTypeId() + " ");

        String parameters = DASHBOARD_PARAMETERS.apply(dashboardRequestParamsTO);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        return sb.toString();
    };

    /**
     * Retrieves the details of Dashboard records list based on the provided parameters.
     * @param dashboardRequestParamsTO - object containing the filter criteria for fetching request details
     * @param pagination                                   - object for pagination
     * @return the DashboardResponseTO object representing the response object
     * which contains all the Dashboard records list
     */
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
            dashBoardTO.setDoctorStatus((String) result[0]);
            dashBoardTO.setSmcStatus((String) result[1]);
            dashBoardTO.setCollegeDeanStatus((String) result[2]);
            dashBoardTO.setCollegeRegistrarStatus((String) result[3]);
            dashBoardTO.setNmcStatus((String) result[4]);
            dashBoardTO.setNbeStatus((String) result[5]);
            dashBoardTO.setHpProfileId((BigInteger) result[6]);
            dashBoardTO.setRequestId((String) result[7]);
            dashBoardTO.setRegistrationNo((String) result[8]);
            dashBoardTO.setCreatedAt((String) result[9]);
            dashBoardTO.setCouncilName((String) result[10]);
            dashBoardTO.setApplicantFullName((String) result[11]);
            dashboardTOList.add(dashBoardTO);
        });
        dashBoardResponseTO.setDashboardTOList(dashboardTOList);
        dashBoardResponseTO.setTotalNoOfRecords(getCount(dashboardRequestParamsTO));
        return dashBoardResponseTO;
    }
}