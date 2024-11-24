package com.animalia.saludanimalia.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("jeronimo.jsa.133@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String buildCitaCreacionEmailContent(String userName, String citaDetails) {
        return "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">"
                + "<div style=\"background-color: #00FF00; color: #fff; padding: 10px 20px; text-align: center;\">"
                + "<h1 style=\"margin: 0;\">Salud Animalia</h1>"
                + "</div>"
                + "<div style=\"padding: 20px;\">"
                + "<h2 style=\"color: #00FF00;\">Hola " + userName + ",</h2>"
                + "<p>Tu cita ha sido creada con éxito.</p>"
                + "<p>Detalles de la cita:</p>"
                + "<p>" + citaDetails + "</p>"
                + "<p style=\"color: #00FF00;\">Gracias,<br/>El equipo de Salud Animalia</p>"
                + "</div>"
                + "</div>";
    }

    public String buildCitaModificacionEmailContent(String userName, String citaDetails) {
        return "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">"
                + "<div style=\"background-color: #00FF00; color: #fff; padding: 10px 20px; text-align: center;\">"
                + "<h1 style=\"margin: 0;\">Salud Animalia</h1>"
                + "</div>"
                + "<div style=\"padding: 20px;\">"
                + "<h2 style=\"color: #00FF00;\">Hola " + userName + ",</h2>"
                + "<p>Tu cita ha sido modificada con éxito.</p>"
                + "<p>Detalles de la nueva cita:</p>"
                + "<p>" + citaDetails + "</p>"
                + "<p style=\"color: #00FF00;\">Gracias,<br/>El equipo de Salud Animalia</p>"
                + "</div>"
                + "</div>";
    }

    public String buildCitaCancelacionEmailContent(String userName, String citaDetails) {
        return "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">"
                + "<div style=\"background-color: #00FF00; color: #fff; padding: 10px 20px; text-align: center;\">"
                + "<h1 style=\"margin: 0;\">Salud Animalia</h1>"
                + "</div>"
                + "<div style=\"padding: 20px;\">"
                + "<h2 style=\"color: #00FF00;\">Hola " + userName + ",</h2>"
                + "<p>Lamentamos informarte que tu cita ha sido cancelada.</p>"
                + "<p>Detalles de la cita:</p>"
                + "<p>" + citaDetails + "</p>"
                + "<p style=\"color: #00FF00;\">Gracias,<br/>El equipo de Salud Animalia</p>"
                + "</div>"
                + "</div>";
    }

}
