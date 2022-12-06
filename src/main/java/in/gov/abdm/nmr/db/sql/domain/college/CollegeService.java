package in.gov.abdm.nmr.db.sql.domain.college;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class CollegeService implements ICollegeService {

    public CollegeRepository collegeRepository;

    private CollegeDtoMapper collegeDtoMapper;

    public CollegeService(CollegeRepository collegeRepository, CollegeDtoMapper collegeDtoMapper) {
        this.collegeRepository = collegeRepository;
        this.collegeDtoMapper = collegeDtoMapper;
    }

    @Override
    public List<CollegeTO> getCollegeData(Long universityId) {
    	return collegeDtoMapper.collegeDataToDto(collegeRepository.getCollege(universityId));
    }

}
