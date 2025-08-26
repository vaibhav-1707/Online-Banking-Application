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
 * EmailService provides SMTP email sending capability.
 */
public class EmailService {

    private final String username; // Sender's email
    private final String password; // Sender's email password or app password
    private final Properties props;
    private final Session session;

    /**
     * Constructs the EmailService with SMTP configuration.
     *
     * @param host     SMTP server host (e.g., smtp.gmail.com)
     * @param port     SMTP port (e.g., 587)
     * @param username Email address used for authentication
     * @param password Password or app password for the email account
     */
    public EmailService(String host, String port, String username, String password) {
        this.username = username;
        this.password = password;

        // Set SMTP server properties
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.debug", "true");  // Enable debug logs for troubleshooting

        // Create mail session with authentication
        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailService.this.username, EmailService.this.password);
            }
        });
    }

    /**
     * Sends an email.
     *
     * @param to      Recipient email address
     * @param subject Subject of the email
     * @param body    Email body content
     * @throws MessagingException if an error occurs while sending the email
     */
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        if (username == null || username.isBlank() || username.equals("your-email@gmail.com") || 
            username.equals("YOUR_GMAIL_APP_PASSWORD_HERE") || password == null || password.isBlank()) {
            System.err.println("Email service is not properly configured. Please check config.properties");
            System.err.println("Username: " + (username != null ? username : "null"));
            System.err.println("Password configured: " + (password != null && !password.isBlank() ? "Yes" : "No"));
            return;
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + to);
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            throw e;
        }
    }
}
