import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Server {
  public static void main(String[] args) throws IOException {
    System.out.println("JAVA UDP SERVER");
    DatagramSocket socket = null;
    int portNumber = 9008;

    try {
      socket = new DatagramSocket(portNumber);
      byte[] receiveBuffer = new byte[1024];

      while (true) {
        Arrays.fill(receiveBuffer, (byte)0);
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        System.out.println("waiting for message");
        socket.receive(receivePacket);
        ByteBuffer bb = ByteBuffer.wrap(receivePacket.getData());
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int receivedInt = bb.getInt();

        System.out.println("received msg: " + Integer.toString(receivedInt));

        ByteBuffer bb2 = ByteBuffer.allocate(4);
        bb2.order(ByteOrder.LITTLE_ENDIAN);
        bb2.putInt(receivedInt + 1);
        byte[] sendBuffer = bb2.array();

        DatagramPacket sendPacket = new DatagramPacket(
            sendBuffer,
            sendBuffer.length,
            receivePacket.getAddress(),
            receivePacket.getPort()
        );

        socket.send(sendPacket);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (socket != null) {
        socket.close();
      }
    }
  }
}
