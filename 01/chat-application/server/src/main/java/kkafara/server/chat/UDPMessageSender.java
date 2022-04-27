package kkafara.server.chat;

import kkafara.server.data.model.Message;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class UDPMessageSender implements MessageSender {
  private final DatagramSocket mSocket;
  private final InetAddress mAddress;

  private int mPort;

  public UDPMessageSender(DatagramSocket socket, String address) throws UnknownHostException {
    mSocket = socket;
    mAddress = InetAddress.getByName(address);
    mPort = 8080;
  }

  public void setPort(int port) {
    mPort = port;
  }

  @Override
  public boolean send(Message message) {
    byte[] bytesToSend = message.encodeToString().getBytes(StandardCharsets.UTF_8);
    DatagramPacket packet = new DatagramPacket(bytesToSend, bytesToSend.length, mAddress, mPort);
    try {
      mSocket.send(packet);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
