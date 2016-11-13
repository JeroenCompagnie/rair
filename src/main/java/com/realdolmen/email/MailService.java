package com.realdolmen.email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailService {
    final String username="rairnoreply@gmail.com";
    final String password="rairnoreply12345";
    Properties props;
    public MailService(){
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }
    public boolean mail(String to, String subject, String body){
        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message mess = new MimeMessage(session);
            mess.setFrom(new InternetAddress(username));
            mess.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            MimeMultipart multipart = new MimeMultipart();

            mess.setSubject(subject);
            BodyPart messBodyPart = new MimeBodyPart();
            messBodyPart.setContent(body, "text/html");

            multipart.addBodyPart(messBodyPart);

            mess.setContent(multipart);


            Transport.send(mess);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}