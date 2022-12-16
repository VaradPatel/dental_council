package in.gov.abdm.nmr.db.sql.domain.district;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class DistrictService implements IDistrictService {

    public DistrictRepository districtRepository;

    private DistrictDtoMapper districtDtoMapper;

    public DistrictService(DistrictRepository districtRepository, DistrictDtoMapper districtDtoMapper) {
        this.districtRepository = districtRepository;
        this.districtDtoMapper = districtDtoMapper;
    }

    @Override
    public List<DistrictTO> getDistrictData(BigInteger stateId) {
    	System.out.println("==========================================");
    	System.out.println("districtRepository :: " + districtRepository.getDistrict(stateId).get(0).getCode());
    	System.out.println("==========================================");

    	return districtDtoMapper.districtDataToDto(districtRepository.getDistrict(stateId));
    }
}
