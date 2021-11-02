package ru.bachinin.cardealership.rabbit;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    Logger logger = Logger.getLogger(RabbitMqListener.class);

    @RabbitListener(queues = "vehicleQueue")
    public void processQueue1(String message) {
        logger.info("Received from vehicleQueue: " + message);
    }
}
