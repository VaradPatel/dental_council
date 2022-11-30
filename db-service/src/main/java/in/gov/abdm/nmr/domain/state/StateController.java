package in.gov.abdm.nmr.domain.state;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/master")
public class StateController {

	private IStateService stateService;

	public StateController(IStateService stateService) {
		this.stateService = stateService;
	}

	@GetMapping(path = "/country/{country_id}/state", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<StateTO> getCountryData(@PathVariable("country_id") BigInteger countryId) {
		return stateService.getStateData(countryId);
	}
}
