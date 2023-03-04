package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalRequestParam;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalTO;
import in.gov.abdm.nmr.repository.IWorkFlowCustomRepository;
import in.gov.abdm.nmr.util.NMRConstants;
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

/**
 * A class for the customized work flow repository that implements the methods in the customized repository interface
 * IWorkFlowCustomRepository with the help of customized query.
 */
@Repository
@Transactional
@Slf4j
public class WorkFlowCustomRepositoryImpl implements IWorkFlowCustomRepository {
    @PersistenceContext
    EntityManager entityManager;
    private static final Function<ReactivateHealthProfessionalRequestParam, String> REACTIVATION_SEARCH_PARAMETERS = reactivateHealthProfessionalQueryParam -> {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getSearch()) && (!reactivateHealthProfessionalQueryParam.getSearch().isEmpty())) {
            sb.append("  AND (cast(r.id as varchar) LIKE '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR r.full_name ILIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR cast(r.created_at as varchar) LIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR cast(r.start_date as varchar) LIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR r.remarks ILIKE '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR r.suspension_type ILIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append("  OR r.registration_id ILIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%') ");
        }
        return sb.toString();
    };
    private static final Function<ReactivateHealthProfessionalRequestParam, String> SORT_RECORDS = reactivateHealthProfessionalQueryParam -> {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        sb.append("ORDER BY  ");
        sb.append(reactivateHealthProfessionalQueryParam.getSortBy());
        sb.append("  ");
        sb.append(reactivateHealthProfessionalQueryParam.getSortType());
        return sb.toString();
    };
    private static final Function<ReactivateHealthProfessionalRequestParam, String> REACTIVATE_HEALTH_PROFESSIONAL = reactivateHealthProfessionalQueryParam -> {
        StringBuilder sb = new StringBuilder();
        sb.append(NMRConstants.FETCH_REACTIVATION_RECORDS);
        String parameters = REACTIVATION_SEARCH_PARAMETERS.apply(reactivateHealthProfessionalQueryParam);
        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getSortBy()) && !reactivateHealthProfessionalQueryParam.getSortBy().isEmpty()) {
            String sortRecords = SORT_RECORDS.apply(reactivateHealthProfessionalQueryParam);
            sb.append(sortRecords);
        }
        return sb.toString();
    };

    private static final Function<ReactivateHealthProfessionalRequestParam, String> GET_RECORD_COUNT = reactivateHealthProfessionalRequestParam -> {
        StringBuilder sb = new StringBuilder();
        sb.append(NMRConstants.FETCH_COUNT_OF_REACTIVATION_RECORDS);
        String parameters = REACTIVATION_SEARCH_PARAMETERS.apply(reactivateHealthProfessionalRequestParam);
        if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
            sb.append(parameters);
        }
        return sb.toString();
    };

    private BigInteger getCount(ReactivateHealthProfessionalRequestParam reactivateHealthProfessionalRequestParam) {
        BigInteger totalRecords = null;
        try {
            Query query = entityManager.createNativeQuery(GET_RECORD_COUNT.apply(reactivateHealthProfessionalRequestParam));

            Object result = query.getSingleResult();
            totalRecords = (BigInteger) result;
        } catch (Exception e) {
            log.error("Repository: getRecords " + e.getMessage());
        }
        return totalRecords;
    }

    /**
     * Customized  method for fetching the reactivation records
     * of the health professionals for the NMC to approve or reject the reactivation request.
     *
     * @param reactivateHealthProfessionalQueryParam - Object that has the query params that has been passed to search, sort and pagination
     * @param pageable                               - Object of Pageable that helps in pagination
     * @return the ReactivateHealthProfessionalResponseTO  response Object
     * which contains all the details related to the health professionals who have
     * raised a request to NMC to reactivate their profiles
     */
    @Override
    public ReactivateHealthProfessionalResponseTO getReactivationRecordsOfHealthProfessionalsToNmc(ReactivateHealthProfessionalRequestParam reactivateHealthProfessionalQueryParam, Pageable pageable) {
        ReactivateHealthProfessionalResponseTO reactivateHealthProfessionalResponseTO = new ReactivateHealthProfessionalResponseTO();
        List<ReactivateHealthProfessionalTO> reactivateHealthProfessionalTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(REACTIVATE_HEALTH_PROFESSIONAL.apply(reactivateHealthProfessionalQueryParam));

       query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
       query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            ReactivateHealthProfessionalTO reactivateHealthProfessionalTO = new ReactivateHealthProfessionalTO();
            reactivateHealthProfessionalTO.setHealthProfessionalId((BigInteger) result[0]);
            reactivateHealthProfessionalTO.setRegistrationId((String) result[1]);
            reactivateHealthProfessionalTO.setRequestId((String) result[2]);
            reactivateHealthProfessionalTO.setHealthProfessionalName((String) result[3]);
            reactivateHealthProfessionalTO.setSubmittedDate((Date) result[4]);
            reactivateHealthProfessionalTO.setReactivation((Date) result[5]);
            reactivateHealthProfessionalTO.setTypeOfSuspension((String) result[6]);
            reactivateHealthProfessionalTO.setRemarks((String) result[7]);
            reactivateHealthProfessionalTOList.add(reactivateHealthProfessionalTO);
        });
        reactivateHealthProfessionalResponseTO.setHealthProfessionalDetails(reactivateHealthProfessionalTOList);
        reactivateHealthProfessionalResponseTO.setTotalNoOfRecords(getCount(reactivateHealthProfessionalQueryParam));
        return reactivateHealthProfessionalResponseTO;
    }
}
