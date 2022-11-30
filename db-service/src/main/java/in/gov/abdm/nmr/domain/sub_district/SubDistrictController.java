package in.gov.abdm.nmr.domain.sub_district;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/master")
public class SubDistrictController {

	private ISubDistrictService subDistrictService;

	public SubDistrictController(ISubDistrictService subDistrictService) {
		this.subDistrictService = subDistrictService;
	}

	@GetMapping(path = "/district/{district_id}/sub_district", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SubDistrictTO> getSubDistrictData(@PathVariable("district_id") BigInteger districtId) {
		return subDistrictService.getSubDistrictData(districtId);
	}
}
