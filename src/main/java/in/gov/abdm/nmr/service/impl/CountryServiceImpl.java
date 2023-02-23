package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.mapper.CountryDtoMapper;
import in.gov.abdm.nmr.repository.CountryRepository;
import in.gov.abdm.nmr.service.ICountryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
        return countryDtoMapper.CountryDataToDto(countryRepository.getCountry());

    }
}
