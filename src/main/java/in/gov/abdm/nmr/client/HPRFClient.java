package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.PractitionerRequestTO;
import in.gov.abdm.nmr.dto.RegisteredPractitionerResponseTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@FeignClient(name = HPR_SERVICE, url = HPR_SERVICE_ENDPOINT)
public interface HPRFClient {
    @PostMapping(value = HPR_SERVICE_REGISTER_HEALTH_PROFESSIONAL)
    RegisteredPractitionerResponseTO registerHealthProfessional(@RequestHeader(value = "Authorization") String authorization, @RequestBody PractitionerRequestTO practitionerRequestTO);
}
