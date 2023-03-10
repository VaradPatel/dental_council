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

import static in.gov.abdm.nmr.util.NMRConstants.FETCH_CARD_DETAILS_COUNT_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_CARD_DETAILS_QUERY;

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
     *
     * @param dashboardRequestParamsTO - object that contains the criteria for the query.
     * @return a string query with appended WHERE clause for the query.
     */
    private static final Function<DashboardRequestParamsTO, String> DASHBOARD_PARAMETERS = dashboardRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(dashboardRequestParamsTO.getSearch()) && !dashboardRequestParamsTO.getSearch().isEmpty()) {
            sb.append("AND ( rd.registration_no ILIKE '%").append(dashboardRequestParamsTO.getSearch()).append("%' OR stmc.name ILIKE '%").append(dashboardRequestParamsTO.getSearch()).append("%' OR hp.full_name ILIKE '%").append(dashboardRequestParamsTO.getSearch()).append("%') ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getName()) && !dashboardRequestParamsTO.getName().isEmpty()) {
            sb.append("AND stmc.name ILIKE '%").append(dashboardRequestParamsTO.getName()).append("%' ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getRegistrationNo()) && !dashboardRequestParamsTO.getRegistrationNo().isEmpty()) {
            sb.append("AND rd.registration_no ILIKE '%").append(dashboardRequestParamsTO.getRegistrationNo()).append("%' ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getCouncilId()) && !dashboardRequestParamsTO.getCouncilId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = ").append(dashboardRequestParamsTO.getCouncilId()).append(" ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getCollegeId()) && !dashboardRequestParamsTO.getCollegeId().isEmpty()) {
            sb.append("AND qd.college_id = ").append(dashboardRequestParamsTO.getCollegeId()).append(" ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getSmcId()) && !dashboardRequestParamsTO.getSmcId().isEmpty()) {
            sb.append("AND rd.state_medical_council_id = ").append(dashboardRequestParamsTO.getSmcId()).append(" ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getWorkFlowStatusId()) && !dashboardRequestParamsTO.getWorkFlowStatusId().isEmpty()) {
            sb.append(" AND  work_flow_status_id = '").append(dashboardRequestParamsTO.getWorkFlowStatusId()).append("' ");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getUserGroupId())) {
            BigInteger groupId = dashboardRequestParamsTO.getUserGroupId();
            String userGroupStatus = dashboardRequestParamsTO.getUserGroupStatus().toUpperCase();
            if (!dashboardRequestParamsTO.getUserGroupStatus().contains("Total")) {
                if (groupId.equals(Group.SMC.getId())) {
                    sb.append("AND smc_status = '").append(userGroupStatus).append("' ");
                } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
                    sb.append("AND college_dean_status = '").append(userGroupStatus).append("' ");
                } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
                    sb.append("AND college_registrar_status = '").append(userGroupStatus).append("' ");
                } else if (groupId.equals(Group.NMC.getId())) {
                    sb.append("AND nmc_status = '").append(userGroupStatus).append("' ");
                } else if (groupId.equals(Group.NBE.getId())) {
                    sb.append("AND nbe_status = '").append(userGroupStatus).append("' ");
                } else if (groupId.equals(Group.COLLEGE_ADMIN.getId())) {
                    sb.append("AND college_registrar_status = '").append(userGroupStatus).append("' ");
                    sb.append("AND college_dean_status = '").append(userGroupStatus).append("' ");
                }
            } else {
                if (groupId.equals(Group.SMC.getId())) {
                    sb.append(" AND smc_status NOT IN ('FORWARDED','NOT YET RECEIVED') ");
                } else if (groupId.equals(Group.COLLEGE_DEAN.getId())) {
                    sb.append(" AND college_dean_status NOT IN ('NOT YET RECEIVED') ");
                } else if (groupId.equals(Group.COLLEGE_REGISTRAR.getId())) {
                    sb.append(" AND college_registrar_status NOT IN ('NOT YET RECEIVED') ");
                } else if (groupId.equals(Group.NMC.getId())) {
                    sb.append(" AND nmc_status NOT IN ('NOT YET RECEIVED') ");
                } else if (groupId.equals(Group.NBE.getId())) {
                    sb.append(" AND nbe_status NOT IN ('NOT YET RECEIVED') ");
                } else if (groupId.equals(Group.COLLEGE_ADMIN.getId())) {
                    sb.append(" AND college_registrar_status NOT IN ('NOT YET RECEIVED') ");
                    sb.append(" AND college_dean_status NOT IN ('NOT YET RECEIVED') ");
                }
            }
        }
        return sb.toString();
    };

    /**
     * Represents a functional interface to sort the results based on the parameters specified in DashboardRequestParamsTO.
     *
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
     *
     * @param dashboardRequestParamsTO - an object that contains parameters for the function
     * @return a string query to get the request details.
     */
    private static final Function<DashboardRequestParamsTO, String> DASHBOARD = dashboardRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        BigInteger groupId = dashboardRequestParamsTO.getUserGroupId();

        sb.append(FETCH_CARD_DETAILS_QUERY);

        if (Objects.nonNull(dashboardRequestParamsTO.getCollegeId()) && !dashboardRequestParamsTO.getCollegeId().isEmpty()) {
            sb.append("INNER JOIN main.qualification_details as qd on qd.hp_profile_id = rd.hp_profile_id AND qd.request_id = rd.request_id ");
        }

        if (Objects.nonNull(groupId) && groupId.equals(Group.NBE.getId())) {
            sb.append("INNER JOIN main.foreign_qualification_details as fqd on fqd.hp_profile_id = rd.hp_profile_id AND fqd.request_id = rd.request_id ");
        }

        sb.append(" WHERE calculate.hp_profile_id IS NOT NULL and current_status = 1 " + "AND calculate.application_type_id IN ( ").append(dashboardRequestParamsTO.getApplicationTypeId()).append( " ) ");

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
    private static final Function<DashboardRequestParamsTO, String> GET_RECORD_COUNT = dashboardRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        BigInteger groupId = dashboardRequestParamsTO.getUserGroupId();

        sb.append(FETCH_CARD_DETAILS_COUNT_QUERY);

        if (Objects.nonNull(dashboardRequestParamsTO.getCollegeId()) && !dashboardRequestParamsTO.getCollegeId().isEmpty()) {
            sb.append("INNER JOIN main.qualification_details as qd on qd.hp_profile_id = rd.hp_profile_id AND qd.request_id = rd.request_id ");
        }

        if (Objects.nonNull(groupId) && groupId.equals(Group.NBE.getId())) {
            sb.append("INNER JOIN main.foreign_qualification_details as fqd on fqd.hp_profile_id = rd.hp_profile_id AND fqd.request_id = rd.request_id ");
        }

        sb.append(" WHERE calculate.hp_profile_id IS NOT NULL and current_status = 1 " + "AND calculate.application_type_id IN ( ").append(dashboardRequestParamsTO.getApplicationTypeId()).append( " ) ");

        String parameters = DASHBOARD_PARAMETERS.apply(dashboardRequestParamsTO);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        return sb.toString();
    };

    /**
     * Retrieves the details of Dashboard records list based on the provided parameters.
     *
     * @param dashboardRequestParamsTO - object containing the filter criteria for fetching request details
     * @param pagination               - object for pagination
     * @return the DashboardResponseTO object representing the response object
     * which contains all the Dashboard records list
     */
    @Override
    public DashboardResponseTO fetchDashboardData(DashboardRequestParamsTO dashboardRequestParamsTO, Pageable pagination) {
        DashboardResponseTO dashBoardResponseTO = new DashboardResponseTO();
        List<DashboardTO> dashboardTOList = new ArrayList<>();

        Query query = entityManager.createNativeQuery(DASHBOARD.apply(dashboardRequestParamsTO));

        query.setFirstResult((pagination.getPageNumber() - 1) * pagination.getPageSize());
        query.setMaxResults(pagination.getPageSize());

        List<Object[]> results = query.getResultList();
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
            dashBoardTO.setWorkFlowStatusId((BigInteger) result[12]);
            dashBoardTO.setPendency((Double) result[13]);
            dashboardTOList.add(dashBoardTO);
        });
        dashBoardResponseTO.setDashboardTOList(dashboardTOList);
        dashBoardResponseTO.setTotalNoOfRecords(getCount(dashboardRequestParamsTO));
        return dashBoardResponseTO;
    }
}