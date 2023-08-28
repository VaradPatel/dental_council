package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequestTo {

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("type")
    private List<String> type;

    @JsonProperty("contentType")
    private String contentType;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("receiver")
    private List<KeyValue> receiver;

    @JsonProperty("notification")
    private List<KeyValue> notification = Collections.emptyList();

    public NotificationRequestTo() {
    }

    public NotificationRequestTo(List<String> type) {
        this.type = new ArrayList<>(type);
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getType() {
        return Collections.unmodifiableList(type);
    }

    public void setType(List<String> type) {
        this.type = Collections.unmodifiableList(type);
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<KeyValue> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<KeyValue> receiver) {
        this.receiver = Collections.unmodifiableList(receiver);
    }

    public List<KeyValue> getNotification() {
        return notification;
    }

    public void setNotification(List<KeyValue> notification) {
        this.notification = Collections.unmodifiableList(notification);
    }

}
