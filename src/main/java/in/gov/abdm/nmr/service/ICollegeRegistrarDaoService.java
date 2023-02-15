package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import in.gov.abdm.nmr.exception.NmrException;

public interface ICollegeRegistrarDaoService {

    CollegeRegistrar saveCollegeRegistrar(BigInteger collegeId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;
    CollegeRegistrar updateRegisterRegistrar(BigInteger collegeId,BigInteger registrarId, CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo) throws NmrException;

    CollegeRegistrar findCollegeRegistrarById(BigInteger registrarId, BigInteger collegeId) throws NmrException;

    CollegeRegistrar findByUserDetail(BigInteger userDetailId);
}
