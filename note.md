eureka: http://localhost:8761/
rabbitmq: http://localhost:15672
zipkin: http://localhost:9411/zipkin

Openfein: 
    1. Make services talk to each other, a client module decouple info to itself for services to communicate
    
Eureka server(load balancer, service discovery, no need in kubernetes):
    1. For multiple instances of one service while not configuring ports manually
    2. Instead use a central place to store that info, that response for load balancing request of multiple instances of service
    3. Service using Eureka client that register in "Eureka server"
    4. eureka server knows all the client app running on each port and Ip address
    5. Service when needing other services will look up in "Eureka server"
    6. Connect

Load balancer:
    0. External and internal LB that direct traffic
    1. For small traffic to a app, one single service in a stand alone application is fine, but when clients reaches 10k, it requires a much more advanced hardware with high spec to be able to process to traffic request, and this does not scale.
    2. Usually we spun up multiple replicate instances of the same application, the problem is the client does not know which app/vm to connect to(if all go into one, the other will not receive traffic)
    3. Introduce LB: instead of letting client go to application/private network directly, they will go through an external LB, otherwise client will be blocked by the firewall
    4. Things to consider: TLS, Certificate management, Authentication, High availability, Logging, Caching, Path based routing
    5. Conclusion: instead of manage a LB by oneself, one should choose a managed LB, because an managed external LB is the main entry point for the application
    6. Let cloud providers to manage the external LB for us, so we can focus on our business logic

Load balancer algorithm:
    1. Determine which app to choose from
    2. Round robin(most common): alternate requests/distribute requests across servers sequentially
    3. Others: least connections, least time, hash, IP hash, random with two choices

Load balancer health check:
    1. By examining the health check endpoint, returns 200 if healthy, requests will be sent

Zipkin: API gateway response time analyze

Message queue:
    1. AMQP 0-9-1: Advanced message queuing protocol, is a messaging protocol that enables conforming client applications to communicate with conforming messaging middleware brokers.
    2. Brokers, producers, and consumers: Messaging brokers receive messages from publishers(applications that publish them, also known as producers) and route them to consumers(applications that process them). Since it's a network protocol, the publishers, consumers and the broker can all reside on different machines.
    3. Benefits: Loose coupling, performance(if one of the service is down, other service can work as normal, after the service back up, it can read from the queue, and client won't be affected), asynchronous, scalability, language agnostic, acknowledgement(if service have problem reading msg queue, and broker does not  receive an acknowledgement, then the msg is still intact), management UI, plugins, cloud.
    4. Question: how producers produce msg and different types of exchanges

How producers produce msg:
    1. Producers sends messages to exchange
    2. Exchange forwards the messages based on routing pattern
    3. Message queue use binding to attach to exchange
    4. An exchange can have different types: direct exchange(routing key equals to binding), fan out exchange(all queue will receive message), topic exchange(partial match, eg foo.* matches foo.bar), headers exchange(use message header as opposed to routing key), nameless exchange(rabbit MQ default, routing key name equals to queue name)