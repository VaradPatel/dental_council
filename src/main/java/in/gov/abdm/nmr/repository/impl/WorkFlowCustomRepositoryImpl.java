package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.minio.connector.service.S3ServiceImpl;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalRequestParam;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalTO;
import in.gov.abdm.nmr.repository.IWorkFlowCustomRepository;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Value("${feature.toggle.minio.enable}")
    private boolean minioEnable;

    @Autowired
    S3ServiceImpl s3Service;
    private static final Function<ReactivateHealthProfessionalRequestParam, String> REACTIVATION_SEARCH_PARAMETERS = reactivateHealthProfessionalQueryParam -> {
        StringBuilder sb = new StringBuilder();

        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getGroupId()) && (!reactivateHealthProfessionalQueryParam.getGroupId().isEmpty())) {
            sb.append(" AND current_group_id IN ( " + reactivateHealthProfessionalQueryParam.getGroupId() + " ) ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getSearch()) && (!reactivateHealthProfessionalQueryParam.getSearch().isEmpty())) {
            sb.append("  AND (cast(hp.id as varchar) LIKE '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR hp.full_name ILIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR cast(wf.created_at as varchar) LIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR cast(wf.start_date as varchar) LIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR wf.remarks ILIKE '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR hp.hp_profile_status_id  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%'  ");
            sb.append(" OR hp.registration_id ILIKE  '%" + reactivateHealthProfessionalQueryParam.getSearch() + "%') ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getApplicantFullName()) && (!reactivateHealthProfessionalQueryParam.getApplicantFullName().isEmpty())) {
            sb.append(" AND hp.full_name ILIKE  '%" + reactivateHealthProfessionalQueryParam.getApplicantFullName() + "%'  ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getRegistrationNumber()) && (!reactivateHealthProfessionalQueryParam.getRegistrationNumber().isEmpty())) {
            sb.append(" AND hp.registration_id ILIKE  '%" + reactivateHealthProfessionalQueryParam.getRegistrationNumber() + "%' ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getGender()) && !reactivateHealthProfessionalQueryParam.getGender().isEmpty()) {
            sb.append(" AND hp.gender ILIKE '%").append(reactivateHealthProfessionalQueryParam.getGender()).append("%' ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getEmailId()) && !reactivateHealthProfessionalQueryParam.getEmailId().isEmpty()) {
            sb.append(" AND hp.email_id ILIKE '%").append(reactivateHealthProfessionalQueryParam.getEmailId()).append("%' ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getMobileNumber()) && !reactivateHealthProfessionalQueryParam.getMobileNumber().isEmpty()) {
            sb.append(" AND hp.mobile_number ILIKE '%").append(reactivateHealthProfessionalQueryParam.getMobileNumber()).append("%' ");
        }
        if (Objects.nonNull(reactivateHealthProfessionalQueryParam.getYearOfRegistration()) && !reactivateHealthProfessionalQueryParam.getYearOfRegistration().isEmpty()) {
            sb.append(" AND wf.created_at ILIKE '%").append(reactivateHealthProfessionalQueryParam.getYearOfRegistration()).append("%' ");
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
        sb.append(NMRConstants.FETCH_REACTIVATION_REQUESTS);
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
        reactivateHealthProfessionalResponseTO.setTotalNoOfRecords(BigInteger.ZERO);
        List<ReactivateHealthProfessionalTO> reactivateHealthProfessionalTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(REACTIVATE_HEALTH_PROFESSIONAL.apply(reactivateHealthProfessionalQueryParam));

       query.setFirstResult(pageable.getPageNumber() != 0 ?((pageable.getPageNumber() - 1) * pageable.getPageSize()) : 0);
       query.setMaxResults(pageable.getPageSize());

        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            SimpleDateFormat dateFormat =new SimpleDateFormat("dd-MM-yyyy");
            ReactivateHealthProfessionalTO reactivateHealthProfessionalTO = new ReactivateHealthProfessionalTO();
            reactivateHealthProfessionalTO.setHealthProfessionalId((BigInteger) result[0]);
            reactivateHealthProfessionalTO.setRegistrationId((String) result[1]);
            reactivateHealthProfessionalTO.setRequestId((String) result[2]);
            reactivateHealthProfessionalTO.setHealthProfessionalName((String) result[3]);
            reactivateHealthProfessionalTO.setSubmittedDate(result[4] != null ? dateFormat.format(result[4]) : "");
            reactivateHealthProfessionalTO.setReactivation(result[5] != null ? dateFormat.format(result[5]) : "");
            reactivateHealthProfessionalTO.setGender((String) result[6]);
            reactivateHealthProfessionalTO.setEmailId((String) result[7]);
            reactivateHealthProfessionalTO.setMobileNumber((String) result[8]);
            reactivateHealthProfessionalTO.setTypeOfSuspension((BigInteger) result[9]);
            reactivateHealthProfessionalTO.setRemarks((String) result[10]);
            reactivateHealthProfessionalResponseTO.setTotalNoOfRecords((BigInteger) result[11]);

            if(minioEnable){
                try {
                    byte[] file=s3Service.downloadFile((String) result[12]);
                    reactivateHealthProfessionalTO.setReactivationFile(Base64.getEncoder().encodeToString(file));
                } catch (IOException e) {
                    log.error("Something went wrong while downloading file from minio", e);
                }
            }
            else {
                if (result[13] != null) {
                    reactivateHealthProfessionalTO.setReactivationFile(Base64.getEncoder().encodeToString((byte[]) result[13]));
                }
            }
            if(result[14]!=null) {
                String fileName = (String) result[14];
                reactivateHealthProfessionalTO.setFileName(fileName);
                reactivateHealthProfessionalTO.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
            }
            reactivateHealthProfessionalTOList.add(reactivateHealthProfessionalTO);
        });
        reactivateHealthProfessionalResponseTO.setHealthProfessionalDetails(reactivateHealthProfessionalTOList);
        return reactivateHealthProfessionalResponseTO;
    }
}
