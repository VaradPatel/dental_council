package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Repository
public interface IFetchDetailsByRegNoRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

//    @Query(value = FETCH_DETAILS_BY_REG_NO_QUERY)
//    List<IFetchSpecificDetails> fetchDetailsByRegNo(@Param(REGISTRATION_NUMBER) String registrationNumber,
//                                                    @Param(SMC_NAME) String smcName,
//                                                    @Param(USER_TYPE) String userType,
//                                                    @Param(USER_SUB_TYPE) String userSubType);
//
//    @Query(value = FETCH_DETAILS_FOR_NMC_BY_REG_NO_QUERY)
//    List<IFetchSpecificDetails> fetchDetailsForNMCByRegNo(@Param(REGISTRATION_NUMBER) String registrationNumber,
//                                                    @Param(USER_TYPE) String userType,
//                                                    @Param(USER_SUB_TYPE) String userSubType);

}
