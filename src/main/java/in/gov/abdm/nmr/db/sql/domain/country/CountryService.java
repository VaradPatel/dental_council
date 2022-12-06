package in.gov.abdm.nmr.db.sql.domain.country;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class CountryService implements ICountryService {

    public CountryRepository countryRepository;

    private CountryDtoMapper countryDtoMapper;

    public CountryService(CountryRepository countryRepository, CountryDtoMapper countryDtoMapper) {
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
