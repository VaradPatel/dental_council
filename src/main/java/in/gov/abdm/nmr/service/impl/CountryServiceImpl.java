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

    private CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CountryTO> getCountryData() {
        return CountryDtoMapper.COUNTRY_DTO_MAPPER.countryDataToDto(countryRepository.getCountry());
    }
}
