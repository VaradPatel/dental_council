package in.gov.abdm.nmr.redis.repository;

import java.util.List;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

import in.gov.abdm.nmr.redis.hash.Otp;

public interface IOtpRepository extends KeyValueRepository<Otp, String> {

    List<Otp> findAllBycontact(String contact);
}

