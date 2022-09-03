package com.example.amqp.exchange;

import org.springframework.beans.factory.annotation.Value;

public class Notification {

  // @Value("${rabbitmq.exchanges.internal}")
  public static final String internalExchange = "internal.exchange";

  // @Value("${rabbitmq.queues.notification}")
  public static final String notificationQueue = "notification.queue";

  // @Value("${rabbitmq.routing-keys.internal-notification}")
  public static final String notificationRoutingKey =
    "internal.notification.routing-key";
}
