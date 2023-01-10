package in.gov.abdm.nmr.controller;
import in.gov.abdm.nmr.dto.QueryResponseTo;
import in.gov.abdm.nmr.dto.QueryCreateTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IQueriesService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

/**
 * Controller for query raised by SMC, NMC and Colleges
 */
@RestController
public class QueriesController {

    @Autowired
	IQueriesService queryService;

	/**
	 * Endpoint to create multiple queries at a time
	 * @param queryCreateTo coming from user
	 * @return returns created list of queries
	 */
	@PostMapping(path = NMRConstants.RAISE_QUERY, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessageTo raiseQuery(@Valid @RequestBody QueryCreateTo queryCreateTo) throws WorkFlowException {
		return queryService.createQueries(queryCreateTo);
	}

	/**
	 * Endpoint to get all the queries against hpProfileId
	 * @param hpProfileId takes hpProfileId as a input
	 * @return returns list of queries associated with hpProfileId
	 */
	@GetMapping(path = NMRConstants.GET_QUERIES, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<QueryResponseTo> getQueries(@PathVariable BigInteger hpProfileId){
		return queryService.getQueriesByHpProfileId(hpProfileId);
	}
}