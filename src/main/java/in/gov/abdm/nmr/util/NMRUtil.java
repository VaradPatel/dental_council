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

    /**
     * Return value if it is not null otherwise fallBackValue.
     * @param value the main value to be returned when not null.
     * @param fallBackValue the fallback value to be returned when main value is not available.
     * @param <T> the object data type.
     * @return the value when it's not null otherwise returns fallBackValue.
     */
    public static <T> T coalesce(T value, T fallBackValue){
        return value != null ? value : fallBackValue;
    }
}
