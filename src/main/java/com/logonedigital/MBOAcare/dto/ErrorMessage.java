package com.logonedigital.MBOAcare.dto;

import java.time.LocalDateTime;

public class ErrorMessage {
    private String message;
    private LocalDateTime timestamp;
    private String status;
    private int code;

    public ErrorMessage(String message, LocalDateTime timestamp, String status, int code) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.code = code;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
}
