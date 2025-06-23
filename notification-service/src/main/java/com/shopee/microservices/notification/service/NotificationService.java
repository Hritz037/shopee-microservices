package com.shopee.microservices.notification.service;

import com.shopee.microservices.order.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final JavaMailSender javaMailSender;
    @KafkaListener(topics="order-placed")
    public void Listen(OrderPlacedEvent orderPlacedEvent){

        log.info("Got Message from order-placed topic: {}", orderPlacedEvent);
        MimeMessagePreparator messagePreparator = mimeMessage ->{
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("shopee@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject(String.format("Order Placed {}", orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                    Hi %s %s
                    Your order with order number %s is now placed successfully.
                    Best Regards
                    Shopee
                    """, orderPlacedEvent.getFirstName().toString(), orderPlacedEvent.getLastName().toString(), orderPlacedEvent.getOrderNumber()));

        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Order Notification email sent!!!");
        }catch (MailException e){
            log.error("Exception occured when sending mail",e);
            throw new RuntimeException(String.format("Exception occured when sending mail for orderNumber {}",orderPlacedEvent.getOrderNumber()), e);
        }

    }

}
