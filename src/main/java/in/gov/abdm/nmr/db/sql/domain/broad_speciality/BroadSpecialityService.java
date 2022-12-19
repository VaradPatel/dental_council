package in.gov.abdm.nmr.db.sql.domain.broad_speciality;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class BroadSpecialityService implements IBroadSpecialityService {

    public BroadSpecialityRepository broadSpecialityRepository;

    private BroadSpecialityDtoMapper broadSpecialityDtoMapper;

    public BroadSpecialityService(BroadSpecialityRepository broadSpecialityRepository, BroadSpecialityDtoMapper broadSpecialityDtoMapper) {
        this.broadSpecialityRepository = broadSpecialityRepository;
        this.broadSpecialityDtoMapper = broadSpecialityDtoMapper;
    }

    @Override
    public List<BroadSpecialityTO> getSpecialityData() {
        return broadSpecialityDtoMapper.SpecialityDataToDto(broadSpecialityRepository.getSpeciality());

    }
}
