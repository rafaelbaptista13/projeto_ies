package ua.ies.g23.Covinfo19;

import org.apache.logging.log4j.message.Message;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ua.ies.g23.Covinfo19.model.*;
import ua.ies.g23.Covinfo19.repository.*;


@SpringBootApplication
public class Covinfo19Application {

  static final String topicExchangeName = "filagerar-exchange";
  static final String topicExchangeName2 = "filaupdate-exchange";
  
  static final String queueName = "filagerar";
  static final String queueName2 = "filaupdate";


  @Bean
  Queue queueGerar() {
    return new Queue(queueName, false);
  }

  @Bean
  Queue queueUpdate() {
    return new Queue(queueName2, false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  TopicExchange exchange2() {
    return new TopicExchange(topicExchangeName2);
  }

  @Bean
  Binding binding() {
    return BindingBuilder.bind(queueGerar()).to(exchange()).with(queueName);
  }

  @Bean
  Binding binding2() {
    return BindingBuilder.bind(queueUpdate()).to(exchange2()).with(queueName2);
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, @Qualifier("listenerAdapter") MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  SimpleMessageListenerContainer container2(ConnectionFactory connectionFactory, @Qualifier("listenerAdapter2") MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName2);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(Receiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  MessageListenerAdapter listenerAdapter2(ReceiverUpdateData receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }
  
  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(Covinfo19Application.class, args);
  }

}