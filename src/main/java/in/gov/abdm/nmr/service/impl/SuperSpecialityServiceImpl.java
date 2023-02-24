package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.mapper.SuperSpecialityDtoMapper;
import in.gov.abdm.nmr.repository.SuperSpecialityRepository;
import in.gov.abdm.nmr.service.ISuperSpecialityService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SuperSpecialityServiceImpl implements ISuperSpecialityService {

    private SuperSpecialityRepository superSpecialityRepository;

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
