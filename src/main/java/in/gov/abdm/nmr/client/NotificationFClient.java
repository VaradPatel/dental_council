package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.NotificationRequestTo;
import in.gov.abdm.nmr.dto.NotificationResponseTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Notification service client to call APIS
 */
@FeignClient(name = NOTIFICATION_SERVICE, url = GLOBAL_NOTIFICATION_ENDPOINT)
public interface NotificationFClient {

    @PostMapping(value = NOTIFICATION_SERVICE_SEND_MESSAGE)
    NotificationResponseTo sendNotification(@RequestBody NotificationRequestTo req, @RequestHeader("TIMESTAMP") Timestamp timeStamp, @RequestHeader("REQUEST-ID") String requestId);

}