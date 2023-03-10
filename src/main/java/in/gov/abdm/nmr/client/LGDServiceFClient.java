package in.gov.abdm.nmr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * LGD service client to retrieve the address details
 */
@FeignClient(name = LGD_DB_SERVICE, url = GLOBAL_LGD_ENDPOINT)
public interface LGDServiceFClient {

    @GetMapping(LGD_FEIGN_SEARCH_URL)
    List fetchAddressByPinCodeFromLGD(@RequestParam(value = PIN_CODE,required = false) String pinCode,
                               @RequestParam(value = VIEW,required = false) String view);

}