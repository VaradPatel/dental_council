package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.SessionRequestTo;
import in.gov.abdm.nmr.dto.SessionResponseTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Client to call Aadhaar Service APIS
 */
@FeignClient(name = "gateway", url = "https://preprod.abdm.gov.in")
public interface GatewayFClient {

    @PostMapping(value = "/gateway/v0.5/sessions")
    SessionResponseTo sessions(@RequestBody SessionRequestTo sessionRequestTo);
}