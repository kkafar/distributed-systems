package kkafara.server.chat;


import kkafara.server.data.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPMessageReceiver implements MessageReceiver {
  private final DatagramSocket mSocket;
  private final byte[] mBuffer;
  private final Logger mLogger;

  public UDPMessageReceiver(int bufferSize, DatagramSocket socket) {
    mSocket = socket;
    mBuffer = new byte[bufferSize];
    mLogger = LogManager.getLogger(UDPMessageReceiver.class);
  }

  @Override
  public Message receive() {
    cleanBuffer();
    DatagramPacket incomingPacket = new DatagramPacket(mBuffer, mBuffer.length);

    try {
      mSocket.receive(incomingPacket);
      Message message = Message.parseFromString(new String(incomingPacket.getData()));
      message.setType(Message.Type.UDP);
//      message.setPort(incomingPacket.getPort());
//      mLogger.info("Setting port: " + incomingPacket.getPort() + " for message from ");
      return message;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void cleanBuffer() {
    Arrays.fill(mBuffer, (byte)0);
  }
}
