package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.UniversityMasterTo;
import in.gov.abdm.nmr.entity.UniversityMaster;
import in.gov.abdm.nmr.mapper.IUniversityMasterToMapper;
import in.gov.abdm.nmr.mapper.UniversityDtoMapper;
import in.gov.abdm.nmr.repository.IUniversityRepository;
import in.gov.abdm.nmr.repository.UniversityMasterRepository;
import in.gov.abdm.nmr.service.IUniversityMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class UniversityServiceImpl implements IUniversityMasterService {

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
    public List<UniversityMasterTo> getUniversitiesByCollegeId(BigInteger collegeId) {
        return universititesToMapper.universitiesTo(collegeId!=null ? universitiesRepository.getUniversitiesByCollegeId(collegeId):universitiesRepository.getUniversities());
    }
    
    @Override
    public UniversityMaster findById(BigInteger id) {
        return universitiesRepository.findById(id).orElse(null);
    }

    @Override
    public UniversityMaster save(UniversityMaster universityMaster) {
        return universitiesRepository.save(universityMaster);
    }
}
