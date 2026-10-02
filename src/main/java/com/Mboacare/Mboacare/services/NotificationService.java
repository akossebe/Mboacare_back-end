package com.Mboacare.Mboacare.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyPatient(Long patientId, String title, String message) {
        Map<String, String> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("message", message);
        payload.put("time", String.valueOf(System.currentTimeMillis()));
        messagingTemplate.convertAndSend("/topic/patient/" + patientId, payload);
    }
    
    public void notifyMedecin(Long medecinId, String title, String message) {
        Map<String, String> payload = new HashMap<>();
        payload.put("title", title);
        payload.put("message", message);
        payload.put("time", String.valueOf(System.currentTimeMillis()));
        messagingTemplate.convertAndSend("/topic/medecin/" + medecinId, payload);
    }
}
