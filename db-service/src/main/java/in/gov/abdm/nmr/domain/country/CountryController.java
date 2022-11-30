package in.gov.abdm.nmr.domain.country;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/master")
public class CountryController {

	private ICountryService countryService;

	public CountryController(ICountryService countryService) {
		this.countryService = countryService;
	}

	@GetMapping(path = "/country", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CountryTO> getCountryData() {
		return countryService.getCountryData();
	}
}
