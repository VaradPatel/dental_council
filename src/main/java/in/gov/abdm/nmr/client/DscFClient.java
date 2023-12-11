package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.dsc.DscDocumentTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * DSC service client to call APIS
 */
@FeignClient(name = DSC_SERVICE, url = DSC_SERVICE_ENDPOINT)
public interface DscFClient {
    //http://digisign2dev.abdm.gov.internal/digiSign/genEspReques
    @PostMapping(value = GEN_ESP_REQUEST_URL)
    String genEspRequest(@RequestBody DscDocumentTo dscDocumentTo);

    @GetMapping(value = VERIFY_ESP_REQUEST_URL)
    ResponseEntity<Resource> verifyEspRequest(@PathVariable String transactionId);

}