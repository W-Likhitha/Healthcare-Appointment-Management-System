package com.example.notification.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String twilioNumber;

    public void sendSms(String to, String messageBody) {
        try {
            Twilio.init(accountSid, authToken);

            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(to),
                    new com.twilio.type.PhoneNumber(twilioNumber),
                    messageBody
            ).create();

            System.out.println("SMS sent successfully: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
}
