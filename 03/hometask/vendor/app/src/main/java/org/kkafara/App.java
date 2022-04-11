package org.kkafara;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class App {
  private static Logger logger;

  private static final String HOST = "localhost";
  private static final String ADMIN_EXCHANGE_ID = "ex_admin";
  private static final String TRADE_EXCHANGE_ID = "ex_trade";
  private static final String ORDER_QUEUE_ID = "q_vendors";

  public static void main(String[] args) throws IOException, TimeoutException {
    logger = LogManager.getLogger(App.class);
    Configurator.setRootLevel(Level.DEBUG);

    logger.info("Vendor started. Awaiting for vendorId...");

    String vendorId = getId();

    logger.info("Setting up vendor for id: " + vendorId);

    ConnectionFactory connectionFactory = new ConnectionFactory();

    List<String> stock = Arrays.stream(getStock()).toList();

    logger.info("Setting host for connection: " + HOST);
    connectionFactory.setHost(HOST);

    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();
    Channel adminChannel = connectionFactory.newConnection().createChannel();

    logger.info("Maybe-create exchange for id: " + ADMIN_EXCHANGE_ID + " with type: TOPIC");
    adminChannel.exchangeDeclare(ADMIN_EXCHANGE_ID, BuiltinExchangeType.TOPIC);

    logger.info("Create queue for id: " + getQueueId(vendorId));
    adminChannel.queueDeclare(getQueueId(vendorId), false, false, false, null);

    // load balancing
    channel.basicQos(1);

    logger.info("Maybe-create exchange for id: " + TRADE_EXCHANGE_ID + " with type: FANOUT");
    channel.exchangeDeclare(TRADE_EXCHANGE_ID, BuiltinExchangeType.FANOUT);

    logger.info("Binding personal queue to admin exchange with routeKey: " + getRoutingKeyForPersonalQueue(vendorId));
    adminChannel.queueBind(getQueueId(vendorId), ADMIN_EXCHANGE_ID, getRoutingKeyForPersonalQueue(vendorId));

    logger.info("Maybe-create vendors queue for id: " + ORDER_QUEUE_ID);
    channel.queueDeclare(getVendorsQueueId(), false, false, false, null);

    logger.info("Binding vendors queue to trade exchange with routeKey: " + getRoutingKeyForVendorsQueueForTradeExchange());
    channel.queueBind(getVendorsQueueId(), TRADE_EXCHANGE_ID, getRoutingKeyForVendorsQueueForTradeExchange());


    Consumer orderConsumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String order = new String(body, StandardCharsets.UTF_8);
        logger.info("Vendor with id: " + vendorId + " received order: " + order);

        if (stock.contains(order)) {
          logger.info("Sending ack");
          channel.basicAck(envelope.getDeliveryTag(), false);
        } else {
          logger.info("Rejecting");
          channel.basicNack(envelope.getDeliveryTag(), false, true);
        }
      }
    };

    Consumer adminConsumer = new DefaultConsumer(adminChannel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String order = new String(body, StandardCharsets.UTF_8);
        logger.info("Vendor with id: " + vendorId + " received message from admin: " + order);
      }
    };


    logger.info("Start listening for orders");
    channel.basicConsume(getVendorsQueueId(), false, orderConsumer);

    logger.info("Start listening for admin");
    adminChannel.basicConsume(getQueueId(vendorId), true, adminConsumer);

    logger.info("Quit");
  }

  private static String getRoutingKeyForVendorsQueueForTradeExchange() {
//    return "org.kkafara." + ORDER_QUEUE_ID;
    return "";
  }

  public static String getId() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    return bufferedReader.readLine();
  }

  public static String[] getStock() throws IOException {
    logger.info("Awaiting for stock...");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    // TODO: Split the order!
    String line = bufferedReader.readLine();

    return line.split(" ", 2);
  }
  public static String getQueueId(String vendorId) {
    return "q_vendor_" + vendorId;
  }

  public static String getRoutingKeyForPersonalQueue(String vendorId) {
    return "*.vendor";
  }

  public static String getVendorsQueueId() {
    return ORDER_QUEUE_ID;
  }
}
