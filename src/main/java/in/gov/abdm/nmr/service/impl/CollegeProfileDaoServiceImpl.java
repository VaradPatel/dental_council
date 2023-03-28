package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.entity.CollegeProfile;
import in.gov.abdm.nmr.repository.ICollegeProfileRepository;
import in.gov.abdm.nmr.service.ICollegeProfileDaoService;

@Service
@Transactional
public class CollegeProfileDaoServiceImpl implements ICollegeProfileDaoService {
    
    private ICollegeProfileRepository collegeProfileRepository;

    public CollegeProfileDaoServiceImpl(ICollegeProfileRepository collegeProfileRepository) {
        this.collegeProfileRepository = collegeProfileRepository;
    }

    @Override
    public CollegeProfile save(CollegeProfile collegeProfile) {
        return collegeProfileRepository.save(collegeProfile);
    }

    @Override
    public CollegeProfile findByCollegeId(BigInteger id) {
        return collegeProfileRepository.findByCollegeId(id);
    }

    @Override
    public CollegeProfile findById(BigInteger id) {
        return collegeProfileRepository.findById(id).orElse(null);
    }

    @Override
    public CollegeProfile findByUserId(BigInteger userId) {
        return this.collegeProfileRepository.findByUserId(userId);
    }
}
