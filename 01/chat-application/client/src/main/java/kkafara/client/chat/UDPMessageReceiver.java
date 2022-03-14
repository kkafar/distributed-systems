package kkafara.client.chat;

import kkafara.data.model.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPMessageReceiver implements MessageReceiver {
  private final DatagramSocket mSocket;
  private final byte[] mBuffer;

  public UDPMessageReceiver(int bufferSize, DatagramSocket socket) {
    mSocket = socket;
    mBuffer = new byte[bufferSize];
  }

  @Override
  public Message receive() {
    cleanBuffer();
    DatagramPacket incomingPacket = new DatagramPacket(mBuffer, mBuffer.length);

    try {
      mSocket.receive(incomingPacket);
      return Message.parseFromString(new String(incomingPacket.getData()));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void cleanBuffer() {
    Arrays.fill(mBuffer, (byte)0);
  }
}
