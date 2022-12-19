package in.gov.abdm.nmr.db.sql.domain.super_speciality;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class SuperSpecialityService implements ISuperSpecialityService {

    public SuperSpecialityRepository superSpecialityRepository;

    private SuperSpecialityDtoMapper superSpecialityDtoMapper;

    public SuperSpecialityService(SuperSpecialityRepository superSpecialityRepository, SuperSpecialityDtoMapper superSpecialityDtoMapper) {
        this.superSpecialityRepository = superSpecialityRepository;
        this.superSpecialityDtoMapper = superSpecialityDtoMapper;
    }

    @Override
    public List<SuperSpecialityTO> getSpecialityData() {
        return superSpecialityDtoMapper.SpecialityDataToDto(superSpecialityRepository.getSpeciality());

    }
}
