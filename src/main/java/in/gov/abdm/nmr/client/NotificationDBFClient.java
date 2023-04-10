package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.Template;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigInteger;
import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Notification-DB service client to get templates
 */
@FeignClient(name = NOTIFICATION_DB_SERVICE, url = GLOBAL_NOTIFICATION_ENDPOINT)
public interface NotificationDBFClient {

    @GetMapping(value = NOTIFICATION_DB_SERVICE_GET_TEMPLATE)
    Template getTemplateById(@RequestHeader("TIMESTAMP") Timestamp timeStamp, @RequestHeader("REQUEST-ID") String requestId, @PathVariable BigInteger id);

}