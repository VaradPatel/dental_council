package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.CountryTO;

import java.util.List;

public interface ICountryService {

    List<CountryTO> getCountryData();
}
