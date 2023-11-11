package com.example;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SendMail {

    private String mailTo;
    private String mailFrom;
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailFrom() {
        return mailFrom;
    }
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void sending() throws MessagingException {
        System.out.println("Mail 1"+mailTo);
        System.out.println("Mail 2"+mailFrom);
        Properties props = new Properties();
        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(mailFrom));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        try {
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mailTo));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        message.setSubject("Notification");
        message.setText("Successful!", "UTF-8");
        message.setSentDate(new Date());
        Transport.send(message);
    }
}
