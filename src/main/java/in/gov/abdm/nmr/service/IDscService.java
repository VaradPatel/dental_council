package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.dto.dsc.DscRequestTo;
import in.gov.abdm.nmr.dto.dsc.DscResponseTo;

public interface IDscService {

	DscResponseTo invokeDSCGenEspRequest(DscRequestTo dscRequestTo) throws JsonProcessingException;

}
