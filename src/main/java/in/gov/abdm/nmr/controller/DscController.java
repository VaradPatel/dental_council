package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.dto.dsc.DscRequestTo;
import in.gov.abdm.nmr.dto.dsc.DscResponseTo;
import in.gov.abdm.nmr.service.IDscService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * Controller for e-sign API'S
 */
@RestController
public class DscController {

	@Autowired
	private IDscService dscService;

	/**
	 * @param dscRequestTo holds all user details which required for esign.
	 * @return response from genDscRequest.
	 */
	@PostMapping(path = NMRConstants.E_SIGN, produces = MediaType.APPLICATION_JSON_VALUE)
	public DscResponseTo invokeDSCGenEspRequest(@Valid @RequestBody DscRequestTo dscRequestTo) throws JsonProcessingException {
		return dscService.invokeDSCGenEspRequest(dscRequestTo);
	}
}
