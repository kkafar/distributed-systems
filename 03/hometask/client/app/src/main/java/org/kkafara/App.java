/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.kkafara;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    logger.info("Client started. Awaiting for clientId...");

    String clientId = getId();

    logger.info("Setting up client for id: " + clientId);

    ConnectionFactory connectionFactory = new ConnectionFactory();

    logger.info("Setting host for connection: " + HOST);
    connectionFactory.setHost(HOST);

    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();
    Channel adminChannel = connectionFactory.newConnection().createChannel();

    logger.info("Maybe-create exchange for id: " + ADMIN_EXCHANGE_ID + " with type: TOPIC");
    adminChannel.exchangeDeclare(ADMIN_EXCHANGE_ID, BuiltinExchangeType.TOPIC);

    logger.info("Create queue for id: " + getQueueId(clientId));
    adminChannel.queueDeclare(getQueueId(clientId), false, false, false, null);

    logger.info("Maybe-create exchange for id: " + TRADE_EXCHANGE_ID + " with type: FANOUT");
    channel.exchangeDeclare(TRADE_EXCHANGE_ID, BuiltinExchangeType.FANOUT);

    logger.info("Binding personal queue to admin exchange with route key: " + getRoutingKeyForPersonalQueue(clientId));
    adminChannel.queueBind(getQueueId(clientId), ADMIN_EXCHANGE_ID, getRoutingKeyForPersonalQueue(clientId));

    Consumer adminConsumer = new DefaultConsumer(adminChannel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String order = new String(body, StandardCharsets.UTF_8);
        logger.info("Client with id: " + clientId + " received message from admin: " + order);
        logger.info("Sending ack");
//        adminChannel.basicAck(envelope.getDeliveryTag(), false);
      }
    };

    logger.info("Start listening for admin messages");
    adminChannel.basicConsume(getQueueId(clientId), true, adminConsumer);

    while (true) {
      String order = getOrder();

      if ("exit".equals(order)) {
        break;
      }

      logger.info("Sending order to trade exchange " + TRADE_EXCHANGE_ID);
      channel.basicPublish(TRADE_EXCHANGE_ID, getRouteKeyForOrder(), null, order.getBytes(StandardCharsets.UTF_8));
    }
  }

  public static String getId() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    return bufferedReader.readLine();
  }

  public static String getOrder() throws IOException {
    logger.info("Awaiting for order...");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    // TODO: Split the order!
    return bufferedReader.readLine();
  }

  public static String getQueueId(String clientId) {
    return "q_client_" + clientId;
  }

  public static String getRoutingKeyForPersonalQueue(String clientId) {
    return "client.*";
  }

  public static String getRouteKeyForOrder() {
//    return "org.kkafara." + ORDER_QUEUE_ID;
    return "";
  }
}