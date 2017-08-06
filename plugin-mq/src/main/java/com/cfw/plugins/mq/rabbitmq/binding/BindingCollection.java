package com.cfw.plugins.mq.rabbitmq.binding;

import com.cfw.plugins.mq.rabbitmq.exchange.ExchangeCollection;
import com.cfw.plugins.mq.rabbitmq.queue.QueueCollection;
import org.springframework.amqp.core.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Binding collection contains all defined bindings on the same JVM.<p></p>
 * It provides an convenient way to initialize bindings of exchange and queue.<br/>
 * An exchange may routing messages to one or several queues with same routing key or
 * different routing keys. The BindingCollection can deal this by passing formatted
 * string like exchangeType/exchangeName/queueName/routingKeys to RabbitConfigurationProperties
 * class. This can be finished through Spring Boot's application.properties file.
 *<p></p>
 * Created by Duskrain on 2017/8/2.
 */
public class BindingCollection {

    private static Map<String,Binding> bindings = new HashMap<>();

    public static void addBinding(Binding binding){

    }

    public static void addBinding(String exchangeType,String exchangeName,String queueName,String routingKey){
        String bindingName = exchangeType + "/" +exchangeName + "/" + queueName + "/" + routingKey;

        BindingBuilder.DestinationConfigurer destinationConfigurer = BindingBuilder.bind(QueueCollection.getQueue(queueName));

        switch(exchangeType){
            case ExchangeTypes.DIRECT:
                BindingCollection.bindings.put(
                        bindingName,
                        destinationConfigurer
                                .to((DirectExchange) ExchangeCollection.getExchange(exchangeType,exchangeName))
                                .with(routingKey)
                );
                break;
            case ExchangeTypes.FANOUT:
                BindingCollection.bindings.put(
                        bindingName,
                        destinationConfigurer
                                .to((FanoutExchange) ExchangeCollection.getExchange(exchangeType,exchangeName))
                );
                break;
            case ExchangeTypes.TOPIC:
                BindingCollection.bindings.put(
                        bindingName,
                        destinationConfigurer
                                .to((TopicExchange) ExchangeCollection.getExchange(exchangeType,exchangeName))
                                .with(routingKey)
                );
                break;
            default:
                break;

        }
    }
}
