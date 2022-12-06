package in.gov.abdm.nmr.db.sql.domain.university;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class UniversityService implements IUniversityService {

    public UniversityRepository universityRepository;

    private UniversityDtoMapper universityDtoMapper;

    public UniversityService(UniversityRepository universityRepository, UniversityDtoMapper universityDtoMapper) {
        this.universityRepository = universityRepository;
        this.universityDtoMapper = universityDtoMapper;
    }

    @Override
    public List<UniversityTO> getUniversityData() {
        return universityDtoMapper.UniversityDataToDto(universityRepository.getUniversity());

    }
}
