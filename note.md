openfeig: 
    1. make services talk to each other, a client module decouple info to itself for services to communicate
    
Eureka server(load balancer, service discovery, no need in kubernetes):
    1. for multiple instances of one service while not configuring ports manually
    2. instead use a centrual place to store that info, that response for load balancing request of multiple instances of service
    3. sevice using Eureka client that register in "Eureka server"
    4. Eureka server knows all the client app running on each port and Ip address
    5. service when needing other services will look up in "Eureka server"
    6. connect

