import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
        String msg = new String(receivePacket.getData(), StandardCharsets.UTF_8);
        System.out.println("received msg: " + msg);
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
