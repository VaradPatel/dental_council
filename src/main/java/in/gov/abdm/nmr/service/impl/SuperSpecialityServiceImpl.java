package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.mapper.SuperSpecialityDtoMapper;
import in.gov.abdm.nmr.repository.SuperSpecialityRepository;
import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.service.ISuperSpecialityService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SuperSpecialityServiceImpl implements ISuperSpecialityService {

    public SuperSpecialityRepository superSpecialityRepository;

    private SuperSpecialityDtoMapper superSpecialityDtoMapper;

    public SuperSpecialityServiceImpl(SuperSpecialityRepository superSpecialityRepository, SuperSpecialityDtoMapper superSpecialityDtoMapper) {
        this.superSpecialityRepository = superSpecialityRepository;
        this.superSpecialityDtoMapper = superSpecialityDtoMapper;
    }

    @Override
    public List<SuperSpecialityTO> getSpecialityData() {
        return superSpecialityDtoMapper.SpecialityDataToDto(superSpecialityRepository.getSpeciality());

    }
}
