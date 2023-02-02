package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestParamsTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationResponseTO;
import in.gov.abdm.nmr.dto.CollegeRegistrationTO;
import in.gov.abdm.nmr.repository.ICollegeRepositoryCustom;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Repository
@Transactional
@Slf4j
public class CollegeRegistrationImpl implements ICollegeRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private static final Function<CollegeRegistrationRequestParamsTO, String> COLLEGE_REGISTRATION_PARAMETERS = collegeRegistrationRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(collegeRegistrationRequestParamsTO.getSearch()) && !collegeRegistrationRequestParamsTO.getSearch().isEmpty()) {
            sb.append("AND ( upper(c.college_code) LIKE upper('%" + collegeRegistrationRequestParamsTO.getSearch() + "%') OR upper(c.name) LIKE upper('%" + collegeRegistrationRequestParamsTO.getSearch() + "%')  OR upper(smc.name) LIKE upper('%" + collegeRegistrationRequestParamsTO.getSearch() + "%') )");
        }

        if (Objects.nonNull(collegeRegistrationRequestParamsTO.getCollegeId()) && !collegeRegistrationRequestParamsTO.getCollegeId().isEmpty()) {
            sb.append(" AND upper(c.college_code) LIKE upper ('%" + collegeRegistrationRequestParamsTO.getCollegeId() + "%') ");
        }

        if (Objects.nonNull(collegeRegistrationRequestParamsTO.getCollegeName()) && !collegeRegistrationRequestParamsTO.getCollegeName().isEmpty()) {
            sb.append("AND upper(c.name) LIKE upper ('%" + collegeRegistrationRequestParamsTO.getCollegeName() + "%')");
        }

        if (Objects.nonNull(collegeRegistrationRequestParamsTO.getCouncilName()) && !collegeRegistrationRequestParamsTO.getCouncilName().isEmpty()) {
            sb.append("AND upper(smc.name) LIKE upper ('%" + collegeRegistrationRequestParamsTO.getCouncilName() + "%')");
        }
        return sb.toString();
    };
    private static final Function<CollegeRegistrationRequestParamsTO, String> SORT_RECORDS = collegeRegistrationRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER BY  ");
        sb.append(collegeRegistrationRequestParamsTO.getColumnToSort());
        sb.append("  ");
        sb.append(collegeRegistrationRequestParamsTO.getSortOrder());
        return sb.toString();
    };
    private static final Function<CollegeRegistrationRequestParamsTO, String> COLLEGE_REGISTRATION = collegeRegistrationRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append(NMRConstants.FETCH_COLLEGE_REGISTRATION_RECORDS);
        String parameters = COLLEGE_REGISTRATION_PARAMETERS.apply(collegeRegistrationRequestParamsTO);
        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        if (Objects.nonNull(collegeRegistrationRequestParamsTO.getColumnToSort()) && !collegeRegistrationRequestParamsTO.getColumnToSort().isEmpty()) {
            String sortRecords = SORT_RECORDS.apply(collegeRegistrationRequestParamsTO);
            sb.append(sortRecords);
        }
        return sb.toString();
    };


    private static final Function<CollegeRegistrationRequestParamsTO, String> GET_RECORD_COUNT = collegeRegistrationRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append(NMRConstants.FETCH_COUNT_OF_COLLEGE_REGISTRATION_RECORDS);
        String parameters = COLLEGE_REGISTRATION_PARAMETERS.apply(collegeRegistrationRequestParamsTO);

        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }

        return sb.toString();
    };

    private BigInteger getCount(CollegeRegistrationRequestParamsTO collegeRegistrationRequestParamsTO) {
        BigInteger totalRecords = null;
        try {
            Query query = entityManager.createNativeQuery(GET_RECORD_COUNT.apply(collegeRegistrationRequestParamsTO));

            Object result = query.getSingleResult();
            totalRecords = (BigInteger) result;
        } catch (Exception e) {
            log.error("Repository:: getRecords " + e.getMessage());
        }
        return totalRecords;
    }

    @Override
    public CollegeRegistrationResponseTO getCollegeRegistrationData(CollegeRegistrationRequestParamsTO collegeRegistrationRequestParamsTO, Pageable pageable) {
        CollegeRegistrationResponseTO collegeRegistrationResponseTO = new CollegeRegistrationResponseTO();
        List<CollegeRegistrationTO> collegeRegistrationTOS = new ArrayList<>();
        Query query = entityManager.createNativeQuery(COLLEGE_REGISTRATION.apply(collegeRegistrationRequestParamsTO));

        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            CollegeRegistrationTO collegeRegistrationTO = new CollegeRegistrationTO();
            collegeRegistrationTO.setCollegeId((String) result[0]);
            collegeRegistrationTO.setCollegeName((String) result[1]);
            collegeRegistrationTO.setCouncilName((String) result[2]);
            if (result[3] != null) {
                if (Status.APPROVED.getName().equals(result[3])) {
                    collegeRegistrationTO.setStatus(Status.VERIFIED.getName());
                } else if (Status.PENDING.getName().equals(result[3])) {
                    collegeRegistrationTO.setStatus(Status.SUMBMITTED.getName());
                } else {
                    collegeRegistrationTO.setStatus((String) result[3]);
                }
            } else {
                collegeRegistrationTO.setStatus((String) result[3]);
            }
            collegeRegistrationTO.setSubmittedOn((Date) result[4]);
            collegeRegistrationTO.setPendency((Double) result[5]);
            collegeRegistrationTO.setRequestId((String) result[6]);

            collegeRegistrationTOS.add(collegeRegistrationTO);
        });
        collegeRegistrationResponseTO.setTotalNoOfRecords(getCount(collegeRegistrationRequestParamsTO));
        collegeRegistrationResponseTO.setCollegeDetails(collegeRegistrationTOS);
        return collegeRegistrationResponseTO;
    }
}
