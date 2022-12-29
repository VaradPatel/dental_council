package in.gov.abdm.nmr.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.mapper.CountryDtoMapper;
import in.gov.abdm.nmr.repository.CountryRepository;
import in.gov.abdm.nmr.service.ICountryService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CountryServiceImpl implements ICountryService {

    public CountryRepository countryRepository;

    private CountryDtoMapper countryDtoMapper;

    public CountryServiceImpl(CountryRepository countryRepository, CountryDtoMapper countryDtoMapper) {
        this.countryRepository = countryRepository;
        this.countryDtoMapper = countryDtoMapper;
    }

    @Override
    public List<CountryTO> getCountryData() {
//        List<Tuple> countryList = countryRepository.getCountry();
//        List<CountryTO> countryTOList = new ArrayList<CountryTO>();
//        for (Tuple country : countryList) {
//            CountryTO countryTO = new CountryTO();
//            countryTO.setId(country.get("id", BigInteger.class));
//            countryTO.setName(country.get("name", String.class));
//            countryTO.setNationality(country.get("nationality", String.class));
//            countryTOList.add(countryTO);
//        }
//        return countryTOList;
        return countryDtoMapper.CountryDataToDto(countryRepository.getCountry());

    }
}
