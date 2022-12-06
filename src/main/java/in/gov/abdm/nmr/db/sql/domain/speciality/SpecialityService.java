package in.gov.abdm.nmr.db.sql.domain.speciality;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class SpecialityService implements ISpecialityService {

    public SpecialityRepository specialityRepository;

    private SpecialityDtoMapper specialityDtoMapper;

    public SpecialityService(SpecialityRepository specialityRepository, SpecialityDtoMapper specialityDtoMapper) {
        this.specialityRepository = specialityRepository;
        this.specialityDtoMapper = specialityDtoMapper;
    }

    @Override
    public List<SpecialityTO> getSpecialityData() {
        return specialityDtoMapper.SpecialityDataToDto(specialityRepository.getSpeciality());

    }
}
