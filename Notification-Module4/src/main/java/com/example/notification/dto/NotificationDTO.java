package com.example.notification.dto;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private String message;
    private String type;
    private Boolean isSent;
    private LocalDateTime createdAt;

    public NotificationDTO() {}

    public NotificationDTO(Long notificationId, Long userId, String message, String type, Boolean isSent, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.isSent = isSent;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Boolean getIsSent() {
        return isSent;
    }
    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
