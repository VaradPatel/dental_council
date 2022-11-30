package in.gov.abdm.nmr.domain.city;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/master")
public class CityController {

	private ICityService cityService;

	public CityController(ICityService cityService) {
		this.cityService = cityService;
	}

	@GetMapping(path = "/sub_district/{sub_district_id}/city", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CityTO> getCityData(@PathVariable("sub_district_id") BigInteger subDistrictId) {
		return cityService.getCityData(subDistrictId);
	}
}
