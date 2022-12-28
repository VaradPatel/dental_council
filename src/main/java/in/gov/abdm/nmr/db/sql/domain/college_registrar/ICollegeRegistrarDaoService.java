package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;

public interface ICollegeRegistrarDaoService {

    CollegeRegistrar saveCollegeRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);
    
    CollegeRegistrar findCollegeRegistrarById(BigInteger id);

    CollegeRegistrar findByUserDetail(BigInteger userDetailId);
}
