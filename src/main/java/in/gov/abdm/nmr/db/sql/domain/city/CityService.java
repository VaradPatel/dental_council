package in.gov.abdm.nmr.db.sql.domain.city;

import java.math.BigInteger;
import java.util.List;

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
    	return cityDtoMapper.cityDataToDto(cityRepository.getCity(subdistrictId));
    }
}
