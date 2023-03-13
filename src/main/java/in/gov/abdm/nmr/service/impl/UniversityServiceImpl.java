package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.UniversityMasterTo;
import in.gov.abdm.nmr.dto.UniversityTO;
import in.gov.abdm.nmr.mapper.IUniversityMasterToMapper;
import in.gov.abdm.nmr.mapper.UniversityDtoMapper;
import in.gov.abdm.nmr.repository.IUniversityRepository;
import in.gov.abdm.nmr.repository.UniversityMasterRepository;
import in.gov.abdm.nmr.service.IUniversityMasterService;
import in.gov.abdm.nmr.service.IUniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class UniversityServiceImpl implements IUniversityService, IUniversityMasterService {

    private IUniversityRepository universityRepository;

    private UniversityDtoMapper universityDtoMapper;
    @Autowired
    private IUniversityMasterToMapper universititesToMapper;
    @Autowired
    private UniversityMasterRepository universitiesRepository;

    public UniversityServiceImpl(IUniversityRepository universityRepository, UniversityDtoMapper universityDtoMapper) {
        this.universityRepository = universityRepository;
        this.universityDtoMapper = universityDtoMapper;
    }

    @Override
    public List<UniversityTO> getUniversityData() {
        return universityDtoMapper.universityDataToDto(universityRepository.getUniversity());

    }

    @Override
    public List<UniversityMasterTo> getUniversitiesByCollegeId(BigInteger collegeId) {
        return universititesToMapper.universitiesTo(universitiesRepository.getUniversitiesByCollegeId(collegeId));
    }

    @Override
    public List<UniversityMasterTo> getUniversitiesByState(BigInteger stateId) {
        return universititesToMapper.universitiesTo(universitiesRepository.getUniversitiesByState(stateId));
    }
}
