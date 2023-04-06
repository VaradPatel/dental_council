package in.gov.abdm.nmr.client;
import in.gov.abdm.nmr.dto.image.ProfileImageCompareTo;
import in.gov.abdm.nmr.dto.image.ImageTokenTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Image API client to call APIS
 */
@FeignClient(name = IMAGE_SERVICE, url = GLOBAL_IMAGE_ENDPOINT)
public interface ImageFClient {

    @PostMapping(value = IMAGE_API_TOKEN_ENDPOINT,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ImageTokenTo getToken(@RequestHeader("Authorization")String authHeader, @RequestBody Map<String,?> form);

    @PostMapping(value = IMAGE_API_ENDPOINT)
    Object compareImages(@RequestBody ProfileImageCompareTo imageCompareTo, @RequestHeader("Authorization") String accessToken);

}