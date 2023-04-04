package in.gov.abdm.nmr.service;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.jpa.entity.Password;

public interface IPasswordDaoService {

    Password save(Password entity);
    
    List<Password> findLast5(BigInteger userId);
}
