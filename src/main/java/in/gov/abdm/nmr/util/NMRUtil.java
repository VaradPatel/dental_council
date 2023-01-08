package in.gov.abdm.nmr.util;

import in.gov.abdm.nmr.entity.RequestCounter;
import lombok.experimental.UtilityClass;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Util class for NMR.
 */
@UtilityClass
public final class NMRUtil {

    public static String buildRequestIdForWorkflow(RequestCounter requestCounter){
        return requestCounter.getApplicationType().getRequestPrefixId().concat(String.valueOf(requestCounter.getCounter()));
    }
}
