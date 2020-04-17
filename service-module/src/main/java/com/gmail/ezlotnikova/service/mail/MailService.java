package com.gmail.ezlotnikova.service.mail;

public interface MailService {

    void sendMessage(String to, String subject, String text);

}