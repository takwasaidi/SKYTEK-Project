package tn.esprit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Envoyer notification réservation
    public void notifyNewReservation(String message) {
        messagingTemplate.convertAndSend("/topic/reservations", message);
    }

    // Envoyer notification quota dépassé
    public void notifyQuotaExceeded(String message) {
        messagingTemplate.convertAndSend("/topic/quota", message);
    }
    // Envoyer un reminder (10 min avant)
    public void notifyReminder(String message) {
        // ⚠️ Ici tu peux personnaliser le topic par utilisateur
        messagingTemplate.convertAndSend("/topic/reminder",
                "⏰ Rappel : " + message);
    }

}
