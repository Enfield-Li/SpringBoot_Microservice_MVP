package com.example.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class CustomerApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx = SpringApplication.run(
      CustomerApplication.class,
      args
    );

    log.info("Starts up");

    Customer customer = new Customer();
    customer.setEmail("email");
    customer.setUsername("username");

    CustomerRepository repo = ctx.getBean(CustomerRepository.class);
    Customer c = repo.save(customer);
    log.info("saved: " + c.toString());
  }
}
