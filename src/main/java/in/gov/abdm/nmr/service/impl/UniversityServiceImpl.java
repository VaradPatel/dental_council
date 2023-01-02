package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.mapper.UniversityDtoMapper;
import in.gov.abdm.nmr.dto.UniversityTO;
import in.gov.abdm.nmr.repository.IUniversityRepository;
import in.gov.abdm.nmr.service.IUniversityService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UniversityServiceImpl implements IUniversityService {

    public IUniversityRepository universityRepository;

    private UniversityDtoMapper universityDtoMapper;

    public UniversityServiceImpl(IUniversityRepository universityRepository, UniversityDtoMapper universityDtoMapper) {
        this.universityRepository = universityRepository;
        this.universityDtoMapper = universityDtoMapper;
    }

    @Override
    public List<UniversityTO> getUniversityData() {
        return universityDtoMapper.UniversityDataToDto(universityRepository.getUniversity());

    }
}