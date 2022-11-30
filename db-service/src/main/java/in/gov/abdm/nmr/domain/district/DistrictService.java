package in.gov.abdm.nmr.domain.district;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
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
	public List<DistrictTO> getDistrictData(BigInteger districtId) {    
        List<Tuple> districtList = districtRepository.getDistrict(districtId);
        List<DistrictTO> districtTOList = new ArrayList<DistrictTO>();
        for(Tuple district : districtList)
        {  
        	DistrictTO districtTO = new DistrictTO();
        	districtTO.setId(district.get("id", BigInteger.class));
        	districtTO.setName(district.get("name", String.class));
        	districtTOList.add(districtTO);
        }
        return districtTOList;
	}
}
