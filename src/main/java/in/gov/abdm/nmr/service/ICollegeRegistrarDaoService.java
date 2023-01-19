package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeRegistrarDaoService {

    CollegeRegistrar saveCollegeRegistrar(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;
    
    CollegeRegistrar findCollegeRegistrarById(BigInteger id) throws NmrException;

    CollegeRegistrar findByUserDetail(BigInteger userDetailId);
}
