package in.gov.abdm.nmr.domain.district;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/master")
public class DistrictController {

	private IDistrictService districtService;

	public DistrictController(IDistrictService districtService) {
		this.districtService = districtService;
	}

	@GetMapping(path = "/state/{state_id}/district", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DistrictTO> getDistrictData(@PathVariable("state_id") BigInteger stateId) {
		return districtService.getDistrictData(stateId);
	}
}
