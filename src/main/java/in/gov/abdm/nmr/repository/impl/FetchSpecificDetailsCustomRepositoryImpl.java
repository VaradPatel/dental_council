package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.DashboardRequestParamsTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.DashboardTO;
import in.gov.abdm.nmr.enums.DashboardStatus;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.repository.IFetchSpecificDetailsCustomRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Stream;

import static in.gov.abdm.nmr.util.NMRConstants.*;

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

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getWorkFlowStatusId())) {
            sb.append(" AND  work_flow_status_id = :workFlowStatusId");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getCollegeId())) {
            sb.append(" AND qd.college_id = :collegeId");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getSearch())) {
            sb.append(" AND (rd.registration_no ILIKE :search OR stmc.name ILIKE :search OR hp.full_name ILIKE :search)");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getCouncilName())) {
            sb.append(" AND stmc.name ILIKE :councilName");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getSmcId())) {
            sb.append(" AND rd.state_medical_council_id = :smcId");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getRegistrationNumber())) {
            sb.append(" AND rd.registration_no ILIKE :registrationNumber");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getApplicantFullName())) {
            sb.append(" AND hp.full_name ILIKE :applicantFullName");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getCouncilId())) {
            sb.append(" AND rd.state_medical_council_id = :councilId");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getGender())) {
            sb.append(" AND hp.gender ILIKE :gender");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getEmailId())) {
            sb.append(" AND hp.email_id ILIKE :emailId");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getMobileNumber())) {
            sb.append(" AND hp.mobile_number ILIKE :mobileNumber");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getYearOfRegistration())) {
            sb.append(" AND EXTRACT(YEAR FROM rd.registration_date) = :yearOfRegistration");
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getRequestId())) {
            sb.append(" AND d.request_id ILIKE :requestId");
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getUserGroupId())) {
            BigInteger groupId = dashboardRequestParamsTO.getUserGroupId();
            if (!dashboardRequestParamsTO.getUserGroupStatus().contains("Total")) {
                if (groupId.equals(Group.SMC.getId())) {
                    sb.append(" AND smc_status in (:userGroupStatus)");
                } else if (groupId.equals(Group.COLLEGE.getId())) {
                    sb.append(" AND college_status = :userGroupStatus");
                } else if (groupId.equals(Group.NMC.getId())) {
                    sb.append(" AND nmc_status in (:userGroupStatus)");
                } else if (groupId.equals(Group.NBE.getId())) {
                    sb.append(" AND nbe_status = :userGroupStatus");
                }
            } else {
                if (groupId.equals(Group.SMC.getId())) {
                    sb.append(" AND smc_status IN (1,2,3,4,5,6,7,8)");
                } else
                    if (groupId.equals(Group.COLLEGE.getId()) && !dashboardRequestParamsTO.getApplicationTypeId().equals("1,8")) {
                        sb.append(" AND college_status IN (1,3,4,5)");
                    } else if (groupId.equals(Group.NMC.getId())) {
                        sb.append(" AND nmc_status IN (1,3,4,5,6,7)");
                    } else
                        if (groupId.equals(Group.NBE.getId()) && !dashboardRequestParamsTO.getApplicationTypeId().equals("7")) {
                            sb.append(" AND nbe_status IN (1,3,4,5)");
                        }
            }
        }
        return sb.toString();
    };

    /**
     * Represents a functional interface to sort the results based on the parameters specified in
     * DashboardRequestParamsTO.
     *
     * @param dashboardRequestParamsTO - which holds the parameters.
     * @return A query string with appended sort order in the format "ORDER BY column_name sort_order"
     */
    private static final Function<DashboardRequestParamsTO, String> SORT_RECORDS = dashboardRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append(" ORDER BY  " + dashboardRequestParamsTO.getSortBy() + " " + dashboardRequestParamsTO.getSortOrder());

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

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getCollegeId())) {
            sb.append("INNER JOIN main.qualification_details as qd on qd.hp_profile_id = rd.hp_profile_id AND qd.request_id = d.request_id ");
        }

        if (Objects.nonNull(groupId) && groupId.equals(Group.NBE.getId())) {
            sb.append("INNER JOIN main.foreign_qualification_details as fqd on fqd.hp_profile_id = rd.hp_profile_id AND fqd.request_id = d.request_id ");
        }

        sb.append(" WHERE d.hp_profile_id IS NOT NULL AND d.application_type_id IN (:applicationTypeId)");

        String parameters = DASHBOARD_PARAMETERS.apply(dashboardRequestParamsTO);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getSortBy())) {
            String sortRecords = SORT_RECORDS.apply(dashboardRequestParamsTO);
            sb.append(sortRecords);
        }

        log.debug("Query : {}", sb.toString());
        return sb.toString();
    };


    /**
     * Retrieves the details of Dashboard records list based on the provided parameters.
     *
     * @param dashboardRequestParamsTO - object containing the filter criteria for fetching request details
     * @param pageable                 - object for pagination
     * @return the DashboardResponseTO object representing the response object
     *         which contains all the Dashboard records list
     */
    @Override
    public DashboardResponseTO fetchDashboardData(DashboardRequestParamsTO dashboardRequestParamsTO, Pageable pageable) {
        DashboardResponseTO dashBoardResponseTO = new DashboardResponseTO();
        dashBoardResponseTO.setTotalNoOfRecords(BigInteger.ZERO);
        List<DashboardTO> dashboardTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(DASHBOARD.apply(dashboardRequestParamsTO));
        setParameters(query, dashboardRequestParamsTO);

        log.debug("Fetched dashboard detail successfully.");
        query.setFirstResult(pageable.getPageNumber() != 0 ? (pageable.getPageNumber() - 1) * pageable.getPageSize() : 0);
        query.setMaxResults(pageable.getPageSize());
        @SuppressWarnings("unchecked") List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            DashboardTO dashBoardTO = new DashboardTO();
            dashBoardTO.setDoctorStatus(result[0] != null ? WorkflowStatus.getWorkflowStatus((BigInteger) result[0]).getDescription() : "");
            dashBoardTO.setSmcStatus(result[1] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[1]).getSmcStatus() : NOT_YET_RECEIVED);
            dashBoardTO.setNmcStatus(result[2] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[2]).getStatus() : NOT_YET_RECEIVED);
            dashBoardTO.setNbeStatus(result[3] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[3]).getStatus() : NOT_YET_RECEIVED);
            dashBoardTO.setHpProfileId((BigInteger) result[4]);
            dashBoardTO.setRequestId((String) result[5]);
            dashBoardTO.setRegistrationNo((String) result[6]);
            dashBoardTO.setCreatedAt(result[7].toString());
            dashBoardTO.setCouncilName((String) result[8]);
            dashBoardTO.setApplicantFullName((String) result[9]);
            dashBoardTO.setWorkFlowStatusId((BigInteger) result[10]);
            dashBoardTO.setPendency(result[11] != null ? (int) Math.floor((Double) result[11]): 0);
            dashBoardTO.setGender((String) result[12]);
            dashBoardTO.setEmailId((String) result[13]);
            dashBoardTO.setMobileNumber((String) result[14]);
            dashBoardTO.setNmrId((String) result[15]);
            dashBoardTO.setYearOfRegistration(result[16] != null ? result[16].toString() : "");
            dashBoardTO.setCollegeStatus(result[17] != null ? DashboardStatus.getDashboardStatus((BigInteger) result[17]).getStatus() : NOT_YET_RECEIVED);
            dashBoardTO.setApplicationTypeId((BigInteger) result[18]);
            dashBoardResponseTO.setTotalNoOfRecords((BigInteger) result[19]);
            dashboardTOList.add(dashBoardTO);
        });
        dashBoardResponseTO.setDashboardTOList(dashboardTOList);
        return dashBoardResponseTO;
    }

    private Query setParameters(Query query, DashboardRequestParamsTO dashboardRequestParamsTO) {
        query.setParameter("applicationTypeId", Stream.of(dashboardRequestParamsTO.getApplicationTypeId().split(",")).map(Integer::parseInt).toList());

        if (Objects.nonNull(dashboardRequestParamsTO.getWorkFlowStatusId()) && !dashboardRequestParamsTO.getWorkFlowStatusId().isEmpty()) {
            query.setParameter("workFlowStatusId", Integer.parseInt(dashboardRequestParamsTO.getWorkFlowStatusId()));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getCollegeId()) && !dashboardRequestParamsTO.getCollegeId().isEmpty()) {
            query.setParameter("collegeId", Integer.parseInt(dashboardRequestParamsTO.getCollegeId()));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getSearch()) && !dashboardRequestParamsTO.getSearch().isEmpty()) {
            query.setParameter("search", "%".concat(dashboardRequestParamsTO.getSearch()).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getCouncilName()) && !dashboardRequestParamsTO.getCouncilName().isEmpty()) {
            query.setParameter("councilName", "%".concat(dashboardRequestParamsTO.getCouncilName()).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getSmcId()) && !dashboardRequestParamsTO.getSmcId().isEmpty()) {
            query.setParameter("smcId", Integer.parseInt(dashboardRequestParamsTO.getSmcId()));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getRegistrationNumber()) && !dashboardRequestParamsTO.getRegistrationNumber().isEmpty()) {
            query.setParameter("registrationNumber", "%".concat(dashboardRequestParamsTO.getRegistrationNumber()).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getApplicantFullName()) && !dashboardRequestParamsTO.getApplicantFullName().isEmpty()) {
            query.setParameter("applicantFullName", "%".concat(dashboardRequestParamsTO.getApplicantFullName()).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getCouncilId()) && !dashboardRequestParamsTO.getCouncilId().isEmpty()) {
            query.setParameter("councilId", Integer.parseInt(dashboardRequestParamsTO.getCouncilId()));
        }

        String gender = dashboardRequestParamsTO.getGender();
        if (StringUtils.isNotBlank(gender)) {
            if (GENDER_MALE.equalsIgnoreCase(gender)) {
                gender = "m";
            } else if (GENDER_FEMALE.equalsIgnoreCase(gender)) {
                gender = "f";
            }
            query.setParameter("gender", "%".concat(gender).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getEmailId()) && !dashboardRequestParamsTO.getEmailId().isEmpty()) {
            query.setParameter("emailId", "%".concat(dashboardRequestParamsTO.getEmailId()).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getMobileNumber()) && !dashboardRequestParamsTO.getMobileNumber().isEmpty()) {
            query.setParameter("mobileNumber", "%".concat(dashboardRequestParamsTO.getMobileNumber()).concat("%"));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getYearOfRegistration()) && !dashboardRequestParamsTO.getYearOfRegistration().isEmpty()) {
            query.setParameter("yearOfRegistration", Integer.parseInt(dashboardRequestParamsTO.getYearOfRegistration()));
        }

        if (Objects.nonNull(dashboardRequestParamsTO.getUserGroupId())) {
            BigInteger groupId = dashboardRequestParamsTO.getUserGroupId();
            if (!dashboardRequestParamsTO.getUserGroupStatus().contains("Total") && !groupId.equals(Group.HEALTH_PROFESSIONAL.getId())) {
                List<Integer> userGroupStatus = Stream.of(dashboardRequestParamsTO.getUserGroupStatus().split(",")).map(Integer::parseInt).toList();
                query.setParameter("userGroupStatus", userGroupStatus);
            }
        }

        if (StringUtils.isNotBlank(dashboardRequestParamsTO.getRequestId())) {
            query.setParameter("requestId", "%".concat(dashboardRequestParamsTO.getRequestId()).concat("%"));
        }
        
        return query;

    }
}