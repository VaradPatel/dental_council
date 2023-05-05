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

    public SuperSpecialityServiceImpl(SuperSpecialityRepository superSpecialityRepository) {
        this.superSpecialityRepository = superSpecialityRepository;

    }

    @Override
    public List<SuperSpecialityTO> getSpecialityData() {
        return SuperSpecialityDtoMapper.SUPER_SPECIALITY_DTO_MAPPER.specialityDataToDto(superSpecialityRepository.getSpeciality());

    }
}
