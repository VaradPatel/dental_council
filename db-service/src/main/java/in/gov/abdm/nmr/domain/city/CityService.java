package in.gov.abdm.nmr.domain.city;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class CityService implements ICityService {
	
	public CityRepository cityRepository;
	
	private CityDtoMapper cityDtoMapper;
	
    public CityService(CityRepository cityRepository, CityDtoMapper cityDtoMapper) {
        this.cityRepository = cityRepository;
        this.cityDtoMapper = cityDtoMapper;
    }

    @Override
	public List<CityTO> getCityData(BigInteger subdistrictId) {    
        List<Tuple> cityList = cityRepository.getCity(subdistrictId);
        List<CityTO> cityTOList = new ArrayList<CityTO>();
        for(Tuple city : cityList)
        {  
        	CityTO cityTO = new CityTO();
        	cityTO.setId(city.get("id", BigInteger.class));
        	cityTO.setName(city.get("name", String.class));
        	cityTOList.add(cityTO);
        }
        return cityTOList;
	}
}
