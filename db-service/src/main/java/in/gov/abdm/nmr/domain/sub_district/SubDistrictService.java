package in.gov.abdm.nmr.domain.sub_district;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class SubDistrictService implements ISubDistrictService {
	
	public SubDistrictRepository subDistrictRepository;
	
	private SubDistrictDtoMapper subDistrictDtoMapper;
	
    public SubDistrictService(SubDistrictRepository subDistrictRepository, SubDistrictDtoMapper subDistrictDtoMapper) {
        this.subDistrictRepository = subDistrictRepository;
        this.subDistrictDtoMapper = subDistrictDtoMapper;
    }

    @Override
	public List<SubDistrictTO> getSubDistrictData(BigInteger districtId) {    
        List<Tuple> districtList = subDistrictRepository.getSubDistrict(districtId);
        List<SubDistrictTO> subDistrictTOList = new ArrayList<SubDistrictTO>();
        for(Tuple district : districtList)
        {  
        	SubDistrictTO subDistrictTO = new SubDistrictTO();
        	subDistrictTO.setId(district.get("id", BigInteger.class));
        	subDistrictTO.setName(district.get("name", String.class));
        	subDistrictTOList.add(subDistrictTO);
        }
        return subDistrictTOList;
	}
}
