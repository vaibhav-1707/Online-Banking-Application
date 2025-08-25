package com.banking.services;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * EmailService provides functionality to send emails using SMTP.
 */
public class EmailService {

    private final String username; // sender's email
    private final String password; // sender's password or app password
    private final Properties props;

    /**
     * Constructs the EmailService with SMTP settings.
     * 
     * @param host     SMTP server host (e.g., smtp.gmail.com)
     * @param port     SMTP server port (e.g., 587)
     * @param username Email username (sender)
     * @param password Email password or app-specific password
     */
    public EmailService(String host, String port, String username, String password) {
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
    }

    /**
     * Sends an email with the provided details.
     * 
     * @param to      Recipient email address
     * @param subject Email subject
     * @param body    Email body text
     * @throws MessagingException If sending email fails
     */
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        if (username == null || username.isBlank() || username.equals("your-email@gmail.com")) {
            System.err.println("Email service is not configured. Skipping email send.");
            return;
        }

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
