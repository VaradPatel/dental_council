package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.repository.CountryRepository;
import in.gov.abdm.nmr.service.impl.CountryServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.ID;
import static in.gov.abdm.nmr.util.CommonTestData.getCountry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryServiceImpl countryService;

    @Test
    void testGetCountryDataShouldReturnValidListOfCountries(){
        when(countryRepository.getCountry()).thenReturn(List.of(getCountry()));
        List<CountryTO> countries = countryService.getCountryData();
        assertEquals(1, countries.size());
        CountryTO countryTO = countries.get(0);
        assertEquals(ID, countryTO.getId());
        assertEquals(NMRConstants.INDIA, countryTO.getName());

    }
}
