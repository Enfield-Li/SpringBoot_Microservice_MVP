package com.example.customer;

import lombok.AllArgsConstructor;
// import com.amigoscode.amqp.RabbitMQMessageProducer;
// import com.amigoscode.clients.fraud.FraudCheckResponse;
// import com.amigoscode.clients.fraud.FraudClient;
// import com.amigoscode.clients.notification.NotificationClient;
// import com.amigoscode.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  // private final RestTemplate restTemplate;

  //   private final FraudClient fraudClient;
  //   private final RabbitMQMessageProducer rabbitMQMessageProducer;

  public void registerCustomer(CustomerRegistrationRequest request) {
    Customer customer = Customer
      .builder()
      .username(request.getUsername())
      .email(request.getPassword())
      .build();

    customerRepository.saveAndFlush(customer);
    // Customer savedCustomer = customerRepository.save(customer);
    // System.out.println(customer);
    // System.out.println("***************");
    // System.out.println(savedCustomer);

    // FraudCheckResponse fraudcheckRespose = restTemplate.getForObject(
    //   "http://FRAUD/api/v1/fraud-check/{customerId}",
    //   FraudCheckResponse.class,
    //   customer.getId()
    // );

    // if (fraudcheckRespose.isFraudster()) {
    //   throw new IllegalStateException("fraudster");
    // }
    //     // todo: check if email valid
    //     // todo: check if email not taken
    // customerRepository.saveAndFlush(customer);
    //     FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(
    //       customer.getId()
    //     );

    //     if (fraudCheckResponse.isFraudster()) {
    //       throw new IllegalStateException("fraudster");
    //     }

    //     NotificationRequest notificationRequest = new NotificationRequest(
    //       customer.getId(),
    //       customer.getEmail(),
    //       String.format("Hi %s, welcome to Amigoscode...", customer.getFirstName())
    //     );
    //     rabbitMQMessageProducer.publish(
    //       notificationRequest,
    //       "internal.exchange",
    //       "internal.notification.routing-key"
    //     );
  }
}
