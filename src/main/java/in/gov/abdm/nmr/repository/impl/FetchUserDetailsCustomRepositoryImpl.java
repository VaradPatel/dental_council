package in.gov.abdm.nmr.repository.impl;

import in.gov.abdm.nmr.dto.UserRequestParamsTO;
import in.gov.abdm.nmr.dto.UserResponseTO;
import in.gov.abdm.nmr.dto.UserTO;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.repository.IFetchUserDetailsCustomRepository;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Repository
@Transactional
@Slf4j
public class FetchUserDetailsCustomRepositoryImpl implements IFetchUserDetailsCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final Function<UserRequestParamsTO, String> SORT_RECORDS = userRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        sb.append(" ORDER BY  ");
        sb.append(userRequestParamsTO.getSortBy());
        sb.append("  ");
        sb.append(userRequestParamsTO.getSortOrder());
        return sb.toString();
    };

    private static final Function<UserRequestParamsTO, String> USER_PARAMETERS_FOR_CLG = userRequestParamsTO -> {
        StringBuilder stringBuilder = new StringBuilder();

        if (Objects.nonNull(userRequestParamsTO.getFirstName()) && !userRequestParamsTO.getFirstName().isEmpty()) {
            stringBuilder.append(" AND name ILIKE '%").append(userRequestParamsTO.getFirstName()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getLastName()) && !userRequestParamsTO.getLastName().isEmpty()) {
            stringBuilder.append(" AND name ILIKE '%").append(userRequestParamsTO.getLastName()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getEmailId()) && !userRequestParamsTO.getEmailId().isEmpty()) {
            stringBuilder.append(" AND u.email ILIKE '%").append(userRequestParamsTO.getEmailId()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getMobileNumber()) && !userRequestParamsTO.getMobileNumber().isEmpty()) {
            stringBuilder.append(" AND u.mobile_number ILIKE '%").append(userRequestParamsTO.getMobileNumber()).append("%' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserTypeId()) && !userRequestParamsTO.getUserTypeId().isEmpty()) {
            stringBuilder.append(" AND u.user_type_id = '").append(userRequestParamsTO.getUserTypeId()).append("' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getName()) && !userRequestParamsTO.getName().isEmpty()) {
            stringBuilder.append(" and a.name ILIKE '%").append(userRequestParamsTO.getName()).append("%' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserSubTypeID()) && !userRequestParamsTO.getUserSubTypeID().isEmpty()) {

            if (UserSubTypeEnum.NMC_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                stringBuilder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.NMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.NMC_VERIFIER.getId() + "," +
                                UserSubTypeEnum.NBE_ADMIN.getId() + "," +
                                UserSubTypeEnum.COLLEGE_ADMIN.getId()).append(") ");
            }
            if (UserSubTypeEnum.SMC_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                stringBuilder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.SMC_VERIFIER.getId()).append(") ");
            }
            if (UserSubTypeEnum.NBE_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                stringBuilder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.NBE_ADMIN.getId() + "," +
                                UserSubTypeEnum.NBE_VERIFIER.getId()).append(") ");
            }
        }
        return stringBuilder.toString();
    };

    private static final Function<UserRequestParamsTO, String> USER_PARAMETERS = userRequestParamsTO -> {
        StringBuilder builder = new StringBuilder();

        if (Objects.nonNull(userRequestParamsTO.getFirstName()) && !userRequestParamsTO.getFirstName().isEmpty()) {
            builder.append(" AND first_name ILIKE '%").append(userRequestParamsTO.getFirstName()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getLastName()) && !userRequestParamsTO.getLastName().isEmpty()) {
            builder.append(" AND last_name ILIKE '%").append(userRequestParamsTO.getLastName()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getEmailId()) && !userRequestParamsTO.getEmailId().isEmpty()) {
            builder.append(" AND u.email ILIKE '%").append(userRequestParamsTO.getEmailId()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getMobileNumber()) && !userRequestParamsTO.getMobileNumber().isEmpty()) {
            builder.append(" AND u.mobile_number ILIKE '%").append(userRequestParamsTO.getMobileNumber()).append("%' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserTypeId()) && !userRequestParamsTO.getUserTypeId().isEmpty()) {
            builder.append(" AND u.user_type_id = '").append(userRequestParamsTO.getUserTypeId()).append("' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserTypeId()) && !userRequestParamsTO.getUserTypeId().isEmpty()) {
            builder.append(" AND u.user_type_id = '").append(userRequestParamsTO.getUserTypeId()).append("' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getName()) && !userRequestParamsTO.getName().isEmpty()) {
            builder.append(" and a.first_name ILIKE '%").append(userRequestParamsTO.getName()).append("%' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserSubTypeID()) && !userRequestParamsTO.getUserSubTypeID().isEmpty()) {

            if (UserSubTypeEnum.NMC_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                builder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.NMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.NMC_VERIFIER.getId() + "," +
                                UserSubTypeEnum.NBE_ADMIN.getId() + "," +
                                UserSubTypeEnum.COLLEGE_ADMIN.getId()).append(") ");
            }
            if (UserSubTypeEnum.SMC_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                builder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.SMC_VERIFIER.getId()).append(") ");
            }
            if (UserSubTypeEnum.NBE_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                builder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.NBE_ADMIN.getId() + "," +
                                UserSubTypeEnum.NBE_VERIFIER.getId()).append(") ");
            }
        }
        return builder.toString();
    };

    private static final Function<UserRequestParamsTO, String> USER_PARAMETERS_FOR_SMC = userRequestParamsTO -> {
        StringBuilder builder = new StringBuilder();

        if (Objects.nonNull(userRequestParamsTO.getFirstName()) && !userRequestParamsTO.getFirstName().isEmpty()) {
            builder.append(" AND first_name ILIKE '%").append(userRequestParamsTO.getFirstName()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getLastName()) && !userRequestParamsTO.getLastName().isEmpty()) {
            builder.append(" AND last_name ILIKE '%").append(userRequestParamsTO.getLastName()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getEmailId()) && !userRequestParamsTO.getEmailId().isEmpty()) {
            builder.append(" AND u.email ILIKE '%").append(userRequestParamsTO.getEmailId()).append("%' ");
        }

        if (Objects.nonNull(userRequestParamsTO.getMobileNumber()) && !userRequestParamsTO.getMobileNumber().isEmpty()) {
            builder.append(" AND u.mobile_number ILIKE '%").append(userRequestParamsTO.getMobileNumber()).append("%' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserTypeId()) && !userRequestParamsTO.getUserTypeId().isEmpty()) {
            builder.append(" AND u.user_type_id = '").append(userRequestParamsTO.getUserTypeId()).append("' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserTypeId()) && !userRequestParamsTO.getUserTypeId().isEmpty()) {
            builder.append(" AND u.user_type_id = '").append(userRequestParamsTO.getUserTypeId()).append("' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getName()) && !userRequestParamsTO.getName().isEmpty()) {
            builder.append(" and a.name ILIKE '%").append(userRequestParamsTO.getName()).append("%' ");
        }
        if (Objects.nonNull(userRequestParamsTO.getUserSubTypeID()) && !userRequestParamsTO.getUserSubTypeID().isEmpty()) {

            if (UserSubTypeEnum.NMC_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                builder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.NMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.NMC_VERIFIER.getId() + "," +
                                UserSubTypeEnum.NBE_ADMIN.getId() + "," +
                                UserSubTypeEnum.COLLEGE_ADMIN.getId()).append(") ");
            }
            if (UserSubTypeEnum.SMC_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                builder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.SMC_VERIFIER.getId()).append(") ");
            }
            if (UserSubTypeEnum.NBE_ADMIN.getId().toString().equals(userRequestParamsTO.getUserSubTypeID())) {
                builder.append(" AND u.user_sub_type_id IN(").append(
                        UserSubTypeEnum.SMC_ADMIN.getId() + "," +
                                UserSubTypeEnum.NBE_ADMIN.getId() + "," +
                                UserSubTypeEnum.NBE_VERIFIER.getId()).append(") ");
            }
        }
        return builder.toString();
    };
    private static final Function<UserRequestParamsTO, String> GET_ALL_USER = userRequestParamsTO -> {
        StringBuilder sb = new StringBuilder();
        String parameters = USER_PARAMETERS.apply(userRequestParamsTO);
        String parametersForSmc = USER_PARAMETERS_FOR_SMC.apply(userRequestParamsTO);
        String parametersForCollege = USER_PARAMETERS_FOR_CLG.apply(userRequestParamsTO);
        sb.append(NMRConstants.FETCH_SMC_DETAILS).append(parametersForSmc).append(FETCH_COLLEGE_DETAILS)
                .append(parametersForCollege).append(FETCH_NMC_DETAILS).append(parameters)
                .append(FETCH_NBE_DETAILS).append(parameters);
        if (Objects.nonNull(userRequestParamsTO.getSortBy()) && !userRequestParamsTO.getSortBy().isEmpty()) {
            String sortRecords = SORT_RECORDS.apply(userRequestParamsTO);
            sb.append(sortRecords);
        }
        return sb.toString();
    };

    public UserResponseTO fetchUserData(UserRequestParamsTO userRequestParamsTO, Pageable pageable) {
        UserResponseTO userResponseTO = new UserResponseTO();
        userResponseTO.setTotalNoOfRecords(BigInteger.ZERO);
        List<UserTO> userTOList = new ArrayList<>();
        Query query = entityManager.createNativeQuery(GET_ALL_USER.apply(userRequestParamsTO));
        log.debug("Fetched user detail successfully.");
        query.setFirstResult(pageable.getPageNumber() != 0 ?(pageable.getPageNumber() - 1) * pageable.getPageSize() : 0);
        query.setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();
        results.forEach(result -> {
            UserTO user = new UserTO();
            user.setId((BigInteger) result[0]);
            user.setUserTypeId((BigInteger) result[1]);
            user.setFirstName((String) result[2]);
            user.setLastName((String) result[3]);
            user.setEmailId((String) result[4]);
            user.setMobileNumber((String) result[5]);
            user.setAdmin(result[6] != null &&
                    Objects.equals(result[6], UserSubTypeEnum.COLLEGE_ADMIN.getId())
                    || Objects.equals(result[6], UserSubTypeEnum.SMC_ADMIN.getId())
                    || Objects.equals(result[6], UserSubTypeEnum.NBE_ADMIN.getId())
                    || Objects.equals(result[6], UserSubTypeEnum.NMC_ADMIN.getId()));
            user.setLastLogin(result[7] != null ? (Timestamp) result[7] : null);
            user.setCouncilName(result[8] != null ? (String) result[8] : null);
            user.setCollegeName(result[9] != null ? (String) result[9] : null);
            user.setCreatedAt(result[10] != null ? (Timestamp) result[10] : null);
            userTOList.add(user);
        });
        userResponseTO.setTotalNoOfRecords(BigInteger.valueOf(results.size()));
        userResponseTO.setUsers(userTOList);
        return userResponseTO;
    }
}