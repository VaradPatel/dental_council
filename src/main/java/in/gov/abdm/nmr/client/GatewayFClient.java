package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client to call Session Service APIs through the Gateway service.
 */
@FeignClient(name = NMRConstants.GATEWAY_SERVICE, url = NMRConstants.GATEWAY_SERVICE_ENDPOINT)
public interface GatewayFClient {

    /**
     * Calls the /session endpoint of the Gateway service to create a new session.
     *
     * @param sessionRequestTo the request body containing session details.
     * @return a {@link SessionResponseTo} object containing the response details
     */
    @PostMapping(value = NMRConstants.SESSION_URL)
    SessionResponseTo sessions(@RequestBody SessionRequestTo sessionRequestTo);
}