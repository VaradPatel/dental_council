package in.gov.abdm.nmr.api.controller.notification;

public enum NotificationType {
    EMAIL("email"),
    SMS("sms");

    private final String notificaionType;

    private NotificationType(String notificationType) {
        this.notificaionType = notificationType;
    }

    public String getNotificationType(){
        return this.notificaionType;
    }
}
