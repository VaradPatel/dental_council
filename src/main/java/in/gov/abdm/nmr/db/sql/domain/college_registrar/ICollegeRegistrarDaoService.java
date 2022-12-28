package in.gov.abdm.nmr.db.sql.domain.college_registrar;

import java.math.BigInteger;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeRegistrarDaoService {

    CollegeRegistrar saveCollegeRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;
    
    CollegeRegistrar findCollegeRegistrarById(BigInteger id);

    CollegeRegistrar findByUserDetail(BigInteger userDetailId);
}
