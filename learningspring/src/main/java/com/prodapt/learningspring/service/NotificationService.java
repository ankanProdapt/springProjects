package com.prodapt.learningspring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.entity.Notification;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User user, Post post, String notificationType, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setPost(post);
        notification.setNotificationType(notificationType);
        notification.setMessage(message);   
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
}