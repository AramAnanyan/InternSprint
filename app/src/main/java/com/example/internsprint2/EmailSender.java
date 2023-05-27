package com.example.internsprint2;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String recipient, String subject, String message) {
        // Execute the email sending operation asynchronously
        new SendEmailTask().execute(recipient, subject, message);
    }

    private static class SendEmailTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String recipient = params[0];
            String subject = params[1];
            String message = params[2];

            // Set up email credentials
            final String senderEmail = "internsprint@gmail.com";
            final String senderPassword = "intern-2006";

            // Set up properties for the mail session
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Create a mail session with the properties
            Session session = Session.getInstance(props);

            try {

                MimeMessage emailMessage = new MimeMessage(session);
                emailMessage.setFrom(new InternetAddress(senderEmail));
                emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                emailMessage.setSubject(subject);


                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(message);


                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);


                emailMessage.setContent(multipart);


                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com", senderEmail, senderPassword);
                transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
                transport.close();

                Log.d("EmailSender", "Email sent successfully");
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("EmailSender", "Failed to send email: " + e.getMessage());
            }

            return null;
        }
    }
}
