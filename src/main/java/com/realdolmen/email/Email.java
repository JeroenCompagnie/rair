package com.realdolmen.email;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Email {
	private static String USER_NAME = "rairnoreply@gmail.com";  // GMail user name (just the part before "@gmail.com")
	private static String PASSWORD = "rairnoreply12345"; // GMail password
	private static String TESTRECIPIENT = "jeroen.compagnie@gmail.com";

	public static void main(String[] args) {
		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { TESTRECIPIENT }; // list of recipient email addresses
		String subject = "Java send mail example";
		String body = "Welcome to JavaMail!";

		sendMail(from, pass, to, subject, body);
	}

//	static Properties props;
	public Email(){
//		props = new Properties();
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "smtp.gmail.com");
//		props.put("mail.smtp.port", "587");
	}
	
	public boolean sendMailStandardSender(String[] to, String subject, String body){
		return sendMail(USER_NAME, PASSWORD, to, subject, body);
	}
	
	public static boolean sendMail(String from, String pass, String[] to, String subject, String body){
		Properties props;
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,
				new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

			MimeMultipart multipart = new MimeMultipart();

			message.setSubject(subject);
			BodyPart messBodyPart = new MimeBodyPart();
			messBodyPart.setContent(body, "text/html");

			multipart.addBodyPart(messBodyPart);

			message.setContent(multipart);


			Transport.send(message);
			return true;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}


}