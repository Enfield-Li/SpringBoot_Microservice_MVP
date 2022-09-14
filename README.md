# 微服务演示项目

    ⏬⏬⏬ English Description Below ⏬⏬⏬

一个演示项目，展现不同服务之间，通过 REST 进行“同步”连接，或“异步”消息传输的整体结构。

<sub><sub>欢迎创建 [new issue](https://github.com/Enfield-Li/SpringBoot_Microservice_MVP/issues/new) 点评本项目，或提供任何见解。</sub></sub>

## 1. 技术栈：

```
SpringBoot、
SpringCloud（Spring cloud Gateway、Eureka server、OpenFeign、Sleuth-Zipkin)、
Docker（Docker-compose）
```

## 2. 结构图：

```
+------------------+
|                  |
|                  |
|                  |
|                  |
|                  |
|                  |        +------------------+                 +----------------+                    +------------------+
|                  |        |                  | Sync connection |                |   Async message    |                  |
|     Gateway      |------->|   Fraud check    |---------------->|    Customer    |------------------->|   Notification   |
|                  |        |                  |                 |                |   (Spring AMQP)    |                  |
|                  |        +------------------+                 +----------------+          |         +------------------+
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
+------------------+                                             +---------------------------+
                                                                 |
                                                                 |
                                                                 v
                     +---------------------------------------------------------------------------------------+
                     |                                                                                       |
                     |                       +---------------+                                               |
                     |  +----------+  Send   |               | Bind   +---------+ Receive  +--------------+  |
                     |  | Customer |-------->|   Exchange    |------->|  Queue  |--------->| Notification |  |
                     |  +----------+         |               |        +---------+ (Async)  +--------------+  |
                     |    Producer           +---------------+                                 Consumer      |
                     |                                                                                       |
                     +---------------------------------------------------------------------------------------+
```

## 3. “开发环境” 和 “Docker 环境” 变量处理的方法:

- 方法一：运用 [SpringBoot Profile](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.profiles) 为开发环境和 Docker 环境配置不同的 `application.properties`，并通过 [compose environment](https://docs.docker.com/compose/compose-file/#environment) 设置 [`SPRING_PROFILES_ACTIVE`](https://stackoverflow.com/a/60565969/16648127) 值，而后 App 将根据系统变量，自动选择相应的文件，举例：

```
App 配置：
  开发环境：application.properties
  Docker环境：application-docker.properties

docker-compose.yml 文件：
  services:
    customer:
      environment:
        SPRING_PROFILES_ACTIVE: docker
```

- 方法二：通过 [compose environment](https://docs.docker.com/compose/compose-file/#environment) 覆盖开发环境的 `application.properties` 值，举例（使用两种不同的语法）：

```
docker-compose.yml 文件：
  services:
    customer:
      environment:
        spring.datasource.username: root,
        SPRING_APPLICATION_JSON: '{
          "spring.datasource.password": "password"
        }'
```

## 4. Gateway 负载均衡：

- Gateway 作为一个集中处理请求的中转站，在单个微服务存在多个实例情况下，用于分散请求到不同实例当中；
- 实施细节：1. 配置运行 Eureka Server；2. 发现服务：使用 [`@EnableEurekaClient`](https://stackoverflow.com/a/31993406/16648127) 将网关、微服务发现注册到 Eureka Server；3. 配置网关：配置微服务的 URI 使用格式 `lb://<service-name>`, 网关通过 [LoadBalancerClientFilter](https://cloud.spring.io/spring-cloud-gateway/reference/html/#the-loadbalancerclient-filter) 自动解析微服务名称，替换成实际的主机和端口信息 （并同步所有参数）。

## 5. 理解 Spring AMQP（RabbitMQ） 中的“同步”和“异步”两种消息传输模式：

- 在 [Stackoverflow 提问](https://stackoverflow.com/questions/73553272/how-to-understand-async-vs-sync-in-spring-amqp)，试图理解 Spring AMQP 实现 RabbitMQ 同步、异步消息传输的方式，收到 Spring AMQP techLead 的回答，简要概括如下：

1. Spring AMQP 同步、异步消息传输的区别在于[消息消费者](https://www.rabbitmq.com/consumers.html)。因 RabbitMQ 底层采用的 [AMQP 0-9-1 协议](https://www.rabbitmq.com/tutorials/amqp-concepts.html) 规定了两种消息消费模式：1. [注册消费者](https://www.rabbitmq.com/consumers.html#subscribing) （又称“订阅”或“Push API”）2. [获取单个消息](https://www.rabbitmq.com/consumers.html#fetching)（又称“Pull API”）；
2. 异步：Spring AMQP 的异步传输即使用 RabbitMQ 的 “Push API”，并由框架本身处理订阅 RabbitMQ 接口的实施细节，开发者实际使用可以通过配置 [`MessageListenerAdapter`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/listener/adapter/MessageListenerAdapter.html) 或直接使用 [`@RabbitListener`](https://docs.spring.io/spring-amqp/api/org/springframework/amqp/rabbit/annotation/RabbitListener.html) 进行异步消息消费，其中前者可进一步分离各模块的职责、降低耦合度；
3. 同步：（阻塞式）使用“Pull API”，该方式逐一获取消息，会消耗额外系统资源，并不推荐；
4. 总结：同步通讯主要由“应用”本身处理消息消费方式，而异步通讯主要由“框架”本身进行处理。

## 6. Docker(Compose):

- 使用 `Dockerfile` 将各服务打包成 “Fat jar”，并在 Compose 文件中定义暴露的端口（[expose](https://docs.docker.com/compose/compose-file/compose-file-v3/#expose)），使得只有互相连接的服务才可对端口进行访问，从而对外隐藏了服务，客户端只能通过 Gateway 访问服务器资源；
- 实现在一个 MySql 容器内启动多个数据库，以供微服务使用，解决方案来自 [Stackoverflow 问题](https://stackoverflow.com/a/52899915/16648127)。

## 7. OpenFeign:

- [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/) 提供断言式 REST Client。通过使用 `@FeignClient` 注解，搭配使用 Eureka service registry，以发现服务，简化了微服务之间的交流。

## 8. Sleuth-Zipkin:

- 微服务监控，用于分布式追踪。可对请求中，各微服务所用时间进行分析，清楚了解服务的运行情况。还可以自定义 [`Span`](https://www.baeldung.com/spring-cloud-sleuth-single-application#3-manually-adding-a-span)，用于确定单个请求中的不同区域，达到精准定位。

## 9. 在 Docker 中运行:

    Maven clean install
    docker compose up -d

# Micro-services project demonstration

A Demo project for both synchronous connection and asynchronous messaging between services.

<sub><sub>If you like, please open a [new issue](https://github.com/Enfield-Li/SpringBoot_Microservice_MVP/issues/new). Any criticism, suggestions are welcomed.</sub></sub>

## 1. Stacks:

```
SpringBoot,
SpringCloud (Spring cloud gateway, Eureka server, OpenFeign, Sleuth-Zipkin),
Docker (Docker-compose)
```

## 2. Project structure:

```
+------------------+
|                  |
|                  |
|                  |
|                  |
|                  |
|                  |        +------------------+                 +----------------+                    +------------------+
|                  |        |                  | Sync connection |                |   Async message    |                  |
|     Gateway      |------->|   Fraud check    |---------------->|    Customer    |------------------->|   Notification   |
|                  |        |                  |                 |                |   (Spring AMQP)    |                  |
|                  |        +------------------+                 +----------------+          |         +------------------+
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
|                  |                                                                         |
+------------------+                                             +---------------------------+
                                                                 |
                                                                 |
                                                                 v
                     +----------------------------------------------------------------------------------------+
                     |                                                                                        |
                     |                       +---------------+                                                |
                     |  +----------+  Send   |               | Bind   +---------+ Receive   +--------------+  |
                     |  | Customer |-------->|   Exchange    |------->|  Queue  |---------->| Notification |  |
                     |  +----------+         |               |        +---------+ (Async)   +--------------+  |
                     |    Producer           +---------------+                                  Consumer      |
                     |                                                                                        |
                     +----------------------------------------------------------------------------------------+
```

## 3. Solving variables between dev and Docker environment:

- Method one: By setting up different `application.properties` for dev and Docker environment, and specifying [`SPRING_PROFILES_ACTIVE`](https://stackoverflow.com/a/60565969/16648127) value in [compose environment](https://docs.docker.com/compose/compose-file/#environment), the App will choose the corresponding file according to the system environment. eg:

```
App:
  dev environment: application.properties
  Docker environment: application-docker.properties

docker-compose.yml：
  services:
    customer:
      environment:
        SPRING_PROFILES_ACTIVE: docker
```

- Method two: By using [compose environment](https://docs.docker.com/compose/compose-file/#environment) in an attempt to override the values in `application.properties` from dev environment. eg with different syntax:

```
docker-compose.yml：
  services:
    customer:
      environment:
        spring.datasource.username: root,
        SPRING_APPLICATION_JSON: '{
          "spring.datasource.password": "password"
        }'
```

## 4. Gateway loadBalancer:

- Implementation details: 1. Set up a Eureka Server; 2. Service discovery: Discover and register services and Gateway through [`@EnableEurekaClient`](https://stackoverflow.com/a/31993406/16648127); 3. Configure Gateway: By using such format `lb://<service-name>`, the Gateway will use the Spring Cloud [LoadBalancerClientFilter](https://cloud.spring.io/spring-cloud-gateway/reference/html/#the-loadbalancerclient-filter) to resolve the service name to an actual host and port and replaces the URI in the same attribute.

## 5. Understanding "sync" vs "async" in Spring AMQP (RabbitMQ):

- Asking [question on Stackoverflow](https://stackoverflow.com/questions/73553272/how-to-understand-async-vs-sync-in-spring-amqp), and receive an answer from the techLead of Spring AMQP, and can be summarized as follows:

1. Sync vs async mainly differs in consumer. Since RabbitMQ's underlying protocol is [AMQP 0-9-1](https://www.rabbitmq.com/tutorials/amqp-concepts.html). And there are two ways for applications to do consume: 1. [Registering a Consumer (Subscribing, "Push API")](https://www.rabbitmq.com/consumers.html#subscribing), 2. [Fetching Individual Messages ("Pull API")](https://www.rabbitmq.com/consumers.html#fetching);
2. Async: Asynchronous connection in Spring AMQP uses RabbitMQ's Push API, and the framework takes care of the underlying subscription implementation details, developers can use [`MessageListenerAdapter`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/listener/adapter/MessageListenerAdapter.html) or [`@RabbitListener`](https://docs.spring.io/spring-amqp/api/org/springframework/amqp/rabbit/annotation/RabbitListener.html) to perform an async action, in which the former will further decouple the application since it delegates responsibility to other modules;
3. Sync: (blocking) It uses the "Pull API" that fetch messages one by one. This method is discouraged as it is very inefficient;
4. In conclusion: In the sync case, the application controls when messages are received, with async, the framework is in control.

## 6. Docker(Compose):

- Using `Dockerfile` to generate a "Fat jar", and specify [exposed ports](https://docs.docker.com/compose/compose-file/compose-file-v3/#expose). It will expose ports without publishing them to the host machine - they’ll only be accessible to linked services, and thus hiding services from clients, making Gateway the only accessible endpoint.
- Implementing multiple databases in one single MySql container for services (solution from [Stackoverflow](https://stackoverflow.com/a/52899915/16648127));

## 7. OpenFeign:

- [OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/) provides declarative a REST Client. By using `@FeignClient` annotation, along with Eureka service registry, so as to discover service, which makes writing web service clients easier.

## 8. Sleuth-Zipkin:

- Monitoring microservice using distributed tracing. This allows us to correlate activity between servers and get a much clearer picture of exactly what is happening in our services. We can also use [`Span`](https://www.baeldung.com/spring-cloud-sleuth-single-application#3-manually-adding-a-span) to locate different sections in a single request for more fine-grained tracing.

## 9. Run in Docker:

    Maven clean install
    docker compose up -d
