package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.HPRIdTokenRequestTO;
import in.gov.abdm.nmr.dto.HPRIdTokenResponseTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@FeignClient(name = HPR_ID_SERVICE, url = HPR_ID_SERVICE_ENDPOINT)
public interface HPRIDFClient {
    @PostMapping(value = HPR_ID_SERVICE_TOKEN_BY_HPRID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    HPRIdTokenResponseTO getTokensByHprId(@RequestHeader(value = AUTHORIZATION) String authorization, @RequestBody HPRIdTokenRequestTO hprIdTokenRequestTo);
}
