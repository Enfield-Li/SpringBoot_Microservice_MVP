package com.example.customer;

import com.example.clients.fraud.FraudCheckResponse;
import com.example.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final RestTemplate restTemplate;
  private final FraudClient fraudClient;

  //   private final RabbitMQMessageProducer rabbitMQMessageProducer;

  public void registerCustomer(CustomerRegistrationRequest request) {
    Customer customer = Customer
      .builder()
      .username(request.getUsername())
      .email(request.getPassword())
      .build();

    customerRepository.saveAndFlush(customer);
    // Customer savedCustomer = customerRepository.save(customer);

    FraudCheckResponse fraudcheckRespose = fraudClient.isFraudster(
      customer.getId()
    );
    // FraudCheckResponse fraudcheckRespose = restTemplate.getForObject(
    //   "http://FRAUD-SERVICE/api/v1/fraud-check/{customerId}",
    //   FraudCheckResponse.class,
    //   customer.getId()
    // );

    if (fraudcheckRespose.getIsFraudster()) {
      throw new IllegalStateException("fraudster");
    }
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
